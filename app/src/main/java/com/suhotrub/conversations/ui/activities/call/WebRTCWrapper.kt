package com.suhotrub.conversations.ui.activities.call

import android.content.Context
import android.util.Log
import com.suhotrub.conversations.interactor.signalr.MainHubInteractor
import com.suhotrub.conversations.interactor.signalr.invokeEvent
import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.ui.util.subscribe
import com.suhotrub.conversations.ui.util.subscribeIoHandleError
import io.reactivex.subjects.PublishSubject
import org.webrtc.*
import timber.log.Timber

class WebRTCWrapper(
        private val context: Context,
        private val mainHubInteractor: MainHubInteractor
) {
    private lateinit var peerConnectionFactory: PeerConnectionFactory
    lateinit var rootEglBase: EglBase

    private var localPeer: PeerConnection? = null
    private var localAudioSource: AudioSource? = null
    private var localVideoSource: VideoSource? = null
    private var localVideoCapturer: VideoCapturer? = null
    private val connetions = mutableListOf<PeerConnection>()

    // INIT GLOBALS
    init {
        init()
    }

    private fun init() {

        rootEglBase = EglBase.create()

        val defaultVideoEncoderFactory = DefaultVideoEncoderFactory(
                rootEglBase.eglBaseContext, true, true)
        val defaultVideoDecoderFactory = DefaultVideoDecoderFactory(rootEglBase.eglBaseContext)


        PeerConnectionFactory.initialize(
                PeerConnectionFactory.InitializationOptions
                        .builder(context.applicationContext)
                        .createInitializationOptions())

        val options = PeerConnectionFactory.Options()

        peerConnectionFactory = PeerConnectionFactory.builder()
                .setOptions(options)
                .setVideoDecoderFactory(defaultVideoDecoderFactory)
                .setVideoEncoderFactory(defaultVideoEncoderFactory)
                .createPeerConnectionFactory();



        subscribe(mainHubInteractor.observeEvent("NewPublisher", NewPublisherResponse::class.java)) {
            onOfferReceived(it)
        }
    }

    private fun initPeerConnection() {
        val iceServers = arrayListOf(
                PeerConnection.IceServer.builder("stun:stun.l.google.com:19302"/*"stun:89.249.28.54:3478"*/).createIceServer()
        )
        localPeer = peerConnectionFactory.createPeerConnection(iceServers,
                PeerConnectionObserver(
                        onIceCandidateCb = {
                            onIceCandidate(it)
                        },
                        onAddStreamCb = {
                            onAddStream(it)
                        },
                        onRemoveStreamCb = {
                            onRemoveStream(it)
                        }))

    }

    val constraints = MediaConstraints()

    fun call() {
        constraints.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
        constraints.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))


        initPeerConnection()
        val localMediaStream = getUserMediaAndAddStream(localPeer!!)
        localStreamPublisher.onNext(localMediaStream)

        createOffer(localPeer, constraints)
    }

    fun stop() {
        terminate()
    }

    // INIT LISTENERS
    private fun onIceCandidate(iceCandidate: IceCandidate) {
        mainHubInteractor.safeOperation {
            subscribeIoHandleError(
                    it.invokeEvent<TrickleResponse>("Trickle",
                            TrickleCandidateReceived(
                                    sdpMid = iceCandidate.sdpMid,
                                    sdpMLineIndex = iceCandidate.sdpMLineIndex,
                                    completed = iceCandidate.sdp.isNullOrEmpty(),
                                    candidate = iceCandidate.sdp
                            )
                    ),
                    {
                        Log.d("ANTON", it.toString())
                    },
                    {
                        Log.e("ANTON", it.toString(), it)
                    })
        }

    }

    private fun onAddStream(stream: MediaStream) {
        remoteStreamPublisher.onNext(stream)
        /*runOnUiThread {
            try {
                videoTrack?.addSink(view2)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }*/
    }

    private fun onRemoveStream(stream: MediaStream) {
        remoteStreamPublisher.onNext(stream)
    }

    private val localStreamPublisher = PublishSubject.create<MediaStream>()
    private val remoteStreamPublisher = PublishSubject.create<MediaStream>()
    fun observeRemoteStream() = remoteStreamPublisher
    fun observeLocalStream() = localStreamPublisher


    // GATHER USER MEDIA

    private fun getUserMediaAndAddStream(localPeer: PeerConnection): MediaStream {
        val localAudioTrack = initAudio(peerConnectionFactory, createAudioConstraints())
        var localVideoTrack = initVideo(peerConnectionFactory, rootEglBase, true)

        val localMediaStream = peerConnectionFactory.createLocalMediaStream("STREAM1")
        localMediaStream.addTrack(localVideoTrack)
        localMediaStream.addTrack(localAudioTrack)

        localPeer.addStream(localMediaStream)
        return localMediaStream
    }

    private fun initVideo(peerConnectionFactory: PeerConnectionFactory, rootEglBase: EglBase, isFrontal: Boolean): VideoTrack {
        val textureHelper = SurfaceTextureHelper.create(Thread.currentThread().name, rootEglBase.eglBaseContext)
        localVideoCapturer = if (isFrontal) createFrontalVideoCapturer() else createVideoCapturer()
        localVideoSource = peerConnectionFactory.createVideoSource(false)

        localVideoCapturer?.initialize(textureHelper, context, localVideoSource?.capturerObserver)
        localVideoCapturer?.startCapture(1280, 720, 60)

        val localVideoTrack = peerConnectionFactory.createVideoTrack("VIDEO1", localVideoSource)
        localVideoTrack.setEnabled(true)
        return localVideoTrack
    }

    private fun createAudioConstraints(): MediaConstraints {
        val constraints = MediaConstraints()
        constraints.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
        constraints.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
        return constraints
    }

    private fun initAudio(peerConnectionFactory: PeerConnectionFactory, constraints: MediaConstraints): AudioTrack {
        val audioSource = peerConnectionFactory.createAudioSource(constraints);
        val localAudioTrack = peerConnectionFactory.createAudioTrack("AUDIO1", audioSource)
        localAudioTrack.setEnabled(true)
        return localAudioTrack
    }

    private fun createFrontalVideoCapturer(): VideoCapturer? {
        val enumerator = Camera1Enumerator(true)
        val deviceNames = enumerator.deviceNames

        // Trying to find a front facing camera!
        deviceNames.forEach {
            if (enumerator.isFrontFacing(it)) {
                val videoCapturer = enumerator.createCapturer(it, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        // We were not able to find a front cam. Look for other cameras
        deviceNames.forEach {
            if (!enumerator.isFrontFacing(it)) {
                val videoCapturer = enumerator.createCapturer(it, null);
                if (videoCapturer != null) {
                    return videoCapturer
                }
            }
        }

        return null
    }

    private fun createVideoCapturer(): VideoCapturer? {
        val enumerator = Camera2Enumerator(context)
        val deviceNames = enumerator.deviceNames

        // Trying to find a front facing camera!
        deviceNames.forEach {
            if (enumerator.isBackFacing(it)) {
                val videoCapturer = enumerator.createCapturer(it, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        // We were not able to find a front cam. Look for other cameras
        deviceNames.forEach {
            if (!enumerator.isBackFacing(it)) {
                val videoCapturer = enumerator.createCapturer(it, null);
                if (videoCapturer != null) {
                    return videoCapturer
                }
            }
        }

        return null

    }


    // CREATING OFFER
    private fun createOffer(localPeer: PeerConnection?, constraints: MediaConstraints) {
        localPeer?.createOffer(SDPCreateCallback { sdpCreateResult ->

            if (sdpCreateResult is SDPCreateSuccess) {
                try {
                    localPeer.setLocalDescription(SDPSetCallback {

                    }, sdpCreateResult.descriptor)
                } catch (t: Throwable) {
                    Log.d("SDP", "lol", t)
                }
                Log.d("onCreateSuccess", "SignallingClient emit ")


                //ПИНАЕМ АНТОНА
                mainHubInteractor.safeOperation {
                    subscribeIoHandleError(
                            it.invokeEvent<InititateCallResponse>(
                                    "InitiateCall",
                                    InitiateCallRequest(
                                            Jsep(
                                                    sdp = sdpCreateResult.descriptor.description,
                                                    type = sdpCreateResult.descriptor.type.canonicalForm()
                                            ),
                                            groupGuid = ""
                                    )),
                            {
                                Log.d("ANTON", it.toString())
                                localPeer.setRemoteDescription(
                                        SDPSetCallback { },
                                        SessionDescription(SessionDescription.Type.fromCanonicalForm(it.data.type), it.data.sdp))

                            },
                            {
                                Log.e("ANTON", it.toString(), it)

                            })
                }


            }

        }, constraints)
    }


    // OBSERVING OFFER

    private fun onOfferReceived(newPublisherResponse: NewPublisherResponse) {

        val iceServers = arrayListOf(
                PeerConnection.IceServer.builder("stun:stun.l.google.com:19302"/*"stun:89.249.28.54:3478"*/).createIceServer()
        )
        val remotePeer = peerConnectionFactory.createPeerConnection(iceServers,
                PeerConnectionObserver(
                        onIceCandidateCb = {
                            onIceCandidate(it)
                        },
                        onAddStreamCb = {
                            onAddStream(it)
                        },
                        onRemoveStreamCb = {
                            onRemoveStream(it)
                        }))

        connetions.add(remotePeer!!)

        remotePeer?.setRemoteDescription(SDPCreateCallback {
        }, SessionDescription(
                SessionDescription.Type.fromCanonicalForm(newPublisherResponse.answer.type),
                newPublisherResponse.answer.sdp
        ))
        sendAnswer(remotePeer,newPublisherResponse)

    }

    private fun sendAnswer(remotePeer: PeerConnection?, newPublisherResponse: NewPublisherResponse) {
        val falseConstraints = MediaConstraints()
        falseConstraints.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
        falseConstraints.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
        remotePeer?.createAnswer(object : SdpObserver {
            override fun onCreateSuccess(sdp: SessionDescription?) {
                remotePeer?.setLocalDescription(SDPSetCallback {}, sdp)

                mainHubInteractor.safeOperation {

                    it.invoke("AnswerNewPublisher", AnswerNewPublisherRequest(
                            handleId = newPublisherResponse.handleId,
                            answer = Jsep(sdp?.type?.canonicalForm(), sdp?.description)
                    ))
                }
            }

            override fun onSetFailure(p0: String?) {}

            override fun onSetSuccess() {}

            override fun onCreateFailure(p0: String?) {
                Timber.d(p0)
            }
        }, falseConstraints)

    }

    fun terminate() {

        try {
            localVideoCapturer?.stopCapture()
        } catch (t: Throwable) {

        }

        remoteStreamPublisher.onComplete()
        localStreamPublisher.onComplete()

        localVideoCapturer?.dispose()
        localVideoSource?.dispose()

        localAudioSource?.dispose()

        localPeer?.dispose()
        connetions.forEach { it.dispose() }

        rootEglBase.release()

        peerConnectionFactory.dispose()

    }
}

interface SDPCreateResult

data class SDPCreateSuccess(val descriptor: SessionDescription) : SDPCreateResult
data class SDPCreateFailure(val reason: String?) : SDPCreateResult

class SDPCreateCallback(private val callback: (SDPCreateResult) -> Unit) : SdpObserver {

    override fun onSetFailure(reason: String?) {}

    override fun onSetSuccess() {}

    override fun onCreateSuccess(descriptor: SessionDescription) = callback(SDPCreateSuccess(descriptor))

    override fun onCreateFailure(reason: String?) = callback(SDPCreateFailure(reason))
}

class SDPSetCallback(private val callback: (String?) -> Unit) : SdpObserver {

    override fun onSetFailure(reason: String?) = callback(reason)

    override fun onSetSuccess() = callback(null)

    override fun onCreateSuccess(descriptor: SessionDescription?) {}

    override fun onCreateFailure(reason: String?) {}
}

data class NewPublisherResponse(
        val handleId: Long,
        val answer: Jsep,
        val user: UserDto
)

data class AnswerNewPublisherRequest(
        val handleId: Long,
        val answer: Jsep
)
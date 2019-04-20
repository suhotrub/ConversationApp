package com.suhotrub.conversations.ui.activities.call

import android.content.Context
import android.util.Log
import com.suhotrub.conversations.interactor.signalr.MainHubInteractor
import com.suhotrub.conversations.interactor.signalr.invokeEvent
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.ui.util.subscribe
import com.suhotrub.conversations.ui.util.subscribeIoHandleError
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.webrtc.*
import timber.log.Timber
import java.util.concurrent.Executor

class WebRTCWrapper(
        private val context: Context,
        private val mainHubInteractor: MainHubInteractor,
        private val groupdDto: GroupDto
) {

    @Volatile
    private var localPeer: PeerConnection? = null
    private var localAudioSource: AudioSource? = null
    private var localVideoSource: VideoSource? = null
    private var localVideoCapturer: VideoCapturer? = null
    private val connetions = mutableListOf<PeerConnection>()

    // INIT GLOBALS

    private val offerAnswerConstraints by lazy {
        MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
        }
    }
    private val peerConnectionConstraints by lazy {
        MediaConstraints().apply {

            mandatory.add(MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("RtpDataChannels", "false"))
        }
    }
    private val audioConstraints by lazy {
        MediaConstraints().apply {

            mandatory.add(MediaConstraints.KeyValuePair("echoCancellation", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("noiseSuppression", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("autoGainControl", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("volume", "1"))
        }

    }
    val rootEglBase: EglBase by lazy {
        EglBase.create()
    }

    private val peerConnectionFactory by lazy {
        val defaultVideoEncoderFactory = DefaultVideoEncoderFactory(
                rootEglBase.eglBaseContext, true, true)
        val defaultVideoDecoderFactory = DefaultVideoDecoderFactory(rootEglBase.eglBaseContext)

        PeerConnectionFactory.initialize(
                PeerConnectionFactory.InitializationOptions
                        .builder(context.applicationContext)
                        .createInitializationOptions())

        val options = PeerConnectionFactory.Options()

        PeerConnectionFactory.builder()
                .setOptions(options)
                .setVideoDecoderFactory(defaultVideoDecoderFactory)
                .setVideoEncoderFactory(defaultVideoEncoderFactory)
                .createPeerConnectionFactory();
    }

    private val executor by lazy {
        Executor {
            Thread(it).start()
        }
    }

    init {
        init()
    }

    var disposable: Disposable? = null
    var subject = PublishSubject.create<Boolean>()
    var offerRequests = mutableListOf<NewPublisherResponse>()
    private fun init() {
        disposable = subscribe(
                mainHubInteractor.observeEvent("NewPublisher", NewPublisherResponse::class.java))
        {
            if (fullyLoaded)
                executor.execute {
                    Thread.sleep(1000)
                    onOfferReceived(it)
                }
            else
                offerRequests.add(it)
        }
        subscribe(mainHubInteractor.observeEvent("Unpublished", NewPublisherResponse::class.java))
        {
            it.handleId?.let {
                remoteStreamUnpublished.onNext(it)
            }
        }
    }

    private fun initLocalPeerConnection() {
        val iceServers = arrayListOf(
                PeerConnection.IceServer.builder("stun:stun.l.google.com:19302"/*"stun:89.249.28.54:3478"*/).createIceServer()
        )
        localPeer = peerConnectionFactory.createPeerConnection(
                iceServers,
                peerConnectionConstraints,
                PeerConnectionObserver(
                        onIceCandidateCb = {
                            onIceCandidate(it)
                        },
                        onAddStreamCb = {
                            onAddStream(it)
                        },
                        onIceConnectionChange = {
                            iceConnectionStatePublisher.onNext(it)
                        },
                        onRemoveStreamCb = {
                            onRemoveStream(it)
                        }))

    }

    fun call() {

        executor.execute {
            initLocalPeerConnection()

            val localMediaStream = getUserMediaAndAddStream(localPeer!!)
            localStreamPublisher.onNext(localMediaStream)
            Thread.sleep(1000)
            createOffer(localPeer)

        }

    }

    fun stop() {
        terminate()
    }

    // INIT LISTENERS
    private fun onIceCandidate(iceCandidate: IceCandidate, handleId: Long? = null) {
        executor.execute {
            mainHubInteractor.safeOperation {
                subscribeIoHandleError(
                        it.invokeEvent<TrickleResponse>("Trickle",
                                TrickleCandidateReceived(
                                        sdpMid = iceCandidate.sdpMid,
                                        sdpMLineIndex = iceCandidate.sdpMLineIndex,
                                        completed = iceCandidate.sdp.isNullOrEmpty(),
                                        candidate = iceCandidate.sdp,
                                        handleId = handleId
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

    }

    private fun onAddStream(stream: MediaStream, userDto: UserDto? = null) {
        remoteStreamPublisher.onNext(stream to userDto)
    }

    private fun onRemoveStream(stream: MediaStream) {
        //remoteStreamPublisher.onNext(stream)
    }

    private val iceConnectionStatePublisher = PublishSubject.create<PeerConnection.IceConnectionState>()
    private val localStreamPublisher = PublishSubject.create<MediaStream>()
    private val remoteStreamPublisher = PublishSubject.create<Pair<MediaStream, UserDto?>>()
    private val remoteStreamUnpublished = PublishSubject.create<Long>()
    fun observeIceConnectionStateChange() = iceConnectionStatePublisher
    fun observeRemoteStream() = remoteStreamPublisher
    fun observeUnpublished() = remoteStreamUnpublished
    fun observeLocalStream() = localStreamPublisher


    // GATHER USER MEDIA

    private fun getUserMediaAndAddStream(localPeer: PeerConnection): MediaStream {
        val localAudioTrack = initAudio(peerConnectionFactory, audioConstraints)
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

    var fullyLoaded = false
    // CREATING OFFER
    private fun createOffer(localPeer: PeerConnection?) {
        localPeer?.createOffer(SDPCreateCallback { sdpCreateResult ->
            //executor.execute {
            if (sdpCreateResult is SDPCreateSuccess) {
                try {
                    localPeer.setLocalDescription(SDPSetCallback {

                        //ПИНАЕМ АНТОНА todo в callback выше
                        mainHubInteractor.safeOperation {
                            subscribeIoHandleError(
                                    it.invokeEvent<InititateCallResponse>(
                                            "InitiateCall",
                                            InitiateCallRequest(
                                                    Jsep(
                                                            sdp = sdpCreateResult.descriptor.description,
                                                            type = sdpCreateResult.descriptor.type.canonicalForm()
                                                    ),
                                                    groupGuid = groupdDto.groupGuid
                                            )),
                                    {
                                        Log.d("ANTON", it.toString())
                                        executor.execute {
                                            Thread.sleep(1000)
                                            localPeer.setRemoteDescription(
                                                    SDPSetCallback {
                                                        fullyLoaded = true
                                                        executor.execute {
                                                            Thread.sleep(3000)
                                                            offerRequests.forEach {
                                                                onOfferReceived(it)
                                                            }
                                                            offerRequests.clear()
                                                        }
                                                    },
                                                    SessionDescription(SessionDescription.Type.fromCanonicalForm(it.data.type), it.data.sdp))
                                        }
                                    },
                                    {
                                        Log.e("ANTON", it.toString(), it)

                                    })
                        }


                    }, sdpCreateResult.descriptor)
                } catch (t: Throwable) {
                    Log.d("SDP", "lol", t)
                }
                Log.d("onCreateSuccess", "SignallingClient emit ")


            }
            //}

        }, offerAnswerConstraints)
    }

    private fun sendOfferToHub() {

    }
    // OBSERVING OFFER

    private fun onOfferReceived(newPublisherResponse: NewPublisherResponse) {
        newPublisherResponse.user?.handleId = newPublisherResponse.handleId

        val iceServers = arrayListOf(
                PeerConnection.IceServer.builder("stun:stun.l.google.com:19302"/*"stun:89.249.28.54:3478"*/).createIceServer()
        )
        var handleId = newPublisherResponse.handleId
        val remotePeer = peerConnectionFactory.createPeerConnection(
                iceServers,
                peerConnectionConstraints,
                PeerConnectionObserver(
                        onIceCandidateCb = {
                            onIceCandidate(it, handleId)
                        },
                        onIceConnectionChange = {
                            iceConnectionStatePublisher.onNext(it)
                        },
                        onAddStreamCb = {
                            onAddStream(it, newPublisherResponse.user)
                        },
                        onRemoveStreamCb = {
                            onRemoveStream(it)
                        }))

        connetions.add(remotePeer!!)

        remotePeer?.setRemoteDescription(SDPSetCallback {
            sendAnswer(remotePeer, newPublisherResponse)
        },
                SessionDescription(
                        SessionDescription.Type.fromCanonicalForm(newPublisherResponse.answer?.type),
                        newPublisherResponse.answer?.sdp
                ))

        //TODO в callback выше

    }

    private fun sendAnswer(remotePeer: PeerConnection?, newPublisherResponse: NewPublisherResponse) {
        remotePeer?.createAnswer(object : SdpObserver {
            override fun onCreateSuccess(sdp: SessionDescription?) {
                executor.execute {
                    remotePeer?.setLocalDescription(SDPSetCallback {}, sdp)

                    mainHubInteractor.safeOperation {

                        it.invoke("AnswerNewPublisher", AnswerNewPublisherRequest(
                                handleId = newPublisherResponse.handleId ?: 0,
                                answer = Jsep(sdp?.type?.canonicalForm(), sdp?.description)
                        ))
                    }
                }
            }

            override fun onSetFailure(p0: String?) {}

            override fun onSetSuccess() {}

            override fun onCreateFailure(p0: String?) {
                Timber.d(p0)
            }
        }, offerAnswerConstraints)

    }

    fun terminate() {
        executor.execute {
            disposable?.dispose()
            localPeer?.close()
            localPeer?.dispose()
            connetions.forEach {
                it.close()
                //it.dispose()
            }
            try {
                localVideoCapturer?.stopCapture()
            } catch (t: Throwable) {

            }
            localVideoCapturer?.dispose()
            localVideoSource?.dispose()
            localAudioSource?.dispose()

            remoteStreamPublisher.onComplete()
            localStreamPublisher.onComplete()

            peerConnectionFactory.dispose()

            rootEglBase.detachCurrent()
            rootEglBase.release()

            connetions.forEach {
                // it.dispose()
            }
        }
    }
}
//offerReceived -> createRemotePeer -> remotePeer.setRemoteDescription -> remotePeer.createAnswer -> remotePeer.setLocalDescription; AnswerNewPublisher
//initLocalPeerConnection -> localPeer.createOffer -> localPeer.setLocalDescription -> InitiateCall -> localPeer.setRemoteDescription

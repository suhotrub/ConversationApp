package com.suhotrub.conversations.ui.activities.call

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.suhotrub.conversations.interactor.signalr.MainHubInteractor
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.SessionDescription
import org.webrtc.voiceengine.WebRtcAudioManager
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.IceCandidate



@InjectViewState
class CallPresenter(
        private val mainHubInteractor: MainHubInteractor
): MvpPresenter<MvpView>(){
    init {

    }

    /*private fun call() {
        //we already have video and audio tracks. Now create peerconnections
        val iceServers = ArrayList()

        //create sdpConstraints
        sdpConstraints = MediaConstraints()
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"))
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"))

        //creating localPeer
        localPeer = peerConnectionFactory.createPeerConnection(iceServers, sdpConstraints, object : CustomPeerConnectionObserver("localPeerCreation") {
            fun onIceCandidate(iceCandidate: IceCandidate) {
                super.onIceCandidate(iceCandidate)
                onIceCandidateReceived(localPeer, iceCandidate)
            }
        })

        //creating remotePeer
        remotePeer = peerConnectionFactory.createPeerConnection(iceServers, sdpConstraints, object : CustomPeerConnectionObserver("remotePeerCreation") {

            fun onIceCandidate(iceCandidate: IceCandidate) {
                super.onIceCandidate(iceCandidate)
                onIceCandidateReceived(remotePeer, iceCandidate)
            }

            fun onAddStream(mediaStream: MediaStream) {
                super.onAddStream(mediaStream)
                gotRemoteStream(mediaStream)
            }
        })

        //creating local mediastream
        val stream = peerConnectionFactory.createLocalMediaStream("102")
        stream.addTrack(localAudioTrack)
        stream.addTrack(localVideoTrack)
        localPeer.addStream(stream)

        //creating Offer
        localPeer.createOffer(object : CustomSdpObserver("localCreateOffer") {
            fun onCreateSuccess(sessionDescription: SessionDescription) {
                //we have localOffer. Set it as local desc for localpeer and remote desc for remote peer.
                //try to create answer from the remote peer.
                super.onCreateSuccess(sessionDescription)
                localPeer.setLocalDescription(CustomSdpObserver("localSetLocalDesc"), sessionDescription)
                remotePeer.setRemoteDescription(CustomSdpObserver("remoteSetRemoteDesc"), sessionDescription)
                remotePeer.createAnswer(object : CustomSdpObserver("remoteCreateOffer") {
                    fun onCreateSuccess(sessionDescription: SessionDescription) {
                        //remote answer generated. Now set it as local desc for remote peer and remote desc for local peer.
                        super.onCreateSuccess(sessionDescription)
                        remotePeer.setLocalDescription(CustomSdpObserver("remoteSetLocalDesc"), sessionDescription)
                        localPeer.setRemoteDescription(CustomSdpObserver("localSetRemoteDesc"), sessionDescription)
                    }
                }, MediaConstraints())
            }
        }, sdpConstraints)
    }*/
}
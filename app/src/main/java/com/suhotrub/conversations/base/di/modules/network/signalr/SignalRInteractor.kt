package com.suhotrub.conversations.base.di.modules.network.signalr

import com.smartarmenia.dotnetcoresignalrclientjava.HubConnection
import com.smartarmenia.dotnetcoresignalrclientjava.WebSocketHubConnectionP2
import io.reactivex.Observable

class SignalRInteractor(){

    fun estabilishConnection(url:String,token:String) =
            WebSocketHubConnectionP2(url, token)


    /*fun <T> HubConnection.observe(name:String) =
           subscribeToEvent(name) {
               it.
           }*/

    /*fun <T> HubConnection.invoke(name:String, vararg params:Any):Observable<T> {
        invoke(name, params)
        subscribeToEvent(name) {}
    }*/

}
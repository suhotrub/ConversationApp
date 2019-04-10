package com.suhotrub.conversations.interactor.signalr

import com.suhotrub.conversations.interactor.user.TokenStorage
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Module
class MainHubModule {

    /*@Provides
    @Singleton
    fun provideHub(
            @Named("BASE_URL") baseUrl: String,
            tokenStorage: TokenStorage): HubConnection =
            WebSocketHubConnectionP2("$baseUrl/signalr?acess_token=${tokenStorage.getToken()} ", tokenStorage.getToken()).apply(HubConnection::connect)
*/

}
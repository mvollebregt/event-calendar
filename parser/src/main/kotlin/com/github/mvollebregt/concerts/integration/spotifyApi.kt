package com.github.mvollebregt.concerts.integration

import se.michaelthelin.spotify.SpotifyApi

val spotifyApi = createSpotifyApi()

private fun createSpotifyApi(): SpotifyApi {
    val spotifyApi: SpotifyApi = SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRefreshToken(refreshToken)
            .setRedirectUri(redirectUri)
            .build()

    val credentials = spotifyApi.authorizationCodeRefresh().build().execute()
    spotifyApi.accessToken = credentials.accessToken
    return spotifyApi
}

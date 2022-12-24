package com.github.mvollebregt.concerts.parser

import com.github.mvollebregt.concerts.integration.spotifyApi
import com.github.mvollebregt.concerts.model.Artist
import normalizeText

/**
 * Searches the artist database for an artist with the specified name and returns it if it exists. If it does not exist,
 * it searches Spotify for the same artist and returns if. If the artist does not exist in Spotify either, returns null.
 */
fun findArtist(artistName: String): Artist? {
    val trimmedArtistName = artistName.trim(' ', '+')
    return if (trimmedArtistName.isEmpty()) null
    else spotifyApi.searchArtists(trimmedArtistName).build().execute().items
            .find { normalizeText(it.name) == normalizeText(artistName) }
            ?.let {
                Artist(
                        uri = it.uri,
                        name = it.name
                )
            }
}

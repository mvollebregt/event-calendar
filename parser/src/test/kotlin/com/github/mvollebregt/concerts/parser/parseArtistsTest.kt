package com.github.mvollebregt.concerts.parser

import com.github.mvollebregt.concerts.model.Artist
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness

private const val URI = "Uri"
private const val KNOWN_ARTIST_NAME = "Known artist"
private const val KNOWN_ARTIST_NAME_WITH_SPLIT_CHARACTER = "Simon & Garfunkel"
private const val KNOWN_ARTIST_NAME_WITH_PREFIX_CHARACTER = "Songs: Ohia"
private const val UNKNOWN_ARTIST_NAME = "Unknown artist"

private val knownArtistNames =
        listOf(KNOWN_ARTIST_NAME, KNOWN_ARTIST_NAME_WITH_SPLIT_CHARACTER, KNOWN_ARTIST_NAME_WITH_PREFIX_CHARACTER)

private val knownArtist = Artist(URI, KNOWN_ARTIST_NAME)

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ParseArtistsTest {

    @Mock
    lateinit var findArtist: (String) -> Artist?

    @BeforeEach
    fun setUp() {
        knownArtistNames.forEach {
            `when`(findArtist(it)).thenReturn(Artist(URI, it))
        }
    }

    @ParameterizedTest
    @MethodSource("testCases")
    fun `parsing artists should return the correct artists`(
            textContainingArtists: String,
            expectedResult: List<Artist>
    ) {
        assertThat(parseArtists(textContainingArtists, findArtist)).isEqualTo(expectedResult)
    }

    companion object {
        @JvmStatic
        fun testCases(): List<Arguments> {
            return listOf(
                    arguments(
                            KNOWN_ARTIST_NAME,
                            listOf(knownArtist)
                    ),
                    arguments(
                            KNOWN_ARTIST_NAME_WITH_SPLIT_CHARACTER,
                            listOf(Artist(URI, KNOWN_ARTIST_NAME_WITH_SPLIT_CHARACTER))
                    ),
                    arguments(
                            KNOWN_ARTIST_NAME_WITH_PREFIX_CHARACTER,
                            listOf(Artist(URI, KNOWN_ARTIST_NAME_WITH_PREFIX_CHARACTER))
                    ),
                    arguments(
                            UNKNOWN_ARTIST_NAME,
                            emptyList<Artist>()
                    ),
                    arguments(
                            "prefix: $KNOWN_ARTIST_NAME",
                            listOf(knownArtist)
                    ),
                    arguments(
                            "prefix: $UNKNOWN_ARTIST_NAME",
                            emptyList<Artist>()
                    ),
                    arguments(
                            "$KNOWN_ARTIST_NAME & $KNOWN_ARTIST_NAME",
                            listOf(knownArtist, knownArtist)
                    ),
                    arguments(
                            "$KNOWN_ARTIST_NAME & $UNKNOWN_ARTIST_NAME",
                            listOf(knownArtist)
                    ),
                    arguments(
                            "$UNKNOWN_ARTIST_NAME & $UNKNOWN_ARTIST_NAME",
                            emptyList<Artist>()
                    ),
                    arguments(
                            "$KNOWN_ARTIST_NAME_WITH_SPLIT_CHARACTER + $KNOWN_ARTIST_NAME",
                            listOf(Artist(URI, KNOWN_ARTIST_NAME_WITH_SPLIT_CHARACTER), knownArtist)
                    ),
                    arguments(
                            "$KNOWN_ARTIST_NAME_WITH_PREFIX_CHARACTER & $KNOWN_ARTIST_NAME",
                            listOf(Artist(URI, KNOWN_ARTIST_NAME_WITH_PREFIX_CHARACTER), knownArtist)
                    ),
                    arguments(
                            "prefix: $UNKNOWN_ARTIST_NAME & $KNOWN_ARTIST_NAME",
                            listOf(knownArtist)
                    ),
                    arguments(
                            "$UNKNOWN_ARTIST_NAME & $UNKNOWN_ARTIST_NAME + $UNKNOWN_ARTIST_NAME",
                            emptyList<Artist>()
                    )
            )
        }
    }
}

package com.github.mvollebregt.concerts.parser

import com.github.mvollebregt.concerts.model.Artist

private const val SPLIT_CHARACTERS = "+&"
private const val PREFIX_CHARACTERS = ":"
private val TEXTS_THAT_ARE_NOT_ARTISTS = listOf("eurosonic", "more")

/**
 * Takes a text containing one or more artists and extracts the individual artists from the text.
 * Artists may be separated by characters like comma's or pluses, and may contain extra information like "Support".
 * The returned artists are checked against a database of known artists and the result of that check will be included
 * in the "verified" property of the artist.
 */
fun parseArtists(
    textContainingArtists: String,
    findArtist: (String) -> Artist? = ::findArtist
): List<Artist> {
    // check the whole text against the database
    val trimmedText = textContainingArtists.trim()
    return if (TEXTS_THAT_ARE_NOT_ARTISTS.contains(trimmedText.lowercase()))
        emptyList()
    else {
        val wholeTextResult = listOfNotNull(findArtist(trimmedText))
        // if the text contains split characters, split it
        val splitResults = splitByFirstFoundCharacter(textContainingArtists, SPLIT_CHARACTERS).ifEmpty {
            // otherwise, try to remove special prefixes from the text
            listOfNotNull(splitByFirstFoundCharacter(textContainingArtists, PREFIX_CHARACTERS).lastOrNull())
        }.flatMap { parseArtists(it, findArtist) }
        wholeTextResult + splitResults
    }
}

/**
 * Finds the first character of the delimiters that is contained in textToSplit, and splits textToSplit around that
 * delimiter. If none of the delimiters is found, returns an empty list.
 */
private fun splitByFirstFoundCharacter(textToSplit: String, delimiters: String): List<String> =
    delimiters.find(textToSplit::contains)?.let { textToSplit.split(it) } ?: emptyList()

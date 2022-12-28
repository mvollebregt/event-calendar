package com.github.mvollebregt.concerts.parser

import com.github.mvollebregt.concerts.model.Venue
import java.util.*

data class ParseSpec<N, S>(
    val document: Document<N, S>,
    val concertSelector: S,
    val linkSelector: S,
    val titleSelector: S,
    val artistSelector: S,
    val dateSelector: S,
    val datePattern: String,
    val dateLocale: Locale = Locale.forLanguageTag("nl-NL"),
    val venue: Venue
)

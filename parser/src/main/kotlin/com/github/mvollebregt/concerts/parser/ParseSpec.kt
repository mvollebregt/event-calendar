package com.github.mvollebregt.concerts.parser

import com.github.mvollebregt.concerts.model.Venue

data class ParseSpec<N, S>(
    val document: Document<N, S>,
    val concertSelector: S,
    val linkSelector: S,
    val titleSelector: S,
    val artistSelector: S,
    val dateSelector: S,
    val datePattern: String,
    val venue: Venue
)

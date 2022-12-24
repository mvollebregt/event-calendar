package com.github.mvollebregt.concerts.model

import java.time.LocalDate

data class Concert(
        val uri: String,
        val title: String,
        val artists: List<Artist>,
        val date: LocalDate,
        val venue: Venue
)

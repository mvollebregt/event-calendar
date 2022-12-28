package com.github.mvollebregt.concerts.parser

import com.github.mvollebregt.concerts.model.Concert
import java.time.LocalDate
import java.time.MonthDay
import java.time.format.DateTimeFormatter
import java.util.*

fun <N, S> parseConcerts(spec: ParseSpec<N, S>): List<Concert> =
    spec.document.selectNodes(spec.concertSelector).map { concertElement ->
        Concert(
            uri = spec.document.selectLinkText(concertElement, spec.linkSelector),
            title = spec.document.selectText(concertElement, spec.titleSelector),
            artists = spec.document.selectTexts(concertElement, spec.artistSelector).flatMap { parseArtists(it) },
            date = parseDate(spec.document.selectDateText(concertElement, spec.dateSelector), spec.datePattern),
            venue = spec.venue
        )
    }

private fun parseDate(dateText: String, datePattern: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern(datePattern, Locale.ENGLISH)
    val monthDay = MonthDay.parse(dateText, formatter)
    val now = LocalDate.now()
    val monthDayAtThisYear = monthDay.atYear(now.year)
    return if (monthDayAtThisYear.isAfter(now)) monthDayAtThisYear else monthDay.atYear(now.year + 1)
}

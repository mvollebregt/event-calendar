package com.github.mvollebregt.concerts.parser

import com.github.mvollebregt.concerts.model.Concert
import java.time.Instant
import java.time.LocalDate
import java.time.MonthDay
import java.time.ZoneId
import java.time.format.DateTimeFormatterBuilder
import java.util.*

const val secondsSinceEpoch = "seconds since epoch"

fun <N, S> parseConcerts(spec: ParseSpec<N, S>): List<Concert> =
    spec.document.selectNodes(spec.concertSelector)
        .map { concertElement ->
            Concert(
                uri = spec.transform(spec.document.selectLinkText(concertElement, spec.linkSelector)),
                title = spec.transform(spec.document.selectText(concertElement, spec.titleSelector)),
                artists = spec.document.selectTexts(concertElement, spec.artistSelector)
                    .flatMap { parseArtists(spec.transform(it), spec.exclude) },
                date = parseDate(
                    spec.transform(spec.document.selectDateText(concertElement, spec.dateSelector)),
                    spec.datePattern,
                    spec.dateLocale
                ),
                venue = spec.venue
            )
        }
        .filter { concert -> concert.artists.isNotEmpty() }

private fun parseDate(dateText: String, datePattern: String, locale: Locale): LocalDate {
    return if (datePattern == secondsSinceEpoch) {
        Instant.ofEpochSecond(dateText.toLong()).atZone(ZoneId.of("Europe/Amsterdam")).toLocalDate()
    } else {
        val formatter = DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern(datePattern).toFormatter(locale)
        val monthDay = MonthDay.parse(dateText, formatter)
        val now = LocalDate.now()
        val monthDayAtThisYear = monthDay.atYear(now.year)
        if (monthDayAtThisYear.isAfter(now)) monthDayAtThisYear else monthDay.atYear(now.year + 1)
    }
}

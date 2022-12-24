package com.github.mvollebregt.concerts.parser

import com.github.mvollebregt.concerts.model.Concert
import com.github.mvollebregt.concerts.model.Venue
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.time.LocalDate
import java.time.MonthDay
import java.time.format.DateTimeFormatter
import java.util.*

fun parseConcerts(
        url: String,
        linkSelector: String,
        concertSelector: String,
        titleSelector: String,
        artistSelector: String,
        dateSelector: String,
        datePattern: String,
        venue: Venue
): List<Concert> =
        Jsoup.connect(url).get().select(concertSelector).map { concertElement ->
            Concert(
                    uri = concertElement.select(linkSelector).attr("href"),
                    title = selectText(concertElement, titleSelector).joinToString(" "),
                    artists = selectText(concertElement, artistSelector).flatMap { parseArtists(it) },
                    date = selectDate(concertElement, dateSelector, datePattern),
                    venue = venue
            )
        }

fun main() {
    parseConcerts(
            url = "https://www.vera-groningen.nl/wp/wp-admin/admin-ajax.php?action=renderProgramme&category=concert&page=1&perpage=80",
            linkSelector = ".event-link",
            concertSelector = ".event-wrapper",
            titleSelector = ".artist",
            artistSelector = ".artist, .extra",
            dateSelector = ".date",
            datePattern = "EEEE d MMMM",
            venue = Venue("VERA Groningen", "https://www.vera-groningen.nl")
    ).forEach { println(it) }
}

private fun selectText(element: Element, selector: String): List<String> =
        element.select(selector).textNodes().map { it.text() }

private fun selectDate(element: Element, selector: String, datePattern: String): LocalDate {
    val dateText = selectText(element, selector).joinToString(" ").trim()
    val formatter = DateTimeFormatter.ofPattern(datePattern, Locale.ENGLISH)
    val monthDay = MonthDay.parse(dateText, formatter)
    val now = LocalDate.now()
    val monthDayAtThisYear = monthDay.atYear(now.year)
    return if (monthDayAtThisYear.isAfter(now)) monthDayAtThisYear else monthDay.atYear(now.year + 1)
}

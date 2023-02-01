package com.github.mvollebregt.concerts

import com.github.mvollebregt.concerts.model.Venue
import com.github.mvollebregt.concerts.parser.*
import com.github.mvollebregt.concerts.parser.html.CssSelector
import com.github.mvollebregt.concerts.parser.html.HtmlDocument
import com.github.mvollebregt.concerts.parser.json.JsonDocument
import com.github.mvollebregt.concerts.parser.json.jsonPath
import com.github.mvollebregt.concerts.parser.json.multipleJsonPaths
import org.jsoup.parser.Parser
import java.util.*

fun main() {
    parseConcerts(paradiso).forEach { println(it) }
}

val vera = ParseSpec(
    document = HtmlDocument("https://www.vera-groningen.nl/wp/wp-admin/admin-ajax.php?action=renderProgramme&category=concert&page=1&perpage=80"),
    concertSelector = CssSelector(".event-wrapper"),
    linkSelector = CssSelector(".event-link"),
    titleSelector = CssSelector(".artist", directTextNodesOnly = true),
    artistSelector = CssSelector(".artist, .extra", directTextNodesOnly = true),
    exclude = textEquals("eurosonic"),
    dateSelector = CssSelector(".date"),
    datePattern = "EEEE d MMMM",
    dateLocale = Locale.ENGLISH,
    venue = Venue("VERA Groningen", "https://www.vera-groningen.nl")
)

val spot = ParseSpec(
    document = HtmlDocument("https://www.spotgroningen.nl/programma/#genres=muziek"),
    concertSelector = CssSelector(".program__item[data-genres*=\"muziek\"]"),
    linkSelector = CssSelector(".program__link"),
    titleSelector = CssSelector("h1"),
    artistSelector = CssSelector("h1"),
    dateSelector = CssSelector("time", "datetime"),
    datePattern = "yyyy-MM-dd'T'HH:mm:ssXXX",
    venue = Venue("Spot Groningen", "https://www.spotgroningen.nl")
)

val simplon = ParseSpec(
    document = HtmlDocument("https://www.simplon.nl"),
    concertSelector = CssSelector("a.item"),
    linkSelector = CssSelector(":root"),
    titleSelector = CssSelector(".title"),
    artistSelector = CssSelector(".title, .subtitle"),
    dateSelector = CssSelector(".date"),
    datePattern = "EE d MMMM yyyy",
    venue = Venue("Simplon Groningen", "https://www.simplon.nl")
)

val neushoorn = ParseSpec(
    document = JsonDocument("https://neushoorn.nl/wp-json/production/v1/all/"),
    concertSelector = jsonPath(),
    linkSelector = jsonPath("permalink"),
    titleSelector = jsonPath("title"),
    artistSelector = multipleJsonPaths("title", "subtitle"),
    exclude = textStartsWith("NH Café", "Xolar") + textEquals("REMIND", "UNPLUGGED", "Dubstance"),
    dateSelector = jsonPath("event", "datetime"),
    datePattern = secondsSinceEpoch,
    venue = Venue("Neushoorn Leeuwarden", "https://www.neushoorn.nl"),
    transform = { Parser.unescapeEntities(it, true) }
)

val paradiso = ParseSpec(
    document = JsonDocument("https://api.paradiso.nl/api/events?lang=nl&start_time=now&sort=date&order=asc&categories=1100&limit=200&with=locations"),
    concertSelector = jsonPath(),
    linkSelector = jsonPath("ticket_url"),
    titleSelector = jsonPath("title"),
    artistSelector = jsonPath("title"),
    dateSelector = jsonPath("start_date_time"),
    datePattern = "yyyy-MM-dd HH:mm:ss",
    venue = Venue("Paradiso Amsterdam", "https://www.paradiso.nl"),
    transform = { it.replace("\\/", "/") }
)

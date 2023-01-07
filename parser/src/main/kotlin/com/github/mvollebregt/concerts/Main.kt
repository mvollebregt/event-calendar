package com.github.mvollebregt.concerts

import com.github.mvollebregt.concerts.model.Venue
import com.github.mvollebregt.concerts.parser.ParseSpec
import com.github.mvollebregt.concerts.parser.html.CssSelector
import com.github.mvollebregt.concerts.parser.html.HtmlDocument
import com.github.mvollebregt.concerts.parser.json.JsonDocument
import com.github.mvollebregt.concerts.parser.json.JsonPath
import com.github.mvollebregt.concerts.parser.json.MultipleJsonPaths
import com.github.mvollebregt.concerts.parser.parseConcerts
import com.github.mvollebregt.concerts.parser.secondsSinceEpoch
import java.util.*

fun main() {
    parseConcerts(neushoorn).forEach { println(it) }
}

val vera = ParseSpec(
    document = HtmlDocument("https://www.vera-groningen.nl/wp/wp-admin/admin-ajax.php?action=renderProgramme&category=concert&page=1&perpage=80"),
    concertSelector = CssSelector(".event-wrapper"),
    linkSelector = CssSelector(".event-link"),
    titleSelector = CssSelector(".artist", directTextNodesOnly = true),
    artistSelector = CssSelector(".artist, .extra", directTextNodesOnly = true),
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
    concertSelector = JsonPath(),
    linkSelector = JsonPath("permalink"),
    titleSelector = JsonPath("title"),
    artistSelector = MultipleJsonPaths("title", "subtitle"),
    dateSelector = JsonPath("event", "datetime"),
    datePattern = secondsSinceEpoch,
    venue = Venue("Neushoorn Leeuwarden", "https://www.neushoorn.nl")
)

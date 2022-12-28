package com.github.mvollebregt.concerts

import com.github.mvollebregt.concerts.model.Venue
import com.github.mvollebregt.concerts.parser.ParseSpec
import com.github.mvollebregt.concerts.parser.html.CssSelector
import com.github.mvollebregt.concerts.parser.html.HtmlDocument
import com.github.mvollebregt.concerts.parser.parseConcerts

fun main() {
    parseConcerts(vera).forEach { println(it) }
}

val vera = ParseSpec(
    document = HtmlDocument("https://www.vera-groningen.nl/wp/wp-admin/admin-ajax.php?action=renderProgramme&category=concert&page=1&perpage=80"),
    concertSelector = CssSelector(".event-wrapper"),
    linkSelector = CssSelector(".event-link"),
    titleSelector = CssSelector(".artist", directTextNodesOnly = true),
    artistSelector = CssSelector(".artist, .extra", directTextNodesOnly = true),
    dateSelector = CssSelector(".date"),
    datePattern = "EEEE d MMMM",
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

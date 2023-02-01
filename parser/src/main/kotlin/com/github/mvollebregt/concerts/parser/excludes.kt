package com.github.mvollebregt.concerts.parser

fun textEquals(vararg excludes: String) =
    any(excludes) { text, exclude -> text.equals(exclude, ignoreCase = true) }

fun textContains(vararg excludes: String) =
    any(excludes) { text, exclude -> text.contains(exclude, ignoreCase = true) }

fun textStartsWith(vararg excludes: String) =
    any(excludes) { text, exclude -> text.startsWith(exclude, ignoreCase = true) }

private fun any(excludes: Array<out String>, condition: (String, String) -> Boolean) =
    listOf { text: String -> excludes.any { condition(text, it) } }

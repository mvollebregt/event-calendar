package com.github.mvollebregt.concerts.parser.json

data class JsonSelector(
    val paths: List<List<String>>
)

fun jsonPath(vararg path: String) = JsonSelector(listOf(path.asList()))

fun multipleJsonPaths(vararg singleElementPaths: String) = JsonSelector(singleElementPaths.asList().map { listOf(it) })

fun multipleJsonPaths(vararg jsonSelectors: JsonSelector) = JsonSelector(jsonSelectors.map { it.paths }.flatten())

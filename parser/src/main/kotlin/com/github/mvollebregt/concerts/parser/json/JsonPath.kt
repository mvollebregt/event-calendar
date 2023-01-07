package com.github.mvollebregt.concerts.parser.json

class JsonPath(
    vararg val path: String
) : JsonSelector {
    override val paths: List<List<String>>
        get() = listOf(path.asList())

}

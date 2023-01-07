package com.github.mvollebregt.concerts.parser.json

class MultipleJsonPaths : JsonSelector {

    override val paths: List<List<String>>

    constructor(vararg singleElementPaths: String) {
        this.paths = singleElementPaths.asList().map { listOf(it) }
    }

    constructor(vararg jsonPaths: JsonPath) {
        this.paths = jsonPaths.asList().map { it.path.asList() }
    }
}

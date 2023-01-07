package com.github.mvollebregt.concerts.parser.json

interface JsonSelector { // TODO: make this a data class and create functions to create instances, instead of subclasses
    val paths: List<List<String>>
}

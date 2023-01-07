package com.github.mvollebregt.concerts.parser.json

import com.github.mvollebregt.concerts.parser.Document
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.net.URL

class JsonDocument(private val url: String) : Document<JsonObject, JsonSelector> {

    override fun selectNodes(selector: JsonSelector): Iterable<JsonObject> {
        val rootElement = JsonParser.parseString(URL(url).readText())
        return (if (selector.paths.isEmpty()) applySelector(rootElement, selector)
        else rootElement) as Iterable<JsonObject>
    }

    override fun selectTexts(node: JsonObject, selector: JsonSelector): List<String> =
        applySelector(node, selector).map { element -> element.asString }

    private fun applySelector(element: JsonElement, selector: JsonSelector): List<JsonElement> =
        selector.paths.map { path ->
            path.fold(element) { element, selector -> (element.asJsonObject)[selector] }
        }

}

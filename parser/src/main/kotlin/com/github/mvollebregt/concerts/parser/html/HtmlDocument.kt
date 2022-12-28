package com.github.mvollebregt.concerts.parser.html

import com.github.mvollebregt.concerts.parser.Document
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class HtmlDocument(private val url: String) : Document<Element, CssSelector> {

    override fun selectNodes(selector: CssSelector): Iterable<Element> =
        Jsoup.connect(url).get().select(selector.query)

    override fun selectText(node: Element, selector: CssSelector): String =
        if (selector.attribute != null)
            node.select(selector.query).attr(selector.attribute)
        else
            selectTexts(node, selector).joinToString(" ").trim()

    override fun selectTexts(node: Element, selector: CssSelector): List<String> =
        node.select(selector.query).flatMap {
            val selectedNodes = if (selector.directTextNodesOnly) it.textNodes() else it.allElements.textNodes()
            selectedNodes.map { it.text() }
        }

    override fun selectLinkText(node: Element, selector: CssSelector): String =
        selectText(node, selector.copy(attribute = "href"))

    override fun selectDateText(node: Element, selector: CssSelector): String =
        selectText(node, selector.copy(directTextNodesOnly = true))
}

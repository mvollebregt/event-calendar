package com.github.mvollebregt.concerts.parser

/**
 * A document that contains events.
 * @param N     the type of a single node containing an event
 * @param S     the selector type that is used to select parts of the event
 */
interface Document<N, S> {

    /**
     * Selects a list of event nodes from the document, where each node contains an event.
     */
    fun selectNodes(selector: S): Iterable<N>

    /**
     * Selects a text from a single node by applying the selector.
     */
    fun selectText(node: N, selector: S): String =
        selectTexts(node, selector).joinToString(" ")

    /**
     * Selects a number of texts from a single node by applying the selector.
     */
    fun selectTexts(node: N, selector: S): List<String>

    /**
     * Selects a link text from a single node by applying the selector.
     */
    fun selectLinkText(node: N, selector: S): String = selectText(node, selector)

    /**
     * Selects a date text from a single node by applying the selector.
     */
    fun selectDateText(node: N, selector: S): String = selectText(node, selector)

}

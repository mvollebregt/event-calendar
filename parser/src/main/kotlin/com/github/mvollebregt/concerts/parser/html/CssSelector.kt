package com.github.mvollebregt.concerts.parser.html

data class CssSelector(
    val query: String,
    val attribute: String? = null,
    val directTextNodesOnly: Boolean = false
)


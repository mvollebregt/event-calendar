fun normalizeText(artistName: String): String =
        artistName.trim().lowercase().split("\\W+".toRegex()).joinToString(" ")

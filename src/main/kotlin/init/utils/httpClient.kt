package org.example.init.utils

import okio.IOException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.system.exitProcess


fun getHtmlJsoupDocument(url: String, shouldRetry: Boolean? = true): Document {
    try {
        println("Pobieram dane z: $url")
        return Jsoup.connect(url).get()
    } catch (_: IOException) {
        println("Nie udało się pobrać danych z: $url")
        if (shouldRetry!!) {
            Thread.sleep(5000)
            return getHtmlJsoupDocument(url, false)
        } else {
            exitProcess(0)
        }
    }
}

package tlp.media.client

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import tlp.media.server.model.MangaWithChapter
import tlp.media.server.model.MangasPage
import tlp.media.server.model.Page

class TlpResponseUtil {
    companion object {
        val gson = Gson()
    }
    fun parseMangasPage(input: String): MangasPage {
        return gson.fromJson(input)
    }

    fun parseManga(input: String): MangaWithChapter {
        return gson.fromJson(input)
    }

    fun parsePageList(input: String): List<Page> {
        return gson.fromJson(input)
    }
}

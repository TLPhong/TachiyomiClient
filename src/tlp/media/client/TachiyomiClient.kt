package tlp.media.client

import android.app.Application
import android.content.SharedPreferences
import android.widget.Toast
import eu.kanade.tachiyomi.source.ConfigurableSource
import eu.kanade.tachiyomi.source.model.FilterList
import eu.kanade.tachiyomi.source.model.MangasPage
import eu.kanade.tachiyomi.source.model.Page
import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.source.model.SManga
import eu.kanade.tachiyomi.source.online.HttpSource
import okhttp3.Request
import okhttp3.Response
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class TachiyomiClient : HttpSource(), ConfigurableSource {
    override val lang: String = "en"
    override val name: String = "Home server"
    override val supportsLatest: Boolean = true
    val host: String by lazy { getPrefHost() }
    val port: Int by lazy { getPrefPort() }
    override val baseUrl: String by lazy { getPrefBaseUrl() }

    private val requests by lazy { TlpRequests(host, port) }
    private val parser = TlpResponseUtil()

    private val preferences: SharedPreferences by lazy {
        Injekt.get<Application>().getSharedPreferences("source_$id", 0x0000)
    }

    override fun popularMangaRequest(page: Int): Request {
        return requests.popularMangas(page, pageSize = 50)
    }

    override fun searchMangaRequest(page: Int, query: String, filters: FilterList): Request {
        return requests.searchMangas(query, page, pageSize = 50)
    }

    override fun latestUpdatesRequest(page: Int): Request {
        return requests.latestMangas(page, pageSize = 50)
    }

    override fun latestUpdatesParse(response: Response): MangasPage {
        return handleMangasPageRespond(response, "Malformed latest mangas response")
    }

    override fun popularMangaParse(response: Response): MangasPage {
        return handleMangasPageRespond(response, "Malformed popular mangas response")
    }

    override fun searchMangaParse(response: Response): MangasPage {
        return handleMangasPageRespond(response, "Malformed search mangas response")
    }

    private fun handleMangasPageRespond(response: Response, errorMessage: String): MangasPage {
        val responseContent = response.body?.string() ?: error(errorMessage)
        val parseMangasPage = parser.parseMangasPage(responseContent)
        return parseMangasPage.toTachiyomiModel()
    }

    override fun imageUrlParse(response: Response): String {
        TODO("Not yet implemented")
    }

    override fun mangaDetailsParse(response: Response): SManga {
        val responseContent = response.body?.string() ?: error("Malformed manga detail response")
        val mangaWithChapter = parser.parseManga(responseContent)
        return mangaWithChapter.manga.toTachiyomiModel()
    }

    override fun chapterListParse(response: Response): List<SChapter> {
        val responseContent = response.body?.string() ?: error("Malformed chapter list response")
        val mangaWithChapter = parser.parseManga(responseContent)
        val chapter = mangaWithChapter.chapter.toTachiyomiModel()
        return listOf(chapter)
    }

    override fun pageListParse(response: Response): List<Page> {
        val responseContent = response.body?.string() ?: error("Malformed page list response")
        val parsePageList = parser.parsePageList(responseContent)
        return parsePageList.map { it.toTachiyomiModel() }.toList()
    }

    /***
     * See Komga.setupPreferenceScreen
     */
    override fun setupPreferenceScreen(screen: androidx.preference.PreferenceScreen) {
        screen.addPreference(screen.editTextPreference(HOST_TITLE, HOST_DEFAULT))
        screen.addPreference(screen.editTextPreference(PORT_TITLE, PORT_DEFAULT.toString()))
    }

    private fun getPrefBaseUrl(): String =
        "http://$host:$port"

    private fun getPrefHost(): String =
        this.preferences.getString(HOST_TITLE, HOST_DEFAULT)!!

    private fun getPrefPort(): Int =
        this.preferences.getString(PORT_TITLE, PORT_DEFAULT.toString())!!.toInt()

    private fun androidx.preference.PreferenceScreen.editTextPreference(
        title: String,
        default: String
    ): androidx.preference.EditTextPreference {
        return androidx.preference.EditTextPreference(context).apply {
            key = title
            this.title = title
            summary = ""
            this.setDefaultValue(default)
            dialogTitle = title

            setOnPreferenceChangeListener { _, newValue ->
                try {
                    val res = preferences.edit().putString(title, newValue as String).commit()
                    Toast.makeText(
                        context,
                        "Restart Tachiyomi to apply new setting.",
                        Toast.LENGTH_LONG
                    ).show()
                    res
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            }
        }
    }

    companion object {
        private const val HOST_TITLE = "Host"
        private const val HOST_DEFAULT = "192.168.86.3"
        private const val PORT_TITLE = "Port"
        private const val PORT_DEFAULT = 8080
    }
}

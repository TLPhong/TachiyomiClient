package tlp.media.client

import tlp.media.server.model.Chapter
import tlp.media.server.model.Manga
import tlp.media.server.model.MangaWithChapter
import tlp.media.server.model.MangasPage
import tlp.media.server.model.Page
import eu.kanade.tachiyomi.source.model.MangasPage as TachiyomiMangasPage
import eu.kanade.tachiyomi.source.model.Page as TachiyomiPage
import eu.kanade.tachiyomi.source.model.SChapter as TachiyomiChapter
import eu.kanade.tachiyomi.source.model.SManga as TachiyomiManga

fun MangasPage.toTachiyomiModel(): TachiyomiMangasPage {
    val tachiyomiMangaList = this.mangas.map { it.toTachiyomiModel() }.toList()

    return TachiyomiMangasPage(
        tachiyomiMangaList,
        hasNextPage = this.hasNextPage
    )
}

fun Manga.toTachiyomiModel(): TachiyomiManga {
    return TachiyomiManga.create().let {
        it.url = this.url
        it.title = this.title
        it.artist = this.artist
        it.author = this.author
        it.description = this.description
        it.genre = this.genre
        it.status = this.status
        it.thumbnail_url = this.thumbnail_url
        it.initialized = this.initialized
        it
    }
}

fun MangaWithChapter.toTachiyomiModel(): TachiyomiManga {
    val manga = this.manga
    return TachiyomiManga.create().let {
        it.url = manga.url
        it.title = manga.title
        it.artist = manga.artist
        it.author = manga.author
        it.description = manga.description
        it.genre = manga.genre
        it.status = manga.status
        it.thumbnail_url = manga.thumbnail_url
        it.initialized = manga.initialized
        it
    }
}

fun Chapter.toTachiyomiModel(): TachiyomiChapter {
    return TachiyomiChapter.create().let {
        it.url = this.url
        it.chapter_number = this.chapter_number
        it.date_upload = this.date_upload
        it.name = this.name
        it.scanlator = this.scanlator
        it
    }
}

fun Page.toTachiyomiModel(): TachiyomiPage {
    return TachiyomiPage(
        index = this.index,
        imageUrl = this.imageUrl,
        url = "",
        uri = null
    )
}

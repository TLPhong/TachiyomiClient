package tlp.media.server.model

import eu.kanade.tachiyomi.source.model.SManga

data class Manga(
    override var artist: String?,
    override var description: String?,
    override var thumbnail_url: String?,
    override var title: String,
    override var url: String
) : SManga {
    override var author: String? = null
    override var genre: String? = null
    override var status: Int = SManga.COMPLETED
    override var initialized: Boolean = true
}

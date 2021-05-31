package eu.kanade.tachiyomi.extension.all.tlp

import eu.kanade.tachiyomi.source.Source
import eu.kanade.tachiyomi.source.SourceFactory
import tlp.media.client.TachiyomiClient

class TlpFactory : SourceFactory {

    override fun createSources(): List<Source> =
        listOf(
            TachiyomiClient(),
        )
}

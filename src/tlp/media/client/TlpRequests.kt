package tlp.media.client

import okhttp3.HttpUrl
import okhttp3.Request

class TlpRequests(private val host: String, private val port: Int) {

    fun latestMangas(page: Int, pageSize: Int = 60): Request {
        val httpUrl = urlBuilder()
            .addPathSegment("latest")
            .addQueryParameter("page", page.toString())
            .addQueryParameter("size", pageSize.toString())
            .build()

        return Request.Builder()
            .url(httpUrl.toUrl())
            .get()
            .build()
    }

    fun popularMangas(page: Int, pageSize: Int = 60): Request {
        val httpUrl = urlBuilder()
            .addPathSegment("popular")
            .addQueryParameter("page", page.toString())
            .addQueryParameter("size", pageSize.toString())
            .build()

        return Request.Builder()
            .url(httpUrl.toUrl())
            .get()
            .build()
    }

    fun searchMangas(query: String, page: Int, pageSize: Int = 60): Request {
        val httpUrl = urlBuilder()
            .addPathSegment("search")
            .addQueryParameter("query", query)
            .addQueryParameter("page", page.toString())
            .addQueryParameter("size", pageSize.toString())
            .build()

        return Request.Builder()
            .url(httpUrl.toUrl())
            .get()
            .build()
    }

    private fun urlBuilder(): HttpUrl.Builder {
        return HttpUrl.Builder()
            .scheme("http")
            .host(host)
            .port(port)
            .addPathSegment("api")
    }
}

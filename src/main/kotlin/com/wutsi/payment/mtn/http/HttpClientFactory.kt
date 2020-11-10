package com.wutsi.payment.mtn.http

import org.apache.http.client.HttpClient
import org.apache.http.client.config.RequestConfig
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.TrustAllStrategy
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.client.LaxRedirectStrategy
import org.apache.http.ssl.SSLContextBuilder


class HttpClientFactory {
    fun httpClient(): HttpClient =
        HttpClients
                .custom()
                .setDefaultRequestConfig(
                        RequestConfig.copy(RequestConfig.DEFAULT)
                                .setConnectTimeout(30000)
                                .setSocketTimeout(30000)
                                .setConnectionRequestTimeout(30000)
                                .build()
                )
                .setRedirectStrategy(LaxRedirectStrategy())
                .setSSLContext(SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build()
}

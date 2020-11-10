package com.wutsi.payment.mtn.http

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.payment.mtn.MtnException
import com.wutsi.payment.mtn.model.MtnError
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity


class MtnHttp(
        private val httpClient: HttpClient
) {
    private val objectMapper: ObjectMapper = objectMapper()

    fun <T> get(
            uri: String,
            responseType: Class<T>,
            headers: Map<String, String> = emptyMap()
    ): T? {
        val request = HttpGet(uri)
        headers.keys.map { request.addHeader(it, headers[it]) }

        val response = httpClient.execute(request)
        if (response.statusLine.statusCode/100 == 2) {
            return toResponseBody(response, responseType)
        } else {
            throw toMtnException(response)
        }
    }

    fun <T> post(
            uri: String,
            requestPayload: Any,
            responseType: Class<T>,
            headers: Map<String, String> = emptyMap()
    ): T? {
        val request = HttpPost(uri)
        headers.keys.map { request.addHeader(it, headers[it]) }

        val json = objectMapper.writeValueAsString(requestPayload)
        request.entity = StringEntity(json)

        val response = httpClient.execute(request)
        if (response.statusLine.statusCode/100 == 2) {
            return toResponseBody(response, responseType)
        } else {
            throw toMtnException(response)
        }
    }

    private fun <T> toResponseBody(response: HttpResponse, responseType: Class<T>): T? {
        val entity = response.entity ?: return null

        return if (entity.contentLength == 0L)
            null
        else
            objectMapper.readValue(entity.content, responseType)
    }

    private fun toMtnException(response: HttpResponse): MtnException {
        return MtnException(
                statusCode = response.statusLine.statusCode,
                message = response.statusLine.reasonPhrase,
                error = toMtnError(response)
        )
    }

    private fun toMtnError (response: HttpResponse): MtnError? {
        val body = response.entity?.content
                ?: return null
        try {
            return objectMapper.readValue(body, MtnError::class.java)
        } catch(ex: Exception) {
            return null
        }
    }

    private fun objectMapper () : ObjectMapper {
        val mapper = ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        return mapper
    }

}

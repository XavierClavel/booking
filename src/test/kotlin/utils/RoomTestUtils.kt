package com.xavierclavel.utils

import com.xavierclavel.dtos.RoomIn
import com.xavierclavel.dtos.RoomOut
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlin.test.assertEquals

suspend fun HttpClient.getRoom(id: Long): RoomOut  {
    this.get("$ROOMS_URL/$id").apply {
        assertEquals(HttpStatusCode.OK, status)
        val room = Json.decodeFromString<RoomOut>(bodyAsText())
        return room
    }
}

suspend fun HttpClient.listRooms(): List<RoomOut>  {
    this.get(ROOMS_URL).apply {
        assertEquals(HttpStatusCode.OK, status)
        val rooms = Json.decodeFromString<List<RoomOut>>(bodyAsText())
        return rooms
    }
}

suspend fun HttpClient.createRoom(dto: RoomIn): RoomOut  {
    this.post(ROOMS_URL){
        contentType(ContentType.Application.Json)
        setBody(dto)
    }.apply {
        assertEquals(HttpStatusCode.OK, status)
        return Json.decodeFromString<RoomOut>(bodyAsText())
    }
}

suspend fun HttpClient.assertRoomExists(id: Long) {
    this.get("$ROOMS_URL/$id").apply {
        assertEquals(HttpStatusCode.OK, status)
    }
}

suspend fun HttpClient.assertRoomDoesNotExist(id: Long) {
    this.get("$ROOMS_URL/$id").apply {
        assertEquals(HttpStatusCode.NotFound, status)
    }
}
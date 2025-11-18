package com.xavierclavel.controllers

import com.xavierclavel.ApplicationTest
import com.xavierclavel.dtos.RoomIn
import com.xavierclavel.dtos.SignupDto
import com.xavierclavel.utils.ROOMS_URL
import com.xavierclavel.utils.createRoom
import com.xavierclavel.utils.listUsers
import com.xavierclavel.utils.signup
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RoomControllerTest: ApplicationTest() {

    @Test
    fun `non admin user cannot create rooms`() = runTestAsUser {
        val dto = RoomIn(name = "Room1", capacity = 50)
        client.post(ROOMS_URL){
            contentType(ContentType.Application.Json)
            setBody(dto)
        }.apply {
            assertEquals(HttpStatusCode.Forbidden, status)
        }
    }

    @Test
    fun `admin users can create rooms`() = runTestAsAdmin {
        val dto = RoomIn(name = "Room1", capacity = 50)

        val result = client.createRoom(dto)

        assertEquals("Room1", result.name)
    }



}
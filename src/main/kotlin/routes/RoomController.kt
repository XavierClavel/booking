package com.xavierclavel.routes

import com.xavierclavel.dtos.RoomIn
import com.xavierclavel.dtos.UserIn
import com.xavierclavel.exceptions.ForbiddenCause
import com.xavierclavel.exceptions.ForbiddenException
import com.xavierclavel.plugins.RedisService
import com.xavierclavel.services.RoomService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.ROOMS_URL
import com.xavierclavel.utils.USERS_URL
import com.xavierclavel.utils.getPaging
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.getSessionUserId
import com.xavierclavel.utils.logger
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.setupRoomController() = route(ROOMS_URL) {
    val roomService by inject<RoomService>()

    get("/{id}") {
        val roomId = getPathId()
        val result = roomService.getRoom(roomId)
        call.respond(result)
    }

    post {
        val dto = call.receive<RoomIn>()
        val result = roomService.createRoom(dto)
        call.respond(result)
    }

    patch("/{id}/activate") {
        val id = getPathId()
        val result = roomService.activateRoom(id)
        call.respond(result)
    }

    patch("/{id}/inactivate") {
        val id = getPathId()
        val result = roomService.deactivateRoom(id)
        call.respond(result)
    }
}


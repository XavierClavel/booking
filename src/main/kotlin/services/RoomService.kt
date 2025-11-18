package com.xavierclavel.services

import com.xavierclavel.dtos.RoomIn
import com.xavierclavel.dtos.RoomOut
import com.xavierclavel.enums.RoomStatus
import com.xavierclavel.exceptions.BadRequestCause
import com.xavierclavel.exceptions.BadRequestException
import com.xavierclavel.exceptions.NotFoundCause
import com.xavierclavel.exceptions.NotFoundException
import com.xavierclavel.models.Room
import com.xavierclavel.models.query.QRoom
import org.koin.core.component.KoinComponent

class RoomService: KoinComponent {
    private fun Room.toOutput() =
        RoomOut(
            id = id,
            name = name,
            capacity = capacity,
            status = status,
        )

    private fun Room.deactivate() {
        this.status = RoomStatus.INACTIVE
    }

    private fun Room.activate() {
        this.status = RoomStatus.ACTIVE
    }

    private fun findRoom(id: Long): Room =
        QRoom().id.eq(id).findOne() ?: throw NotFoundException(NotFoundCause.ROOM_NOT_FOUND)


    fun createRoom(dto: RoomIn): RoomOut {
        val room = Room(
            name = dto.name,
            capacity = dto.capacity,
        )
        room.save()
        return room.toOutput()
    }

    fun getRoom(id: Long): RoomOut =
        findRoom(id).toOutput()

    fun deactivateRoom(id: Long) {
        val room = findRoom(id)
        if (room.status == RoomStatus.INACTIVE) {
            throw BadRequestException(BadRequestCause.ROOM_IS_ALREADY_INACTIVE)
        }
        room.deactivate()
        room.save()
    }

    fun activateRoom(id: Long) {
        val room = findRoom(id)
        if (room.status == RoomStatus.ACTIVE) {
            throw BadRequestException(BadRequestCause.ROOM_IS_ALREADY_ACTIVE)
        }
        room.activate()
        room.save()
    }

}
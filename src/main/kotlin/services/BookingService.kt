package com.xavierclavel.services

import com.xavierclavel.config.Configuration
import com.xavierclavel.dtos.BookingIn
import com.xavierclavel.dtos.BookingOut
import com.xavierclavel.dtos.SignupDto
import com.xavierclavel.dtos.UserIn
import com.xavierclavel.dtos.UserOut
import com.xavierclavel.enums.BookingStatus
import com.xavierclavel.enums.RoomStatus
import com.xavierclavel.enums.UserRole
import com.xavierclavel.exceptions.BadRequestCause
import com.xavierclavel.exceptions.BadRequestException
import com.xavierclavel.exceptions.ForbiddenCause
import com.xavierclavel.exceptions.ForbiddenException
import com.xavierclavel.exceptions.NotFoundCause
import com.xavierclavel.exceptions.NotFoundException
import com.xavierclavel.exceptions.UnauthorizedCause
import com.xavierclavel.exceptions.UnauthorizedException
import com.xavierclavel.models.Booking
import com.xavierclavel.models.User
import com.xavierclavel.models.query.QBooking
import com.xavierclavel.models.query.QRoom
import com.xavierclavel.models.query.QUser
import com.xavierclavel.utils.withOptimisticLockRetry
import io.ebean.Paging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Duration
import java.time.LocalDateTime

class BookingService: KoinComponent {

    private fun isRoomAvailable(roomId: Long, startTime: LocalDateTime, endTime: LocalDateTime): Boolean =
        !QBooking()
            .room.id.eq(roomId)
            .status.eq(BookingStatus.CONFIRMED)
            .or()
                .startTime.ge(endTime)
                .endTime.le(startTime)
            .endOr()
            .exists()

    private fun Booking.toOutput() = BookingOut(
        id = this.id,
        roomId = this.room.id,
        userId = this.user.id,
        startTime = this.startTime,
        endTime = this.startTime,
    )

    private fun Booking.cancel() {
        this.status = BookingStatus.CANCELLED
    }


    private fun createBooking(userId: Long, dto: BookingIn): BookingOut = withOptimisticLockRetry {
        if (dto.startTime < LocalDateTime.now()) {
            throw BadRequestException(BadRequestCause.CANNOT_BOOK_IN_THE_PAST)
        }

        if (dto.startTime >= dto.endTime) {
            throw BadRequestException(BadRequestCause.START_TIME_MUST_BE_BEFORE_END_TIME)
        }
        val duration = Duration.between(dto.startTime, dto.endTime)
        if (duration.toHours() > 8) {
            throw BadRequestException(BadRequestCause.MAX_8_HOURS)
        }

        val user = QUser().id.eq(userId).findOne() ?: throw NotFoundException(NotFoundCause.USER_NOT_FOUND)
        val room = QRoom().id.eq(dto.roomId).findOne() ?: throw NotFoundException(NotFoundCause.ROOM_NOT_FOUND)
        if (room.status == RoomStatus.INACTIVE) {
            throw BadRequestException(BadRequestCause.ROOM_IS_INACTIVE)
        }

        if (!isRoomAvailable(dto.roomId, dto.startTime, dto.endTime)) {
            throw BadRequestException(BadRequestCause.ROOM_MUST_BE_AVAILABLE)
        }

        val booking = Booking(
            user = user,
            room = room,
            startTime = dto.startTime,
            endTime = dto.endTime,
        )

        booking.save()
        return@withOptimisticLockRetry booking.toOutput()
    }


    fun cancelBooking(userId: Long, bookingId: Long): BookingOut {
        val booking = QBooking().id.eq(userId).findOne() ?: throw NotFoundException(NotFoundCause.BOOKING_NOT_FOUND)

        if (booking.user.id != userId) {
            throw ForbiddenException(ForbiddenCause.MUST_HAVE_CREATED_BOOKING)
        }

        if (booking.status == BookingStatus.CANCELLED) {
            throw BadRequestException(BadRequestCause.BOOKING_ALREADY_CANCELLED)
        }

        if (booking.startTime < LocalDateTime.now()) {
            throw BadRequestException(BadRequestCause.BOOKING_MUST_BE_IN_FUTURE)
        }

        booking.cancel()
        booking.save()
        return booking.toOutput()
    }

}
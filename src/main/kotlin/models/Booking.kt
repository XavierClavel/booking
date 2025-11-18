package com.xavierclavel.models

import com.xavierclavel.enums.BookingStatus
import com.xavierclavel.enums.RoomStatus
import com.xavierclavel.services.BookingService
import io.ebean.Model
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "rooms")
class Booking(

    @ManyToOne
    var room: Room,

    @ManyToOne
    var user: User,

    @Column(updatable = false)
    val startTime: LocalDateTime,

    @Column(updatable = false)
    val endTime: LocalDateTime,


    ): Model() {

    @Id
    var id: Long = 0

    @Column(updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Enumerated(EnumType.STRING)
    var status: BookingStatus = BookingStatus.CONFIRMED
}
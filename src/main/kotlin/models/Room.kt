package com.xavierclavel.models

import com.xavierclavel.dtos.UserOut
import com.xavierclavel.enums.RoomStatus
import io.ebean.Model
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version

@Entity
@Table(name = "rooms")
class Room(

    @Column(unique = true)
    var name: String = "",

    @Column(updatable = false)
    val capacity: Int = 0,

    @Enumerated(EnumType.STRING)
    var status: RoomStatus = RoomStatus.ACTIVE,

    ): Model() {

    @Id
    var id: Long = 0

    @Version
    var version: Long = 0
}
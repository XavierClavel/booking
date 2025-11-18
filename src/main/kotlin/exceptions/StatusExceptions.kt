package com.xavierclavel.exceptions

class UnauthorizedException(val reason: UnauthorizedCause): Exception(reason.key)

class ForbiddenException(val reason: ForbiddenCause): Exception(reason.key)

class BadRequestException(val reason: BadRequestCause): Exception(reason.key)

class NotFoundException(val reason: NotFoundCause): Exception(reason.key)

enum class UnauthorizedCause(val key: String) {
    SESSION_NOT_FOUND("session_not_found"),
    INVALID_PASSWORD("invalid_password"),
    INVALID_CREDENTIALS("invalid_mail_or_password"),
    INVALID_TOKEN("invalid_token"),
    OAUTH_FAILED("oauth_failed"),
    OAUTH_NOT_SETUP("oauth_not_setup"),
}

enum class ForbiddenCause(val key: String) {
    MUST_BE_PERFORMED_ON_SELF("must_be_performed_on_self"),
    MUST_BE_ADMIN("must_be_admin"),
    MUST_HAVE_CREATED_BOOKING("must_have_created_booking"),
}

enum class NotFoundCause(val key: String) {
    USER_NOT_FOUND("user_not_found"),
    ROOM_NOT_FOUND("room_not_found"),
    BOOKING_NOT_FOUND("booking_not_found"),
}

enum class BadRequestCause (val key: String) {
    NOT_APPLICABLE_ON_SELF("not_applicable_on_self"),
    MAIL_ALREADY_USED("mail_already_used"),
    USERNAME_ALREADY_USED("username_already_used"),
    OAUTH_ONLY("oauth_only"),
    INVALID_REQUEST("invalid_request"),
    ROOM_IS_ALREADY_INACTIVE("room_is_already_inactive"),
    ROOM_IS_ALREADY_ACTIVE("room_is_already_active"),
    ROOM_IS_INACTIVE("room_is_inactive"),
    START_TIME_MUST_BE_BEFORE_END_TIME("start_time_must_be_before_end_time"),
    MAX_8_HOURS("max_8_hours"),
    ROOM_MUST_BE_AVAILABLE("room_must_be_available"),
    CANNOT_BOOK_IN_THE_PAST("cannot_book_in_the_past"),
    BOOKING_ALREADY_CANCELLED("booking_already_canceled"),
    BOOKING_MUST_BE_IN_FUTURE("booking_must_be_in_future"),
}
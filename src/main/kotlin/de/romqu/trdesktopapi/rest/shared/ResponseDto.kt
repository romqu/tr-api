package de.romqu.trdesktopapi.rest.shared

import de.romqu.trdesktopapi.rest.shared.errorhandling.ErrorDto

class ResponseDto<T>(
    val data: T?,
    val errors: List<ErrorDto>,
) {

    companion object {
        fun <T> success(data: T) = ResponseDto(data, emptyList())
        fun <T> failure(errors: List<ErrorDto>): ResponseDto<T> = ResponseDto(null, errors)
        fun <T> failure(error: ErrorDto): ResponseDto<T> = ResponseDto(null, listOf(error))

    }

}
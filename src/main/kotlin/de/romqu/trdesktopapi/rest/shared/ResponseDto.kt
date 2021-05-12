package de.romqu.trdesktopapi.rest.shared

import de.romqu.trdesktopapi.rest.shared.errorhandling.ErrorDto

class ResponseDto<T>(
    val data: T?,
    val errors: List<ErrorDto>,
) {

    companion object {
        fun <T> success(data: T) = ResponseDto(data, emptyList())
        fun failure(errors: List<ErrorDto>) = ResponseDto(null, errors)
        fun failure(error: ErrorDto) = ResponseDto(null, listOf(error))

    }

}
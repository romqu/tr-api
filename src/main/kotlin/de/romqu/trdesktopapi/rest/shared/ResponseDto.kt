package de.romqu.trdesktopapi.rest.shared

class ResponseDto<T>(
    val data: T?,
    val errors: List<ErrorDto>,
)
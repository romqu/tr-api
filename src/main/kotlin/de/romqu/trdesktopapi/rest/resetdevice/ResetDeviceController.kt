package de.romqu.trdesktopapi.rest.resetdevice

import de.romqu.trdesktopapi.domain.ResetDeviceService
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
class ResetDeviceController(
    private val service: ResetDeviceService,
) {
    companion object {
        const val URL_PATH = "/v1/auth/device/reset"
    }

    @PostMapping(URL_PATH)
    suspend fun login(
        @RequestBody @Valid dto: ResetDeviceInDto,
        session: SessionEntity,
    ): ResponseEntity<Nothing> =
        service.execute(dto.code, session)
            .doOn(::success, ::failure)

    private fun success(_i: Unit): ResponseEntity<Nothing> = ResponseEntity.ok().build()

    private fun failure(error: ResetDeviceService.Error): ResponseEntity<Nothing> = when (error) {
        ResetDeviceService.Error.CouldNotResetDevice -> ResponseEntity.badRequest().build()
    }
}


package de.romqu.trdesktopapi.data.auth.account.resetdevice

import de.romqu.trdesktopapi.data.shared.ApiCall
import de.romqu.trdesktopapi.data.shared.ApiCallDelegate
import org.springframework.stereotype.Repository

@Repository
class ResetDeviceRepository(
    private val api: ResetDeviceApi,
    private val apiCallDelegate: ApiCallDelegate,
) : ApiCall by apiCallDelegate
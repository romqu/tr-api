package de.romqu.trdesktopapi.rest.shared.config

import de.romqu.trdesktopapi.data.auth.session.SessionRepository
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import de.romqu.trdesktopapi.rest.shared.SessionUtil
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class HeaderSessionMethodArgumentResolver(
    private val sessionRepository: SessionRepository,
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == SessionEntity::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): SessionEntity {

        val authHeader = webRequest.getHeader(HttpHeaders.AUTHORIZATION)
            ?: throw  Exception() // Header does not exist

        return SessionUtil.getSessionFromAuthHeader(authHeader, sessionRepository)
    }
}

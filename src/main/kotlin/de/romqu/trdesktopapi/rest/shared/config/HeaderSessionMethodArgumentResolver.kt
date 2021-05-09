package de.romqu.trdesktopapi.rest.shared.config

import de.romqu.trdesktopapi.data.auth.session.SessionRepository
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.util.*

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
            ?: throw  Exception()

        val authMatcher = AUTH_BEARER_PATTERN.matcher(authHeader)

        val token = authMatcher.group(1)

        val sessionUuid = try {
            UUID.fromString(token)
        } catch (ex: Exception) {
            throw Exception()
        }

        return sessionRepository.getByUuid(sessionUuid) ?: throw Exception()
    }
}

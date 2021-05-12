package de.romqu.trdesktopapi.rest.shared.session

import de.romqu.trdesktopapi.domain.GetSessionByAuthHeaderTask
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import de.romqu.trdesktopapi.rest.shared.errorhandling.InvalidSessionExceptionHandler.InvalidSessionException
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class HeaderSessionMethodArgumentResolver(
    private val getSessionByAuthHeaderTask: GetSessionByAuthHeaderTask,
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.parameterType == SessionEntity::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): SessionEntity =
        getSessionByAuthHeaderTask.execute(
            webRequest.getHeader(HttpHeaders.AUTHORIZATION)
        ).doOn(success = { it }, failure = { throw InvalidSessionException })
}

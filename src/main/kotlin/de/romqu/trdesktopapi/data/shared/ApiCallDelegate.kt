package de.romqu.trdesktopapi.data.shared

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import de.romqu.trdesktopapi.shared.Result
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import retrofit2.Response
import java.io.IOException

interface ApiCall {
    suspend fun <S : Any> makeApiCall(
        call: suspend () -> Response<S>,
    ): Result<ApiCallError, S>
}

@Component
class ApiCallDelegate(
    private val objectMapper: ObjectMapper,
) : ApiCall {

    override suspend fun <S : Any> makeApiCall(
        call: suspend () -> Response<S>,
    ): Result<ApiCallError, S> {
        return try {
            val response = call()
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                Result.Success(responseBody)
            } else {
                when (response.code()) {
                    HttpStatus.UNAUTHORIZED.value() -> Result.Failure(
                        ApiCallError.Unauthorized
                    )
                    HttpStatus.BAD_REQUEST.value() -> {
                        val errorBody = response.errorBody()

                        val apiCallErrorsInDto = if (errorBody != null) {
                            objectMapper.readValue(
                                errorBody.byteStream(),
                                ApiCallErrorsInDto::class.java
                            )
                        } else ApiCallErrorsInDto<Any>()

                        Result.Failure(
                            ApiCallError.BadRequest(apiCallErrorsInDto)
                        )
                    }
                    HttpStatus.TOO_MANY_REQUESTS.value() -> {
                        val errorBody = response.errorBody()

                        val apiCallErrorsInDto = if (errorBody != null) {
                            objectMapper.readValue(
                                errorBody.byteStream(),
                                object : TypeReference<ApiCallErrorsInDto<RetryMeta>>() {}
                            )

                        } else ApiCallErrorsInDto()

                        Result.Failure(
                            ApiCallError.BadRequest(apiCallErrorsInDto)
                        )
                    }
                    else ->
                        Result.Failure(
                            ApiCallError.UndefinedNetworkError(
                                "${response.message()}: ${response.errorBody()?.string() ?: ""}"
                            ),
                        )
                }
            }
        } catch (ioException: IOException) {
            Result.Failure(ApiCallError.NoNetworkError(ioException.toString()))
        }
    }

}

sealed class ApiCallError {
    object Unauthorized : ApiCallError()
    class BadRequest<T>(val dto: ApiCallErrorsInDto<T>) : ApiCallError()
    class TooManyRequests(val dto: ApiCallErrorsInDto<RetryMeta>) : ApiCallError()
    class UndefinedNetworkError(val message: String) : ApiCallError()
    class NoNetworkError(val message: String) : ApiCallError()
}

class ApiCallErrorsInDto<T>(
    val errors: List<ApiCallErrorInDto<T>> = emptyList(),
)

class ApiCallErrorInDto<T>(
    val errorCode: String = "",
    val errorField: String? = null,
    val errorMessage: String? = null,
    val meta: T? = null,
)

class RetryMeta(
    @JsonProperty("_meta_type")
    val metaType: String,
    val nextAttemptInSeconds: Int,
    val nextAttemptTimestamp: String,
)
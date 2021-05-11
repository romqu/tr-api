package de.romqu.trdesktopapi.rest.login

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders


@SpringBootTest
@AutoConfigureMockMvc
internal class LoginControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun login() {

        val dto = LoginInDto(countryCode = 4, phoneNumber = 123456789, 1234)

        val dtoAsString = objectMapper.writeValueAsString(dto)

        val result = mvc.perform(
            MockMvcRequestBuilders.post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                //.header(HttpHeaders.AUTHORIZATION, "Bearer a4e0c94a-35b7-4c5e-aea1-7ec73c08405e")
                .content(dtoAsString)
            //.accept(MediaType.APPLICATION_JSON)
        ).andReturn()

        result.response.contentAsString
    }
}
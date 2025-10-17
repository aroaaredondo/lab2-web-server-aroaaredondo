package es.unizar.webeng.lab2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfig::class)
class IntegrationTest {
    @LocalServerPort private var port: Int = 0

    @Autowired private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `should return error page `() {
        // se ha tenido que pedir en html explicitamente porque sino lo devolvia en Json
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.TEXT_HTML)

        val entity = HttpEntity<String>(headers)

        val response =
            restTemplate.exchange(
                "https://localhost:$port",
                HttpMethod.GET,
                entity,
                String::class.java,
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).contains("¡Oops! Página no encontrada")
    }

    @Test
    fun ` URI time return localtime`() {
        val response =
            restTemplate.getForEntity("https://localhost:$port/time", TimeDTO::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertNotNull(response.body?.time)
        assertThat(response.body!!.time).isBeforeOrEqualTo(LocalDateTime.now())
    }
}

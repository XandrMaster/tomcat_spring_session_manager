package digital.equinox.backend.tools.session_manager.demo.test

import digital.equinox.backend.tools.session_manager.core.InvalidateRequestBody
import digital.equinox.backend.tools.session_manager.core.TomcatSession
import digital.equinox.backend.tools.session_manager.core.getAll
import digital.equinox.backend.tools.session_manager.core.invalidate
import digital.equinox.backend.tools.session_manager.demo.Application
import digital.equinox.backend.tools.session_manager.demo.getNewSession
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@RunWith(BlockJUnit4ClassRunner::class)
@SpringBootTest(classes = [Application::class])
class SessionManagerTest {

    private lateinit var restTemplate: TestRestTemplate

    private val restUrl = "http://localhost:8080/session_manager/session_manager/rest"

    @Before
    fun setUp() {
        restTemplate = TestRestTemplate()
    }

    @Test
    fun invalidateTest() {

        createNewSession()
        createNewSession()
        createNewSession()

        var response = restTemplate.getForEntity(restUrl + "/$getAll", Array<TomcatSession>::class.java)
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK))
        var sessions: Array<TomcatSession> = response.body as Array<TomcatSession>
        val sessionsHash = IntArray(sessions.size)
        sessions.forEachIndexed { index, tomcatSession ->
            sessionsHash[index] = tomcatSession.hash
        }

        val invalidateRequestBody = InvalidateRequestBody(sessionsHash)

        val invalidateResponse = restTemplate
            .postForEntity(restUrl + "/$invalidate", invalidateRequestBody, String::class.java)

        assertThat(invalidateResponse.getStatusCode(), equalTo(HttpStatus.OK))

        response = restTemplate.getForEntity(restUrl + "/$getAll", Array<TomcatSession>::class.java)
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK))
        sessions = response.body as Array<TomcatSession>
        assertTrue(sessions.isEmpty())
    }

    private fun createNewSession() {
        val response = restTemplate.getForEntity(restUrl + "/$getNewSession", String::class.java)
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK))
    }
}
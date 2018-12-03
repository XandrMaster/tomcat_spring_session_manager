package digital.equinox.backend.tools.session_manager.demo.test

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RunWith(JUnit4::class)
class FluxTest {

    @Test
    fun flux() {
        val mono: Mono<String> = Flux.just(1, 2, 3, 4, 5)
            .log()
            .map {
                println(" current item $it ")
            }
            .last()
            .map { "flux completed" }

        mono.subscribe { println(it) }
    }
}
/*
 * Created Xandr https://xandrwix.wixsite.com/resume
 * equinox.digital Inc. https://equinox.digital
 * Copyright (c) 2018
 */
package digital.equinox.backend.tools.session_manager.core

import org.apache.catalina.Manager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.atomic.AtomicReference

@Lazy
@Service
class SessionManager(@Autowired private val manager: AtomicReference<Manager>) {

    fun getTomcatSessions(): Flux<TomcatSession> {

        return Flux.just(manager)
            .map {
                val managerLocal = it.get()
                if (managerLocal == null) {
                    throw KotlinNullPointerException(" tomcatManager is empty !")
                }
                return@map managerLocal.findSessions()
            }
            .flatMap { Flux.fromArray(it) }
            .map {
                return@map TomcatSession(it)
            }
            .sort()
            .index { id, tomcatSession ->
                tomcatSession.id = id + 1
                return@index tomcatSession
            }
    }

    fun getTomcatSessionByHash(hash: Int): Mono<TomcatSession> {
        return getTomcatSessions()
            .filter {
                it.hash == hash
            }
            .next()
    }

    fun getTomcatSessionsByHashes(hash: IntArray): Flux<TomcatSession> {
        return getTomcatSessions()
            .filter { it.hash in hash }
    }

    fun invalidate(hash: IntArray): Mono<Unit> {
        if (hash.isEmpty()) {
            return Mono.empty()
        }
        return getTomcatSessionsByHashes(hash)
            .map { it.invalidate() }
            .last(Unit)
    }
}
/*
 * Created Xandr https://xandrwix.wixsite.com/resume
 * equinox.digital Inc. https://equinox.digital
 * Copyright (c) 2018
 */
package digital.equinox.backend.tools.session_manager.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler

@Lazy
@RestController
@RequestMapping("\${digital.equinox.session_manager.baseurl:/session_manager}/rest")
internal class SessionManagerRest(
    @Autowired private val sessionManager: SessionManager,
    @Autowired private val defaultScheduler: Scheduler
) {

    @Async
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/$getAll")
    fun getAllSessions(): Flux<TomcatSession> {
        return sessionManager
            .getTomcatSessions()
            .log()
            .subscribeOn(defaultScheduler)
    }

    @Async
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/$getByHash/{hash}")
    fun getSessionByHash(@PathVariable(required = true) hash: Int): Mono<TomcatSession> {
        return sessionManager
            .getTomcatSessionByHash(hash)
            .log()
            .subscribeOn(defaultScheduler)
    }

    @Async
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/$invalidate")
    fun invalidate(@RequestBody(required = true) body: InvalidateRequestBody): Mono<ResponseEntity<HttpStatus>> {
        return sessionManager
            .invalidate(body.bodyArray)
            .log()
            .subscribeOn(defaultScheduler)
            .map { ResponseEntity.ok().build<HttpStatus>() }
    }
}
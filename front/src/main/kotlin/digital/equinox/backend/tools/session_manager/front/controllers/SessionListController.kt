/*
 * Created Xandr https://xandrwix.wixsite.com/resume
 * equinox.digital Inc. https://equinox.digital
 * Copyright (c) 2018
 */
package digital.equinox.backend.tools.session_manager.front.controllers

import digital.equinox.backend.tools.session_manager.core.SessionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Flux
import reactor.core.scheduler.Scheduler

@Lazy
@Controller
@RequestMapping("\${digital.equinox.session_manager.baseurl:/session_manager}/front")
class SessionListController(
    @Autowired private var sessionManager: SessionManager,
    @Autowired private val defaultScheduler: Scheduler
) {

    @GetMapping
    fun sessionList(model: Model): String {
        initSessionList(model)
        return "sessionList"
    }

    @PostMapping
    fun invalidateSessions(
        model: Model
        , @ModelAttribute form: InvalidateSessionsForm
    ): String {

        form.sessions?.let {
            if (it.isNotEmpty()) {
                Flux.fromIterable(it)
                    .log()
                    .subscribeOn(defaultScheduler)
                    .filter { it.invalidate }
                    .map { it.hash }
                    .collectList()
                    .flatMap {
                        sessionManager.invalidate(it.toIntArray())
                    }
                    .block()
            }
        }

        initSessionList(model)

        return "sessionList"
    }

    private fun initSessionList(
        model: Model
    ) {
        val tomcatSessions = sessionManager.getTomcatSessions()
            .log()
            .subscribeOn(defaultScheduler)
            .map { TomcatSessionView(it) }
            .collectList()
            .block()
        model.addAttribute(
            "tomcatSessions", tomcatSessions
        )
        model.addAttribute("form", InvalidateSessionsForm())
    }
}
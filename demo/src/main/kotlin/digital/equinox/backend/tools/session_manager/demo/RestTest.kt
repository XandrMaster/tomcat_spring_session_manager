/*
 * Created Xandr https://xandrwix.wixsite.com/resume
 * equinox.digital Inc. https://equinox.digital
 * Copyright (c) 2018
 */
package digital.equinox.backend.tools.session_manager.demo

import digital.equinox.backend.tools.session_manager.core.SessionManager
import digital.equinox.backend.tools.session_manager.core.TomcatSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpSession

@Lazy
@RestController
@RequestMapping("\${digital.equinox.session_manager.baseurl:/session_manager}/rest")
class RestTest(@Autowired val sessionManager: SessionManager) {

    @GetMapping("/$getNewSession")
    fun getSession(httpSession: HttpSession): String {

//        httpSession.invalidate()
//
//        val requestAttr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
//        val session = requestAttr.request.getSession(true)

        return "new session ID: ${httpSession.id}"
    }

    @GetMapping("/getTomcatSessions")
    fun getTomcatSessions(): List<TomcatSession> {

        return sessionManager.getTomcatSessions().collectList().block()
    }

}
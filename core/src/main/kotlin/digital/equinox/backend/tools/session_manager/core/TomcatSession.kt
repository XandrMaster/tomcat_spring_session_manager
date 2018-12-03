/*
 * Created Xandr https://xandrwix.wixsite.com/resume
 * equinox.digital Inc. https://equinox.digital
 * Copyright (c) 2018
 */
package digital.equinox.backend.tools.session_manager.core

import org.apache.catalina.Session
import org.apache.catalina.session.StandardSession
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import javax.servlet.http.HttpSession

class TomcatSession : Comparable<TomcatSession> {

    var id: Long = -1
    var principalName: String = ""
        private set
    var creationTime: Long = -1
    var idleTime: Long = -1
    var lastAccessedTime: Long = -1
    var thisAccessedTime: Long = -1
    var remoteAddress: String = ""
        private set
    var isValid: Boolean = false
        private set
    var isAuthenticated: Boolean = false
        private set

    @Transient
    private var httpSession: HttpSession? = null

    var hash: Int = -1
        private set

    constructor()

    constructor(session: Session) {
        principalName = session.principal?.name.orEmpty()
        creationTime = session.creationTime
        idleTime = session.idleTime
        lastAccessedTime = session.lastAccessedTime
        thisAccessedTime = session.thisAccessedTime
        isValid = session.isValid
        httpSession = session.session
        hash = customHashCode()

        init(session)
    }

    private fun init(session: Session) {
        if (session.isValid && session is StandardSession) {
            val attributeNames = session.attributeNames
            for (attribute in attributeNames) {
                if (attribute.compareTo(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, true) == 0) {
                    val securityContext =
                        session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY)
                    if (securityContext is SecurityContext) {
                        isAuthenticated = securityContext.authentication.isAuthenticated
                        val principal = securityContext.authentication.principal
                        if (principalName.isBlank() && principal is UserDetails) {
                            principalName = principal.username
                        }
                        val details = securityContext.authentication.details
                        if (details != null && details is WebAuthenticationDetails) {
                            remoteAddress = details.remoteAddress
                        }
                    }
                }
            }
        }
    }

    override fun compareTo(other: TomcatSession): Int {
        return creationTime.compareTo(other.creationTime)
    }

    fun invalidate() {
        if (isValid) {
            httpSession?.invalidate()
            isValid = false
            isAuthenticated = false
        }
    }

    private fun customHashCode(): Int {
        return httpSession?.id.hashCode()
    }
}
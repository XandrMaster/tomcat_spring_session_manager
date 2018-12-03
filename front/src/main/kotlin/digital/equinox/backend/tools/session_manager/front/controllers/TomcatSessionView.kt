/*
 * Created Xandr https://xandrwix.wixsite.com/resume
 * equinox.digital Inc. https://equinox.digital
 * Copyright (c) 2018
 */

package digital.equinox.backend.tools.session_manager.front.controllers

import digital.equinox.backend.tools.session_manager.core.TomcatSession
import java.sql.Timestamp
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class TomcatSessionView(
    tomcatSession: TomcatSession
    , dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    , timeZone: TimeZone = TimeZone.getDefault()
) {

    var id: Long = -1
        private set
    var principalName: String = n_aStr
        private set
    var creationTime: String = n_aStr
        private set
    var idleTime: String = n_aStr
        private set
    var lastAccessedTime: String = n_aStr
        private set
    var remoteAddress: String = n_aStr
        private set
    var valid: Boolean = false
        private set
    var authenticated: Boolean = false
        private set

    var hash: Int = -1
        private set

    init {
        id = tomcatSession.id
        principalName = tomcatSession.principalName
        creationTime = Timestamp(tomcatSession.creationTime).toLocalDateTime().format(dateTimeFormatter)
        idleTime = Period.between(
            Timestamp(0).toLocalDateTime().toLocalDate()
            , Timestamp(tomcatSession.idleTime).toLocalDateTime().toLocalDate()
        ).toString()
        lastAccessedTime = Timestamp(tomcatSession.lastAccessedTime).toLocalDateTime().format(dateTimeFormatter)
        remoteAddress = tomcatSession.remoteAddress
        hash = tomcatSession.hash
        valid = tomcatSession.isValid
        authenticated = tomcatSession.isAuthenticated
    }
}
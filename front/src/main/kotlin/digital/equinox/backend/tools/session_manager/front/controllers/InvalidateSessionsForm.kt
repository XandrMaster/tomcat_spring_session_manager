/*
 * Created Xandr https://xandrwix.wixsite.com/resume
 * equinox.digital Inc. https://equinox.digital
 * Copyright (c) 2018
 */

package digital.equinox.backend.tools.session_manager.front.controllers

class InvalidateSessionsForm {
    var sessions: ArrayList<SessionView>? = null
}

class SessionView {
    var hash: Int = -1
    var invalidate = false
}
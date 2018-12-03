/*
 * Created Xandr https://xandrwix.wixsite.com/resume
 * equinox.digital Inc. https://equinox.digital
 * Copyright (c) 2018
 */

package digital.equinox.backend.tools.session_manager.core

class InvalidateRequestBody {

    constructor()

    constructor(bodyArray: IntArray) {
        this.bodyArray = bodyArray
    }

    lateinit var bodyArray: IntArray
}
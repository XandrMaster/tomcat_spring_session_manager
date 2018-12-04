/*
 * Created Xandr https://xandrwix.wixsite.com/resume
 * equinox.digital Inc. https://equinox.digital
 * Copyright (c) 2018
 */
package digital.equinox.backend.tools.session_manager.demo

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Lazy
@Controller
class Controller {

    @GetMapping("/$privateUrlPath/admin")
    fun getAdmin(): String {
        return "$privateUrlPath/admin"
    }

    @GetMapping("/")
    fun getIndex(): String {
        return "index"
    }
}
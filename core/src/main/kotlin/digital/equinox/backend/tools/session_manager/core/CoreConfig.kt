/*
 * Created Xandr https://xandrwix.wixsite.com/resume
 * equinox.digital Inc. https://equinox.digital
 * Copyright (c) 2018
 */
package digital.equinox.backend.tools.session_manager.core

import org.apache.catalina.Context
import org.apache.catalina.Manager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers
import java.util.concurrent.atomic.AtomicReference

@Configuration
class CoreConfig {

    @Lazy
    @Bean(sessionManager)
    fun logger(): Logger {
        return LoggerFactory.getLogger(sessionManager)
    }

    @Bean
    @Lazy
    fun getTomcat(): TomcatServletWebServerFactory {
        val tomcat = object : TomcatServletWebServerFactory() {
            override fun postProcessContext(context: Context?) {
                super.postProcessContext(context)
                tomcatManager().set(context?.manager)
            }
        }
        return tomcat
    }

    @Bean
    @Lazy
    fun tomcatManager(): AtomicReference<Manager> {
        val atomicReference = AtomicReference<Manager>()
        return atomicReference
    }

    @Bean
    @Lazy
    fun defaultScheduler(): Scheduler {
        return Schedulers.elastic()
    }
}
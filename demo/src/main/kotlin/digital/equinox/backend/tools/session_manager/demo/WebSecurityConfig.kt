/*
 * Created Xandr https://xandrwix.wixsite.com/resume
 * equinox.digital Inc. https://equinox.digital
 * Copyright (c) 2018
 */
package digital.equinox.backend.tools.session_manager.demo

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.NoOpPasswordEncoder

@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http
            ?.csrf()?.disable()
            ?.authorizeRequests()
            ?.antMatchers("/$privateUrlPath/**")
            ?.hasAnyRole(adminRole)
            ?.and()
            ?.formLogin()
            ?.defaultSuccessUrl("/$privateUrlPath/admin")
            ?.and()
            ?.logout()
            ?.logoutSuccessUrl("/")
            ?.invalidateHttpSession(true)
            ?.and()
            ?.sessionManagement()
            ?.maximumSessions(5)
            ?.and()
            ?.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.inMemoryAuthentication()
            ?.passwordEncoder(NoOpPasswordEncoder.getInstance())
            ?.withUser("admin")?.password("admin")?.roles(adminRole)
    }

    override fun configure(web: WebSecurity?) {
        web?.ignoring()
            ?.antMatchers(
                "/",
                "/$publicUrlPath/**"
            )
    }
}
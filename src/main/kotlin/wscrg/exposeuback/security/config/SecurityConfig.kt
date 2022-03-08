package wscrg.exposeuback.security.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import wscrg.exposeuback.security.filter.AjaxLoginProcessingFilter
import wscrg.exposeuback.security.handler.AjaxAccessDeniedHandler
import wscrg.exposeuback.security.handler.AjaxAuthenticationFailureHandler
import wscrg.exposeuback.security.handler.AjaxAuthenticationSuccessHandler
import wscrg.exposeuback.security.provider.AjaxAuthenticationProvider
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@EnableWebSecurity
@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var ajaxAuthenticationProvider: AjaxAuthenticationProvider

    override fun configure(web: WebSecurity?) {
        web?.ignoring()?.antMatchers("/css/**", "/js/**", "/img/**")
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
        http.headers().frameOptions().disable()
        http.cors().configurationSource(corsConfigurationSource())
        http.authorizeRequests().antMatchers("/", "/h2-console/**").permitAll()

        http.addFilterBefore(ajaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)

        http
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessHandler { _: HttpServletRequest, httpServletResponse: HttpServletResponse, _: Authentication? ->
                httpServletResponse.status = HttpServletResponse.SC_OK
            }
            .deleteCookies("JSESSIONID")

        ajaxConfigurer(http)
    }

    fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration()

        corsConfiguration.addAllowedHeader("*")
        corsConfiguration.addAllowedMethod("*")
        corsConfiguration.addAllowedOriginPattern("*")
        corsConfiguration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfiguration)
        return source
    }

    private fun ajaxConfigurer(http: HttpSecurity) {
        http
            .apply(AjaxLoginConfigurer())
            .successHandlerAjax(authenticationSuccessHandler())
            .failureHandlerAjax(authenticationFailureHandler())
            .setAuthenticationManager(ajaxAuthenticationManager())
    }

    private fun ajaxAuthenticationManager(): AuthenticationManager {
        val authProviderList = arrayListOf<AuthenticationProvider>()
        authProviderList.add(ajaxAuthenticationProvider)
        return ProviderManager(authProviderList)
    }

    @Bean
    fun authenticationSuccessHandler() = AjaxAuthenticationSuccessHandler()

    @Bean
    fun authenticationFailureHandler() = AjaxAuthenticationFailureHandler()

    @Bean
    fun accessDeniedHandler() = AjaxAccessDeniedHandler()

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun ajaxAuthenticationFilter(): AjaxLoginProcessingFilter {
        val ajaxLoginProcessingFilter = AjaxLoginProcessingFilter()
        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean())
        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler())
        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler())
        return ajaxLoginProcessingFilter
    }
}
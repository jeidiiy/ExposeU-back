package wscrg.exposeuback.security.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import wscrg.exposeuback.security.filter.AjaxLoginProcessingFilter
import wscrg.exposeuback.security.handler.AjaxAccessDeniedHandler
import wscrg.exposeuback.security.handler.AjaxAuthenticationFailureHandler
import wscrg.exposeuback.security.handler.AjaxAuthenticationSuccessHandler
import wscrg.exposeuback.security.provider.AjaxAuthenticationProvider

@EnableWebSecurity
@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var ajaxAuthenticationProvider: AjaxAuthenticationProvider

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
        http.cors().configurationSource(corsConfigurationSource())

        http.addFilterBefore(ajaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
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
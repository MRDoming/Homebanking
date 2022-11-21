package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .antMatchers("/rest/**", "/h2-console/**", "api/accounts", "api/clients").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/clientloan").hasAuthority("ADMIN")
                .antMatchers("/web/index.html", "/web/extras/tarjetas.html", "/web/extras/pagos.html").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers("/web/accounts.html", "/web/account.html", "/web/cards.html", "/web/createCards.html", "/web/transfers.html", "/web/loan-application.html").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/clients/current/accounts").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/clients/current").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/clients/current/cards").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/transactions", "/api/loans").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/clientloan").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/clientloanelim").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/loan").hasAuthority("ADMIN");;


        http.formLogin()

                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");


        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        //desactivas la comprobacion por defecto de tokens de spring security
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        //manejo de excepcion cuando vas a un enpoint que no tenes autorizacion. (request, response, exception)
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        //manejo de fallas -> de la falla, tal error
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }


    }



}

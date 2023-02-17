package jp.co.axa.apidemo.security;

import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class EmployeeApiSecurity extends WebSecurityConfigurerAdapter implements ApplicationContextAware {

    /* Secure the endpoints with HTTP Basic authentication */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //HTTP Basic authentication
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v1/employees/**").permitAll()
                .antMatchers("/api/v1/employees/**").authenticated()
                .and()
                .csrf().disable()
                .formLogin().disable();
    }
}

package cn.heyuantao.onlinejudgeserver.config;

import cn.heyuantao.onlinejudgeserver.filter.TokenAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author he_yu
 * Spring Security 安全配置
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenAuthFilter tokenAuthFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                //The urls use for swagger
                .antMatchers("/swagger-ui.html","/swagger-resources","/swagger-resources/**","/v2/api-docs","/webjars/**").permitAll()
                //.antMatchers("/permissiontest").permitAll()
                //.antMatchers("/api/**").permitAll()
                .antMatchers("/api/v1/debug/**").permitAll()
                .antMatchers("/","/version").permitAll()
                //.antMatchers("/","/index","/css/*","/js/*","/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }


    /*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService);
    }
    */


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

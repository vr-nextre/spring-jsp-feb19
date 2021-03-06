package it.nextre.academy.springdemo.config;

import it.nextre.academy.springdemo.service.UserService;
import it.nextre.academy.springdemo.service.impl.CustomUserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity //abilita la sicurezza sulle pagine
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login*","/registration").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                //per rendere pubblici i file statici rendendole tutte richieste autorizzate
                .antMatchers("/res/**").permitAll()
                //rotte per pagine
                .antMatchers("/", "/index", "/menu", "/servizi", "/contatti", "/piatto", "/piatto/*", "/cerca","/test").permitAll()

                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login") //custom page
                .failureUrl("/login?error")
                .defaultSuccessUrl("/menu",false)
                .permitAll()
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true") //custom redirect page
                .permitAll()
                //controllo la creazione del cookie solo se necessario
                .and()
                .exceptionHandling().accessDeniedPage("/error")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                ;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /*
     * USATO PER LA DEMO SENZA DB
     * */
/*
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1").password(passwordEncoder().encode("1234")).roles("USER")
                .and()
                .withUser("user2").password(passwordEncoder().encode("5678")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
    }
*/

    /*
     * CONTROLLO UTENTI CON DB VERO
     * */
    @Autowired
    private UserService userService;


    //riscritto per ottenere un user corretto pieno dei nostri campi che il principal potrà usare nelle jsp
    @Autowired
    CustomUserDetailServiceImpl customUserDetailService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(customUserDetailService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }


}//end class

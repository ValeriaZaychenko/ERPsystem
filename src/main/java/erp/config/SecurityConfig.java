package erp.config;

import erp.service.IAuthenticationService;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true, order = 0, mode = AdviceMode.PROXY, proxyTargetClass = false
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    private IAuthenticationService authenticationService;


    @Override
    protected void configure(AuthenticationManagerBuilder builder) {
        builder.authenticationProvider(this.authenticationService);
    }

    @Override
    public void configure(WebSecurity security)
    {
        security.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {

        security

                    .authorizeRequests()
                        .antMatchers("/reports/**").hasAuthority("AUTH_USER")
                        .antMatchers("/users/**").hasAuthority("AUTH_ADMIN")
                        .antMatchers("/home/**").authenticated()
                        .antMatchers("/changePassword/**").authenticated()
                        .antMatchers("/setlocale/**").permitAll()

                .and()

                    .formLogin()
                        .loginPage("/login")
                        .defaultSuccessUrl("/home")
                        .usernameParameter("userLogin")
                        .passwordParameter("password")
                        .permitAll()

                .and()

                    .logout()
                        .logoutUrl("/logout").logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .permitAll()

                .and()

                    .sessionManagement()
                        .sessionFixation().changeSessionId()

                .and()

                    .csrf()
                        .requireCsrfProtectionMatcher((r) -> {
                            return false;
                        })
                .and()

                    .exceptionHandling().accessDeniedPage( "/accessDenied" )


               ;
    }
}

package gr.hua.dit.dissys.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    DataSource dataSource;
    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager() throws Exception {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(dataSource);
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/verifyuser").permitAll()
                .antMatchers("/user/lessor/{id}").hasRole("ADMIN")
                .antMatchers("/user/tenant/{id}").hasRole("ADMIN")
                .antMatchers("/user/admin/{id}").hasRole("ADMIN")
                .antMatchers("/user/leases/{id}").hasRole("LESSOR")
                .antMatchers("/user/leases/agree/{id}").hasRole("TENANT")
                //.antMatchers("/registerTenant").permitAll()
                //.antMatchers("/registerLessor").permitAll()
                .antMatchers("/lessorform").permitAll()
                .antMatchers("/tenantform").permitAll()
                .antMatchers("/adminform").hasRole("ADMIN")
                .antMatchers("/adminlist").hasRole("ADMIN")
                .antMatchers("/leaseform").hasRole("LESSOR")
                .antMatchers("/leaseupdate").hasRole("LESSOR")
                .antMatchers("/leaselist").hasAnyRole("LESSOR", "TENANT")
                .antMatchers("/contractlist").hasAnyRole("LESSOR", "TENANT")
                .antMatchers("/leasecom").hasRole("TENANT")
                .anyRequest().authenticated()
                .and().formLogin().defaultSuccessUrl("/", true)
                .permitAll()
                .and()
                .logout().permitAll();


        http.headers().frameOptions().sameOrigin();

        return http.build();

    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) ->
                web.ignoring().antMatchers(
                        "/css/**", "/js/**", "/images/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
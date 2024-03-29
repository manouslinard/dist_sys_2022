package gr.hua.dit.dissys.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {

//    @Autowired
//    DataSource dataSource;
//    @Bean
//    public JdbcUserDetailsManager jdbcUserDetailsManager() throws Exception {
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
//        jdbcUserDetailsManager.setDataSource(dataSource);
//        return jdbcUserDetailsManager;
//    }

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    @Bean
    public AuthenticationManager authenticationManagerBean (AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/auth/**").permitAll()
		        .antMatchers("/").permitAll()
                .antMatchers("/lessor/**").hasAnyRole("LESSOR","ADMIN")
                .antMatchers("/tenant/**").hasAnyRole("TENANT","ADMIN")
		        .antMatchers("/user/lessor/{id}").hasRole("ADMIN")
		        .antMatchers("/user/tenant/{id}").hasRole("ADMIN")
		        .antMatchers("/user/admin/{id}").hasRole("ADMIN")
		        .antMatchers("/user/leases/{id}").hasRole("LESSOR")
		        .antMatchers("/user/leases/agree/{id}").hasRole("TENANT")
		        .antMatchers("/registerTenant").permitAll()
		        .antMatchers("/registerLessor").permitAll()
		        .antMatchers("/lessorform").hasRole("ADMIN")
		        .antMatchers("/tenantform").hasAnyRole("ADMIN", "LESSOR")
		        .antMatchers("/adminform").hasRole("ADMIN")
		        .antMatchers("/adminlist").hasRole("ADMIN")
		        .antMatchers("/leaseform").hasRole("LESSOR")
		        .antMatchers("/leaseupdate").hasRole("LESSOR")
		        .antMatchers("/leaselist").hasAnyRole("LESSOR", "TENANT")
		        .antMatchers("/contractlist").hasAnyRole("LESSOR", "TENANT")
		        .antMatchers("/leasecom").hasRole("TENANT")
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}




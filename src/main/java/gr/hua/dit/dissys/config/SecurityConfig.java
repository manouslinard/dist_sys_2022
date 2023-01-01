package gr.hua.dit.dissys.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
		    	.cors().and().csrf().disable()
		    	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		    	.authorizeRequests()
		    	.antMatchers("/api/auth/**").permitAll()
		        .antMatchers("/").permitAll()
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
	        	.antMatchers("/lessor/**").hasAnyRole("LESSOR","ADMIN")
	        	.antMatchers("/tenant/**").hasAnyRole("TENANT","ADMIN")
		        .anyRequest().authenticated()
		    	.and()
		    	.httpBasic();
		
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
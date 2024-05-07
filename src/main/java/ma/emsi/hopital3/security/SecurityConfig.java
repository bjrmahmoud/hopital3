package ma.emsi.hopital3.security;

import lombok.AllArgsConstructor;
import ma.emsi.hopital3.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
//au niveau de controlleur ces moi que je dois proteger routes
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    private UserDetailsServiceImpl userDetailsServiceImpl;
   // @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //@Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }
   // @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode("1234");
        System.out.println(encodedPassword);
        return new InMemoryUserDetailsManager(
                User.withUsername("mahmoud").password(encodedPassword).roles("USER").build(),
                User.withUsername("merouan").password(encodedPassword).roles("USER").build(),
                User.withUsername("admin").password(encodedPassword).roles("USER", "ADMIN").build()
        );
    }

//in memo auth
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/user/index", true)
                )
                .authorizeHttpRequests(ar -> ar.requestMatchers("/webjars/**").permitAll())
                .authorizeHttpRequests(ar -> ar.requestMatchers("/deletePatient/**").hasRole("ADMIN"))
                .authorizeHttpRequests(ar -> ar.requestMatchers("/admin/**").hasRole("ADMIN"))
                .authorizeHttpRequests(ar -> ar.requestMatchers("/user/**").hasRole("USER"))
                .authorizeHttpRequests(ar -> ar.anyRequest().authenticated())
                .exceptionHandling(ar -> ar
                        .accessDeniedPage("/notAuthorized"))
                .rememberMe(Customizer.withDefaults())
                .userDetailsService(userDetailsServiceImpl)
                .build();
    }

}

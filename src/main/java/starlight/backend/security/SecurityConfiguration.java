package starlight.backend.security;

import starlight.backend.security.mapper.SecurityMapper;
import starlight.backend.talent.repository.TalentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(c -> c
                .requestMatchers(antMatcher("/h2/**")).permitAll()
                //.requestMatchers(POST, "/register").permitAll()
                .requestMatchers("/talents").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/hello").authenticated()
                .anyRequest().permitAll()
        ); /*authenticated());*/
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.httpBasic();
//        http.csrf().disable();
        http.csrf()
                .ignoringRequestMatchers("/talents")
                .ignoringRequestMatchers(antMatcher("/h2/**"));
//        http.cors().disable();
        http.headers().frameOptions().disable();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(
            TalentRepository repository,
            SecurityMapper securityMapper
    ) {
        return email -> repository.findByMail(email)
                .map(securityMapper::toTalentDetails)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found user by email"));
    }

    //TODO do UserDetailsServiceImpl
}
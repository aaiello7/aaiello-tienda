package com.tiendaTech.tienda;

import com.tiendaTech.tienda.domain.Ruta;
import com.tiendaTech.tienda.service.RutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

//    public static final String[] PUBLIC_URLS = {
//        "/", "/index", "/fav/**", "/carrito/**", "/consultas/**", "/registro/**",
//        "/js/**", "/webjars/**", "/login", "/acceso_denegado"
//    };
//    public static final String[] USUARIO_URLS = {
//        "/facturar/carrito"
//    };
//    public static final String[] ADMIN_OR_VENDEDOR_URLS = {
//        "/producto/listado", "/categoria/listado", "/usuario/listado"
//    };
//    public static final String[] ADMIN_URLS = {
//        "/producto/**", "/categoria/**", "/usuario/**"
//    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, @Lazy RutaService rutaService)
            throws Exception {

        var rutas = rutaService.getRutas();

        http.authorizeHttpRequests(requests -> {
            for (Ruta ruta : rutas) {
                if (ruta.isRequiereRol()) {
                    requests.requestMatchers(ruta.getRuta()).hasRole(ruta.getRol().getRol());
                } else {
                    requests.requestMatchers(ruta.getRuta()).permitAll();
                }
            }
            requests.anyRequest().authenticated();
        });
        http.formLogin(form -> form // Configuración de formulario de login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
        ).logout(logout -> logout // Configuración de logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        ).exceptionHandling(exceptions -> exceptions // Manejo de excepciones
                .accessDeniedPage("/acceso_denegado")
        ).sessionManagement(session -> session // Configuración de sesiones
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    //Este método será reemplazado la siguiente semana
//    @Bean
//    public UserDetailsService users(PasswordEncoder passwordEncoder) {
//        UserDetails juan = User.builder()
//                .username("juan")
//                .password(passwordEncoder.encode("123"))
//                .roles("ADMIN")
//                .build();
//        UserDetails rebeca = User.builder()
//                .username("rebeca")
//                .password(passwordEncoder.encode("456"))
//                .roles("VENDEDOR")
//                .build();
//        UserDetails pedro = User.builder()
//                .username("pedro")
//                .password(passwordEncoder.encode("789"))
//                .roles("USUARIO") // Consistent con tu configuración
//                .build();
//        return new InMemoryUserDetailsManager(juan, rebeca, pedro);
//    }
    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build,
            @Lazy PasswordEncoder passwordEncoder,
            @Lazy UserDetailsService userDetailsService) throws Exception {
        build.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

}
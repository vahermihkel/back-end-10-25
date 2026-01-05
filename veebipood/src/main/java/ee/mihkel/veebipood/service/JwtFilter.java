package ee.mihkel.veebipood.service;

import ee.mihkel.veebipood.entity.Person;
import ee.mihkel.veebipood.entity.PersonRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// @Service --> täpselt sama. erinevus siis kui tehakse rakenduse ülene analüüs vms
//      milles on võimalik eristada @Service vs @Component klasse
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");
            Person person = jwtService.getPersonByToken(token);
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            if (person.getRole().equals(PersonRole.ADMIN)) {
                // see sulgudes olev roll on täpselt sama mis SecurityConfig failis olev
                // .hasRole()
                // .requestMatchers(HttpMethod.POST,"products").hasRole("MANAGER")
                //grantedAuthorities.add(new SimpleGrantedAuthority("MANAGER"));
                grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
            }
            if (person.getRole().equals(PersonRole.SUPERADMIN)) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
                grantedAuthorities.add(new SimpleGrantedAuthority("SUPERADMIN"));
            }
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            person.getId(),
                            person.getFirstName() + " " + person.getLastName(),
                            grantedAuthorities
                    )
            );
        }


        filterChain.doFilter(request,response); // toome originaali tagasi
    }
}

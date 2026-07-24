package com.finovoria.finastrax.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.finovoria.finastrax.tenant.TenantContext;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {


        String authHeader = request.getHeader("Authorization");


        if(authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }


        String token = authHeader.substring(7);


        try {


            Claims claims = jwtService.extractAllClaims(token);


            String email = claims.getSubject();

            String role = claims.get("role", String.class);


            Long tenantId =
                    claims.get("tenantId", Long.class);


            String databaseName =
                    claims.get("databaseName", String.class);



            TenantContext.setTenantId(tenantId);

            TenantContext.setDatabaseName(databaseName);



            if(email != null &&
                    SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {


                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(
                                    new SimpleGrantedAuthority(
                                            "ROLE_" + role
                                    )
                                )
                        );


                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }


        } catch(Exception e) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }


        filterChain.doFilter(request, response);
    }
}
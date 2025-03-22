package com.expenses.tracker.ExpensesTracker.Filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.expenses.tracker.ExpensesTracker.Exception.JwtTokenException;
import com.expenses.tracker.ExpensesTracker.Service.Implementation.CustomUserDetailService;
import com.expenses.tracker.ExpensesTracker.Service.Implementation.JwtHandlerService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHandlerService jwtService;
    @Autowired
    private CustomUserDetailService customUserDetailService;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7).trim();

                if (jwtService.validateToken(token)) {
                    String usernameFortoken = jwtService.getUsernameFromToken(token);

                    /*
                     * username means whatever take at login time
                     * username = email
                     */

                    UserDetails user = customUserDetailService.loadUserByUsername(usernameFortoken);

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                user,
                                null, user.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }

            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"JWT Token has expired. Please login again.\"}");
            throw new JwtTokenException("JWT Token has expired. Please login again.");
        } catch (MalformedJwtException | UnsupportedJwtException |

                IllegalArgumentException e) {
            throw new JwtTokenException("Invalid JWT Token.");
        }
    }

}

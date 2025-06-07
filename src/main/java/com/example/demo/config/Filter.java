package com.example.demo.config;

import com.example.demo.entity.Account;
import com.example.demo.exception.TokenException;
import com.example.demo.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
public class Filter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;
    @Autowired
    HandlerExceptionResolver handlerExceptionResolver;
    private final List<String> AUTH_PERMISSION = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/api/auth/login",
            "/api/auth/register",
            "/api/products",
            "/api/brands",
            "/api/categories",
            "/api/auth/forgot-password"
    );
    public boolean checkIsPublicAPI(String uri){
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return AUTH_PERMISSION.stream().anyMatch(pattern -> antPathMatcher.match(pattern, uri));
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isPublicAPI = checkIsPublicAPI(request.getRequestURI());
        if(isPublicAPI){
            filterChain.doFilter(request, response);
        }else{
            String token = getToken(request);
            if (token == null){
                handlerExceptionResolver.resolveException(request,response,null, new TokenException("Empty token!!"));
                return;
            }else{
                Account account;
                try {
                    account = tokenService.verifyAccountThroughToken(token);
                }catch(ExpiredJwtException e){
                    handlerExceptionResolver.resolveException(request,response,null, new TokenException("Expired token!!"));
                    return;
                }catch(MalformedJwtException e){
                    handlerExceptionResolver.resolveException(request,response,null, new TokenException("Invalid token!!"));
                    return;
                }

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken
                                (account, token, account.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            }

        }



    }
    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null){
            return null;
        }else {
            return authHeader.substring(7); // Bearer "token"
        }
    }
}

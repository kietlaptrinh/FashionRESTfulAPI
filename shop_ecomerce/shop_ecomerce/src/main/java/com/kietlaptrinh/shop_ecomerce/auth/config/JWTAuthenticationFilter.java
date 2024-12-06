package com.kietlaptrinh.shop_ecomerce.auth.config;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    // Dependency injection of helper class and user details service
    private final UserDetailsService userDetailsService;
    private final JWTTokenHelper jwtTokenHelper;

    public JWTAuthenticationFilter(JWTTokenHelper jwtTokenHelper,UserDetailsService userDetailsService) {
        this.jwtTokenHelper = jwtTokenHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        // If the header is missing or doesn't start with "Bearer", skip the filter
        if(null == authHeader || !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }
        try{
            String authToken = jwtTokenHelper.getToken(request);
            if(null != authToken){
                String userName = jwtTokenHelper.getUserNameFromToken(authToken);
                if(null != userName){
                    UserDetails userDetails= userDetailsService.loadUserByUsername(userName);

                    if(jwtTokenHelper.validateToken(authToken,userDetails)) {
                        // Create an authentication token and set it in the security contex
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

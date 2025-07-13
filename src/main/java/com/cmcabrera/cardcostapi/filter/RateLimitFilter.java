package com.cmcabrera.cardcostapi.filter;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Bucket paymentCardsCostBucket;
    private final Bucket authenticationBucket;

    public RateLimitFilter(Bucket paymentCardsCostBucket, Bucket authenticationBucket) {
        this.paymentCardsCostBucket = paymentCardsCostBucket;
        this.authenticationBucket = authenticationBucket;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/api/v1/payment-cards-cost")) {
            if (!paymentCardsCostBucket.tryConsume(1)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too many requests for payment cards cost. Please try again later.");
                return;
            }
        } else if (requestURI.startsWith("/api/v1/authenticate")) { // Corrected URI for authentication
            if (!authenticationBucket.tryConsume(1)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too many authentication attempts. Please try again later.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

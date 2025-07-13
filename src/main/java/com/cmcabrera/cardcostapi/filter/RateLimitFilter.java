package com.cmcabrera.cardcostapi.filter;

import com.cmcabrera.cardcostapi.dto.ApiErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Bucket paymentCardsCostBucket;
    private final Bucket authenticationBucket;
    private final ObjectMapper objectMapper;

    public RateLimitFilter(Bucket paymentCardsCostBucket, Bucket authenticationBucket, ObjectMapper objectMapper) {
        this.paymentCardsCostBucket = paymentCardsCostBucket;
        this.authenticationBucket = authenticationBucket;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/api/v1/payment-cards-cost")) {
            if (!paymentCardsCostBucket.tryConsume(1)) {
                handleTooManyRequests(response, "Too many requests for payment cards cost. Please try again later.");
                return;
            }
        } else if (requestURI.startsWith("/api/v1/authenticate")) {
            if (!authenticationBucket.tryConsume(1)) {
                handleTooManyRequests(response, "Too many authentication attempts. Please try again later.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleTooManyRequests(HttpServletResponse response, String message) throws IOException {
        ApiErrorDTO apiError = new ApiErrorDTO(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase(),
                Collections.singletonList(message)
        );

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(apiError));
    }
}

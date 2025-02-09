package co.jht.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoggingFilter implements jakarta.servlet.Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        chain.doFilter(wrappedRequest, wrappedResponse);

        // Log request details
        logRequestDetails(wrappedRequest);

        // Log response details
        logResponseDetails(wrappedResponse);

        // Copy the response body to the original response
        wrappedResponse.copyBodyToResponse();
    }

    private void logRequestDetails(ContentCachingRequestWrapper request) throws IOException {
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Request Headers: " + request.getHeaderNames());
        System.out.println("Request Body: " + new String(request.getContentAsByteArray(), StandardCharsets.UTF_8));
    }

    private void logResponseDetails(ContentCachingResponseWrapper response) throws IOException {
        System.out.println("Response Status: " + response.getStatus());
        System.out.println("Response Headers: " + response.getHeaderNames());
        System.out.println("Response Body: " + new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
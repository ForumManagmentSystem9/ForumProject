package com.example.demo.helpers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper extends WebAuthenticationDetails {
    private final String customField;

    public AuthenticationHelper(HttpServletRequest request) {
        super(request);
        this.customField = request.getParameter("customField");
    }

    public String getCustomField() {
        return customField;
    }

}

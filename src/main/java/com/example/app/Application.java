package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Map;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Controller
    static class AppController {

        private final RestClient restClient = RestClient.create();

        @Value("${api.base-url}")
        private String apiBaseUrl;

        @GetMapping("/")
        String home() {
            return "home";
        }

        @GetMapping("/profile")
        @PreAuthorize("hasAuthority('SCOPE_profile')")
        ModelAndView userProfile(OAuth2AuthenticationToken authentication) {
            return new ModelAndView("userProfile",
                Collections.singletonMap("details", authentication.getPrincipal().getAttributes()));
        }

        /**
         * Calls the protected API with the signed-in user's access token. The API rejects
         * the call with 403 if the token does not carry the {@code messages.read} scope.
         */
        @GetMapping("/messages")
        String messages(@RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client, Model model) {
            try {
                Map<String, Object> response = restClient.get()
                    .uri(apiBaseUrl + "/api/messages")
                    .headers(headers -> headers.setBearerAuth(client.getAccessToken().getTokenValue()))
                    .retrieve()
                    .body(new org.springframework.core.ParameterizedTypeReference<>() {});
                model.addAttribute("response", response);
            } catch (RestClientResponseException e) {
                model.addAttribute("error", "The API responded with " + e.getStatusCode().value()
                    + ". A 401 means the token was not accepted (check the API's ISSUER and AUDIENCE);"
                    + " a 403 means the token is missing the messages.read scope.");
            } catch (RestClientException e) {
                model.addAttribute("error", "Could not reach the API at " + apiBaseUrl
                    + ". Make sure it is running.");
            }
            return "messages";
        }
    }
}

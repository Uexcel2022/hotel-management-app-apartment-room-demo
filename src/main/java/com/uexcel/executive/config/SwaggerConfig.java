package com.uexcel.executive.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

    @Configuration
    public class SwaggerConfig {

        @Value("${keycloak.auth-server-url}")
        String authServerUrl;
        @Value("${keycloak.token-url}")
        String tokenUrl;

        private static final String OAUTH_SCHEME_NAME = "my_oAuth_security_schema";

        @Bean
        public OpenAPI openAPI() {
            return new OpenAPI().components(new Components()
                            .addSecuritySchemes(OAUTH_SCHEME_NAME, createOAuthScheme()))
                    .addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME))
                    .info(new Info().title("Executive Rooms Microservice")
                            .description("A service for executive rooms.")
                            .version("1.0"));
        }

        private SecurityScheme createOAuthScheme() {
            OAuthFlows flows = createOAuthFlows();
            return new SecurityScheme().type(SecurityScheme.Type.OAUTH2)
                    .flows(flows);
        }

        private OAuthFlows createOAuthFlows() {
            OAuthFlow flow = createAuthorizationCodeFlow();
            return new OAuthFlows().authorizationCode(flow);
        }

        private OAuthFlow createAuthorizationCodeFlow() {
            return new OAuthFlow()
                    .authorizationUrl(authServerUrl)
                    .tokenUrl(tokenUrl);

//                    .scopes(new Scopes().addString("read_access", "read data")
//                            .addString("write_access", "modify data"));
        }
    }

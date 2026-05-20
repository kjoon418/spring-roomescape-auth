package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

@Configuration
@EnableSpringHttpSession
public class HttpSessionConfig {

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        CookieHttpSessionIdResolver cookieResolver = new CookieHttpSessionIdResolver();
        HeaderHttpSessionIdResolver headerResolver = HeaderHttpSessionIdResolver.xAuthToken();

        return new HttpSessionIdResolver() {
            @Override
            public List<String> resolveSessionIds(HttpServletRequest request) {
                List<String> sessionIds = new ArrayList<>(cookieResolver.resolveSessionIds(request));
                if (sessionIds.isEmpty()) {
                    sessionIds.addAll(headerResolver.resolveSessionIds(request));
                }

                return sessionIds;
            }

            @Override
            public void setSessionId(HttpServletRequest request, HttpServletResponse response, String sessionId) {
                cookieResolver.setSessionId(request, response, sessionId);
                headerResolver.setSessionId(request, response, sessionId);
            }

            @Override
            public void expireSession(HttpServletRequest request, HttpServletResponse response) {
                cookieResolver.expireSession(request, response);
                headerResolver.expireSession(request, response);
            }
        };
    }
}

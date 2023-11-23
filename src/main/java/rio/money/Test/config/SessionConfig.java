package rio.money.Test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableSpringHttpSession
public class SessionConfig {

    public static Map<String, Session> sessionMap = new HashMap<>();

    @Bean
    public MapSessionRepository sessionRepository() {
        return new MapSessionRepository(sessionMap); // In-memory session storage
    }
}

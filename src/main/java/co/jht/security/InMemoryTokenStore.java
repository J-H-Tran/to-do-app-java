package co.jht.security;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTokenStore {

    private final ConcurrentHashMap<String, String> tokenStore = new ConcurrentHashMap<>();

    public void storeToken(String username, String token) {
        tokenStore.put(username, token);
    }

    public String getToken(String username) {
        return tokenStore.get(username);
    }

    public void removeToken(String username) {
        tokenStore.remove(username);
    }

    public boolean containsToken(String username) {
        return tokenStore.containsKey(username);
    }
}
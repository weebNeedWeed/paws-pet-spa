package com.paws.services.urlShortens;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;

@Service
@Scope("singleton")
public class UrlShortenServiceImpl implements UrlShortenService{
    private final Map<String, String> urlMap = new HashMap<>();

    @Override
    public String shorten(String name,String rawUrl) {
        String host = "http://localhost:8080/shorten/";
        urlMap.put(name, rawUrl);

        return host + name;
    }

    @Override
    public String getRawUrl(String name) {
        return urlMap.get(name);
    }
}

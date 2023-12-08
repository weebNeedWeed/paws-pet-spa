package com.paws.services.urlShortens;

public interface UrlShortenService {
    String shorten(String name, String rawUrl);

    String getRawUrl(String name);
}

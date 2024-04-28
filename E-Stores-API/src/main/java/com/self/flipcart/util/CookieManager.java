package com.self.flipcart.util;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieManager {

    public String configure(String name,String value, long maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .domain("localhost")
                .path("/")
                .httpOnly(true)
                .sameSite("Lax")
                .secure(false)
                .maxAge(maxAge)
                .build();
        return cookie.toString();
    }

    public String invalidate(String name){
       ResponseCookie cookie = ResponseCookie.from(name, "")
               .domain("localhost")
               .path("/")
               .httpOnly(true)
               .sameSite("Lax")
               .secure(false)
               .maxAge(0)
               .build();
        return cookie.toString();
    }
}

package com.devb.estores.util;

import com.devb.estores.config.AppEnv;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CookieManager {

    private final AppEnv appEnv;

    public String configure(String name,String value, long maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .domain(appEnv.getDomain())
                .path("/")
                .httpOnly(true)
                .sameSite("Lax")
                .secure(appEnv.isHttps())
                .maxAge(maxAge)
                .build();
        return cookie.toString();
    }

    public String invalidate(String name){
       ResponseCookie cookie = ResponseCookie.from(name, "")
               .domain(appEnv.getDomain())
               .path("/")
               .httpOnly(true)
               .sameSite("Lax")
               .secure(appEnv.isHttps())
               .maxAge(0)
               .build();
        return cookie.toString();
    }
}

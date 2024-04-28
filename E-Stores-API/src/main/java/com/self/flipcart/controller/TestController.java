package com.self.flipcart.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/d/test")
    public String login(){
        return "from d";
    }

    @GetMapping("/api/fkv1/test")
    @PreAuthorize("hasAuthority('SELLER')")
    public String login2() {
        return "from fkv1";
    }
}

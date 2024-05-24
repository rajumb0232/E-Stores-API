package com.devb.estores.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest
public class OptionsControllerTest {

    @Autowired
    private OptionsController optionsController;

    @Test
    public void testGetPrimeCategories(){
        assertEquals(HttpStatus.OK.value(), optionsController.getPrimeCategories().getStatusCode().value());
    }
}

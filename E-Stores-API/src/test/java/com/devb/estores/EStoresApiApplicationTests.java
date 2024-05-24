package com.devb.estores;

import com.devb.estores.controller.OptionsController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EStoresApiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private OptionsController optionsController;

	@Test
	public void testGetPrimeCategories(){
		assertEquals(HttpStatus.OK.value(), optionsController.getPrimeCategories().getStatusCode().value());
	}
}

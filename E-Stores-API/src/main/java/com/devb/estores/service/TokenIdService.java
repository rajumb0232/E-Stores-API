package com.devb.estores.service;

import com.devb.estores.enums.TokenType;

public interface TokenIdService {

    String getJti(String username, String deviceId, TokenType tokenType);
}

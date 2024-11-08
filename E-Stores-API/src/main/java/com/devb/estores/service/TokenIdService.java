package com.devb.estores.service;

import com.devb.estores.enums.TokenType;

public interface TokenIdService {

    String generateJwtId(String username, String deviceId, TokenType tokenType);

    String getJti(String username, String deviceId, TokenType tokenType);

    void deleteJti(String username, String deviceId);

    void deleteAllOtherIds(String username, String currentDeviceId);

    void deleteAllIds(String username);
}

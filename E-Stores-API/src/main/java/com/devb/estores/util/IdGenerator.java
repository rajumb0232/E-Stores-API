package com.devb.estores.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.time.Year;
import java.util.Optional;
import java.util.UUID;

public class IdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        return Optional.of(UUID.randomUUID().toString()).map(id -> {
            String prefix = "fc";
            String year = String.valueOf(Year.now());
            return prefix + year + id;
        }).get();
    }
}


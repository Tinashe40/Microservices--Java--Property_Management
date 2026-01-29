package com.tinash.userservice.shared.domain.id;

import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.stereotype.Component;


@Component
public class UlidGenerator implements IdGenerator {

    @Override
    public String generate() {
        return UlidCreator.getUlid().toString();
    }
}

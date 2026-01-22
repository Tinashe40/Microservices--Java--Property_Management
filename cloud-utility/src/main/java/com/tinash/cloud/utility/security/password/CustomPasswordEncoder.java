package com.tinash.cloud.utility.security.password;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder extends Argon2PasswordEncoder {

    public CustomPasswordEncoder() {
        super(16,
                32,
                1,
                1 << 14,
                2
        );
    }
}

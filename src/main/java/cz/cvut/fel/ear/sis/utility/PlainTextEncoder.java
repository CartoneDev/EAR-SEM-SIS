package cz.cvut.fel.ear.sis.utility;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/*
    Test purpose only PlainText password encoder.
 */
@Component
public class PlainTextEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
    }

}

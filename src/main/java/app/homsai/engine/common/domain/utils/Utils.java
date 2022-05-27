package app.homsai.engine.common.domain.utils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static SecureRandom random;


    public static List<String> getFieldsFromErrors(Errors errors) {
        List<String> result = new ArrayList<>();

        for (FieldError error : errors.getFieldErrors()) {
            result.add(error.getField());
        }

        return result;
    }

    public static String getRandomString(int length) {
        if (random == null) {
            random = new SecureRandom();
        }
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = length;

        String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}


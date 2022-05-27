package app.homsai.engine.common.domain.exceptions.validation;

import app.homsai.engine.common.domain.exceptions.BadRequestException;
import app.homsai.engine.common.domain.models.ErrorCodes;

import java.util.List;

public class ValidationFailedException extends BadRequestException {
    private static final int ERROR_CODE = ErrorCodes.VALIDATION_FAILED_EXCEPTION;

    public ValidationFailedException(List<String> fields) {
        super(ERROR_CODE, ValidationFailedException.buildMessage(fields));
    }

    public static String buildMessage(List<String> fields) {
        if (fields.size() == 1)
            return "Field " + fields.get(0) + " is mandatory";
        else {
            StringBuilder stringBuilder = new StringBuilder("Fields ");

            for (int i = 0; i < fields.size(); i++) {
                if (i >= 1)
                    stringBuilder.append(" / ");

                stringBuilder.append(fields.get(i));
            }

            stringBuilder.append(" are mandatory");
            String message = stringBuilder.toString();

            return message;
        }
    }
}

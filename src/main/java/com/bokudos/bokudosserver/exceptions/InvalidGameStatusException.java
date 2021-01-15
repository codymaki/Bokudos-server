package com.bokudos.bokudosserver.exceptions;

import com.bokudos.bokudosserver.constants.ErrorMessageConstants;

public class InvalidGameStatusException extends GenericException {
    public InvalidGameStatusException() {
        super(ErrorMessageConstants.INVALID_GAME_STATUS);
    }
}

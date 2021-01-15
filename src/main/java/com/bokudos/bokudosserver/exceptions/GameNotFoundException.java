package com.bokudos.bokudosserver.exceptions;

import com.bokudos.bokudosserver.constants.ErrorMessageConstants;

public class GameNotFoundException extends GenericException {
    public GameNotFoundException() {
        super(ErrorMessageConstants.GAME_NOT_FOUND);
    }
}

package com.bokudos.bokudosserver.exceptions;

import com.bokudos.bokudosserver.constants.ErrorMessageConstants;

public class PlayerNotFoundException extends GenericException {
    public PlayerNotFoundException() {
        super(ErrorMessageConstants.PLAYER_NOT_FOUND);
    }
}

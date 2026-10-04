package com.familyfund.domain.shared;

public class InvalidMoneyAmountException extends RuntimeException {

    public InvalidMoneyAmountException(String message) {
        super(message);
    }
}
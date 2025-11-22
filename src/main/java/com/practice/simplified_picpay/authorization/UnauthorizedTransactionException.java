package com.practice.simplified_picpay.authorization;

public class UnauthorizedTransactionException extends RuntimeException {

    public UnauthorizedTransactionException(String message){
        super(message);
    }
}

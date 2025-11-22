package com.practice.simplified_picpay.exceptions;

public class InvalidTransactionException
extends RuntimeException{
    public InvalidTransactionException(String message){
        super(message);
    }
}

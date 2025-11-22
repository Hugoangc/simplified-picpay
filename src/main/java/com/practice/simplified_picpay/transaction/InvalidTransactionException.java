package com.practice.simplified_picpay.transaction;

public class InvalidTransactionException
extends RuntimeException{
    public InvalidTransactionException(String message){
        super(message);
    }
}

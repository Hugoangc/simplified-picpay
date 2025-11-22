package com.practice.picpay.transaction;

public class InvalidTransactionException
extends RuntimeException{
    public InvalidTransactionException(String message){
        super(message);
    }
}

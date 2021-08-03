package com.bm.vendingmachine.service.exceptions;

/**
 * An exception for indicating that not enough funds have been provided for 
 * purchasing an item
 * 
 * @author Benjamin Munoz
 */
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String msg) {
        super(msg);
    }

    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}

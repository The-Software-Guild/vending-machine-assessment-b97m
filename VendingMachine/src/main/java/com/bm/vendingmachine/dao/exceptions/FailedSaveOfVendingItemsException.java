package com.bm.vendingmachine.dao.exceptions;

/**
 * An exception for indicating the failed saving of Vending Items
 * 
 * @author Benjamin Munoz
 */
public class FailedSaveOfVendingItemsException extends Exception {
    public FailedSaveOfVendingItemsException(String msg) {
        super(msg);
    }

    public FailedSaveOfVendingItemsException(String message, Throwable cause) {
        super(message, cause);
    }
}

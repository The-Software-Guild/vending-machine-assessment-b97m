package com.bm.vendingmachine.service.exceptions;

/**
 * An exception for indicating to the user that either the Vending Machine does
 * not have an item, or that item has been depleted.
 * 
 * @author Benjamin Munoz
 */
public class NoItemInventoryException extends Exception {
    public NoItemInventoryException(String msg) {
        super(msg);
    }

    public NoItemInventoryException(String message, Throwable cause) {
        super(message, cause);
    }
}

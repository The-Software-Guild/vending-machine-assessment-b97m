package com.bm.vendingmachine.service;

import java.math.BigDecimal;

/**
 * An enum for representing the values of various U.S. coins
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 1, 2021
 */
public enum Coin {
    QUARTER(new BigDecimal("0.25")),
    DIME(new BigDecimal("0.10")),
    NICKEL(new BigDecimal("0.05")),
    PENNY(new BigDecimal("0.01"));
    
    private BigDecimal value;
    private Coin(BigDecimal value) {
        this.value = value;
    }

    /**
     * @return The value of the coin in dollars
     */
    public BigDecimal getValue() {
        return value;
    }
    
    /**
     * @return The qualified name of this coin
     */
    public String repName() {
        return this.toString().charAt(0)
                + this.toString().substring(1).toLowerCase();
    }
}

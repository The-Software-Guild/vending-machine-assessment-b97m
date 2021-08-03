package com.bm.vendingmachine.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Serves as the primary DTO of the whole application.
 * Each instance represents an item in the vending machine.
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Jul 31, 2021
 */
public class VendingMachineItem {
    private String name;
    private BigDecimal cost;
    private BigInteger quantity;

    public VendingMachineItem(String name, BigDecimal cost, BigInteger quantity) {
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public BigInteger getQuantity() {
        return quantity;
    }

    public void setQuantity(BigInteger quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getCost() {
        return cost;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VendingMachineItem other = (VendingMachineItem) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return "VendingMachineItem{" + "name=" + name + ", cost=" + cost + ", quantity=" + quantity + '}';
    }   
}

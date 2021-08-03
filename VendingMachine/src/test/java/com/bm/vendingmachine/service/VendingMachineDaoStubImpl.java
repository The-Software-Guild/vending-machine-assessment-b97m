/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bm.vendingmachine.service;

import com.bm.vendingmachine.dao.VendingMachineDao;
import com.bm.vendingmachine.dao.exceptions.FailedLoadOfVendingItemsException;
import com.bm.vendingmachine.dao.exceptions.FailedSaveOfVendingItemsException;
import com.bm.vendingmachine.dto.VendingMachineItem;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A Stub Vending Machine DAO implementation for 
 * testing the Service Layer
 * 
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 1, 2021
 */
public class VendingMachineDaoStubImpl implements VendingMachineDao {
    private Map<String, VendingMachineItem> itemsMap;

    public VendingMachineDaoStubImpl() {
        itemsMap = new HashMap<>();
        itemsMap.put(
            "Pepsi", 
            new VendingMachineItem(
                "Pepsi", 
                new BigDecimal("2.99"), 
                new BigInteger("10")
            )
        );
        itemsMap.put(
            "Coke", 
            new VendingMachineItem(
                "Coke", 
                new BigDecimal("2.99"), 
                new BigInteger("1")
            )
        );
    }
    
    @Override
    public void loadItems() throws FailedLoadOfVendingItemsException {
    }

    @Override
    public List<VendingMachineItem> getAllItems() {
        return new LinkedList<>(itemsMap.values());
    }

    @Override
    public Optional<VendingMachineItem> getItemByName(String name) {
        VendingMachineItem item = itemsMap.get(name);
        if (item == null) {
            return Optional.empty();
        }
        return Optional.of(item);
    }

    @Override
    public Optional<VendingMachineItem> removeOneOfItem(String name) {
        VendingMachineItem item = itemsMap.get(name);
        if (item == null || item.getQuantity().signum() <= 0) {
            return Optional.empty();
        }
        item.setQuantity(item.getQuantity().subtract(BigInteger.ONE));
        return Optional.of(item);
    }

    @Override
    public void saveItems() throws FailedSaveOfVendingItemsException {
    }
}

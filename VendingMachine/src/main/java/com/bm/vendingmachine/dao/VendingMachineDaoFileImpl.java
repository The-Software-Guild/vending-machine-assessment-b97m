package com.bm.vendingmachine.dao;

import com.bm.vendingmachine.dao.exceptions.FailedLoadOfVendingItemsException;
import com.bm.vendingmachine.dao.exceptions.FailedSaveOfVendingItemsException;
import com.bm.vendingmachine.dto.VendingMachineItem;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * The file implementation of the VendingMachineDao interface
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Jul 31, 2021
 */
public class VendingMachineDaoFileImpl implements VendingMachineDao {
    private final String ITEMS_FILE;
    private static final String DELIMITER = "::";
    private final Map<String, VendingMachineItem> ITEMS_MAP;
    
    public VendingMachineDaoFileImpl() {
        ITEMS_MAP = new HashMap<>();
        ITEMS_FILE = "items.txt";
    }
    
    public VendingMachineDaoFileImpl(String itemsFile) {
        ITEMS_MAP = new HashMap<>();
        ITEMS_FILE = itemsFile;
    }
    
    @Override
    public void loadItems() throws FailedLoadOfVendingItemsException {
        Scanner reader;
        try {
            reader = new Scanner(new BufferedReader(new FileReader(ITEMS_FILE)));
        } catch (FileNotFoundException ex) {
            throw new FailedLoadOfVendingItemsException(
                "Unable to load vending items", 
                ex
            );
        }
        
        while (reader.hasNextLine()) {
            String[] tokens = reader.nextLine().split(DELIMITER);
            String name = tokens[0];
            BigDecimal cost = new BigDecimal(tokens[1]);
            BigInteger quantity = new BigInteger(tokens[2]);
            
            ITEMS_MAP.put(name, new VendingMachineItem(name, cost, quantity));
        }
        reader.close();
    }

    @Override
    public List<VendingMachineItem> getAllItems() {
        return new LinkedList<>(ITEMS_MAP.values());
    }

    @Override
    public Optional<VendingMachineItem> getItemByName(String name) {
        var item = ITEMS_MAP.get(name);
        if (item == null) {
            return Optional.empty();
        }
        return Optional.of(item);
    }

    @Override
    public Optional<VendingMachineItem> removeOneOfItem(String name) {
        var item = ITEMS_MAP.get(name);
        if (item == null || item.getQuantity().signum() <= 0) {
            return Optional.empty();
        }
        item.setQuantity(item.getQuantity().subtract(BigInteger.ONE));
        return Optional.of(item);
    }

    @Override
    public void saveItems() throws FailedSaveOfVendingItemsException {
        PrintWriter writer;
        try {
            writer = new PrintWriter(new FileWriter(ITEMS_FILE));
        } catch (IOException ex) {
            throw new FailedSaveOfVendingItemsException("Unable to save vending items");
        }
        
        ITEMS_MAP.values().forEach(item -> {
            writer.format(
                "%s::%s::%s%n",
                item.getName(),
                item.getCost(),
                item.getQuantity()
            );
        });
        
        writer.close();
    }
}

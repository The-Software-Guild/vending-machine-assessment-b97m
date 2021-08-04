package com.bm.vendingmachine.ui;

import com.bm.vendingmachine.dto.VendingMachineItem;
import com.bm.vendingmachine.service.Coin;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Serves as the view component of this whole application
 * It communicates with a User I/O component
 * 
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Jul 31, 2021
 */
public class VendingMachineView {
    private UserIo userIo;

    public VendingMachineView(UserIo userIo) {
        this.userIo = userIo;
    }
    
    /**
     * Display a line of text to the user
     * @param line 
     */
    public void displayLine(String line) {
        userIo.displayLine(line);
    }
    
    /**
     * Display an informative line of text to the user
     * @param line 
     */
    public void displayInformationalLine(String line) {
        userIo.displayInformationalLine(line);
    }
    
    /**
     * Display an error line of text to the user
     * @param line 
     */
    public void displayErrorLine(String line) {
        userIo.displayErrorLine(line);
    }
    
    /**
     * Display the menu options to the user
     * @param options 
     */
    public void displayMainMenuOptions(String... options) {
        userIo.displayAroundContents("MAIN MENU OPTIONS", options);
    }
    
    /**
     * Display the coin depositing options to the user
     * @param options 
     */
    public void displayCoinDepositOptions(String... options) {
        userIo.displayAroundContents("COIN DEPOSIT OPTIONS", options);
    }
    
    /**
     * Displays the vending items to the user
     * @param items 
     */
    public void displayVendingItems(List<VendingMachineItem> items) {
        this.userIo.displayInformationalLine("Featured Vending Items");
        items.forEach(item -> {
            this.userIo.displayAroundContents(
                item.getName(),
                new String[] {
                    "Cost: $" + item.getCost(),
                    "Quantity: " + item.getQuantity()
                }
            );
        });
    }
    
    /**
     * Displays the change to the user
     * @param change 
     */
    public void displayChange(BigDecimal change) {
        BigInteger pennies = change.multiply(new BigDecimal("100")).toBigInteger();
        
        BigInteger quarters = pennies.divide(Coin.QUARTER.getCentValue());
        pennies = pennies.remainder(Coin.QUARTER.getCentValue());
        
        BigInteger dimes = pennies.divide(Coin.DIME.getCentValue());
        pennies = pennies.remainder(Coin.DIME.getCentValue());
        
        BigInteger nickels = pennies.divide(Coin.NICKEL.getCentValue());
        pennies = pennies.remainder(Coin.NICKEL.getCentValue());
        
        userIo.displayAroundContents(
            "Change returned",
            new String[] {
                "Quarters: " + quarters.toString(),
                "Dimes: " + dimes.toString(),
                "Nickels: " + nickels.toString(),
                "Pennies: " + pennies.toString()
            }
        );
    }
    
    /**
     * Provides a supplier for a user-entered int
     * 
     * @param prompt The prompt to solicit the user for an int
     * @param test The condition for which the entered int will be accepted
     * @param errorText The error to display for invalid input
     * @return The aforementioned supplier
     */
    public Supplier<Integer> intSupplier(
        String prompt, 
        Predicate<Integer> test, 
        String errorText) {
        
        return userIo.intSupplier(prompt, test, errorText);
    }
    
    /**
     * Provides a supplier for a user-entered String
     * 
     * @param prompt The prompt to solicit the user for a String
     * @param test The condition for which the entered String will be accepted
     * @param errorText The error to display for invalid input
     * @return The aforementioned supplier
     */
    public Supplier<String> stringSupplier(
        String prompt, 
        Predicate<String> test, 
        String errorText) {
        
        return userIo.stringSupplier(prompt, test, errorText);
    }
    
    /**
     * Provides a supplier for a user-entered BigInteger
     * 
     * @param prompt The prompt to solicit the user for a BigInteger
     * @param test The condition for which the entered BigInteger will be accepted
     * @param errorText The error to display for invalid input
     * @return The aforementioned supplier
     */
    public Supplier<BigInteger> bigIntegerSupplier(
        String prompt, 
        Predicate<BigInteger> test, 
        String errorText) {
        
        return userIo.bigIntegerSupplier(prompt, test, errorText);
    }
    
    /**
     * Provides a supplier for a user-entered BigDecimal
     * 
     * @param prompt The prompt to solicit the user for a BigDecimal
     * @param test The condition for which the entered BigDecimal will be accepted
     * @param errorText The error to display for invalid input
     * @return The aforementioned supplier
     */
    public Supplier<BigDecimal> bigDecimalSupplier(
        String prompt, 
        Predicate<BigDecimal> test, 
        String errorText) {
        
        return userIo.bigDecimalSupplier(prompt, test, errorText);
    }

    /**
     * Frees all resources associated with this view
     */
    public void close() {
        userIo.close();
    }
}
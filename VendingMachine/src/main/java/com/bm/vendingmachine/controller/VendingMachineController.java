package com.bm.vendingmachine.controller;

import com.bm.vendingmachine.dao.exceptions.FailedLoadOfVendingItemsException;
import com.bm.vendingmachine.dao.exceptions.FailedSaveOfVendingItemsException;
import com.bm.vendingmachine.service.Coin;
import com.bm.vendingmachine.service.VendingMachineService;
import com.bm.vendingmachine.service.exceptions.InsufficientFundsException;
import com.bm.vendingmachine.service.exceptions.NoItemInventoryException;
import com.bm.vendingmachine.ui.VendingMachineView;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Serves as controller for the whole application
 * 
 * Communicates with service and view components
 * 
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Jul 31, 2021
 */
public class VendingMachineController {
    private VendingMachineView view;
    private VendingMachineService service;

    public VendingMachineController(VendingMachineView view, VendingMachineService service) {
        this.view = view;
        this.service = service;
    }
    
    /**
     * Executes a run of this application
     */
    public void run() {
        // attempt to load Vending Machine Items
        try {
            service.loadItems();
        } catch (FailedLoadOfVendingItemsException ex) {
            view.displayErrorLine(
                "Warning: Unable to load vending items into memory"
            );
        }
        
        boolean active = true;
        
        String[] mainMenuOptions = {
            "1. Add Funds",
            "2. Purchase an Item",
            "3. List Items",
            "4. Exit"
        };
        
        view.displayVendingItems(service.getAllItems());
        view.displayLine("");
        while(active) {
            view.displayMainMenuOptions(mainMenuOptions);

            view.displayInformationalLine(
                "Funds in Machine: $" + service.getFundsAvailable()
            );
            
            int choice = view.intSupplier(
                "Pick an option",
                val -> (1 <= val) && (val <= 4), 
                "The input must be some integer in the domain [1, 4]"
            ).get();
            
            switch (choice) {
                case 1:
                    addFunds();
                    break;
                case 2:
                    purchaseItem();
                    break;
                case 3:
                    listItems();
                    break;
                case 4:
                    active = false;
                    break;
                default:
                    view.displayLine("UNKNOWN COMMAND");
                    break;
            }
        }
        
        // attempt to save items
        try {
            service.saveItems();
        } catch (FailedSaveOfVendingItemsException ex) {
            view.displayErrorLine("Warning: Unable to save vending items");
        }
        
        // free all resources
        view.close();
        service.close();
    }
    
    private void addFunds() {
        boolean active = true;
        String[] coinOptions = new String[Coin.values().length + 1];
        for (int i = 0; i < coinOptions.length - 1; i++) {
            coinOptions[i] = (i + 1) + ". " + Coin.values()[i].repName();
        }
        coinOptions[coinOptions.length - 1] = coinOptions.length + ". " + "Return to main menu";
        
        while (active) {
            view.displayCoinDepositOptions(coinOptions);
            view.displayInformationalLine(
                "Funds in Machine: $" + service.getFundsAvailable()
            );
            
            int choice = view.intSupplier(
                "Pick an option", 
                val -> (1 <= val) && (val <= coinOptions.length),
                "The input must be one of the numbers above"
            ).get();
            BigInteger amount;
            switch(choice) {
                case 1:
                    amount = view.bigIntegerSupplier(
                        "How many quarters do you wish to deposit?",
                        (BigInteger val) -> val.signum() >= 0,
                        "You must enter a nonnegative integer"
                    ).get();
                    
                    service.addCoins(Coin.QUARTER, amount);
                    break;
                case 2:
                    amount = view.bigIntegerSupplier(
                        "How many dimes do you wish to deposit?",
                        (BigInteger val) -> val.signum() >= 0,
                        "You must enter a nonnegative integer"
                    ).get();
                    
                    service.addCoins(Coin.DIME, amount);
                    break;
                case 3:
                    amount = view.bigIntegerSupplier(
                        "How many nickels do you wish to deposit?",
                        (BigInteger val) -> val.signum() >= 0,
                        "You must enter a nonnegative integer"
                    ).get();
                    
                    service.addCoins(Coin.NICKEL, amount);
                    break;
                case 4:
                    amount = view.bigIntegerSupplier(
                        "How many pennies do you wish to deposit?",
                        (BigInteger val) -> val.signum() >= 0,
                        "You must enter a nonnegative integer"
                    ).get();
                    
                    service.addCoins(Coin.PENNY, amount);
                    break;
                case 5:
                    active = false;
                    pauseBeforeContinuation();
                    break;
                default:
                    view.displayErrorLine("UNKNOWN COMMAND");
            }
        }
    }

    private void purchaseItem() {
        BigDecimal currentFunds = service.getFundsAvailable();
        if (currentFunds.signum() <= 0) {
            view.displayErrorLine("There are no funds in the vending machine yet");
            view.displayErrorLine("Please add some funds before you make a purchase");
        } else {
            String itemName = view.stringSupplier(
                "What is the name of the item you want to purchase?", 
                str -> true, 
            "").get();
            
            try {
                BigDecimal change = service.transactItem(itemName);
                view.displayInformationalLine("Transaction Successful");
                view.displayChange(change);
            } catch (NoItemInventoryException | InsufficientFundsException ex) {
                view.displayErrorLine(ex.getMessage());
            }            
        }
        pauseBeforeContinuation();
    }

    private void listItems() {
        view.displayVendingItems(service.getAllItems());
        pauseBeforeContinuation();
    }
    
    private void pauseBeforeContinuation() {
        view.stringSupplier("Press ENTER to continue", val -> true, "").get();
    }
}

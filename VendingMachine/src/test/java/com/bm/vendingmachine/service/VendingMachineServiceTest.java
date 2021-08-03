/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bm.vendingmachine.service;

import com.bm.vendingmachine.dto.VendingMachineItem;
import com.bm.vendingmachine.service.exceptions.InsufficientFundsException;
import com.bm.vendingmachine.service.exceptions.NoItemInventoryException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Benjamin Munoz
 */
public class VendingMachineServiceTest {
    
    private VendingMachineService service;
    
    public VendingMachineServiceTest() {
    }

    @BeforeEach
    public void setupService() {
        service = new VendingMachineService(
            new VendingMachineDaoStubImpl(),
            new VendingMachineAuditDaoStubImpl()
        );
    }
    
    @Test
    public void testGetAllItems() {
        // repeated attempts should not change results
        for (int i = 0; i < 10; i++) {
            List<VendingMachineItem> items = service.getAllItems();
            assertNotNull(items, "This element should not be null");

            // check consistency with the stub implementation of dao
            assertEquals(items.size(), 2, "There should be two items");
            assertTrue(
                items.contains(
                    new VendingMachineItem(
                        "Pepsi", 
                        new BigDecimal("2.99"), 
                        new BigInteger("10")
                    )
                )
            );
            assertTrue(
                items.contains(
                    new VendingMachineItem(
                        "Coke", 
                        new BigDecimal("2.99"), 
                        new BigInteger("1")
                    )
                )
            );
        }
    }

    @Test
    public void testGetFundsAndAddCoins() {
        assertEquals(
            service.getFundsAvailable(), 
            BigDecimal.ZERO,
            "Services should start off with no funds"
        );
        
        BigDecimal totalFunds = BigDecimal.ZERO;
        for (Coin coin : Coin.values()) {
            service.addCoins(coin, BigInteger.ONE);
            totalFunds = totalFunds.add(coin.getValue());
        }
        
        assertEquals(service.getFundsAvailable(), totalFunds);
        
        totalFunds = totalFunds.add(new BigDecimal("0.15"));
        service.addCoins(Coin.NICKEL, new BigInteger("3"));
        
        assertEquals(service.getFundsAvailable(), totalFunds);
    }

    @Test
    public void testGetFundsAddCoinsGetAllAndTransactItem() {
        assertEquals(
            service.getFundsAvailable(), 
            BigDecimal.ZERO, 
            "Services should start with no funds"
        );
        
        // check consistency with the stub implementation of dao
        List<VendingMachineItem> items = service.getAllItems();
        assertNotNull(items, "This element should not be null");
        assertEquals(items.size(), 2, "There should be two items");
        assertTrue(
            items.contains(
                new VendingMachineItem(
                    "Pepsi", 
                    new BigDecimal("2.99"), 
                    new BigInteger("10")
                )
            )
        );
        assertTrue(
            items.contains(
                new VendingMachineItem(
                    "Coke", 
                    new BigDecimal("2.99"), 
                    new BigInteger("1")
                )
            )
        );
        
        // try buying an item that does not exist, should trigger the
        // appropriate exception
        try {
            service.transactItem("aowejfoawiejf");
        } catch (NoItemInventoryException ex) {
        } catch (InsufficientFundsException ex) {
            fail("Should've thrown a NoItemInventoryException");
        }
        
        // try buying a pepsi without enough funds, should trigger the
        // appropriate exception
        try {
            service.transactItem("Pepsi");
        } catch (NoItemInventoryException ex) {
            fail("Should've thrown an InsufficientFundsException");
        } catch (InsufficientFundsException ex) {
        }
        
        // begin $3.00 adding funds to buy the pepsi
        // change should be rendered appropriately
        service.addCoins(Coin.QUARTER, new BigInteger("12"));
        assertEquals(
            service.getFundsAvailable(), 
            new BigDecimal("3.00"),
            "Adding 12 quarters should add $3.00 to the machine"
        );
        try {
            BigDecimal change = service.transactItem("Pepsi");
            assertEquals(
                change, 
                new BigDecimal("0.01"), 
                "The Pepsi cost $2.99, so the change should be $0.01"
            );
        } catch (InsufficientFundsException | NoItemInventoryException ex) {
            fail("No exceptions should be thrown");
        }        
        assertEquals(
            service.getFundsAvailable(), 
            BigDecimal.ZERO, 
            "All the funds should be depleted from the machine after a purchase"
        );
        
        // now try adding funds and then buying a coke
        service.addCoins(Coin.QUARTER, new BigInteger("12"));
        assertEquals(
            service.getFundsAvailable(), 
            new BigDecimal("3.00"),
            "Adding 12 quarters should add $3.00 to the machine"
        );
        try {
            BigDecimal change = service.transactItem("Coke");
            assertEquals(
                change, 
                new BigDecimal("0.01"), 
                "The Coke cost $2.99, so the change should be $0.01"
            );
        } catch (InsufficientFundsException | NoItemInventoryException ex) {
            fail("No exceptions should be thrown");
        }        
        assertEquals(
            service.getFundsAvailable(), 
            BigDecimal.ZERO, 
            "All the funds should be depleted from the machine after a purchase"
        );
        
        // Expect only the quantites of the items to change
        items = service.getAllItems();
        assertNotNull(items, "This element should not be null");
        assertEquals(items.size(), 2, "There should still be two items");
        assertTrue(
            items.contains(
                new VendingMachineItem(
                    "Pepsi", 
                    new BigDecimal("2.99"), 
                    new BigInteger("9")
                )
            )
        );
        assertTrue(
            items.contains(
                new VendingMachineItem(
                    "Coke", 
                    new BigDecimal("2.99"), 
                    new BigInteger("0")
                )
            )
        );

        // now try adding funds and then buying a coke again
        // the appropriate exception should be triggered
        service.addCoins(Coin.QUARTER, new BigInteger("12"));
        assertEquals(
            service.getFundsAvailable(), 
            new BigDecimal("3.00"),
            "Adding 12 quarters should add $3.00 to the machine"
        );        
        try {
            service.transactItem("Coke");
        } catch (NoItemInventoryException ex) {
        } catch (InsufficientFundsException ex) {
            fail("Should've thrown NoItemInventoryException");
        }
    }
}

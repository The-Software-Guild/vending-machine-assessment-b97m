package com.bm.vendingmachine.dao;

import com.bm.vendingmachine.dao.exceptions.FailedLoadOfVendingItemsException;
import com.bm.vendingmachine.dao.exceptions.FailedSaveOfVendingItemsException;
import com.bm.vendingmachine.dto.VendingMachineItem;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author drive
 */
public class VendingMachineDaoConsoleImplTest {
    private static final String ITEMS_EMPTY = "items_sim_empty.txt";
    private static final String ITEMS_SINGLE = "items_sim_single.txt";
    private static final String ITEMS_MULTI = "items_sim_multi.txt";
    private static final String ITEMS_LOAD_SAVE = "items_sim_load_save.txt";
    private static final String ITEMS_GET_REMOVE = "items_sim_get_remove.txt";
    private static final String ITEMS_GET_REMOVE_SAVE = "items_sim_get_remove_save.txt";
    
    private VendingMachineDao dao;
    
    public VendingMachineDaoConsoleImplTest() {
    }
    
    @Test
    public void testLoadAndGetAllOnEmpty() {
        fileSetup(ITEMS_EMPTY);
        
        dao = new VendingMachineDaoFileImpl(ITEMS_EMPTY);
        try {
            dao.loadItems();
        } catch (FailedLoadOfVendingItemsException ex) {
            fail("The dao should not fail to load this file. It exists");
        }
        
        List<VendingMachineItem> allItems = dao.getAllItems();
        assertNotNull(allItems, "This list should not be null");
        assertTrue(allItems.isEmpty(), "The list should be empty");
        
        allItems = dao.getAllItems();
        assertNotNull(allItems, "Again, this list should not be null");
        assertTrue(allItems.isEmpty(), "The list should still be empty");
    }
    
    @Test
    public void testLoadAndGetAllOnSingle() {
        fileSetup(ITEMS_SINGLE, "Pepsi::4.99::10");
        
        dao = new VendingMachineDaoFileImpl(ITEMS_SINGLE);
        try {
            dao.loadItems();
        } catch (FailedLoadOfVendingItemsException ex) {
            fail("The dao should not fail to load this file. It exists");
        }
        
        List<VendingMachineItem> allItems = dao.getAllItems();
        assertNotNull(allItems, "This list should not be null");
        assertTrue(allItems.size() == 1, "There should be one item in the list");
        assertTrue(
            allItems.contains(
                new VendingMachineItem(
                    "Pepsi", 
                    new BigDecimal("4.99"), 
                    new BigInteger("10")
                )
            )
        );
        
        allItems = dao.getAllItems();
        assertNotNull(allItems, "Again, this list should not be null");
        assertTrue(allItems.size() == 1, "This list should still have one item");
        assertTrue(
            allItems.contains(
                new VendingMachineItem(
                    "Pepsi", 
                    new BigDecimal("4.99"), 
                    new BigInteger("10")
                )
            )
        );
    }
    
    @Test
    public void testLoadAndGetAllOnMultiple() {
        fileSetup(ITEMS_MULTI, "Pepsi::4.99::10", "Doritos::3.99::200");
        
        dao = new VendingMachineDaoFileImpl(ITEMS_MULTI);
        try {
            dao.loadItems();
        } catch (FailedLoadOfVendingItemsException ex) {
            fail("The dao should not fail to load this file. It exists");
        }
        
        List<VendingMachineItem> allItems = dao.getAllItems();
        assertNotNull(allItems, "This list should not be null");
        assertTrue(allItems.size() == 2, "There should be two items in the list");
        assertTrue(
            allItems.contains(
                new VendingMachineItem(
                    "Pepsi", 
                    new BigDecimal("4.99"), 
                    new BigInteger("10")
                )
            )
        );
        assertTrue(
            allItems.contains(
                new VendingMachineItem(
                    "Doritos", 
                    new BigDecimal("3.99"), 
                    new BigInteger("200")
                )
            )
        );
        
        allItems = dao.getAllItems();
        assertNotNull(allItems, "Again, this list should not be null");
        assertTrue(allItems.size() == 2, "This list should still have two items");
        assertTrue(
            allItems.contains(
                new VendingMachineItem(
                    "Pepsi", 
                    new BigDecimal("4.99"), 
                    new BigInteger("10")
                )
            )
        );
        assertTrue(
            allItems.contains(
                new VendingMachineItem(
                    "Doritos", 
                    new BigDecimal("3.99"), 
                    new BigInteger("200")
                )
            )
        );
    }
    
    @Test
    public void testLoadAndSave() {
        fileSetup(ITEMS_LOAD_SAVE, "Pepsi::3.99::5", "Snickers::2.99::4");
        
        dao = new VendingMachineDaoFileImpl(ITEMS_LOAD_SAVE);
        
        try {
            dao.loadItems();
        } catch (FailedLoadOfVendingItemsException ex) {
            fail("The dao should not fail to load this file, it exists");
        }
        
        List<VendingMachineItem> initialList = dao.getAllItems();
        assertNotNull(initialList, "This list should not be null");
        assertEquals(initialList.size(), 2, "There should be two elements in this list");
        assertTrue(
            initialList.contains(
                new VendingMachineItem(
                    "Pepsi", 
                    new BigDecimal("3.99"), 
                    new BigInteger("5")
                )
            )
        );
        assertTrue(
            initialList.contains(
                new VendingMachineItem(
                    "Snickers", 
                    new BigDecimal("2.99"), 
                    new BigInteger("4")
                )
            )
        );
        
        try {
            dao.saveItems();
        } catch (FailedSaveOfVendingItemsException ex) {
            fail("The dao should not fail to save to this file either");
        }
        
        try {
            dao.loadItems();
        } catch (FailedLoadOfVendingItemsException ex) {
            fail("Again, the dao should not fail to load this file, it exists");
        }
        
        List<VendingMachineItem> laterList = dao.getAllItems();
        assertNotNull(laterList, "This list should not be null");
        assertEquals(laterList.size(), 2, "There should be two elements in this list");        
        
        initialList.forEach(item -> assertTrue(laterList.contains(item)));
    }
    
    @Test
    public void testGetAndRemove() throws FailedLoadOfVendingItemsException {
        fileSetup(ITEMS_GET_REMOVE, "Pepsi::2.99::1");
        
        dao = new VendingMachineDaoFileImpl(ITEMS_GET_REMOVE);
        
        dao.loadItems();
        
        Optional<VendingMachineItem> possDud = dao.getItemByName("ex nihilo");
        assertTrue(
            possDud.isEmpty(), 
            "There should be nothing since this item is not in the vending machine"
        );
        
        Optional<VendingMachineItem> presentPepsi = dao.getItemByName("Pepsi");
        assertTrue(
            presentPepsi.isPresent(), 
            "This element should be nonempty"
        );
        assertEquals(
            presentPepsi.get(), 
            new VendingMachineItem(
                "Pepsi", 
                new BigDecimal("2.99"), 
                BigInteger.ONE
            ),
            "This element should contain one Pepsi worth $2.99"
        );
        
        dao.removeOneOfItem("Pepsi");
        
        presentPepsi = dao.getItemByName("Pepsi");
        
        presentPepsi.ifPresentOrElse(
            pepsi -> assertEquals(
                    pepsi.getQuantity(), 
                    BigInteger.ZERO, 
                    "There should be no pepsi left"
            ), () -> fail("This element should not be empty")
        );
        
        assertTrue(
            dao.removeOneOfItem("Pepsi").isEmpty(), 
            "This element should be empty since there is no more pepsi to remove"
        );
        
        dao.removeOneOfItem("awef").ifPresent(
            pepsi -> fail("This element should be empty")
        );
    }
    
    @Test
    public void testGetRemoveSave() throws FailedLoadOfVendingItemsException, FailedSaveOfVendingItemsException {
        fileSetup(ITEMS_GET_REMOVE_SAVE, "Pepsi::3.99::10");
        
        dao = new VendingMachineDaoFileImpl(ITEMS_GET_REMOVE_SAVE);
        
        dao.loadItems();
        
        Optional<VendingMachineItem> presentPepsi = dao.getItemByName("Pepsi");
        
        presentPepsi.ifPresentOrElse(
            pepsi -> assertEquals(
                pepsi, 
                new VendingMachineItem(
                    "Pepsi", 
                    new BigDecimal("3.99"), 
                    new BigInteger("10")
                ),
                "There should be ten Pepsis each worth $3.99"
            ),
            () -> fail("This element should not be empty")
        );
        
        assertTrue(dao.removeOneOfItem("Pepsi").isPresent(), "A removal should've occured");
        
        presentPepsi = dao.getItemByName("Pepsi");        
        presentPepsi.ifPresentOrElse(
            pepsi -> assertEquals(
                pepsi, 
                new VendingMachineItem(
                    "Pepsi", 
                    new BigDecimal("3.99"), 
                    new BigInteger("9")
                ),
                "There should be nine Pepsis each worth $3.99"
            ),
            () -> fail("This element should not be empty")
        );
        
        dao.saveItems();
        dao.loadItems();
        
        presentPepsi = dao.getItemByName("Pepsi");        
        presentPepsi.ifPresentOrElse(
            pepsi -> assertEquals(
                pepsi, 
                new VendingMachineItem(
                    "Pepsi", 
                    new BigDecimal("3.99"), 
                    new BigInteger("9")
                ),
                "There should be nine Pepsis each worth $3.99"
            ),
            () -> fail("This element should not be empty")
        );
    }
    
    @Test
    public void testGetAndGetAll() throws FailedLoadOfVendingItemsException {
        fileSetup(ITEMS_MULTI, "Pepsi::4.99::10", "Doritos::3.99::200");
        
        dao = new VendingMachineDaoFileImpl(ITEMS_MULTI);
        dao.loadItems();
        
        for (int i = 0; i < 10; i++) {        
            List<VendingMachineItem> initialList = dao.getAllItems();
            assertNotNull(initialList, "This element should not be null");
            assertTrue(initialList.size() == 2, "This element should have two elements");
        
            // see if it is possible to obtain the two items
            Optional<VendingMachineItem> presentPepsi = dao.getItemByName("Pepsi");
            Optional<VendingMachineItem> presentDorito = dao.getItemByName("Doritos");
            
            assertTrue(presentPepsi.isPresent());
            assertTrue(presentDorito.isPresent());
            
            // ... and see that they're contained in the list
            assertTrue(initialList.contains(presentPepsi.get()));
            assertTrue(initialList.contains(presentDorito.get()));
        }
    }
    
    private void fileSetup(String filename, String... contents) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filename));
            writer.print("");
            for (int i = 0; i < contents.length; i++) {
                writer.println(contents[i]);
            }
            writer.close();
        } catch (IOException ex) {
            fail(filename + " could not be constructed, aborting test");
        }
    }
}

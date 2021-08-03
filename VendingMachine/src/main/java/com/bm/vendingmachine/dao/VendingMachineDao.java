package com.bm.vendingmachine.dao;

import com.bm.vendingmachine.dao.exceptions.FailedLoadOfVendingItemsException;
import com.bm.vendingmachine.dao.exceptions.FailedSaveOfVendingItemsException;
import com.bm.vendingmachine.dto.VendingMachineItem;
import java.util.List;
import java.util.Optional;

/**
 * The Dao interface that all Daos in this application must implement
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Jul 31, 2021
 */
public interface VendingMachineDao {
    /**
     * Loads Vending Machine items from an external source into this DAO.
     * If this loading procedure fails, the below exception will be thrown
     * 
     * @throws FailedLoadOfVendingItemsException 
     */
    public void loadItems() throws FailedLoadOfVendingItemsException;
    
    /**
     * @return A list of all the Vending Machine Items in this machine,
     *         including those items with zero quantity
     */
    public List<VendingMachineItem> getAllItems();
    
    /**
     * Attempts to retrieve the Vending Machine Item in this DAO whose name
     * matches the below name.
     * 
     * If such an Item is found, a nonempty instance will be returned.
     * Otherwise, an empty instance is returned.
     * 
     * @param name The name of the item
     * @return The aformentioned instances depending on the availability of the
     *         item
     */
    public Optional<VendingMachineItem> getItemByName(String name);
    
    /**
     * Attempts to reduce the quantity of the item corresponding to this
     * name by one.
     * 
     * If the item does not exist, an empty instance will be returned
     * 
     * If the item exists but the quantity is not positive, an empty instance
     * will still be returned
     * 
     * If the item exists and its quantity is positive, the quantity of that
     * item will be reduced by one. Then the instance of that modified item
     * will be returned
     * @param name
     * @return The aforementioned instances
     */
    public Optional<VendingMachineItem> removeOneOfItem(String name);
    
    /**
     * Saves the Vending Machine items in this DAO to some external source.
     * If this saving procedure fails, the below exception will be thrown.
     * 
     * @throws FailedSaveOfVendingItemsException 
     */
    public void saveItems() throws FailedSaveOfVendingItemsException;
}

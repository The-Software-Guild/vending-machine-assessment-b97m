package com.bm.vendingmachine.service;

import com.bm.vendingmachine.dao.VendingMachineAuditDao;
import com.bm.vendingmachine.dao.VendingMachineDao;
import com.bm.vendingmachine.dao.exceptions.FailedLoadOfVendingItemsException;
import com.bm.vendingmachine.dao.exceptions.FailedSaveOfVendingItemsException;
import com.bm.vendingmachine.dto.VendingMachineItem;
import com.bm.vendingmachine.service.exceptions.InsufficientFundsException;
import com.bm.vendingmachine.service.exceptions.NoItemInventoryException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * Acts as the Service Layer component of this application 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Jul 31, 2021
 */
public class VendingMachineService {
    private BigDecimal fundsAvailable;
    private VendingMachineDao dao;
    private VendingMachineAuditDao auditDao;

    public VendingMachineService(VendingMachineDao dao, VendingMachineAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;
        this.fundsAvailable = BigDecimal.ZERO;
    }
    
    /**
     * Attempts to load Vending Machine items from an external source into
     * memory. If this loading is unsuccessful, the below exception will be 
     * thrown.
     * 
     * @throws FailedLoadOfVendingItemsException 
     */
    public void loadItems() throws FailedLoadOfVendingItemsException {
        dao.loadItems();
        auditDao.appendRecord("Loaded vending items into memory");
    }
    
    /**
     * Attempts to save Vending Machine items to an external source. If 
     * this saving is unsuccessful, the below exception will be thrown.
     * 
     * @throws FailedSaveOfVendingItemsException 
     */
    public void saveItems() throws FailedSaveOfVendingItemsException {
        dao.saveItems();
        auditDao.appendRecord("Saved vending items to external source(s)");
    }
    
    /**
     * @return A list of all the VendingMachineItems available
     */
    public List<VendingMachineItem> getAllItems() {
        auditDao.appendRecord("Acquiring list of all items");
        return dao.getAllItems();
    }
    
    /**
     * @return The amount of funds currently deposited in the Vending Machine
     */
    public BigDecimal getFundsAvailable() {
        auditDao.appendRecord("Vending machine funds queried");
        return fundsAvailable;
    }
    
    /**
     * Deposits the indicated quantities of coins into this Vending Machine
     * 
     * @param coin
     * @param quantity 
     */
    public void addCoins(Coin coin, BigInteger quantity) {
        auditDao.appendRecord("Added " + quantity + " " + coin + "(s)");
        fundsAvailable = fundsAvailable.add(
            coin.getValue().multiply(new BigDecimal(quantity.toString()))
        );
    }
    
    /**
     * Attempts to execute a transaction of the indicated item.
     * 
     * If the item does not exist or has been depleted, a
     * NoItemInventoryException will be thrown.
     * 
     * If the item's cost is higher than the amount of funds currently in the
     * Vending Machine, an InsufficientFundsException will be thrown.
     * 
     * Otherwise, one the transaction succeeds, the quantity of the item will 
     * be decremented by one, and the amount of change is returned. The amount
     * of funds in this machine will revert back to zero.
     * 
     * @param itemName
     * @throws NoItemInventoryException
     * @throws InsufficientFundsException
     * @return The amount of change returned on a successful transaction
     */
    public BigDecimal transactItem(String itemName) throws
        NoItemInventoryException,
        InsufficientFundsException {
        
        auditDao.appendRecord("Transaction attempted for " + itemName);
        
        Optional<VendingMachineItem> possItem = dao.getItemByName(itemName);
        if (possItem.isEmpty()) {
            auditDao.appendRecord("Transaction failed - no such item");
            throw new NoItemInventoryException(
                "The vending machine does not have this item"
            );
        }
        
        VendingMachineItem item = possItem.get();
        
        if (fundsAvailable.compareTo(item.getCost()) < 0) {
            auditDao.appendRecord("Transaction failed - not enough funds for item");
            throw new InsufficientFundsException(
                "Not enough funds have been provided to purchase this item "
                + "(Item cost: $" + item.getCost().toString() 
                + ", Funds available: $" + fundsAvailable.toString() + ")"
            );
        }
        
        if (dao.removeOneOfItem(item.getName()).isEmpty()) {
            auditDao.appendRecord("Transaction failed - no such item");
            throw new NoItemInventoryException(
                "The vending machine has run out of this item"
            );
        }
        BigDecimal change = fundsAvailable.subtract(item.getCost());
        auditDao.appendRecord("Transaction successful, returned $" + change + " in change");
        fundsAvailable = BigDecimal.ZERO;
        return change;
    }
    
    /**
     * Frees all resources associated with this Service Layer
     */
    public void close() {
        auditDao.close();
    }
}

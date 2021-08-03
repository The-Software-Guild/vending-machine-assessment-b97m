package com.bm.vendingmachine.dao;

/**
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Jul 31, 2021
 */
public interface VendingMachineAuditDao {
    /**
     * Appends a new line into the audit record
     * @param s The new line
     */
    public void appendRecord(String s);
    
    /**
     * Frees all resources dedicated to this auditing dao.
     */
    public void close();
}

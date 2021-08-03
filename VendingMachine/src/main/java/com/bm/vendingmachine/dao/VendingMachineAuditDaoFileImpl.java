package com.bm.vendingmachine.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * The file implementation of the AuditDao interface
 * 
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Jul 31, 2021
 */
public class VendingMachineAuditDaoFileImpl implements VendingMachineAuditDao {

    public static final String AUDIT_FILE = "audit.txt";
    private PrintWriter writer;
    
    public VendingMachineAuditDaoFileImpl() {
        try {
            writer = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException ex) {
        }
    }
    
    @Override
    public void appendRecord(String s) {
        if (writer != null) {
          writer.println(LocalDateTime.now().toString() + ": " + s);
          writer.flush();
        }
    }
    
    @Override
    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
}

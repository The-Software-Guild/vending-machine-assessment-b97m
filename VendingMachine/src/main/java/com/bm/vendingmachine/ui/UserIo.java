package com.bm.vendingmachine.ui;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Provides basic user i/o functionalities
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Jul 31, 2021
 */
public interface UserIo {
    /**
     * Displays a line of text to the user
     * @param line 
     */
    public void displayLine(String line);
    
    /**
     * Displays a line of error text to the user
     * @param line 
     */
    public void displayErrorLine(String line);
    
    /**
     * Displays a line of informative text to the user
     * @param line 
     */
    public void displayInformationalLine(String line);
    
    /**
     * Displays a line of text for solicitation of the user
     * @param line 
     */
    public void displaySolicitationLine(String line);
    
    /**
     * Displays surrounding lines around a given set of lines to print
     * @param basis
     * @param contents 
     */
    public void displayAroundContents(String basis, String[] contents);
    
    /**
     * A supplier for a user-supplied int
     * @param prompt The prompt soliciting the user for an int
     * @param test The test for which the entered int will be accepted
     * @param errorText The error text to display on invalid input
     * @return The aforementioned supplier
     */
    public Supplier<Integer> intSupplier(
        String prompt, 
        Predicate<Integer> test, 
        String errorText
    );
    
    /**
     * A supplier for a user-supplied String
     * @param prompt The prompt soliciting the user for a String
     * @param test The test for which the entered int will be accepted
     * @param errorText The error text to display on invalid input
     * @return The aforementioned supplier
     */
    public Supplier<String> stringSupplier(
        String prompt, 
        Predicate<String> test, 
        String errorText
    );
    
    /**
     * A supplier for a user-supplied BigInteger
     * @param prompt The prompt soliciting the user for a BigInteger
     * @param test The test for which the entered int will be accepted
     * @param errorText The error text to display on invalid input
     * @return The aforementioned supplier
     */
    public Supplier<BigInteger> bigIntegerSupplier(
        String prompt, 
        Predicate<BigInteger> test, 
        String errorText
    );
    
    /**
     * A supplier for a user-supplied BigDecimal
     * @param prompt The prompt soliciting the user for a BigDecimal
     * @param test The test for which the entered int will be accepted
     * @param errorText The error text to display on invalid input
     * @return The aforementioned supplier
     */
    public Supplier<BigDecimal> bigDecimalSupplier(
        String prompt, 
        Predicate<BigDecimal> test, 
        String errorText
    );
    
    /**
     * Frees all resources associated with this User I/O component
     */
    public void close();
}

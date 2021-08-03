package com.bm.vendingmachine.ui;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * The console implementation of the User I/O component
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Jul 31, 2021
 */
public class UserIoConsoleImpl implements UserIo {
    private Scanner uInput;

    public UserIoConsoleImpl() {
        uInput = new Scanner(System.in);
    }
    
    @Override
    public void displayLine(String line) {
        System.out.println(line);
    }

    @Override
    public void displayErrorLine(String line) {
        System.out.format("!! %s !!%n", line);
    }

    @Override
    public void displayInformationalLine(String line) {
        System.out.format("|| %s ||%n", line);
    }

    @Override
    public void displaySolicitationLine(String line) {
        System.out.format("-- %s --%n", line);
    }

    @Override
    public void displayAroundContents(String basis, String[] contents) {
        System.out.format("/| %s |\\%n", basis);
        Arrays.stream(contents).forEach(line -> {
            System.out.format(" | %s%n", line);
        });
        System.out.format("\\| %s /|%n", "-".repeat(basis.length()));
    }
    
    @Override
    public Supplier<Integer> intSupplier(String prompt, Predicate<Integer> test, String errorText) {
        return () -> {
            boolean invalid = true;
            int receivedInt = -1;
            while (invalid) {
                displaySolicitationLine(prompt);
                try {
                    receivedInt = Integer.parseInt(uInput.nextLine());
                    invalid = !test.test(receivedInt);
                    if (invalid) {
                        displayErrorLine(errorText);
                    }
                } catch (NumberFormatException ex) {
                    displayErrorLine(errorText);
                }
            }
            return receivedInt;
        };
    }
    
    @Override
    public Supplier<String> stringSupplier(String prompt, Predicate<String> test, String errorText) {
        return () -> {
            boolean invalid = true;
            String receivedString = "";
            while (invalid) {
                displaySolicitationLine(prompt);
                receivedString = uInput.nextLine();
                invalid = !test.test(receivedString);
                if (invalid) {
                    displayErrorLine(errorText);
                }
            }
            return receivedString;
        };
    }

    @Override
    public Supplier<BigInteger> bigIntegerSupplier(String prompt, Predicate<BigInteger> test, String errorText) {
        return () -> {
            boolean invalid = true;
            BigInteger receivedBigInt = BigInteger.ZERO;
            while (invalid) {
                displaySolicitationLine(prompt);
                try {
                    receivedBigInt = new BigInteger(uInput.nextLine());
                    invalid = !test.test(receivedBigInt);
                    if (invalid) {
                        displayErrorLine(errorText);
                    }
                } catch (NumberFormatException ex) {
                    displayErrorLine(errorText);
                }
            }
            return receivedBigInt;
        };
    }

    @Override
    public Supplier<BigDecimal> bigDecimalSupplier(String prompt, Predicate<BigDecimal> test, String errorText) {
        return () -> {
            boolean invalid = true;
            BigDecimal receivedBigDec = BigDecimal.ZERO;
            while (invalid) {
                displaySolicitationLine(prompt);
                try {
                    receivedBigDec = new BigDecimal(uInput.nextLine());
                    invalid = !test.test(receivedBigDec);
                    if (invalid) {
                        displayErrorLine(errorText);
                    }
                } catch (NumberFormatException ex) {
                    displayErrorLine(errorText);
                }
            }
            return receivedBigDec;
        };
    }

    @Override
    public void close() {
        uInput.close();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author PYT
 */
public class NoSuchCustomerException extends Exception {

    /**
     * Creates a new instance of <code>NoSuchCustomerException</code> without
     * detail message.
     */
    public NoSuchCustomerException() {
    }

    /**
     * Constructs an instance of <code>NoSuchCustomerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoSuchCustomerException(String msg) {
        super(msg);
    }
}

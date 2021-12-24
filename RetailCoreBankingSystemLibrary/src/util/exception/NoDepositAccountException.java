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
public class NoDepositAccountException extends Exception{

    /**
     * Creates a new instance of <code>NoDepositAccountException</code> without
     * detail message.
     */
    public NoDepositAccountException() {
    }

    /**
     * Constructs an instance of <code>NoDepositAccountException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoDepositAccountException(String msg) {
        super(msg);
    }
}

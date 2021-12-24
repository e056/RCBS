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
public class NoSuchAtmCardException extends Exception{

    /**
     * Creates a new instance of <code>NoSuchAtmCardException</code> without
     * detail message.
     */
    public NoSuchAtmCardException() {
    }

    /**
     * Constructs an instance of <code>NoSuchAtmCardException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoSuchAtmCardException(String msg) {
        super(msg);
    }
}

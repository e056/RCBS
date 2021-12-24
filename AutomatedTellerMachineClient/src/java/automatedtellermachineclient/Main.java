/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatedtellermachineclient;
import ejb.session.stateless.AtmCardSessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import javax.ejb.EJB;

import ejb.session.stateless.DepositAccountSessionBeanRemote;

/**
 *
 * @author PYT
 */
public class Main {

    @EJB(name = "CustomerEntitySessionBeanRemote")
    private static CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;

    @EJB(name = "AtmCardSessionBeanRemote")
    private static AtmCardSessionBeanRemote atmCardSessionBeanRemote;

    @EJB(name = "DepositAccountSessionBeanRemote")
    private static DepositAccountSessionBeanRemote depositAccountSessionBeanRemote;

    
    
    
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(depositAccountSessionBeanRemote, customerEntitySessionBeanRemote, atmCardSessionBeanRemote);
        mainApp.runApp();
    }
    
}

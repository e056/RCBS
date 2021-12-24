/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tellerterminalclient;

import ejb.session.stateless.AtmCardSessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.DepositAccountSessionBeanRemote;
import javax.ejb.EJB;
import util.exception.NoDepositAccountException;
import util.exception.NoSuchCustomerException;

/**
 *
 * @author PYT
 */
public class Main {

    @EJB(name = "CustomerEntitySessionBeanRemote")
    private static CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;

    @EJB
    private static AtmCardSessionBeanRemote atmCardSessionBean;

    @EJB
    private static DepositAccountSessionBeanRemote depositAccountSessionBean;

    

    
    public static void main(String[] args) throws NoSuchCustomerException, NoDepositAccountException 
    {
        MainApp mainApp = new MainApp(depositAccountSessionBean, customerEntitySessionBeanRemote, atmCardSessionBean);
        mainApp.runApp();
    }
    
}

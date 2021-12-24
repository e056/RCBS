/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.DepositAccount;
import java.util.List;
import javax.ejb.Local;
import util.exception.NoSuchCustomerException;

/**
 *
 * @author PYT
 */
@Local
public interface CustomerEntitySessionBeanLocal {
    
    public Long createNewCustomer(Customer customer);
    
    public Customer getCustomerUsingIdentificationNumber(String identificationNumber) throws NoSuchCustomerException;
    
    public List<DepositAccount> getDepositAccounts(String identificationNumber);
    
    public Customer retrieveCustomerByCustomerId(Long customerId);
}

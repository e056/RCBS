/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.DepositAccount;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.NoSuchCustomerException;

/**
 *
 * @author PYT
 */
@Stateless
public class CustomerEntitySessionBean implements CustomerEntitySessionBeanRemote, CustomerEntitySessionBeanLocal {
    
    @PersistenceContext(unitName = "RetailCoreBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createNewCustomer(Customer customer)
    {
        em.persist(customer);
        em.flush();
        return customer.getCustomerId();
    }
    
    @Override
    public Customer getCustomerUsingIdentificationNumber(String identificationNumber) throws NoSuchCustomerException
    {
        
        try
        {
            Query query = em.createQuery("SELECT c FROM Customer c WHERE c.identificationNumber = :inIdentificationNumber");
            query.setParameter("inIdentificationNumber", identificationNumber);
            Customer customer = (Customer)query.getSingleResult();
            
            return customer;
        }
        catch(Exception ex)
        {
            throw new NoSuchCustomerException("No such customer exists!\n");
        }  
    }
    
    @Override
    public List<DepositAccount> getDepositAccounts(String identificationNumber)
    {
        Query query = em.createQuery("SELECT d FROM DepositAccount d WHERE d.customer.identificationNumber = :inIdentificationNumber");
        query.setParameter("inIdentificationNumber", identificationNumber);
        return (List<DepositAccount>)query.getResultList();
      
    }
    
    @Override
    public Customer retrieveCustomerByCustomerId(Long customerId)
    {
        Customer customer = em.find(Customer.class, customerId);
        
        customer.getDepositAccounts().size();
            
        return customer;
                    
    }

}

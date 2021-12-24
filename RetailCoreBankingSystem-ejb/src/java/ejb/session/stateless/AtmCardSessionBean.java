/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AtmCard;
import entity.Customer;
import entity.DepositAccount;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoDepositAccountException;
import util.exception.NoSuchAtmCardException;
import util.exception.NoSuchCustomerException;

/**
 *
 * @author PYT
 */
@Stateless
public class AtmCardSessionBean implements AtmCardSessionBeanRemote, AtmCardSessionBeanLocal {

    @EJB(name = "CustomerEntitySessionBeanLocal")
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @EJB(name = "DepositAccountSessionBeanLocal")
    private DepositAccountSessionBeanLocal depositAccountSessionBeanLocal;
    
    

    @PersistenceContext(unitName = "RetailCoreBankingSystem-ejbPU")
    private EntityManager em;
    
    
    
    @Override
    public AtmCard createAtmCard(AtmCard atmCard, Long customerId, List<Long> stringOfAccountId)throws NoDepositAccountException{
        
        Customer customer = customerEntitySessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        
        for(Long accountId : stringOfAccountId)
        {
            DepositAccount depositAccount = em.find(DepositAccount.class, accountId);
            depositAccount.setAtmCard(atmCard);
            atmCard.getDepositAccounts().add(depositAccount);
            System.out.println("DEBUG: " );
        }
        
        atmCard.setCustomer(customer);
        
        customer.setAtmCard(atmCard);
        
        em.persist(atmCard);
        em.flush();
        return atmCard;
    }

    @Override
    public void deleteAtmCard(String atmCardNumber, Long customerId) throws NoSuchAtmCardException
    {
        
        try {
            AtmCard atmCardToDelete = retrieveAtmCardByAtmCardNumber(atmCardNumber);
            List<DepositAccount> depositAccounts = depositAccountSessionBeanLocal.retrieveDepositAccountByCustomerId(customerId);
            Customer customer = customerEntitySessionBeanLocal.retrieveCustomerByCustomerId(customerId);
            customer.setAtmCard(null);
        
            for(DepositAccount depositAccount : depositAccounts)
            {
                depositAccount.setAtmCard(null);
            }
            
            em.remove(atmCardToDelete);
            
        } catch (Exception ex) {
            throw new NoSuchAtmCardException("Atm Card does not exist!");
        }
      
    }
    
    @Override
    public void changeAtmCardPin(String atmCardNumber,String newPin) throws NoSuchAtmCardException
    {
        AtmCard atmCardToUpdate = retrieveAtmCardByAtmCardNumber(atmCardNumber);
        
        atmCardToUpdate.setPin(newPin);
    }
    
    @Override
    public AtmCard insertAtmCard(String atmCardNumber, String pin) throws InvalidLoginCredentialException
    {
        try {
                AtmCard atmCard = retrieveAtmCardByAtmCardNumber(atmCardNumber);
                if(atmCard.getPin().equals(pin))
                {
                    return atmCard;
                }
                else
                {
                    throw new InvalidLoginCredentialException("Atm Card does not exist or invalid pin!");
                }
            } catch (NoSuchAtmCardException ex) {
                throw new InvalidLoginCredentialException("Atm Card does not exist or invalid pin!");
            }
    }

    @Override
    public AtmCard retrieveAtmCardByAtmCardId(Long atmCardId) throws NoSuchAtmCardException {
        Query query = em.createQuery("SELECT a FROM AtmCard a WHERE a.atmCardId = :inAtmCardId");
        query.setParameter("inAtmCardId", atmCardId);
        
        AtmCard atmCard = (AtmCard) query.getSingleResult();
        
        return atmCard;
    }
    
    @Override
    public AtmCard retrieveAtmCardByAtmCardNumber(String atmCardNumber) throws NoSuchAtmCardException {
        Query query = em.createQuery("SELECT a FROM AtmCard a WHERE a.cardNumber = :inAtmCardNumber");
        query.setParameter("inAtmCardNumber", atmCardNumber);
        AtmCard atmCard = (AtmCard) query.getSingleResult();
        return atmCard;
        
    }
    
    @Override
    public AtmCard retrieveAtmCardByCustomerId(Long customerId) throws NoSuchAtmCardException
    {
            
        try{
            Query query = em.createQuery("SELECT a FROM AtmCard a WHERE a.customer.customerId = :inCustomerId");
        
            query.setParameter("inCustomerId", customerId);
        
            AtmCard atmCard = (AtmCard) query.getSingleResult();
            return atmCard;
        } catch (Exception ex)
        {
            throw new NoSuchAtmCardException("Customer not associated with any atm card");
        }
       
    }
   
  
}

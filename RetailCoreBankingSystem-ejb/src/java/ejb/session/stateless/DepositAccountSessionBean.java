/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AtmCard;
import entity.Customer;
import entity.DepositAccount;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.NoDepositAccountException;
import util.exception.NoSuchAtmCardException;
import util.exception.NoSuchCustomerException;

/**
 *
 * @author PYT
 */
@Stateless
public class DepositAccountSessionBean implements DepositAccountSessionBeanRemote, DepositAccountSessionBeanLocal {

    @EJB(name = "AtmCardSessionBeanLocal")
    private AtmCardSessionBeanLocal atmCardSessionBeanLocal;

    @PersistenceContext(unitName = "RetailCoreBankingSystem-ejbPU")
    private EntityManager em;

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @Override
    public DepositAccount createDepositAccount(DepositAccount depositAccount, Long customerId, Long atmCardId) throws NoSuchCustomerException {

        try {
            Customer customer = customerEntitySessionBeanLocal.retrieveCustomerByCustomerId(customerId);
            depositAccount.setCustomer(customer);
            customer.getDepositAccounts().add(depositAccount);

            if (atmCardId != null) {

                try {
                    AtmCard atmCard = atmCardSessionBeanLocal.retrieveAtmCardByAtmCardId(atmCardId);
                    depositAccount.setAtmCard(atmCard);
                    atmCard.getDepositAccounts().add(depositAccount);

                } catch (NoSuchAtmCardException ex) {
                    throw new NoSuchCustomerException("Atm Card does not exist!");
                }

            }

            em.persist(depositAccount);
            em.flush();

            return depositAccount;
        } catch (NoSuchCustomerException ex) {
            throw new NoSuchCustomerException(ex.getMessage());
        }

    }


    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public List<DepositAccount> retrieveDepositAccountByCustomerId(Long customerId) throws NoDepositAccountException {

        try {
            Query query = em.createQuery("SELECT d FROM DepositAccount d WHERE d.customer.customerId = :inCustomerId");
            query.setParameter("inCustomerId", customerId);

            List<DepositAccount> depositAccounts = query.getResultList();

            return depositAccounts;
        } catch (Exception ex) {
            throw new NoDepositAccountException("Customer not associated with any deposit account");
        }

    }

    @Override
    public DepositAccount retrieveDepositAccountByAccountNumber(String accountNumber) {
        Query query = em.createQuery("SELECT d FROM DepositAccount d WHERE d.accountNumber = :inAccountNumber");
        query.setParameter("inAccountNumber", accountNumber);

        DepositAccount depositAccount = (DepositAccount) query.getSingleResult();

        return depositAccount;
    }

}

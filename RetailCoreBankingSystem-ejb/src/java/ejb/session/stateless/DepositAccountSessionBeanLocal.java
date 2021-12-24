/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.DepositAccount;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.NoResultException;
import util.exception.NoDepositAccountException;
import util.exception.NoSuchCustomerException;

/**
 *
 * @author PYT
 */
@Local
public interface DepositAccountSessionBeanLocal {

    public List<DepositAccount> retrieveDepositAccountByCustomerId(Long customerId) throws NoDepositAccountException;

    public DepositAccount createDepositAccount(DepositAccount depositAccount, Long customerId, Long atmCardId) throws NoSuchCustomerException;

    public DepositAccount retrieveDepositAccountByAccountNumber(String accountNumber);
}

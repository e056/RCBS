/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AtmCard;
import entity.DepositAccount;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoDepositAccountException;
import util.exception.NoSuchAtmCardException;
import util.exception.NoSuchCustomerException;

/**
 *
 * @author PYT
 */
@Local
public interface AtmCardSessionBeanLocal {
    
    public AtmCard createAtmCard(AtmCard atmCard, Long customerId, List<Long> stringOfAccountNumber)throws NoDepositAccountException;
    
    public AtmCard retrieveAtmCardByAtmCardId(Long atmCardId) throws NoSuchAtmCardException;
    
    public AtmCard retrieveAtmCardByAtmCardNumber(String atmCardNumber) throws NoSuchAtmCardException;
    
    public void changeAtmCardPin(String atmCardNumber,String newPin) throws NoSuchAtmCardException;
    
    public AtmCard insertAtmCard(String atmCardNumber, String pin) throws InvalidLoginCredentialException;
    
    public AtmCard retrieveAtmCardByCustomerId(Long customerId) throws NoSuchAtmCardException;
    
    public void deleteAtmCard(String atmCardNumber, Long customerId) throws NoSuchAtmCardException, NoDepositAccountException;
}

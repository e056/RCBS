/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatedtellermachineclient;

import ejb.session.stateless.AtmCardSessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.DepositAccountSessionBeanRemote;
import entity.AtmCard;
import entity.DepositAccount;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoSuchAtmCardException;

/**
 *
 * @author PYT
 */
public class MainApp {
    
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private DepositAccountSessionBeanRemote depositAccountSessionBeanRemote;
    private AtmCardSessionBeanRemote atmCardSessionBeanRemote;
    
    
    public MainApp(DepositAccountSessionBeanRemote depositAccountSessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, AtmCardSessionBeanRemote atmCardSessionBeanRemote) {
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.depositAccountSessionBeanRemote = depositAccountSessionBeanRemote;
        this.atmCardSessionBeanRemote = atmCardSessionBeanRemote;
    }

    public MainApp() {
    }
    
    public void runApp()
    {
        Scanner sc = new Scanner(System.in);
        while(true)
        {
            System.out.println("*** Welcome to Retail Core Banking System (Automated Teller Machine)***\n");
            
            System.out.println("1: Insert Atm Card");
            System.out.println("2: Exit\n");
            
            Integer response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if(response == 1)
                {
                    doInsertAtmCard();
                    System.out.println("Successfully logged in");
                }
                else if(response == 2)
                {
                   break;
                }
                
            }
            
            if(response == 2) {
                break;
            }
        }
    

    }

    private void doInsertAtmCard() {
        Scanner sc = new Scanner(System.in);
        
        String atmNumber = "";
        String atmPin = "";
        
        
        System.out.println("*** Retail Core Banking System (Automated Teller Machine) :: Insert card***\n");
        
        System.out.print("Enter atm card number>");
        atmNumber = sc.nextLine().trim();
        
        System.out.print("Enter atm pin>");
        atmPin = sc.nextLine().trim();
        
        try {
            AtmCard atmCard = atmCardSessionBeanRemote.insertAtmCard(atmNumber, atmPin);
            doOtherTasks(atmCard);
        } catch (InvalidLoginCredentialException ex) {
            System.out.println("Atm Card does not exist or invalid pin!");
        }
        
    }

    private void doOtherTasks(AtmCard atmCard) {
        System.out.println("*** Retail Core Banking System (Automated Teller Machine) :: Logged In***\n");
        System.out.println("1: Change pin");
        System.out.println("2: Enquire available balance");
        System.out.println("3: Exit\n");
        Integer response = 0;
        
        Scanner sc = new Scanner(System.in);
            
        while(response < 1 || response > 3)
        {
            System.out.print("> ");
                
            response = sc.nextInt();
                
            if(response == 1)
            {
                doChangePin(atmCard);
                System.out.println("Successfully changed pin");
            }
            else if(response == 2)
            {
                doEnquireAvailableBalance(atmCard);
          
            }
            else if(response == 3)
            {
                break;
                    
            } else {
                System.out.println("Invalid option, please try again!\n");  
            }
    }

    
    
}

    private void doChangePin(AtmCard atmCard) {
        Scanner sc = new Scanner(System.in);
        String newPin = "";
        
        System.out.print("Enter new pin>");
        newPin = sc.nextLine().trim();
        
        try {
            atmCardSessionBeanRemote.changeAtmCardPin(atmCard.getCardNumber(),newPin);
        } catch (NoSuchAtmCardException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doEnquireAvailableBalance(AtmCard atmCard) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter account you would like the balance for>");
        String accountNumber = sc.nextLine().trim();
        DepositAccount da = depositAccountSessionBeanRemote.retrieveDepositAccountByAccountNumber(accountNumber);
        
        BigDecimal availBalance = da.getAvailableBalance();
        System.out.println("Available Balance: " + availBalance);
    }
}

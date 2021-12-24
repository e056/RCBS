/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tellerterminalclient;

import ejb.session.stateless.AtmCardSessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.DepositAccountSessionBeanRemote;
import entity.AtmCard;
import entity.Customer;
import entity.DepositAccount;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.address.Address;
import util.enumeration.DepositAccountType;
import util.exception.NoDepositAccountException;
import util.exception.NoSuchAtmCardException;
import util.exception.NoSuchCustomerException;

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

    public void runApp() throws NoSuchCustomerException, NoDepositAccountException {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("*** Welcome to Retail Core Banking System (Teller Terminal)***\n");

            System.out.println("1: Create new customer");
            System.out.println("2: Open Deposit Account for existing customer");
            System.out.println("3: Issue ATM Card for existing customer");
            System.out.println("4: Replace ATM Card for existing customer");
            System.out.println("5: Exit\n");
            Integer response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = sc.nextInt();

                if (response == 1) {
                    doCreateNewCustomer();
                    System.out.println("Successfully created new customer");
                } else if (response == 2) {
                    doOpenDepositAccount();
                    System.out.println("Successfully created new deposit account");
                } else if (response == 3) {
                    doIssueAtmCard();

                } else if (response == 4) {
                    doReplaceAtmCard();
                }
                else if (response == 5) {
                    break;
                }

            }
            
            if(response == 5) {
                break;
            }
        }

    }

    private void doCreateNewCustomer() {
        //String firstName, String lastName, String identificationNumber, String contactNumber, Address address)
        Scanner sc = new Scanner(System.in);

        String firstName = "";
        String lastName = "";
        String identificationNumber = "";
        String contactNumber = "";
        String addressLine1 = "";
        String addressLine2 = "";
        String postalCode = "";
        Boolean customerExists = true;
        Customer currentCustomer = new Customer();

        System.out.println("*** Retail Core Banking System (Teller Terminal) :: Create new Customer***\n");

        System.out.print("Enter Identification Number>");
        identificationNumber = sc.nextLine().trim();
        currentCustomer.setIdentificationNumber(identificationNumber);

        try {
            currentCustomer = customerEntitySessionBeanRemote.getCustomerUsingIdentificationNumber(identificationNumber);
            System.out.println("Customer already exists!: " + currentCustomer.getCustomerId());
        } catch (NoSuchCustomerException ex) {
            customerExists = false;
            System.out.println("Identification number does not belong to any customer in database.");

        } finally {
            if (customerExists == false) {
                System.out.print("Enter First Name>");
                firstName = sc.nextLine().trim();
                currentCustomer.setFirstName(firstName);

                System.out.print("Enter last Name>");
                lastName = sc.nextLine().trim();
                currentCustomer.setLastName(lastName);

                System.out.print("Enter contact number>");
                contactNumber = sc.nextLine().trim();
                currentCustomer.setContactNumber(contactNumber);

                System.out.print("Enter address>");
                addressLine1 = sc.nextLine().trim();

                System.out.print("Enter unit number>");
                addressLine2 = sc.nextLine().trim();

                System.out.print("Enter postal code>");
                postalCode = sc.nextLine().trim();

                Address address = new Address(addressLine1, addressLine2, postalCode);
                currentCustomer.setAddress(address);

                Long newCustomerId = customerEntitySessionBeanRemote.createNewCustomer(currentCustomer);
            }
        }
    }

    private void doOpenDepositAccount() {

        Scanner sc = new Scanner(System.in);

        String identificationNumber = "";
        String accountNumber = "";
        Boolean enabled = true;
        Integer bigDecimal = 0;
        Integer depositAccountType = 0;
        Boolean ok = false;

        Customer currentCustomer = new Customer();

        DepositAccount newDepositAccount = new DepositAccount();
        //public DepositAccount(String accountNumber, DepositAccountType accountType, BigDecimal availableBalance, Boolean enabled, Customer customer)
        System.out.println(newDepositAccount.toString());
        System.out.println("*** Retail Core Banking System (Teller Terminal) :: Create new deposit account***\n");
        System.out.print("Enter customer identification number>");
        identificationNumber = sc.nextLine().trim();

        while (!ok) {
            try {
                currentCustomer = customerEntitySessionBeanRemote.getCustomerUsingIdentificationNumber(identificationNumber);
                System.out.println("Customer found!: " + currentCustomer.getCustomerId());
                ok = true;
            } catch (NoSuchCustomerException ex) {
                System.out.println("No such customer found! Please enter identification number again>");
            }
        }

        System.out.print("Enter account number>");
        accountNumber = sc.nextLine().trim();
        newDepositAccount.setAccountNumber(accountNumber);
        newDepositAccount.setEnabled(true);
        BigDecimal number = new BigDecimal(bigDecimal);

        System.out.print("Enter initial deposit>");
        bigDecimal = sc.nextInt();
        BigDecimal initialDeposit = new BigDecimal(bigDecimal);
        newDepositAccount.setAvailableBalance(initialDeposit);

        while (true) {
            System.out.print("Select DepositAccountType (1: Savings, 2: Current)> ");
            depositAccountType = sc.nextInt();

            if (depositAccountType == 1) {
                newDepositAccount.setAccountType(DepositAccountType.SAVINGS);
                break;
            } else if (depositAccountType == 2) {
                newDepositAccount.setAccountType(DepositAccountType.CURRENT);
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

        Long atmCardId = null;
        String input = "";
        if (currentCustomer.getAtmCard() == null) {
            atmCardId = null;
        } else {
            atmCardId = currentCustomer.getAtmCard().getAtmCardId();
            System.out.print("Press Y if you want to link this deposit account with your existing atm card");
            input = sc.nextLine().trim();
            if (input == "Y") {
                atmCardId = currentCustomer.getAtmCard().getAtmCardId();
            } else {
                atmCardId = null;
            }
        }

        try {
            newDepositAccount = depositAccountSessionBeanRemote.createDepositAccount(newDepositAccount, currentCustomer.getCustomerId(), atmCardId);

            System.out.println("New deposit account created successfully!: " + newDepositAccount.getAccountNumber() + "\n");

        } catch (Exception ex) {
            System.out.println("Error occured while creating the deposit account");
        }

    }

    private void doIssueAtmCard() throws NoDepositAccountException {

        Scanner sc = new Scanner(System.in);

        String cardNumber = "";
        String nameOnCard = "";
        Boolean enabled = true;
        String pin = "";
        String identificationNumber = "";
        Customer currentCustomer = new Customer();
        AtmCard newAtmCard = new AtmCard();
        Boolean ok = false;
        Boolean atmCardExists = false;
        Boolean depositAccountExists = false;
        String moreItem = "";

        System.out.println("*** Retail Core Banking System (Teller Terminal) :: Issue new ATM Card***\n");
        System.out.print("Enter customer identification number>");
        identificationNumber = sc.nextLine().trim();

        while (!ok) {
            try {
                currentCustomer = customerEntitySessionBeanRemote.getCustomerUsingIdentificationNumber(identificationNumber);
                System.out.println("Customer found! Customer id: " + currentCustomer.getCustomerId());
                ok = true;
                if (currentCustomer.getAtmCard() != null) {
                    System.out.println("Customer already has an ATM Card! Unable to create new ATM Card for customer!");
                } else if (depositAccountSessionBeanRemote.retrieveDepositAccountByCustomerId(currentCustomer.getCustomerId()).isEmpty()) {
                    System.out.println("Customer does not have a deposit account! Please create a deposit account first before applying for an atm card!");
                } else {
                    System.out.print("Enter card number>");
                    cardNumber = sc.nextLine().trim();
                    newAtmCard.setCardNumber(cardNumber);

                    System.out.print("Enter name on card>");
                    nameOnCard = sc.nextLine().trim();
                    newAtmCard.setNameOnCard(nameOnCard);

                    System.out.print("Enter card pin>");
                    pin = sc.nextLine().trim();
                    newAtmCard.setPin(pin);

                    newAtmCard.setEnabled(enabled);

                    List<Long> stringOfAccountId = new ArrayList();

                    for (DepositAccount depositAccount : depositAccountSessionBeanRemote.retrieveDepositAccountByCustomerId(currentCustomer.getCustomerId())) {
                        int input = 0;
                        System.out.print("Press 1 if you want to link card with this deposit account " + depositAccount.getAccountNumber() + ">");
                        
                        input = sc.nextInt();
                        if (input == 1) {
                            stringOfAccountId.add(depositAccount.getDepositAccountId());
                            System.out.println("added");
                        }

                    }
                    System.out.println("------------------------");

                    AtmCard newATMCard = atmCardSessionBeanRemote.createAtmCard(newAtmCard, currentCustomer.getCustomerId(), stringOfAccountId);
                    System.out.println("New ATM Card issued successfully!: " + newATMCard.getCardNumber() + "\n");
                }
            } catch (NoSuchCustomerException ex) {

                System.out.println("No such customer found! Please enter identification number again!");
                System.out.print("Enter customer identification number>");
                identificationNumber = sc.nextLine().trim();
            }

        }

        /*try {
            AtmCard findAtmCard = atmCardSessionBeanRemote.retrieveAtmCardByCustomerId(currentCustomer.getCustomerId());
            atmCardExists = true;
            System.out.println("Customer already has an ATM Card! Unable to create new ATM Card for customer!");
        } catch (NoSuchAtmCardException ex) {
            atmCardExists = false;
            
        } finally
        {
            if(atmCardExists == false)
            {
                if(currentCustomer.getDepositAccounts().size() > 0)
                {
                   System.out.print("Enter card number>");
                cardNumber = sc.nextLine().trim();
                newAtmCard.setCardNumber(cardNumber);
        
                System.out.print("Enter name on card>");
                nameOnCard = sc.nextLine().trim();
                newAtmCard.setNameOnCard(nameOnCard);
        
                System.out.print("Enter card pin>");
                pin = sc.nextLine().trim();
                newAtmCard.setPin(pin);
        
                newAtmCard.setEnabled(enabled);
            
                List <DepositAccount> depositAccounts = depositAccountSessionBeanRemote.retrieveDepositAccountByCustomerId(currentCustomer.getCustomerId());
            
                AtmCard newATMCard = atmCardSessionBeanRemote.createAtmCard(newAtmCard, currentCustomer.getCustomerId());
                System.out.println("New ATM Card issued successfully!: " + newATMCard.getCardNumber() + "\n");  
                } else {
                    System.out.println("Customer does not have any deposit accounts");
                }
           
        }
        }*/
    }

    private void doReplaceAtmCard() throws NoDepositAccountException {

        Scanner sc = new Scanner(System.in);

        String cardNumber = "";
        String nameOnCard = "";
        Boolean enabled = true;
        String pin = "";
        String identificationNumber = "";
        Customer currentCustomer = new Customer();
        AtmCard newAtmCard = new AtmCard();
        Boolean ok = false;

        System.out.println("*** Retail Core Banking System (Teller Terminal) :: Replace ATM Card***\n");
        System.out.print("Enter customer identification number>");
        identificationNumber = sc.nextLine().trim();
        System.out.print("Enter current customer card number>");
        cardNumber = sc.nextLine().trim();

        while (!ok) {
            try {
                currentCustomer = customerEntitySessionBeanRemote.getCustomerUsingIdentificationNumber(identificationNumber);
                System.out.println("Customer found!: " + currentCustomer.getCustomerId());
                ok = true;
            } catch (NoSuchCustomerException ex) {

                System.out.println("No such customer found! Please enter identification number again!");
                System.out.print("Enter customer identification number>");
                identificationNumber = sc.nextLine().trim();
            }
        }

        if (cardNumber.equals(currentCustomer.getAtmCard().getCardNumber())) {
            try {
                atmCardSessionBeanRemote.deleteAtmCard(cardNumber, currentCustomer.getCustomerId());
                System.out.println("Old atm card has been deleted");
                System.out.println("Create a new atm card...");
                doIssueAtmCard();
            } catch (NoSuchAtmCardException ex) {
                System.out.println("Atm Card cannot be deleted since customer does not have any atm card! ");
            }
        } else {
            System.out.println("Atm Card provided is not associated with this customer!");
        }

    }
}

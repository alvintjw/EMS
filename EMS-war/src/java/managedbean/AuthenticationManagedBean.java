/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Customer;
import exceptions.CustomerNotFoundException;
import javax.inject.Named;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import session.CustomerSessionLocal;

/**
 *
 * @author alvintjw
 */
//EMS
@Named(value = "authenticationManagedBean")
@SessionScoped
public class AuthenticationManagedBean implements Serializable {

  

    @EJB
    private CustomerSessionLocal customerSessionLocal;
    private String email;
    private String contactnumber;
    private String firstname;
    private String lastname;
    private String password;
    private String confirmpassword;
    private int userId = -1;
    private Customer loggedinCustomer;

    /**
     * Creates a new instance of AuthenticationManagedBean
     */
    public AuthenticationManagedBean() {
    }
    
    public String register() {
        
        if (!password.equals(confirmpassword)) {
            System.out.println("password is: " + password);
            System.out.println("confirmpassword is: " + confirmpassword);
            
            // Add error message about passwords not matching
            return "signup";
        }
        
        // Create and persist the new user entity
        Customer newCustomer = new Customer();
        newCustomer.setEmail(email);
        newCustomer.setContactnumber(contactnumber);
        newCustomer.setFirstname(firstname);
        newCustomer.setLastname(lastname);
        newCustomer.setPassword(password);
        customerSessionLocal.createCustomer(newCustomer);
        
        
        return "login.xhtml"; // Redirect to login page on successful registration
    }

    public String login() {
        
        try {
            loggedinCustomer = customerSessionLocal.retrieveCustomerByEmail(email);
        } catch (CustomerNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        if (loggedinCustomer != null && loggedinCustomer.getPassword().equals(this.getPassword())) {
            //login successful
            //store the logged in user id
            //setUserId(10);
            //do redirect
            return "index.xhtml";
            //return "/secret/secret.xhtml?faces-redirect=true";
        } else {
            //login failed
            setEmail(null);
            setPassword(null);
            setUserId(-1);
            return "login.xhtml";
        }
    }

    public String logout() {
        setEmail(null);
        setPassword(null);
        setUserId(-1);
        return "/login.xhtml?faces-redirect=true";
    }
     

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactnumber;
    }

    public void setContactNo(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }
    
    
}

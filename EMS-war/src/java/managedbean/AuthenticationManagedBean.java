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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.model.file.UploadedFile;
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
    private String firstname;
    private String contactNumber;
    private UploadedFile uploadedFile;
    private byte[] profilePhoto;
    private String profilePhotoName;
    private String fullname;
    private String lastname;
    private String password;
    private String confirmpassword;
    private Customer loggedinCustomer;
    private Long cId;

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
        newCustomer.setContactnumber(contactNumber);
        newCustomer.setFirstname(firstname);
        newCustomer.setLastname(lastname);
        newCustomer.setPassword(password);
        customerSessionLocal.createCustomer(newCustomer);

        return "login.xhtml"; // Redirect to login page on successful registration
    }

    public String login() {

        FacesContext context = FacesContext.getCurrentInstance();

        try {
            loggedinCustomer = customerSessionLocal.retrieveCustomerByEmail(email);
        } catch (CustomerNotFoundException ex) {
            System.out.println(ex.getMessage());
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Email/Password", "Please try again."));
            // Reset email and password
            setEmail(null);
            setPassword(null);
            return "login.xhtml"; // Stay on the login page
        }

        if (loggedinCustomer != null && loggedinCustomer.getPassword().equals(this.getPassword())) {
            loadCustomer();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login Successful", "Welcome back!"));
            cId = loggedinCustomer.getId();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggedCustomerId", this.loggedinCustomer.getId());

            return "homepage.xhtml";
            //return "/secret/secret.xhtml?faces-redirect=true";
        } else {
            //login failed
            setEmail(null);
            setPassword(null);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Email/Password", "Please try again."));
            return "login.xhtml";
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("loggedinCustomer");
        // Invalidate the session to remove all session data
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        setEmail(null);
        setPassword(null);
        return "/index.xhtml?faces-redirect=true";
    }

    public void loadCustomer() {
        email = loggedinCustomer.getEmail();
        firstname = loggedinCustomer.getFirstname();
        contactNumber = loggedinCustomer.getContactnumber();
        lastname = loggedinCustomer.getLastname();
        fullname = firstname + " " + lastname;
        profilePhoto = loggedinCustomer.getProfilePicture();
        cId = loggedinCustomer.getId();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getProfilePhotoName() {
        return profilePhotoName;
    }

    public void setProfilePhotoName(String profilePhotoName) {
        this.profilePhotoName = profilePhotoName;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    public Customer getLoggedinCustomer() {
        return loggedinCustomer;
    }

    public void setLoggedinCustomer(Customer loggedinCustomer) {
        this.loggedinCustomer = loggedinCustomer;
    }

    public Long getcId() {
        return cId;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Customer;
import exceptions.CustomerNotFoundException;
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
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
@Named(value = "customerManagedBean")
@SessionScoped
public class CustomerManagedBean implements Serializable {

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

    @PostConstruct
    public void init() {
        profilePhotoName = "/resources/images/blankprofilepicture.png";
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
            System.out.println("From Customer Managed Bean: " + loggedinCustomer.getId());
            cId = loggedinCustomer.getId();
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

    public String saveCustomerDetails() {
        
        try {
            // Assume that a customerService exists to handle database operations
            // and that the current user's ID is available via a method getUserId()

            loggedinCustomer.setFirstname(this.firstname);
            loggedinCustomer.setLastname(this.lastname);
            loggedinCustomer.setEmail(this.email);
            loggedinCustomer.setContactnumber(this.contactNumber);

            // If a new profile photo was uploaded, update it
            if (this.uploadedFile != null && this.uploadedFile.getContent() != null) {
                loggedinCustomer.setProfilePicture(this.uploadedFile.getContent());
            }

            // Update the customer details in the database
            customerSessionLocal.updateCustomer(loggedinCustomer);

            // Show a confirmation message
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Your profile has been updated."));
            loadCustomer();
            return "homepage?faces-redirect=true";

        } catch (Exception e) {
            // Handle errors (e.g. database errors) here
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was an error updating your profile."));
            return null;
        }
    }

    public String editCustomer() {
        loadCustomer();
        //System.out.println(this.firstname);
        return "editCustomer.xhtml";
    }

    public void loadCustomer() {
        email = loggedinCustomer.getEmail();
        firstname = loggedinCustomer.getFirstname();
        contactNumber = loggedinCustomer.getContactnumber();
        lastname = loggedinCustomer.getLastname();
        fullname = firstname + " " + lastname;
        profilePhoto = loggedinCustomer.getProfilePicture();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Customer getLoggedinCustomer() {
        return loggedinCustomer;
    }

    public void setLoggedinCustomer(Customer loggedinCustomer) {
        this.loggedinCustomer = loggedinCustomer;
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

    public Long getcId() {
        return cId;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }
    

}

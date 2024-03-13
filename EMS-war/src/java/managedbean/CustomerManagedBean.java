/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Customer;
import exceptions.CustomerNotFoundException;
import exceptions.NoResultException;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;

import javax.faces.view.ViewScoped;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import org.primefaces.model.file.UploadedFile;
import session.CustomerSessionLocal;

/**
 *
 * @author alvintjw
 */
@Named(value = "customerManagedBean")
@ViewScoped
public class CustomerManagedBean implements Serializable {

    @EJB
    private CustomerSessionLocal customerSessionLocal;
    private String email;
    private String firstname;
    private String contactNumber;
    private Part uploadedfile;
    private String filename = "";
    private String profilePhotoName;
    private String fullname;
    private String lastname;
    private String password;
    private String confirmpassword;
    private Customer loggedinCustomer;
    private Customer selectedCustomer;
    private Long selectedCustomerid;
    private String selectedCustomerPhotoName;
    private Long cId;

    @Inject
    private AuthenticationManagedBean authenBean;

    @PostConstruct
    public void init() {
 
        loggedinCustomer = authenBean.getLoggedinCustomer();
        loadCustomer();
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
        cId = loggedinCustomer.getId();
        email = loggedinCustomer.getEmail();
        firstname = loggedinCustomer.getFirstname();
        contactNumber = loggedinCustomer.getContactnumber();
        lastname = loggedinCustomer.getLastname();
        fullname = firstname + " " + lastname;
        profilePhotoName = loggedinCustomer.getProfilePhotoName();
    }
    
     public void loadSelectedCustomer() {
        try {
            selectedCustomer = customerSessionLocal.getCustomer(selectedCustomerid);
        } catch (NoResultException ex) {
            Logger.getLogger(CustomerManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
     public void upload() throws IOException {
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

        //get the deployment path
        String UPLOAD_DIRECTORY = ctx.getRealPath("/") + "upload/";
        System.out.println("#UPLOAD_DIRECTORY : " + UPLOAD_DIRECTORY);

        //debug purposes
        setFilename(Paths.get(uploadedfile.getSubmittedFileName()).getFileName().toString());
        System.out.println("filename: " + getFilename());
        //---------------------
        
        //replace existing file
        Path path = Paths.get(UPLOAD_DIRECTORY + getFilename());
        InputStream bytes = uploadedfile.getInputStream();
        Files.copy(bytes, path, StandardCopyOption.REPLACE_EXISTING);
          loggedinCustomer.setProfilePhotoName(filename);
    
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

    public Part getUploadedfile() {
        return uploadedfile;
    }

    public void setUploadedfile(Part uploadedfile) {
        this.uploadedfile = uploadedfile;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getProfilePhotoName() {
        if (this.profilePhotoName == null || this.profilePhotoName.isEmpty()) {
            return "resources/images/blankprofilepicture.png"; // Adjust path to your default image
        } else {
            return "/upload/" + this.profilePhotoName;
        }
    }

    public String getSelectedCustomerPhotoName() {
        if (this.selectedCustomer.getProfilePhotoName() == null || this.selectedCustomer.getProfilePhotoName().isEmpty()) {
            return "resources/images/blankprofilepicture.png"; // Adjust path to your default image
        } else {
            return "/upload/" + this.selectedCustomer.getProfilePhotoName();
        }
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

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    public Long getSelectedCustomerid() {
        return selectedCustomerid;
    }

    public void setSelectedCustomerid(Long selectedCustomerid) {
        this.selectedCustomerid = selectedCustomerid;
    }
    
    

}

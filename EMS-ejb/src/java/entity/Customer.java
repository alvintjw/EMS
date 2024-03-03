    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author alvintjw
 */
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    
    private String email;
    private String contactnumber;
    @Lob
    private byte[] profilePicture;
    private String profilePhotoName;
    private String firstname;
    private String lastname;
    private String password;
    

//    @Temporal(TemporalType.DATE)
//    private Date dob;
//
//    @Temporal(TemporalType.DATE)
//    private Date joined;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private ArrayList<Contact> contacts;
    
    @OneToMany(mappedBy="host", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private ArrayList<Event> eventsHost;
    
    @ManyToMany(mappedBy= "customerAttend",cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private ArrayList<Event> eventsAttend;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePhotoName() {
        return profilePhotoName;
    }

    public void setProfilePhotoName(String profilePhotoName) {
        this.profilePhotoName = profilePhotoName;
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
    


    /**
     * @return the dob
     */
//    public Date getDob() {
//        return dob;
//    }
//
//    /**
//     * @param dob the dob to set
//     */
//    public void setDob(Date dob) {
//        this.dob = dob;
//    }
//
//    /**
//     * @return the Joined
//     */
//    public Date getJoined() {
//        return joined;
//    }

    /**
     * @param Joined the Joined to set
     */
//    public void setJoined(Date Joined) {
//        this.joined = Joined;
//    }

    /**
     * @return the contacts
     */
    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    /**
     * @param contacts the contacts to set
     */
    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }
    
    
    /**
     * @return the eventsHost
     */
    public ArrayList<Event> getEventsHost() {
        return eventsHost;
    }

    /**
     * @param eventsHost the eventsHost to set
     */
    public void setEventsHost(ArrayList<Event> eventsHost) {
        this.eventsHost = eventsHost;
    }

    /**
     * @return the eventsAttend
     */
    public ArrayList<Event> getEventsAttend() {
        return eventsAttend;
    }

    /**
     * @param eventsAttend the eventsAttend to set
     */
    public void setEventsAttend(ArrayList<Event> eventsAttend) {
        this.eventsAttend = eventsAttend;
    }

    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + id + " ]";
    }
    
}

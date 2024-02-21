/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
    
    private byte gender;
    private String name;

    @Temporal(TemporalType.DATE)
    private Date dob;

    @Temporal(TemporalType.DATE)
    private Date Joined;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private ArrayList<Contact> contacts;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * @return the gender
     */
    public byte getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(byte gender) {
        this.gender = gender;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the dob
     */
    public Date getDob() {
        return dob;
    }

    /**
     * @param dob the dob to set
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * @return the Joined
     */
    public Date getJoined() {
        return Joined;
    }

    /**
     * @param Joined the Joined to set
     */
    public void setJoined(Date Joined) {
        this.Joined = Joined;
    }

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
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author alvintjw
 */
@Entity
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String eventTitle;
    @Temporal(TemporalType.DATE)
    private Date eventDate;
    private String eventLocation;
    private String eventDesc;
    @Temporal(TemporalType.DATE)
    private Date eventDateline;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private ArrayList<Customer> customerAttend;
    
    @ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(nullable = false)
    private Customer host;

    public Event() {
    }

    public Event(String eventTitle, Date eventDate, String eventLocation, String eventDesc, Date eventDateline) {
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventDesc = eventDesc;
        this.eventDateline = eventDateline;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * @return the eventTitle
     */
    public String getEventTitle() {
        return eventTitle;
    }

    /**
     * @param eventTitle the eventTitle to set
     */
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    /**
     * @return the eventDate
     */
    public Date getEventDate() {
        return eventDate;
    }

    /**
     * @param eventDate the eventDate to set
     */
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * @return the eventLocation
     */
    public String getEventLocation() {
        return eventLocation;
    }

    /**
     * @param eventLocation the eventLocation to set
     */
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    /**
     * @return the eventDesc
     */
    public String getEventDesc() {
        return eventDesc;
    }

    /**
     * @param eventDesc the eventDesc to set
     */
    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    /**
     * @return the eventDateline
     */
    public Date getEventDateline() {
        return eventDateline;
    }

    /**
     * @param eventDateline the eventDateline to set
     */
    public void setEventDateline(Date eventDateline) {
        this.eventDateline = eventDateline;
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
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Event[ id=" + id + " ]";
    }

    /**
     * @return the customerAttend
     */
    public ArrayList<Customer> getCustomerAttend() {
        return customerAttend;
    }

    /**
     * @param customerAttend the customerAttend to set
     */
    public void setCustomerAttend(ArrayList<Customer> customerAttend) {
        this.customerAttend = customerAttend;
    }

    /**
     * @return the host
     */
    public Customer getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(Customer host) {
        this.host = host;
    }
    
}

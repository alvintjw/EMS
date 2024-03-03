/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Customer;
import entity.Event;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import session.CustomerSessionLocal;
import session.EventSessionBeanLocal;

/**
 *
 * @author alvintjw
 */
@Named(value = "eventManagedBean")
@SessionScoped
public class EventManagedBean implements Serializable {

    @EJB
    private CustomerSessionLocal customerSession;

    @EJB
    private EventSessionBeanLocal eventSessionBean;

    private String eventTitle;
    @Temporal(TemporalType.DATE)
    private Date eventDate;
    private String eventLocation;
    private String eventDesc;
    @Temporal(TemporalType.DATE)
    private Date eventDateline;
    private Long cId;
    private Customer loggedinCustomer;

    private List<Event> availableEvents;

    private List<Event> hostedEvents;
    private List<Event> registeredEvents;

    /**
     * Creates a new instance of EventManagedBean
     */
    public EventManagedBean() {
    }

    @PostConstruct
    public void init() {
        availableEvents = eventSessionBean.getAvailableEvents();

    }

    public String handleSearch() {
        init();
        return "findEvents.xhtml";
    }

    public void addEvent() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    }

    public String createEvent() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        Map<String, String> params = context.getExternalContext()
//                .getRequestParameterMap();
//        String cIdStr = params.get("cId");
//        System.out.println(cIdStr);
        //Long cId = Long.parseLong(cIdStr);

        Event newEvent = new Event();

        newEvent.setEventTitle(getEventTitle());
        newEvent.setEventDate(getEventDate());
        newEvent.setEventDateline(getEventDateline());
        newEvent.setEventLocation(getEventLocation());
        newEvent.setEventDesc(getEventDesc());
        try {

            eventSessionBean.createEvent(newEvent, cId);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Your event has been created."));
            handleSearch();
            return "homepage.xhtml";

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was an error registering your event!"));
            return null;
        }
    }

    public String navigateToFindEvents() {
        // Logic to navigate to find events page. This method can return a navigation outcome.
        return "findEvents.xhtml";
    }

    public String navigateToCreateEvents() {
        // Logic to navigate to create events page. This method can return a navigation outcome.
        return "createEvents.xhtml";
    }

    public void loadHostedEvents() {
        cId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loggedCustomerId");

        try {
            hostedEvents = customerSession.eventsRegistered(cId);
        } catch (exceptions.NoResultException ex) {
            Logger.getLogger(EventManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadRegisteredEvents() {
        cId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loggedCustomerId");

        try {
            registeredEvents = customerSession.eventsAttended(cId);
        } catch (exceptions.NoResultException ex) {
            Logger.getLogger(EventManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteEvent(Long eId) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            eventSessionBean.deleteEvent(eId);
        } catch (Exception e) {
            //show with an error icon
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to delete event"));
            return;
        }
        context.addMessage(null, new FacesMessage("Success", "Successfully deleted event"));
        viewEvents();

    }

    public String viewEvents() {
        loadRegisteredEvents();
        loadHostedEvents();
        return "ViewMyEvents.xhtml";
    }

    public boolean isCustomerRegistered(Long eventId) {
        cId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loggedCustomerId");
        try {
            loggedinCustomer = customerSession.getCustomer(cId);
        } catch (exceptions.NoResultException ex) {
            Logger.getLogger(EventManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (loggedinCustomer != null && loggedinCustomer.getEventsAttend() != null) {
            for (Event registeredEvent : loggedinCustomer.getEventsAttend()) {
                if (registeredEvent.getId().equals(eventId)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public String registerEvent(Long eId, Long cId) {
         FacesContext context = FacesContext.getCurrentInstance();
        try {
            eventSessionBean.registerEvent(eId, cId);
        } catch (exceptions.NoResultException ex) {
            Logger.getLogger(EventManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to register event"));
        }
        context.addMessage(null, new FacesMessage("Success", "Successfully registered event"));
       return viewEvents();
    }
    
    public String unregisterEvent(Long eId, Long cId) {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            eventSessionBean.unregisterEvent(eId, cId);
        } catch (exceptions.NoResultException ex) {
            Logger.getLogger(EventManagedBean.class.getName()).log(Level.SEVERE, null, ex);
             context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to unregister event"));
        }
        context.addMessage(null, new FacesMessage("Success", "Successfully unregistered event"));
        
        return viewEvents();
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public Date getEventDateline() {
        return eventDateline;
    }

    public void setEventDateline(Date eventDateline) {
        this.eventDateline = eventDateline;
    }

    public Long getcId() {
        return cId;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }

    public Customer getLoggedinCustomer() {
        return loggedinCustomer;
    }

    public void setLoggedinCustomer(Customer loggedinCustomer) {
        this.loggedinCustomer = loggedinCustomer;
    }

    public List<Event> getAvailableEvents() {
        return availableEvents;
    }

    public void setAvailableEvents(List<Event> availableEvents) {
        this.availableEvents = availableEvents;
    }

    public List<Event> getHostedEvents() {
        return hostedEvents;
    }

    public void setHostedEvents(List<Event> hostedEvents) {
        this.hostedEvents = hostedEvents;
    }

    public List<Event> getRegisteredEvents() {
        return registeredEvents;
    }

    public void setRegisteredEvents(List<Event> registeredEvents) {
        this.registeredEvents = registeredEvents;
    }

}

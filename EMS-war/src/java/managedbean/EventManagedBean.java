/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Customer;
import entity.Event;
import exceptions.NoResultException;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import session.CustomerSessionLocal;
import session.EventAttendanceSessionBeanLocal;
import session.EventSessionBeanLocal;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author alvintjw
 */
@Named(value = "eventManagedBean")
@ViewScoped
public class EventManagedBean implements Serializable {

    @EJB
    private EventAttendanceSessionBeanLocal eventAttendanceSessionBean;

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
    private Long eventId;
    private Customer loggedinCustomer;

    private List<Event> availableEvents;
    private List<Event> hostedEvents;
    private List<Event> registeredEvents;
    private String searchType = "TITLE";
    private String searchString;
    private Date selectedDate = new Date();

    private Event selectedEvent;
    private List<Customer> customerAttend;
    private Map<Long, Boolean> attendeesAttendance = new HashMap<>();

    /**
     * Creates a new instance of EventManagedBean
     */
    public EventManagedBean() {
    }

    @Inject
    private AuthenticationManagedBean authenBean;

    @PostConstruct
    public void init() {

        loggedinCustomer = authenBean.getLoggedinCustomer();

        if (loggedinCustomer != null) {
            cId = loggedinCustomer.getId();
            loadRegisteredEvents();
            loadHostedEvents();
        }

        if (eventId != null) {
            try {
                selectedEvent = eventSessionBean.getEvent(eventId);
            } catch (NoResultException ex) {
                Logger.getLogger(EventManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (searchString == null || searchString.equals("")) {

            availableEvents = eventSessionBean.searchEventsByTitleAndDate(null, selectedDate); // Assuming this method fetches all events
        } else {
            switch (searchType) {
                case "TITLE":
                    availableEvents = eventSessionBean.searchEventsByTitleAndDate(searchString, selectedDate);
                    break;
                case "DATE":
                    // Assuming you have a method to search by date. Date parsing might be required.
                    // Example:
                    // Date eventDate = new SimpleDateFormat("dd/MM/yyyy").parse(searchString);
                    // events = eventSessionLocal.searchEventsByDate(eventDate);
                    break;
                case "LOCATION":
                    availableEvents = eventSessionBean.searchEventsByLocationAndDate(searchString, selectedDate);
                    break;
                // You can add more cases here if you have other criteria to search by
                default:
                    availableEvents = new ArrayList<>(); // No events or throw an exception if the search type is unknown
                    break;
            }
        }

    }

    public void handleSearch() {
        init();

    }

    public void addEvent() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    }

    public String createEvent() {
        FacesContext context = FacesContext.getCurrentInstance();
        init();
        Event newEvent = new Event();

        newEvent.setEventTitle(getEventTitle());
        newEvent.setEventDate(getEventDate());
        newEvent.setEventDateline(getEventDateline());
        newEvent.setEventLocation(getEventLocation());
        newEvent.setEventDesc(getEventDesc());

        if (getEventDate().before(getCurrentDateTime())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Event Date must happen in the future!!"));
            return null;
        }

        if (getEventDate().before(getEventDateline())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Event Dateline must be before the event date!"));
            return null;
        }

        try {
            System.out.println("IN CREATEVENT cId: " + cId);
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

    public void loadHostedEvents() {
//        init();
//        hostedEvents = loggedinCustomer.getEventsHost();

        try {
            hostedEvents = customerSession.eventsRegistered(cId);

        } catch (exceptions.NoResultException ex) {
            Logger.getLogger(EventManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadRegisteredEvents() {
//          init();
//        registeredEvents = loggedinCustomer.getEventsAttend();

        try {
            registeredEvents = customerSession.eventsAttended(cId);
        } catch (exceptions.NoResultException ex) {
            Logger.getLogger(EventManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteEvent(Long eId) {
        init();
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            eventAttendanceSessionBean.deleteAttendance(eId);
            eventSessionBean.deleteEvent(eId);

        } catch (Exception e) {
            //show with an error icon
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to delete event"));
            return;
        }
        context.addMessage(null, new FacesMessage("Successfully deleted event", "Successfully deleted event"));
        init();
        viewEvents();

    }

    public String viewEvents() {
        init();
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
            eventAttendanceSessionBean.createAttendance(eId, cId);
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
            eventAttendanceSessionBean.deleteAttendance(eId, cId);
        } catch (exceptions.NoResultException ex) {
            Logger.getLogger(EventManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to unregister event"));
        }
        context.addMessage(null, new FacesMessage("Success", "Successfully unregistered event"));

        return viewEvents();
    }

    public String viewDetails(Long eId) {
        eventId = eId;

        FacesContext context = FacesContext.getCurrentInstance();

        try {
            selectedEvent = eventSessionBean.getEvent(eId);
            init();

            return "ViewEventDetails.xhtml?faces-redirect=true";
        } catch (exceptions.NoResultException ex) {
            Logger.getLogger(EventManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        init();

        return "homepage.xhtml?eId";

    }

    public String editEvent(Long eId) {
        eventId = eId;

        FacesContext context = FacesContext.getCurrentInstance();

        try {
            selectedEvent = eventSessionBean.getEvent(eId);
            init();

            return "editEvent.xhtml?faces-redirect=true";
        } catch (exceptions.NoResultException ex) {
            Logger.getLogger(EventManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        init();

        return "homepage.xhtml?eId";
    }

    public String saveEventDetails() {

        try {
            // Assume that a customerService exists to handle database operations
            // and that the current user's ID is available via a method getUserId()
            selectedEvent = eventSessionBean.getEvent(eventId);

            selectedEvent.setEventTitle(getEventTitle());
            selectedEvent.setEventDate(getEventDate());
            selectedEvent.setEventDateline(getEventDateline());
            selectedEvent.setEventLocation(getEventLocation());
            selectedEvent.setEventDesc(getEventDesc());

            // If a new profile photo was uploaded, update it
            // Update the customer details in the database
            eventSessionBean.updateEvent(selectedEvent);

            // Show a confirmation message
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Your Event has been updated."));
            loadSelectedEvent();
            return "homepage?faces-redirect=true";

        } catch (Exception e) {
            // Handle errors (e.g. database errors) here
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was an error updating your Event."));
            return null;
        }
    }

    public void loadSelectedEvent() {
        if (eventId != null) {
            try {
                selectedEvent = eventSessionBean.getEvent(eventId);
                if (selectedEvent != null && selectedEvent.getCustomerAttend() != null) {
                    System.out.println("I AM IN INIT");
                    this.eventTitle = selectedEvent.getEventTitle();
                    this.eventDate = selectedEvent.getEventDate();
                    this.eventDateline = selectedEvent.getEventDateline();
                    this.eventLocation = selectedEvent.getEventLocation();
                    this.eventDesc = selectedEvent.getEventDesc();
                    attendeesAttendance.clear(); // Clear previous values
                    for (Customer attendee : selectedEvent.getCustomerAttend()) {
                        // Populate the map with true/false based on whether each attendee is marked as present
                        System.out.println(checkIfAttendeeIsPresentForEvent(attendee.getId(), selectedEvent.getId()));
                        attendeesAttendance.put(attendee.getId(), checkIfAttendeeIsPresentForEvent(attendee.getId(), selectedEvent.getId()));
                    }
                }
                // Load attendees and their attendance status for selectedEvent as needed
            } catch (Exception e) {
                // Handle exceptions, such as redirecting to an error page or logging
            }
        }
    }

    public String markAttendance(Long cId, Long eId, Boolean isPresent) {
        eventAttendanceSessionBean.markAttendance(eId, cId, isPresent);
        return "ViewEventDetails.xhtml";
    }

    public String saveAttendance() {
        FacesContext context = FacesContext.getCurrentInstance();
        attendeesAttendance.forEach((attendeeId, isPresent) -> {
            // Update attendance status in the database
            // This may involve finding the EventAttendance record by event and attendee ID
            // and updating its 'isPresent' status.
            markAttendance(attendeeId, selectedEvent.getId(), isPresent);
        });
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Attendance saved successfully."));
        init();
        return "ViewMyEvents.xhtml";
    }

    public String navigateToFindEvents() {
        // Logic to navigate to find events page. This method can return a navigation outcome.
        return "findEvents.xhtml";
    }

    public String navigateToCreateEvents() {
        // Logic to navigate to create events page. This method can return a navigation outcome.
        return "createEvents.xhtml";
    }

    public String navigateToEventDetails(Long id) {
        // Set the eventId in the managed bean
        this.eventId = id;

        // Prepare the redirect URL with the event ID as a parameter
        return "/ViewEventDeets.xhtml?faces-redirect=true&eId=" + eventId;
    }

    public String navigateToHomePage() {
        return "/homepage.xhtml";
    }

    public boolean checkIfAttendeeIsPresentForEvent(Long cId, Long eId) {
        return eventAttendanceSessionBean.checkAttendance(cId, eId);
    }

    public void searchByDate() {
        if (searchString == null || searchString.equals("")) {
            availableEvents = eventSessionBean.searchEventsByTitleAndDate(null, selectedDate); // Assuming this method fetches all events
        } else {
            switch (searchType) {
                case "TITLE":
                    availableEvents = eventSessionBean.searchEventsByTitleAndDate(searchString, selectedDate);
                    break;
                case "DATE":
                    // Assuming you have a method to search by date. Date parsing might be required.
                    // Example:
                    // Date eventDate = new SimpleDateFormat("dd/MM/yyyy").parse(searchString);
                    // events = eventSessionLocal.searchEventsByDate(eventDate);
                    break;
                case "LOCATION":
                    availableEvents = eventSessionBean.searchEventsByLocationAndDate(searchString, selectedDate);
                    break;
                // You can add more cases here if you have other criteria to search by
                default:
                    availableEvents = new ArrayList<>(); // No events or throw an exception if the search type is unknown
                    break;
            }
        }
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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public List<Customer> getCustomerAttend() {
        return customerAttend;
    }

    public void setCustomerAttend(List<Customer> customerAttend) {
        this.customerAttend = customerAttend;
    }

    public Map<Long, Boolean> getAttendeesAttendance() {
        return attendeesAttendance;
    }

    public void setAttendeesAttendance(Map<Long, Boolean> attendeesAttendance) {
        this.attendeesAttendance = attendeesAttendance;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public Date getCurrentDateTime() {
        return new Date(); // This returns the current date and time
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Customer;
import entity.Event;
import exceptions.NoResultException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author alvintjw
 */
@Stateless
public class EventSessionBean implements EventSessionBeanLocal {

    @PersistenceContext(unitName = "EMS-ejbPU")
    private EntityManager em;

    @Override
    public List<Event> searchEvents(String name) {
        Query q;
        if (name != null) {
            q = em.createQuery("SELECT e FROM Event e WHERE "
                    + "LOWER(e.eventTitle) LIKE :title");
            q.setParameter("title", "%" + name.toLowerCase() + "%");
        } else {
            q = em.createQuery("SELECT e FROM Event e");
        }

        return q.getResultList();
    }

    @Override
    public List<Event> getAvailableEvents() {
        // Get the current date without time
        Date currentDate = java.sql.Date.valueOf(LocalDate.now());

        // Create a JPQL query to retrieve events where the date is greater than or equal to today
        TypedQuery<Event> query = em.createQuery(
                "SELECT e FROM Event e WHERE e.eventDate >= :currentDate", Event.class);
        query.setParameter("currentDate", currentDate);

        // Execute the query and get the list of available events
        return query.getResultList();
    }

    @Override
    public void createEvent(Event e, Long cId) {
        Customer cust = em.find(Customer.class, cId);
        e.setHost(cust);
        cust.getEventsHost().add(e);
        em.persist(e);
    }

    @Override
    public Event getEvent(Long eId) throws NoResultException {
        Event event = em.find(Event.class, eId);

        if (event != null) {
            return event;
        } else {
            throw new NoResultException("Not found");
        }
    }

    @Override
    public void updateEvent(Event e) throws NoResultException {
        Event olde = getEvent(e.getId());

        olde.setEventTitle(e.getEventTitle());
        olde.setEventDate(e.getEventDate());
        olde.setEventDateline(e.getEventDateline());
        olde.setEventLocation(e.getEventLocation());
        olde.setEventDesc(e.getEventDesc());
    }

    @Override
    public void deleteEvent(Long eId) throws NoResultException {
        //dissociate the host and the event
        Event e = em.find(Event.class, eId);
        Customer host = em.find(Customer.class, e.getHost().getId());
        e.setHost(null);
        host.getEventsHost().remove(e);
        //Finding all the customers that attend this event and deleting it.
        if (e != null) {
            Query q = em.createQuery("SELECT c FROM Customer c WHERE :eventsAttend MEMBER OF c.eventsAttend");
            q.setParameter("eventsAttend", e);

            for (Object cust : q.getResultList()) {
                Customer cust1 = (Customer) cust;
                cust1.getEventsAttend().remove(e);
            }

            em.remove(e);
        } else {
            throw new NoResultException("Not found");
        }

        //finding all the customers that are attending this event
    }

    @Override
    public void registerEvent(Long eId, Long cId) throws NoResultException {
        Event e = em.find(Event.class, eId);
        Customer c = em.find(Customer.class, cId);

        if (e != null && c != null) {
            e.getCustomerAttend().add(c);
            c.getEventsAttend().add(e);
        } else {
            throw new NoResultException("Customer/Event Not Found");
        }
    }
    
    @Override
    public void unregisterEvent(Long eId, Long cId) throws NoResultException {
        Event e = em.find(Event.class, eId);
        Customer c = em.find(Customer.class, cId);

        if (e != null && c != null) {
            e.getCustomerAttend().remove(c);
            c.getEventsAttend().remove(e);
        } else {
            throw new NoResultException("Customer/Event Not Found");
        }

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}

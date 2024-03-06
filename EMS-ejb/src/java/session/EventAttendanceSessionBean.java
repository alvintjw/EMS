/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Customer;
import entity.Event;
import entity.EventAttendance;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alvintjw
 */
@Stateless
public class EventAttendanceSessionBean implements EventAttendanceSessionBeanLocal {

    @PersistenceContext(unitName = "EMS-ejbPU")
    private EntityManager em;

    @Override
    public void createAttendance(Long eId, Long cId) {
        Customer attendee = em.find(Customer.class, cId);
        Event eventAttending = em.find(Event.class, eId);
        
        EventAttendance ea = new EventAttendance();
        ea.setCustomer(attendee);
        ea.setEvent(eventAttending);
        ea.setIsPresent(false);
        
        em.persist(ea);
    }
    
     @Override
    public void deleteAttendance(Long eId, Long cId) {
        EventAttendance ea = retrieveEA(eId,cId);
        EventAttendance eaDelete = em.find(EventAttendance.class, ea.getId());
        
        em.remove(eaDelete);
    }
    
    @Override 
    public EventAttendance retrieveEA(Long eId, Long cId) {
            // Find the specific EventAttendance entity by eventId and customerId
           EventAttendance eventAttendance = em.createQuery("SELECT ea FROM EventAttendance ea WHERE ea.event.id = :eId AND ea.customer.id = :cId", EventAttendance.class)
                .setParameter("eId", eId)
                .setParameter("cId", cId)
                .getSingleResult();
           
           return eventAttendance;
        
    }
    
    @Override
    public void markAttendance(Long eId, Long cId, boolean isPresent) {
        // Find the specific EventAttendance entity by eventId and customerId
        EventAttendance eventAttendance = em.createQuery("SELECT ea FROM EventAttendance ea WHERE ea.event.id = :eId AND ea.customer.id = :cId", EventAttendance.class)
                .setParameter("eId", eId)
                .setParameter("cId", cId)
                .getSingleResult();

        if (eventAttendance != null) {
//             System.out.println("MarkAttendance SessionBean is called!");
//             System.out.println("Previous attendance for Customer " +cId +": " + eventAttendance.isIsPresent());
//            System.out.println("After attendance: Customer " +cId + ": " + eventAttendance.isIsPresent());
            eventAttendance.setIsPresent(isPresent);
            em.merge(eventAttendance);
           
        }
    }
    
    @Override
    public void deleteAttendance(Long eId) {
        List<EventAttendance> eventattendances = em.createQuery("SELECT ea FROM EventAttendance ea WHERE ea.event.id = :eId")
                .setParameter("eId", eId)
                .getResultList();
        
        for(EventAttendance ea : eventattendances) {
            em.remove(em.find(EventAttendance.class, ea.getId()));
        }
    }
    
    @Override
    public boolean checkAttendance(Long cId, Long eId) {
        EventAttendance ea = retrieveEA(eId,cId);
        return ea.isIsPresent();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}

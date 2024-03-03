/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import entity.Customer;
import entity.Event;
import exceptions.NoResultException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author alvintjw
 */
@Local
public interface EventSessionBeanLocal {

    public List<Event> searchEvents(String name);

    public List<Event> getAvailableEvents();

    public void createEvent(Event e, Long cId);

    public Event getEvent(Long eId) throws NoResultException;

    public void updateEvent(Event e) throws NoResultException;
    
}

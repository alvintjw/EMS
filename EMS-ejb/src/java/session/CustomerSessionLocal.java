/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import entity.Customer;
import entity.Event;
import exceptions.CustomerNotFoundException;
import exceptions.NoResultException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author alvintjw
 */
@Local
public interface CustomerSessionLocal {

    public List<Customer> searchCustomers(String name);

    public Customer getCustomer(Long cId) throws NoResultException;

    public void createCustomer(Customer c);

    public List<Customer> searchCustomersByEmail(String email);

    public Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

    public void updateCustomer(Customer c) throws NoResultException;

    public List<Event> eventsRegistered(Customer c) throws NoResultException;

    public List<Event> eventsAttended(Customer c) throws NoResultException;

}

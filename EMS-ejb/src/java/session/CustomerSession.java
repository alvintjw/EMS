/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Customer;
import entity.Event;
import exceptions.CustomerNotFoundException;
import exceptions.NoResultException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author alvintjw
 */
@Stateless
public class CustomerSession implements CustomerSessionLocal {

    @PersistenceContext(unitName = "EMS-ejbPU")
    private EntityManager em;

    @Override
    public List<Customer> searchCustomers(String name) {
        Query q;
        if (name != null) {
            q = em.createQuery("SELECT c FROM Customer c WHERE "
                    + "LOWER(c.name) LIKE :name");
            q.setParameter("name", "%" + name.toLowerCase() + "%");
        } else {
            q = em.createQuery("SELECT c FROM Customer c");
        }

        return q.getResultList();
    } //end searchCustomers

    @Override
    public Customer getCustomer(Long cId) throws NoResultException {
        Customer cust = em.find(Customer.class, cId);

        if (cust != null) {
            return cust;
        } else {
            throw new NoResultException("Not found");
        }
    } //end getCustomer

    @Override
    public void createCustomer(Customer c) {
        em.persist(c);
    } //end createCustomer

    @Override
    public List<Customer> searchCustomersByEmail(String email) {
        Query q;
        if (email != null) {
            q = em.createQuery("SELECT c FROM Customer c WHERE "
                    + "LOWER(c.email) LIKE :email");
            q.setParameter("email", "%" + email.toLowerCase() + "%");
        } else {
            //code should not reach here
            q = em.createQuery("SELECT c FROM Customer c");
        }

        return q.getResultList();
    } //end searchCustomers

    @Override
    public Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.email = :inEmail");
        query.setParameter("inEmail", email);

        try {
            return (Customer) query.getSingleResult();
        } catch (javax.persistence.NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer email " + email + " does not exist!");
        }
    }

    @Override
    public void updateCustomer(Customer c) throws NoResultException {
        Customer oldC = getCustomer(c.getId());
        
        oldC.setFirstname(c.getFirstname());
        oldC.setLastname(c.getLastname());
        oldC.setEmail(c.getEmail());
        oldC.setContactnumber(c.getContactnumber());
        oldC.setProfilePicture(c.getProfilePicture());
        oldC.setProfilePhotoName(c.getProfilePhotoName());
    }
    
    @Override
    public List<Event> eventsRegistered(Long cId) throws NoResultException {
        Customer cust = getCustomer(cId);
  
        return cust.getEventsHost();
    }
    
    @Override
    public List<Event> eventsAttended(Long cId) throws NoResultException {
        Customer cust = getCustomer(cId);
        
        return cust.getEventsAttend();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}

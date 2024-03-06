/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import entity.EventAttendance;
import javax.ejb.Local;

/**
 *
 * @author alvintjw
 */
@Local
public interface EventAttendanceSessionBeanLocal {

    public void createAttendance(Long eId, Long cId);

    public void markAttendance(Long eId, Long cId, boolean isPresent);

    public EventAttendance retrieveEA(Long eId, Long cId);

    public void deleteAttendance(Long eId, Long cId);

    public void deleteAttendance(Long eId);

    public boolean checkAttendance(Long cId, Long eId);

 
    
}

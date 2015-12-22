package com.despegar.p13n.hestia.service;


/**
 * The listener interface for receiving userActionService events.
 * The class that is interested in processing a userActionService
 * event implements this interface, and the object created
 * with that class is registered via {@link ServiceListenersPostProcessor} class. 
 * When the userAction event occurs, that object's appropriate
 * method is invoked.
 *
 * @author jcastro
 * @see UserActionServiceEvent
 * @since Jan 2, 2013
 */
public interface UserActionServiceListener {

    boolean isListening();

}

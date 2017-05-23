
package presentation.control;

import presentation.gui.MessageableWindow;

/** In this E-Bazaar application, whenever a controller class
 *  needs to pass control to LoginControl, but also
 *  needs to regain control in order to populate
 *  a screen with data that was just made available
 *  during login, this interface is used. The doUpdate
 *  method is called by a LoginControl handler which,
 *  in effect, returns control to the main controller
 *  and allows it to get the necessary data for display.
 *  To get the right doUpdate method to work with the
 *  right handler class, the handler class itself
 *  (and not the concrete controller class) should
 *  implement the Callback interface. See how it is done in 
 *  BrowseSelectUIController in the RetrieveSavedCartHandler
 *  inner class.
 *  
 *  A secondary use of Callback is that it permits
 *  an object to send back a message to the window that
 *  called it -- if some operation cannot be performed
 *  on the object, it can tell the caller that the operation
 *  failed (and the reason why); the calling window 
 *  (which implements Callback) can then display the message
 *  from the object. 
 * 
 */
public interface Callback extends MessageableWindow {
	public void doUpdate();

}

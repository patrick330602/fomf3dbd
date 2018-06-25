/*
 * org.openmicroscopy.shoola.env.ui.SplashScreen
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006 University of Dundee. All rights reserved.
 *
 *
 * 	This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */

package org.openmicroscopy.shoola.env.ui;


//Java imports

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.env.data.login.UserCredentials;

/** 
 * Declares the interface to which the splash screen component has to
 * conform. 
 * <p>The implementation component has to serve both as start up screen to
 * provide the user with feedback about the state of the initialization
 * procedure and as a login dialog to collect the user's credentials.</p>
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author  <br>Andrea Falconi &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:a.falconi@dundee.ac.uk">
 * 					a.falconi@dundee.ac.uk</a>
 * @version 2.2 
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */

public interface SplashScreen
{
	
	/** Pops up the splash screen window. */
	public void open();
	
	/**
	 * Gets the component ready for garbage collection.
	 * Closes the splash screen window and disposes of it.
	 * Also releases any other resource. Clients shouldn't attempt to
	 * use the splash screen component after invoking this method.
	 * <p>This call is asynchronous, that is, the caller can proceed straight
	 * after invocation without having to wait for this method to be actually
	 * executed.</p>
	 */
	public void close();
	
	/**
	 * Sets the total number of initialization tasks that have to be
	 * performed.
	 * This method is guaranteed to be called before the first invocation
	 * of {@link #updateProgress(String) updateProgress()}.
	 * <p>This call is asynchronous, that is, the caller can proceed straight
	 * after invocation without having to wait for this method to be actually
	 * executed.</p>
	 *  
	 * @param value	The total number of tasks.
	 */
	public void setTotalTasks(int value);

	/**
	 * Updates the display to the current state of the initialization
	 * procedure.
	 * <p>This call is asynchronous, that is, the caller can proceed straight
	 * after invocation without having to wait for this method to be actually
	 * executed.</p>
	 * 
	 * @param task	The name of the initialization task that is about to
	 * 				be executed.
	 */
	
	public void updateProgress(String task);
    
	/**
	 * Returns the login credentials of the user.
	 * Blocks the caller until the credentials have been entered.
	 * 
     * @param init Passed <code>true</code> to retrieve the user credentials for
     *             the time, <code>false</code> otherwise.
	 * @return	The user's credentials for logging into OME.
	 */
	public UserCredentials getUserCredentials(boolean init);
	
	/** Notifies a login failure occurred. */
	public void onLoginFailure();

}

/*
 * org.openmicroscopy.shoola.env.init.FakeUserNotifierInit
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

package org.openmicroscopy.shoola.env.init;


//Java imports

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.env.config.Registry;
import org.openmicroscopy.shoola.env.config.RegistryFactory;
import org.openmicroscopy.shoola.env.ui.NullUserNotifier;
import org.openmicroscopy.shoola.env.ui.UserNotifier;

/** 
 * Fake initialization task.
 * Binds the Container's registry to the {@link UserNotifier} specified by its
 * static field.
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
public class FakeUserNotifierInit
    extends InitializationTask
{

    /**
     * Default Null service, so the GUI won't show up.
     * Change it to whatever implementation is required by your tests.
     */
    public static final UserNotifier userNotifier = new NullUserNotifier();
    
    
    /**
     * Constructor required by superclass.
     */
    public FakeUserNotifierInit() {}

    /**
     * Returns the name of this task.
     * @see InitializationTask#getName()
     */
    String getName() 
    {
        return "Creating User Notifier";
    }

    /** 
     * Does nothing, as this task requires no set up.
     * @see InitializationTask#configure()
     */
    void configure() {}

    /** 
     * Carries out this task.
     * 
     * @see InitializationTask#execute()
     */
    void execute() 
        throws StartupException
    {
        Registry reg = container.getRegistry();
        RegistryFactory.linkUserNotifier(userNotifier, reg);
    }
    
    /** 
     * Does nothing.
     * @see InitializationTask#rollback()
     */
    void rollback() {}
    
}

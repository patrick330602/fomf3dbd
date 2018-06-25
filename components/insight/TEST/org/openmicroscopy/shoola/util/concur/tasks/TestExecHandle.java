/*
 * org.openmicroscopy.shoola.util.concur.tasks.TestExecHandle
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

package org.openmicroscopy.shoola.util.concur.tasks;


//Java imports

//Third-party libraries
import junit.framework.TestCase;

//Application-internal dependencies

/** 
 * Routine unit test for {@link ExecHandle}.
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
public class TestExecHandle
    extends TestCase
{
    
    //Mock object.
    private MockExecCommand     command;
    
    //Object under test.
    private ExecHandle          handle;
    
    
    public void setUp() 
    {
        command = new MockExecCommand();
        handle = new ExecHandle();  //Not in a legal state yet (two-step init).
        handle.setCommand(command);  //OK, init completed now.
    }
    
    public void testSetCommand()
    {
        try {
            handle.setCommand(null);
            fail("Shouldn't accept a null command.");
        } catch (NullPointerException npe) {
            //OK, expected.
        }
    }
    
    public void testCancelExecution()
    {
        //Set up expected calls.
        command.cancel();
        
        //Transition mock to verification mode.
        command.activate();
        
        //Test.
        handle.cancelExecution();
        
        //Make sure all expected calls were performed.
        command.verify();
    }

}

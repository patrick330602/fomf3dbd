/*
 * org.openmicroscopy.shoola.env.ui.MockTaskBar
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
import util.mocks.IMock;
import util.mocks.MethodSignature;
import util.mocks.MockSupport;
import util.mocks.MockedCall;

/** 
 * Mock object.
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
public class MockTaskBar
    extends NullTaskBar
    implements IMock
{
    
    private static final MethodSignature open = 
        new MethodSignature(MethodSignature.PUBLIC, void.class, "open");
    
    
    private MockSupport     mockSupport;
    
    
    public MockTaskBar()
    {
        mockSupport = new MockSupport();
    }
    
    //Used both in set up and verification mode.
    public synchronized void open()
    {
        MockedCall mc = new MockedCall(open);
        if (mockSupport.isSetUpMode())
            mockSupport.add(mc);
        else //verification mode.
            mockSupport.verifyCall(mc);
    }
    
    /**
     * @see util.mocks.IMock#activate()
     */
    public synchronized void activate()
    {
        mockSupport.activate(); 
    }
    
    /**
     * @see util.mocks.IMock#verify()
     */
    public synchronized void verify()
    {
        mockSupport.verifyCallSequence();
    }
    
}

/*
 * org.openmicroscopy.shoola.util.tests.common.MockInputStream
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

package org.openmicroscopy.shoola.util.tests.common;


//Java imports
import java.io.IOException;
import java.io.InputStream;

//Third-party libraries

//Application-internal dependencies
import util.mocks.IMock;
import util.mocks.MethodSignature;
import util.mocks.MockSupport;
import util.mocks.MockedCall;

/** 
 * 
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
public class MockInputStream
    extends InputStream
    implements IMock
{

    private static final MethodSignature read3 = 
        new MethodSignature(MethodSignature.PUBLIC, int.class, "read",
                new Class[] {byte[].class, int.class, int.class});
    private static final MethodSignature close = 
        new MethodSignature(MethodSignature.PUBLIC, void.class, "close");
    
    
    private MockSupport     mockSupport;
    
    
    public MockInputStream()
    {
        mockSupport = new MockSupport();
    }
    
    //Used in set up mode.  If e != null then it must be either an instance
    //of IOException or RuntimeException.  If e != null then it will be 
    //thrown in verification mode.  
    public void read(byte[] buffer, int offset, int length, int retVal,
            Exception e)
    {
        Object[] args = new Object[] {buffer, Integer.valueOf(offset), 
        		Integer.valueOf(length)};
        MockedCall mc = new MockedCall(read3, args, Integer.valueOf(retVal));
        if (e != null) mc.setException(e);
        mockSupport.add(mc);
    }
    
    //Used in verification mode.  After verifying the call, it also writes
    //1 into buffer[offset+i], i=0,..,length-1 if the return value is not -1.  
    //Then it returns that return value set in the set-up call.
    public int read(byte[] buffer, int offset, int length)
        throws IOException
    {
        Object[] args = new Object[] {buffer, Integer.valueOf(offset), 
        		Integer.valueOf(length)};
        MockedCall mc = new MockedCall(read3, args, Integer.valueOf(0));
        mc = mockSupport.verifyCall(mc);
        if (mc.hasException()) {
            Exception e = (Exception) mc.getException();
            if (e instanceof IOException) 
                throw (IOException) e;
            throw (RuntimeException) e;
        }
        Integer r = (Integer) mc.getResult();
        int retVal = r.intValue();
        if (retVal != -1)
            for (int i = 0; i < length; ++i) buffer[offset+i] = 1;
        return retVal;
    }
    
    //Used in set up mode.  If e != null then it must be either an instance
    //of IOException or RuntimeException.  If e != null then it will be 
    //thrown in verification mode. 
    public void close(Exception e)
    {
        MockedCall mc = new MockedCall(close);
        if (e != null) mc.setException(e);
        mockSupport.add(mc);
    }
    
    //Used in verification mode. 
    public void close()
        throws IOException
    {
        MockedCall mc = new MockedCall(close);
        mc = mockSupport.verifyCall(mc);
        if (mc.hasException()) {
            Exception e = (Exception) mc.getException();
            if (e instanceof IOException) 
                throw (IOException) e;
            throw (RuntimeException) e;
        }
    }
    
    /**
     * @see util.mocks.IMock#activate()
     */
    public void activate()
    {
        mockSupport.activate(); 
    }
    
    /**
     * @see util.mocks.IMock#verify()
     */
    public void verify()
    {
        mockSupport.verifyCallSequence();
    }
    
    //Not used for our tests, implemented b/c abstract in superclass.
    public int read() throws IOException { return 0; }

}

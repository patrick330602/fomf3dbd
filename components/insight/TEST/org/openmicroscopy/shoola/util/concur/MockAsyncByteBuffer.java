/*
 * org.openmicroscopy.shoola.util.concur.MockAsyncByteBuffer
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

package org.openmicroscopy.shoola.util.concur;


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
public class MockAsyncByteBuffer
    extends AsyncByteBuffer
    implements IMock
{

    private static final MethodSignature writeToBuffer = 
        new MethodSignature(MethodSignature.PACKAGE, int.class, "writeToBuffer",
                new Class[] {ByteBufferFiller.class, int.class});
    
    
    private MockSupport     mockSupport;
    
    
    MockAsyncByteBuffer(int size, int blockSize)
    {
        super(size, blockSize);
        mockSupport = new MockSupport();
    }
    
    //Used in set up mode.  If e != null then it must be either an instance
    //of BufferWriteException or RuntimeException.  If e != null then it
    //will be thrown in verification mode. 
    void writeToBuffer(ByteBufferFiller producer, int offset, int retVal,
            Exception e) 
    {
        Object[] args = new Object[] {producer, Integer.valueOf(offset)};
        MockedCall mc = new MockedCall(writeToBuffer, args, 
        		Integer.valueOf(retVal));
        if (e != null) mc.setException(e);
        mockSupport.add(mc);
    }
    
    //Used in verification mode.
    int writeToBuffer(ByteBufferFiller producer, int offset) 
        throws BufferWriteException
    {
        Object[] args = new Object[] {producer, Integer.valueOf(offset)};
        MockedCall mc = new MockedCall(writeToBuffer, args, Integer.valueOf(0));
        mc = mockSupport.verifyCall(mc);
        if (mc.hasException()) {
            Exception e = (Exception) mc.getException();
            if (e instanceof BufferWriteException) 
                throw (BufferWriteException) e;
            throw (RuntimeException) e;
        }
        Integer r = (Integer) mc.getResult();
        return r.intValue();
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

}

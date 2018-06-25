/*
 * org.openmicroscopy.shoola.util.concur.tasks.InvocationChainAdapter
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

//Application-internal dependencies

/** 
 * Turns an {@link Invocation} chain into a {@link MultiStepTask}.
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
class InvocationChainAdapter
    implements MultiStepTask
{

    /** The adaptee. */
    private Invocation[]    chain;
    
    /** Keeps track of how many times {@link #doStep()} has been invoked. */
    private int             step;
    
    
    /**
     * Creates a new adpter.
     * 
     * @param chain  The adaptee.  Mustn't be <code>null</code> or 
     *                  <code>0</code>-length and none of its elements must be
     *                  <code>null</code> either.
     */
    InvocationChainAdapter(Invocation[] chain)
    {
        if (chain == null) throw new NullPointerException("No call.");
        if (chain.length == 0)
            throw new IllegalArgumentException("The chain has no elements.");
        this.chain = new Invocation[chain.length];
        for(int i = 0; i < chain.length; ++i)
            if ((this.chain[i] = chain[i]) == null)
                throw new NullPointerException("Null element: "+i+".");
        step = 0;
    }
    
    /**
     * Implemented as specified by the interface contract.
     * @see org.openmicroscopy.shoola.util.concur.tasks.MultiStepTask#doStep()
     */
    public Object doStep()
        throws Exception
    {
        Object result = null;
        if (!isDone())
            result = chain[step++].call();
        return result;
    }

    /** 
     * Implemented as specified by the interface contract.
     * @see org.openmicroscopy.shoola.util.concur.tasks.MultiStepTask#isDone()
     */
    public boolean isDone()
    {
        return (step == chain.length);
    }
    

/* 
 * ==============================================================
 *              Follows code to enable testing.
 * ==============================================================
 */
    
    Invocation[] getChain()
    {
        Invocation[] c = new Invocation[chain.length];
        for(int i = 0; i < chain.length; ++i) c[i] = chain[i];
        return c;
    }

}

/*
 * org.openmicroscopy.shoola.env.data.views.FakeBatchCallTree
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

package org.openmicroscopy.shoola.env.data.views;


//Java imports

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.env.event.AgentEventListener;
import org.openmicroscopy.shoola.util.concur.tasks.CmdProcessor;
import org.openmicroscopy.shoola.util.concur.tasks.ExecMonitor;
import org.openmicroscopy.shoola.util.concur.tasks.SyncProcessor;

/** 
 * Extends {@link BatchCallTree} to set up a fixture for our tests.
 * It also sets the tree to work with a {@link SyncProcessor} and
 * a {@link SyncBatchCallMonitor}.
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
public class FakeBatchCallTree
    extends BatchCallTree
{
    
    //Increased every time getPartialResult() is called.
    private int                 partResCount;
    
    //Default processor the tree will work with.
    //Can be set to something different if needed.
    private CmdProcessor        processor = new SyncProcessor();
    
    //Use same numbers as below. Leave [0] = null.
    private FakeLeafBatchCall[] L;
    
    /* We build the following tree:
     *     R
     *     |--- I1
     *     |    |--- I11
     *     |    |     |--- L1  
     *     |    |    
     *     |    |--- I12
     *     |    |     |--- L2  
     *     |
     *     |--- I2 (empty)
     *     |
     *     |--- I3
     *     |    |--- L3  
     *     |    |--- L4  
     *     |
     *     |--- L5
     * 
     * R is root node owned by superclass, Ixx are internal nodes (instances of 
     * CompositeBatchCall), Lx are leaf nodes (instances of
     * FakeLeafBatchCall). 
     * 
     * @see BatchCallTree#buildTree()
     */
    protected void buildTree()
    {
        CompositeBatchCall I1 = new CompositeBatchCall(), //1st-level nodes.
                              I11 = new CompositeBatchCall(), //2nd-level nodes.
                              I12 = new CompositeBatchCall(),
                           I2 = new CompositeBatchCall(),
                           I3 = new CompositeBatchCall();
        add(I1); add(I2); add(I3);  //Add to root.
        I1.add(I11); I1.add(I12);
        I11.add(L[1]); I12.add(L[2]);
        I3.add(L[3]); I3.add(L[4]);
        add(L[5]);  //Add to root.
    }

    /* (non-Javadoc)
     * @see BatchCallTree#getResult()
     */
    protected Object getResult()
    {
        int totalCount = 0;
        for (int i = 1; i < 6; ++i)
            totalCount += L[i].getDoCallCount();
        return Integer.valueOf(totalCount);
    }

    /* (non-Javadoc)
     * @see BatchCallTree#getPartialResult()
     * 
     * Returns 1, 2, ...
     */
    protected Object getPartialResult()
    {
        return Integer.valueOf(++partResCount);
    }
    
    //Called internally by the tree w/in exec().
    protected CmdProcessor getProcessor() { return processor; }
    
    //Sets the tree to work w/ a SyncBatchCallMonitor.
    protected ExecMonitor getMonitor(AgentEventListener observer)
    {
        return new SyncBatchCallMonitor(this, observer);
    }
    
    public FakeBatchCallTree()
    {
        L = new FakeLeafBatchCall[6];
        for (int i = 1; i < 6; ++i)
            L[i] = new FakeLeafBatchCall("L"+i);
    }
    
    public void setProcessor(CmdProcessor p) { processor = p; }

    //0<= i <=5 (=getActualLeavesCount)
    public String getLeafDescription(int i)
    {
        return L[i].getDescription();
    }
    
    //0<= i <=5 (=getActualLeavesCount)
    public void setFaultyLeaf(int i, Exception fault)
    {
        L[i].setException(fault);
    }
    
    //The number of leaves in the tree we build.
    public int getActualLeavesCount() { return 5; }
    
    //The result that should be eventually delivered to the observer.
    public Object getExpectedResult()
    {
        return Integer.valueOf(5);
    }
    
}

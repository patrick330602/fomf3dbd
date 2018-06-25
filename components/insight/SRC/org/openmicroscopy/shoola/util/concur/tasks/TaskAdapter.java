/*
 * org.openmicroscopy.shoola.util.concur.tasks.TaskAdapter
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
 * Turns a {@link Runnable} into a {@link MultiStepTask}.
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
class TaskAdapter
    implements MultiStepTask
{

    /** The adaptee. */
    private Runnable    task;
    
    /** Tells whether {@link #doStep()} has been invoked. */
    private boolean     done;
    
    
    /**
     * Creates a new adpter.
     * 
     * @param task  The adaptee.  Mustn't be <code>null</code>.
     */
    TaskAdapter(Runnable task)
    {
        if (task == null) throw new NullPointerException("No task.");
        this.task = task;
        this.done = false;
    }
    
    /**
     * Implemented as specified by the interface contract.
     * @see org.openmicroscopy.shoola.util.concur.tasks.MultiStepTask#doStep()
     */
    public Object doStep()
        throws Exception
    {
        if (!done) task.run();
        done = true;
        return null;
    }

    /** 
     * Implemented as specified by the interface contract.
     * @see org.openmicroscopy.shoola.util.concur.tasks.MultiStepTask#isDone()
     */
    public boolean isDone()
    {
        return done;
    }

    
/* 
 * ==============================================================
 *              Follows code to enable testing.
 * ==============================================================
 */
    
    Runnable getTask() { return task; }
    
}

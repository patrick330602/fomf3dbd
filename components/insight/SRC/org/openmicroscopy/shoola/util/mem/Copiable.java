/*
 * org.openmicroscopy.shoola.util.mem.Copiable
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

package org.openmicroscopy.shoola.util.mem;


//Java imports

//Third-party libraries

//Application-internal dependencies

/** 
 * Requires an implementing class to provide <i>deep</i> copies of its
 * instances.
 * More precisely, an invocation of the {@link #copy() copy} method, such as
 * <code>y = x.copy()</code>, is required to return an object <code>y</code>
 * for which the following conditions hold true:
 * <ul>
 *  <li>The state of <code>y</code> is the same as the state of 
 *  <code>x</code>.</li>
 *  <li>The state of any object <code>w</code> referenced by <code>y</code> is 
 *   the same as the corresponding object referenced by <code>x</code>,
 *   and so on recursively for every object referenced by <code>w</code>.</li>
 *  <li>Any subsequent state change in <code>y</code> or, recursively, in
 *   any other object referenced by <code>y</code>, is not going to affect
 *   <code>x</code> or recursively, any other object referenced by 
 *   <code>x</code>.</li>
 * </ul> 
 * Note that a deep copy would satisfy the above conditions.  However, a 
 * non-complete deep copy might comply with the above as well &#151; for
 * example immutable objects don't need to be copied.  
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
public interface Copiable
{

    /**
     * Makes a copy of this object.
     * The implementation is required to stick to the copy semantics defined
     * by this interface.
     * 
     * @return The new object.
     */
    public Object copy();
    
}

/*
 * org.openmicroscopy.shoola.util.mem.CopiableArray
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
 * This abstract class provides an implementation of the {@link Copiable}
 * interface. It constructs an array of {@link Copiable}s , note that
 * all subclasses must implement the {@link #makeNew(int)} method.
 * It provides methods to manipulate elements of the array 
 * {@link #set(Copiable, int)} and {@link #get(int)}.
 * It also implements a {@link #copy(int, int)} method which allows to copy 
 * an element from a specified position in the array into a new specified
 * position.
 * Subclasses inherit the {@link #copy()} method
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
public abstract class CopiableArray
    implements Copiable
{
    
    /** The array of {@link Copiable}s. */
    private Copiable[]  elements;
    
    /**
     * Creates a new instance.
     * 
     * @param size The size of the array.
     */
    protected CopiableArray(int size)
    {
        if (size <= 0) 
            throw new IllegalArgumentException("Size cannot be <=0");
        elements = new Copiable[size];
    }
    
    /** 
     * Contructs a new array of the speficied size. 
     * 
     * @param size The size of the array.
     * @return See above.
     */
    protected abstract CopiableArray makeNew(int size);
    
    /** 
     * Returns the number of elements in the array. 
     * 
     * @return See above.
     */
    public int getSize() { return elements.length; }
    
    /** 
     * Replaces the element at the specified position with the 
     * specified {@link Copiable}.
     * 
     * @param element Copiable to set.
     * @param index   position.
     * */
    public void set(Copiable element, int index)
    {
        if (index >= elements.length || index < 0)
            throw new IllegalArgumentException("index not valid");
        elements[index] = element;
    }
    
    /** 
     * Returns the {@link Copiable} at the specified position.
     * 
     * @param index The position in the array.
     * @return See above.
     */
    public Copiable get(int index)
    {
        if (index >= elements.length || index < 0)
            throw new IllegalArgumentException("index not valid");
        return elements[index];
    }
 
    /** 
     * Copies the {@link Copiable} from the specified position <code>from</code>
     * into the specified position <code>to</code>.
     * 
     * @param from  The start position.
     * @param to    The end position.
     */
    public void copy(int from, int to)
    {
        if (from >= elements.length || from < 0)
            throw new IllegalArgumentException("from index not valid");
        if (to >= elements.length || to < 0)
            throw new IllegalArgumentException("to index not valid");
        if (from > to) 
            throw new IllegalArgumentException(from+" must be <= than "+to);
        
        Copiable master = elements[from];
        if (master != null) {
            for (int i = from+1; i <= to; i++)
                elements[i] = (Copiable) master.copy();
        } else {
            for (int i = from+1; i <= to; i++)
                elements[i] = null;
        }
    }
    
    /**
     * Implements the method as specified by {@link Copiable}. 
     * @see Copiable#copy()
     */
    public Object copy()
    {
        CopiableArray copy = makeNew(elements.length);
        Copiable c;
        for (int i = 0; i < elements.length; i++) {
            c = elements[i];
            if (c != null) c = (Copiable) c.copy();
            copy.set(c, i);
        }
        return copy;
    }
    
}

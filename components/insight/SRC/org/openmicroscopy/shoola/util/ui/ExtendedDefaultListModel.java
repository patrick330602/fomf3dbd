/*
 * org.openmicroscopy.shoola.util.ui.ExtendedDefaultListModel
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
package org.openmicroscopy.shoola.util.ui;

//Java imports
import javax.swing.DefaultListModel;

//Third-party libraries

//Application-internal dependencies

/** 
 * Extends the {@link DefaultListModel} class. Adds a non-empty constructor and
 * a {@link #addAllElements(Object[])} method.
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
public class ExtendedDefaultListModel
    extends DefaultListModel
{
    
    /**
     * Creates a new instance.
     * 
     * @param data The array to add to the list.
     */
    public ExtendedDefaultListModel(Object[] data)
    {
        addAllElements(data);
    }
    
    /**
     * Adds each element of the specified array to the list.
     * 
     * @param data The array to add to the list.
     */
    public void addAllElements(Object[] data)
    {
        for (int i = 0; i < data.length; i++)
            addElement(data[i]);
    }
    
}

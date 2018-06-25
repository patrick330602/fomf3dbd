/*
 * org.openmicroscopy.shoola.env.config.DoubleEntry
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
package org.openmicroscopy.shoola.env.config;

//Java imports

//Third-party libraries
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

//Application-internal dependencies

/**
 * Handles an <i>entry</i> of type <i>double</i>.
 * The tag's value is stored into a {@link Double} object which is then
 * returned by the {@link #getValue() getValue} method.
 * 
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 *              <a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author  <br>Andrea Falconi &nbsp;&nbsp;&nbsp;&nbsp;
 *              <a href="mailto:a.falconi@dundee.ac.uk">
 *              a.falconi@dundee.ac.uk</a>
 * @version 2.2 
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
class DoubleEntry
    extends Entry
{
    
	/** The entry value. */
    private Double value;
    
    
	/** Creates a new instance. */
    DoubleEntry() {}
    
    /** 
     * Implemented as specified by {@link Entry}. 
     * @see Entry#setContent(Node)
     * @throws ConfigException If the configuration entry couldn't be handled.
     */  
    protected void setContent(Node node)
    	throws ConfigException
    { 
        String cfgVal = null;
        try {
			cfgVal = node.getFirstChild().getNodeValue();
            value = new Double(cfgVal);
        } catch (DOMException dex) { 
			rethrow("Can't parse double entry, name: "+getName()+".", dex);
        } catch (NumberFormatException nfe) {
			rethrow(cfgVal+" is not a valid double, entry name: "+
					getName()+".", nfe);
        }
    }
    
	/**
	 * Returns a {@link Double} object which represents the tag's content.
	 * The double value wrapped by the returned object will be parsed as
	 * specified by the {@link Double} class. 
	 * 
	 * @return	See above.
	 */  
    Object getValue() { return value; }   
    
}

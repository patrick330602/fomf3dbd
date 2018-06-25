/*
 * org.openmicroscopy.shoola.util.math.Approximation
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

package org.openmicroscopy.shoola.util.math;

//Java imports

//Third-party libraries

//Application-internal dependencies

/** 
 * Utility class. Collection of static methods.
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
public class Approximation
{

	/** 
	 * Returns the nearest integer e.g. 
	 * 1.2 returns 1, 1.6 returns 2.
     * 
     * @param v The value to check.
     * @return See above.
	 */
	public static double nearestInteger(double v)
	{
		double d = Math.floor(v);
		double diff = Math.abs(v-d);
		double value = d;
		if (diff > 0.5) value++; 
		return value;
	}
    
	/** 
     * Returns the smallest integer. 
     * 
     * @param v The value to check
     * @return See above.
     */
	public static double smallestInteger(double v) { return Math.floor(v); }
	
    /** 
     * Returns the largest integer. 
     * 
     * @param v The value to check
     * @return See above.
     */ 
	public static double largestInteger(double v) { return Math.ceil(v); }

}

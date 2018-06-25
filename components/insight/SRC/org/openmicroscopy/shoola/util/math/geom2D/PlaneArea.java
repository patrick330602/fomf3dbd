/*
 * org.openmicroscopy.shoola.util.math.geom2D.PlaneArea
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

package org.openmicroscopy.shoola.util.math.geom2D;


//Java imports
import java.awt.Shape;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.util.mem.Copiable;

/** 
 * Interface that all areas of the Euclidean space <b>R</b><sup>2</sup> must
 * implement. 
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
public interface PlaneArea
    extends Copiable, Shape
{

    /** 
     * Sets the bounding <code>Rectangle</code> of the planeArea
     * to the specified x, y, width, and height.
     * 
     * @param x The x-coordinate of the top-left corner.
     * @param y The y-coordinate of the top-left corner.
     * @param width The width of the area.
     * @param height The height of the area.
     */
    public void setBounds(int x, int y, int width, int height);
    
    /** 
     * Resets the bounding <code>Rectangle</code> of the planeArea
     * according to the specified scaling factor.
     * 
     * @param factor The scaling factor.
     */
    public void scale(double factor);
    
    /** 
     * Returns an array of {@link PlanePoint} contained in the PlaneArea. 
     * 
     * @return See above.
     */
    public PlanePoint[] getPoints();
    
    /** 
     * Controls if a specified point is on the boundary of the PlaneArea. 
     * 
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @return  <code>true</code> if the point is on the boundary, 
     *          <code>false</code> otherwise.
     */
    public boolean onBoundaries(double x, double y);
    
}

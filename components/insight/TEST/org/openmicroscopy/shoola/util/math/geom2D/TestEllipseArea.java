/*
 * org.openmicroscopy.shoola.util.math.geom2D.TestEllipseArea
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
import java.awt.Rectangle;

//Third-party libraries
import junit.framework.TestCase;

//Application-internal dependencies

/** 
 * Unit test for {@link EllipseArea}.
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
public class TestEllipseArea
    extends TestCase
{
    private static final int        LENGTH = 4, MAX_ITER = 1000;
    
    public void testEllipse()
    {
        EllipseArea area = new EllipseArea(0, 0, 5000, 
                                    5000);
        Rectangle r = area.getBounds();
        assertEquals("Should set x to argument passed to constructor.", 
                    0, r.x, 0);
        assertEquals("Should set y to argument passed to constructor.", 
                    0, r.y, 0);
        assertEquals("Should set w to argument passed to constructor.", 
                    5000, r.width, 0);
        assertEquals("Should set h to argument passed to constructor.", 
                    5000, r.height, 0);
    }
    
    public void testSetBounds()
    {
        EllipseArea area = new EllipseArea(0, 0, 1, 1);
        area.setBounds(0, 0, 2, 2);
        Rectangle r = area.getBounds();
        assertEquals("Should set x to argument passed to setBounds() method.", 
                0, r.x, 0);
        assertEquals("Should set y to argument passed to setBounds() method.", 
                0, r.y, 0);
        assertEquals("Should set w to argument passed to setBounds() method.", 
                2, r.width, 0);
        assertEquals("Should set h to argument passed to setBounds() method.", 
                2, r.height, 0);
    }
    
    public void testScale()
    {
        EllipseArea area = new EllipseArea(0, 0, 1, 1);
        double j;
        Rectangle r = area.getBounds();
        Rectangle rScale;
        for (int i = 0; i < MAX_ITER; i++) {
            j = ((double) i)/MAX_ITER;
            area.scale(j);
            rScale = area.getBounds();
            assertEquals("Wrong scale x [i = "+i+"].", rScale.x, (int) (r.x*j), 
                        0);
            assertEquals("Wrong scale y [i = "+i+"].", rScale.y, (int) (r.y*j), 
                    0);
            assertEquals("Wrong scale w [i = "+i+"].", rScale.width, 
                    (int) (r.width*j), 0);
            assertEquals("Wrong scale h [i = "+i+"].", rScale.height, 
                    (int) (r.height*j), 0);
        }
    }
    
    public void testPlanePoints1()
    {
        EllipseArea area = new EllipseArea(0, 0, 1, 1);
        //Empty array
        PlanePoint[] points = area.getPoints();
        assertEquals("Wrong size of the array", 0, points.length, 0);
    }
    
    public void testPlanePoints2()
    {
        //Ellipse which only contains the origin.
        EllipseArea area = new EllipseArea(-1, -1, 2, 2);
        PlanePoint[] points = area.getPoints();
        assertEquals("Wrong size of the array", 1, points.length, 0);
        assertEquals("Wrong x coordinate", 0, points[0].x1, 0);
        assertEquals("Wrong y coordinate", 0, points[0].x2, 0);
    }

    public void testPlanePoints3()
    {
        //Ellipse containing 9 points.
        // (-1, 1), (0, 1), (1, 1), (-1, 0), (0, 0), (0, 1)
        // (-1, -1), (0, -1), (1, -1)
        EllipseArea area = new EllipseArea(-2, -2, LENGTH, LENGTH);
        PlanePoint[] points = area.getPoints();
        assertEquals("Wrong size of the array", 2*LENGTH+1, points.length, 0);
        PlanePoint point;
        int k = -1, j = -1, l = 1;
        for (int i = 0; i < points.length; i++) {
            point = points[i];
            assertEquals("Wrong x coordinate", k, point.x1, 0);
            assertEquals("Wrong y coordinate", j, point.x2, 0);
            if (i == l*(LENGTH-1)-1) {
                l++;
                j++;
                k = -2;
            }
            k++;
       }
    }

    public void testOnBoundaries()
    {
        EllipseArea area = new EllipseArea(-1, -1, 2, 2);
        assertFalse(area.onBoundaries(0, 0));
    }
    
}

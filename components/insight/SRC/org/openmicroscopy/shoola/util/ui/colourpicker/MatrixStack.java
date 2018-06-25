/*
 * org.openmicroscopy.shoola.util.ui.colourpicker.MatrixStack
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

package org.openmicroscopy.shoola.util.ui.colourpicker;

//Java imports
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Stack;

//Third-party libraries

//Application-internal dependencies

/** 
 * This is a matrix stack, it will save the current affine transformation of 
 * the graphic device when pushed onto the stack and return that context when 
 * popped. 
 * <p>
 * I did think of saving the graphics context inside the matrix stack and using
 * simply push and pop to set the states of the graphics context itself. I 
 * decided that this had less utility (also a danger of dangling references) 
 * than passing the affine transform.
 * </p>
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author	Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $ $Date: $)
 * </small>
 * @since OME2.2
 */
class MatrixStack
{

	/** Stack containing affinetransform. */
	private Stack stack;
	
	/** Creates a new instance and constructs the matrix stack. */
	MatrixStack()
	{
		stack = new Stack();
	}
	
	/**
	 * Pushes the passed transformation onto the stack.
     * 
	 * @param saveMatrix The transformation to push.
	 */
	void push(AffineTransform saveMatrix) { stack.push(saveMatrix); }

	/**
 	 * Pushes the current trasformation of the Graphics context onto the stack.
     * 
 	 * @param saveMatrix The transformation to push.
	 */
	void push(Graphics2D saveMatrix)
	{
		stack.push(saveMatrix.getTransform());
	}
	
	/**
	 * Retrieves top transform from stack and returns it.
     * 
	 * @return See above. 
	 */
	AffineTransform pop() { return (AffineTransform) stack.pop(); }
	
	/**
	 * Retrieves the top transform from stack and puts it onto the current 
	 * graphics context.
     * 
	 * @param g The graphic context.
	 */
	void pop(Graphics2D g)
	{
		g.setTransform((AffineTransform) stack.pop());
	}
	
	/** Removes all elements from the stack. */
	void clear() { stack.clear(); }
	
}

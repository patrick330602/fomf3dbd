/*
 * org.openmicroscopy.shoola.util.ui.clsf.TreeCheckModel
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

package org.openmicroscopy.shoola.util.ui.clsf;

//Java imports
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

//Third-party libraries

//Application-internal dependencies

/** 
 * A simple tree data model that uses {@link TreeCheckNode}s.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
public class TreeCheckModel
    extends DefaultTreeModel
{

    /**
     * Creates a new instance.
     * 
     * @param root The root node of the tree. 
     */
    public TreeCheckModel(TreeCheckNode root)
    {
        super(root);
    }

    /**
     * Overridden to make sure that the inserted nodes are instance of 
     * <code>TreeCheckNode</code>.
     * @see DefaultTreeModel#insertNodeInto(MutableTreeNode, MutableTreeNode,
     *                                      int)
     */
    public void insertNodeInto(MutableTreeNode newChild,
                                MutableTreeNode parent, int index)
    {
        if (!(newChild instanceof TreeCheckNode))
            throw new IllegalArgumentException("Node must be an instance of " +
                    "TreeCheckNode");
        if (!(parent instanceof TreeCheckNode))
            throw new IllegalArgumentException("Node must be an instance of " +
                    "TreeCheckNode");
        super.insertNodeInto(newChild, parent, index);
    }
    
}

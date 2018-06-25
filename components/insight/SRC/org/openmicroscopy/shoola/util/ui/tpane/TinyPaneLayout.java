/*
 * org.openmicroscopy.shoola.util.ui.tpane.TinyPaneLayout
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

package org.openmicroscopy.shoola.util.ui.tpane;

//Java imports
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JComponent;

//Third-party libraries

//Application-internal dependencies

/** 
 * The {@link TinyPane}'s layout manager.
 * This class makes sure that the <code>TitleBar</code> and the 
 * <code>Internal desktop</code> are layout properly.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision: 4694 $ $Date: 2006-12-15 17:02:59 +0000 (Fri, 15 Dec 2006) $)
 * </small>
 * @since OME2.2
 */
public class TinyPaneLayout
    implements LayoutManager
{

    /**
     * Returns the {@link TinyPane#getPreferredSize() preferredSize} of the 
     * container.
     * 
     * @param c The component to lay out.
     * @return See above.
     */
    public Dimension preferredLayoutSize(Container c)
    {
        TinyPane frame = (TinyPane) c;
        return frame.getPreferredSize();
    }

    /**
     * Returns the minimum size of the <code>TitleBar</code>.
     * 
     * @param c The component to lay out.
     * @return See above.
     */
    public Dimension minimumLayoutSize(Container c)
    {
        TinyPane frame = (TinyPane) c;
        Dimension d = frame.getTitleBar().getMinimumSize();
        Insets i = frame.getInsets();
        d.width += i.left+i.right;
        d.height += i.top+i.bottom;
        return d;
    }

    /**
     * Lays out the {@link TinyPane}'s components.
     * 
     * @param c The component to lay out.
     */
    public void layoutContainer(Container c)
    {
        TinyPane frame = (TinyPane) c;
        Insets i = frame.getInsets();
        int cx, cy, cw, ch;
        cx = i.left;
        cy = i.top;
        cw = frame.getWidth()-i.left-i.right;
        ch = frame.getHeight()-i.top-i.bottom;
        JComponent titlebar = frame.getTitleBar();
        if (titlebar != null) {
            Dimension size = titlebar.getPreferredSize();
            titlebar.setBounds(cx, cy, cw, size.height);
            cy += size.height;
            ch -= size.height;
        }
        if (frame.getContentPane() != null)
            frame.getContentPane().setBounds(cx, cy, cw, ch);  
    }

    /**
     * Required by {@link LayoutManager} but no-op implementation in our case.
     * @see LayoutManager#addLayoutComponent(String, Component)
     */
    public void addLayoutComponent(String name, Component c) {}
    
    /**
     * Required by {@link LayoutManager} but no-op implementation in our case.
     * @see LayoutManager#removeLayoutComponent(Component)
     */
    public void removeLayoutComponent(Component c) {} 
    
}

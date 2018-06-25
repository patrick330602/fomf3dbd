/*
 * org.openmicroscopy.shoola.util.ui.tpane.SizeButton
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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import org.openmicroscopy.shoola.util.ui.IconManager;

//Third-party libraries

//Application-internal dependencies


/** 
 * The size button in the {@link TitleBar}.
 * This is a small MVC component that is aggregated into the bigger MVC set
 * of the {@link TinyPane}. The MVC parts of this button are all collapsed
 * in this class.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision: 4694 $ $Date: 2006-12-15 17:02:59 +0000 (Fri, 15 Dec 2006) $)
 * </small>
 * @since OME2.2
 */
public class SizeButton
    extends JButton
    implements TinyObserver, PropertyChangeListener, ActionListener
{
 
    /** Tooltip text when the button represents the collapse action. */
    static final String COLLAPSE_TOOLTIP = "Collapse";
    
    /** Tooltip text when the button represents the expand action. */
    static final String EXPAND_TOOLTIP = "Expand";
    
    /** The Model this button is working with. */
    private TinyPane   model;
    
    /**
     * Creates a new instance.
     * 
     * @param model The Model this button will be working with.
     *              Mustn't be <code>null</code>.
     */
    SizeButton(TinyPane model) 
    {
        if (model == null) throw new NullPointerException("No model.");
        setBorder(BorderFactory.createEmptyBorder());  //No border around icon.
        setMargin(new Insets(0, 0, 0, 0));//Just to make sure button sz=icon sz.
        setOpaque(false);  //B/c button=icon.
        setFocusPainted(false);  //Don't paint focus box on top of icon.
        setRolloverEnabled(true);
        this.model = model;
    }

    /**
     * Registers this button with the Model. 
     * @see TinyObserver#attach()
     */
    public void attach() 
    { 
        addActionListener(this);
        model.addPropertyChangeListener(TinyPane.COLLAPSED_PROPERTY, this);
        propertyChange(null);  //Synch button w/ current state.
    }
    
    /**
     * Detaches this button from the Model's change notification registry.
     * @see TinyObserver#detach()
     */
    public void detach() 
    { 
        model.removePropertyChangeListener(TinyPane.COLLAPSED_PROPERTY, this); 
    }
    
    /**
     * Sets the button appearence according to the collapsed state of the Model.
     * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent pce)
    {
        //NOTE: We can only receive COLLAPSED_PROPERTY changes, see attach().
        IconManager icons = IconManager.getInstance();
        if (model.isCollapsed()) {
            setIcon(icons.getIcon(IconManager.PLUS_9));
            setRolloverIcon(icons.getIcon(IconManager.PLUS_OVER_9));
            setToolTipText(EXPAND_TOOLTIP);
        } else {
            setIcon(icons.getIcon(IconManager.MINUS_9));
            setRolloverIcon(icons.getIcon(IconManager.MINUS_OVER_9));   
            setToolTipText(COLLAPSE_TOOLTIP);
        }
    }
    
    /**
     * Expands or collapses the frame depending on the current state of the
     * frame.
     * @see ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        boolean b = !model.isCollapsed();
        model.setCollapsed(b);
    }
    
    /** 
     * Overridden to make sure no focus is painted on top of the icon. 
     * @see JButton#isFocusable()
     */
    public boolean isFocusable() { return false; }
    
    /**
     * Overridden to make sure no focus is painted on top of the icon. 
     * @see JButton#requestFocus()
     */
    public void requestFocus() {}
    
}
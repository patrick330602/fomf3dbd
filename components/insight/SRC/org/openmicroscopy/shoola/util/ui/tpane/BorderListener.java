/*
 * org.openmicroscopy.shoola.util.ui.tpane.BorderListener
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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import javax.swing.DefaultDesktopManager;
import javax.swing.DesktopManager;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

//Third-party libraries

//Application-internal dependencies

/** 
 * Listens to mouse motions and inputs events to move and resize the
 * {@link TinyPane}. The {@link TinyPane} will behave like a 
 * {@link javax.swing.JInternalFrame}.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision: 4811 $ $Date: 2007-05-08 14:48:34 +0000 (Tue, 08 May 2007) $)
 * </small>
 * @since OME2.2
 */
class BorderListener
    extends MouseInputAdapter
    implements SwingConstants
{
    
    /** Identifies the resizing zone. */
    private static final int RESIZE_NONE  = 0;
    
    /** Flag to discard the release action. */
    private boolean                 discardRelease;
       
    /** Value added to resize the corner of the frame. */
    private int                     resizeCornerSize = 16;
    
    /** Reference to the desktopManager. */
    private static DesktopManager   sharedDesktopManager;
    
    /** Flag to control the dragging events. */
    private boolean                 dragging;
    
    /** The mousePressed x-coordinate in absolute coordinate system. */
    private int                     xAbs;
    
    /** The mousePressed y-coordinate in absolute coordinate system. */
    private int						yAbs;
    
    /** The mousePressed x-coordinate in source view's coordinate system. */
    private int                     xView;
    
    /** The mousePressed y-coordinate in source view's coordinate system. */
    private int                     yView;
    
    /** The starting rectangle. */
    private Rectangle               startingBounds;
    
    /** The bounds of the parent of the {@link TinyPane} frame. */
    private Rectangle               parentBounds;
    
    /** The direction of a move. */
    private int                     resizeDir;
    
    /** The Model this listener is for. */
    private TinyPane                frame;
    
    /**
     * Creates a {@link DesktopManager}. 
     * 
     * @return See above.
     */
    protected DesktopManager getDesktopManager()
    {
        if (sharedDesktopManager == null)
              sharedDesktopManager = new DefaultDesktopManager();
        return sharedDesktopManager;  
    }
    
    /**
     * Creates a new instance.
     * 
     * @param frame Reference to the {@link TinyPane model}. Mustn't be 
     *              <code>null</code>.
     */
    BorderListener(TinyPane frame)
    {
        if (frame == null) throw new NullPointerException("No frame.");
        this.frame = frame;
        Container parent = frame.getParent();
        if (parent != null) parentBounds = parent.getBounds();
    }
  
    /**
     * Handles <code>mouseReleased</code> event. 
     * @see MouseInputAdapter#mouseReleased(MouseEvent)
     */
    public void mouseReleased(MouseEvent e)
    {
        if (discardRelease) {
            discardRelease = false;
            return;
        }
        frame.notifyEndMoving();
        if (resizeDir == RESIZE_NONE) {
            getDesktopManager().endDraggingFrame(frame);    
            dragging = false;
        } else {
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            getDesktopManager().endResizingFrame(frame);
            //Rectangle r = frame.getBounds();
            //Dimension d = r.getSize();
            //frame.getInternalDesktop().setPreferredSize(d);
            //frame.getInternalDesktop().setSize(d);
        }
        
        xAbs = 0;
        yAbs = 0;
        xView = 0;
        yView = 0;
        startingBounds = null;
        resizeDir = RESIZE_NONE;
    }
     
    /**
     * Handles <code>mousePressed</code> event.
     * @see MouseInputAdapter#mousePressed(MouseEvent)
     */
    public void mousePressed(MouseEvent e)
    {
        Point p = SwingUtilities.convertPoint((Component) e.getSource(), 
                    e.getX(), e.getY(), null);
        xView = e.getX();
        yView = e.getY();
        xAbs = p.x;
        yAbs = p.y;
        startingBounds = frame.getBounds();
        resizeDir = RESIZE_NONE;
        
        Insets i = frame.getInsets();
        Point ep = new Point(xView, yView);
        JComponent titleBar = frame.getTitleBar();
        if (e.getSource() == titleBar) {
            Point np = titleBar.getLocation();
            ep.x += np.x;
            ep.y += np.y;
        }
        frame.onFrameIconPressed(ep);
        if (e.getSource() == titleBar) {
            if (ep.x > i.left && ep.y > i.top && ep.x < 
                    frame.getWidth()-i.right) {
                getDesktopManager().beginDraggingFrame(frame);
                dragging = true;
                return;
            }
        }
        if (!frame.isResizable()) return;
        if (e.getSource() == frame || e.getSource() == titleBar) {
            if (ep.x <= i.left) {
                if (ep.y < resizeCornerSize + i.top) resizeDir = NORTH_WEST;
                else if (ep.y > frame.getHeight()-resizeCornerSize-i.bottom)
                    resizeDir = SOUTH_WEST;
                else resizeDir = WEST;
            } else if (ep.x >= frame.getWidth()-i.right) {
                if (ep.y < resizeCornerSize+i.top) resizeDir = NORTH_EAST;
                else if (ep.y > frame.getHeight()-resizeCornerSize-i.bottom)
                    resizeDir = SOUTH_EAST;
                else resizeDir = EAST;
            } else if (ep.y <= i.top) {
                if (ep.x < resizeCornerSize+i.left) resizeDir = NORTH_WEST;
                else if (ep.x > frame.getWidth()-resizeCornerSize-i.right)
                    resizeDir = NORTH_EAST;
                else resizeDir = NORTH;
            } else if (ep.y >= frame.getHeight()-i.bottom) {
                if (ep.x < resizeCornerSize+i.left) resizeDir = SOUTH_WEST;
                else if (ep.x > frame.getWidth()-resizeCornerSize-i.right)
                    resizeDir = SOUTH_EAST;
                else resizeDir = SOUTH;
            } else {
                /* the mouse press happened inside the frame, not in the 
                 * border */
            	
                discardRelease = true;
                return;
            }
    
            getDesktopManager().beginResizingFrame(frame, resizeDir);

            Cursor s = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
            switch (resizeDir) {
                case SOUTH:
                    s = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
                    break; 
                case NORTH:
                    s = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
                    break; 
                case WEST:
                    s = Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
                    break; 
                case EAST:
                    s = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
                    break; 
                case SOUTH_EAST:
                    s = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
                    break; 
                case SOUTH_WEST:
                    s = Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
                    break; 
                case NORTH_WEST:
                    s = Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
                    break; 
                case NORTH_EAST:
                    s = Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
                    break;
            } 
            frame.setCursor(s);
            return;
        }
    }

    /** 
     * Handles <code>mouseDragged</code> event.
     * @see MouseInputAdapter#mouseDragged(MouseEvent)
     */
    public void mouseDragged(MouseEvent e)
    {   
        if (startingBounds == null) return;
                                 
        Point p = SwingUtilities.convertPoint((Component) e.getSource(), 
                                                e.getX(), e.getY(), null);
        int deltaX = xAbs-p.x;
        int deltaY = yAbs-p.y;
        Dimension min = frame.getMinimumSize();
        Dimension max = frame.getMaximumSize();
        int newX, newY, newW, newH;
        Insets i = frame.getInsets();
    
        // Handle a MOVE 
        if (dragging) {
            //Don't allow moving of frames if left mouse button was not used.
            if ((e.getModifiers() & 
                 InputEvent.BUTTON1_MASK) != InputEvent.BUTTON1_MASK)
                return;
            
            int pWidth, pHeight;
            Dimension s = frame.getParent().getSize();
            pWidth = s.width;
            pHeight = s.height;
            newX = startingBounds.x-deltaX;
            newY = startingBounds.y-deltaY;

            // Make sure we stay in-bounds
            if (newX+i.left <= -xView) newX = -xView-i.left+1;
            if (newY+i.top <= -yView) newY = -yView-i.top+1;
            if (newX+xView+i.right >= pWidth) newX = pWidth-xView-i.right-1;
            if (newY+yView+i.bottom >= pHeight) newY = pHeight-yView-i.bottom-1;
            getDesktopManager().dragFrame(frame, newX, newY);
            return;
        }

        if (!frame.isResizable()) return;

        newX = frame.getX();
        newY = frame.getY();
        newW = frame.getWidth();
        newH = frame.getHeight();

        parentBounds = frame.getParent().getBounds();
        switch (resizeDir) {
            case RESIZE_NONE:   
                frame.setCursor(Cursor.getPredefinedCursor(
                                Cursor.DEFAULT_CURSOR));
                return;
            case NORTH:      
                if (startingBounds.height+deltaY < min.height)
                    deltaY = min.height-startingBounds.height;
                else if (startingBounds.height+deltaY > max.height)
                    deltaY = max.height-startingBounds.height;
                if (startingBounds.y-deltaY < 0) deltaY = startingBounds.y;
                newX = startingBounds.x;
                newY = startingBounds.y-deltaY;
                newW = startingBounds.width;
                newH = startingBounds.height+deltaY;
                frame.setCursor(Cursor.getPredefinedCursor(
                                    Cursor.N_RESIZE_CURSOR));
            break;
            case NORTH_EAST:     
                if (startingBounds.height+deltaY < min.height)
                    deltaY = min.height-startingBounds.height;
                else if (startingBounds.height+deltaY > max.height)
                    deltaY = max.height-startingBounds.height;
                if (startingBounds.y-deltaY < 0) deltaY = startingBounds.y;

                if (startingBounds.width-deltaX < min.width)
                    deltaX = startingBounds.width-min.width;
                else if (startingBounds.width-deltaX > max.width)
                    deltaX = -(max.width-startingBounds.width);
                if (startingBounds.x+startingBounds.width-deltaX >
                        parentBounds.width) 
                    deltaX = startingBounds.x+startingBounds.width-
                            parentBounds.width;
                newX = startingBounds.x;
                newY = startingBounds.y-deltaY;
                newW = startingBounds.width-deltaX;
                newH = startingBounds.height+deltaY;
                frame.setCursor(Cursor.getPredefinedCursor(
                                            Cursor.NE_RESIZE_CURSOR));
                break;
            case EAST:      
                if (startingBounds.width-deltaX < min.width)
                    deltaX = startingBounds.width-min.width;
                else if (startingBounds.width-deltaX > max.width)
                    deltaX = startingBounds.width-max.width;
                if (startingBounds.x+startingBounds.width-deltaX >
                        parentBounds.width)
                    deltaX = startingBounds.x+startingBounds.width -
                            parentBounds.width;
                newW = startingBounds.width-deltaX;
                newH = startingBounds.height;
                frame.setCursor(Cursor.getPredefinedCursor(
                                    Cursor.E_RESIZE_CURSOR));
                break;
            case SOUTH_EAST:     
                if (startingBounds.width-deltaX < min.width)
                    deltaX = startingBounds.width-min.width;
                else if (startingBounds.width-deltaX > max.width)
                    deltaX = startingBounds.width-max.width;
                if (startingBounds.x+startingBounds.width-deltaX >
                        parentBounds.width)
                    deltaX = startingBounds.x+startingBounds.width-
                            parentBounds.width;
                if (startingBounds.height-deltaY < min.height)
                    deltaY = startingBounds.height-min.height;
                else if (startingBounds.height-deltaY > max.height)
                    deltaY = -(max.height-startingBounds.height);
                if (startingBounds.y+startingBounds.height-deltaY >
                        parentBounds.height) 
                    deltaY = startingBounds.y+startingBounds.height-
                            parentBounds.height ;   
                newW = startingBounds.width-deltaX;
                newH = startingBounds.height-deltaY;
                frame.setCursor(Cursor.getPredefinedCursor(
                                            Cursor.SE_RESIZE_CURSOR));
                break;
            case SOUTH:      
                if (startingBounds.height-deltaY < min.height)
                    deltaY = startingBounds.height-min.height;
                else if (startingBounds.height-deltaY > max.height)
                    deltaY = -(max.height-startingBounds.height);
                if (startingBounds.y+startingBounds.height-deltaY >
                        parentBounds.height)
                    deltaY = startingBounds.y+startingBounds.height-
                            parentBounds.height ;
                newW = startingBounds.width;
                newH = startingBounds.height-deltaY;
                frame.setCursor(Cursor.getPredefinedCursor(
                            Cursor.S_RESIZE_CURSOR));
                break;
            case SOUTH_WEST:
                if (startingBounds.height-deltaY < min.height)
                    deltaY = startingBounds.height - min.height;
                else if (startingBounds.height-deltaY > max.height)
                    deltaY = -(max.height-startingBounds.height);
                if (startingBounds.y+startingBounds.height-deltaY >
                        parentBounds.height)
                    deltaY = startingBounds.y+startingBounds.height-
                                parentBounds.height ;
                if (startingBounds.width+deltaX < min.width)
                    deltaX = -(startingBounds.width-min.width);
                else if (startingBounds.width+deltaX > max.width)
                    deltaX = max.width-startingBounds.width;
                if (startingBounds.x-deltaX < 0) deltaX = startingBounds.x;

                newX = startingBounds.x-deltaX;
                newY = startingBounds.y;
                newW = startingBounds.width+deltaX;
                newH = startingBounds.height-deltaY;
                frame.setCursor(Cursor.getPredefinedCursor(
                                Cursor.SW_RESIZE_CURSOR));
                break;
            case WEST:      
                if (startingBounds.width+deltaX < min.width)
                    deltaX = -(startingBounds.width-min.width);
                else if (startingBounds.width+deltaX > max.width)
                    deltaX = max.width-startingBounds.width;
                if (startingBounds.x-deltaX < 0) deltaX = startingBounds.x;
                
                newX = startingBounds.x-deltaX;
                newY = startingBounds.y;
                newW = startingBounds.width+deltaX;
                newH = startingBounds.height;
                frame.setCursor(Cursor.getPredefinedCursor(
                                        Cursor.W_RESIZE_CURSOR));
                break;
            case NORTH_WEST:     
                if (startingBounds.width+deltaX < min.width)
                    deltaX = -(startingBounds.width-min.width);
                else if (startingBounds.width+deltaX > max.width)
                    deltaX = max.width-startingBounds.width;
                if (startingBounds.x-deltaX < 0) deltaX = startingBounds.x;

                if (startingBounds.height+deltaY < min.height)
                    deltaY = -(startingBounds.height-min.height);
                else if (startingBounds.height+deltaY > max.height)
                    deltaY = max.height-startingBounds.height;
                if (startingBounds.y-deltaY < 0) deltaY = startingBounds.y;
                newX = startingBounds.x-deltaX;
                newY = startingBounds.y-deltaY;
                newW = startingBounds.width+deltaX;
                newH = startingBounds.height+deltaY;
                frame.setCursor(Cursor.getPredefinedCursor(
                                Cursor.NW_RESIZE_CURSOR));
                break;
            default: return;
        }
        //frame.setBounds(newX, newY, newW, newH);
        getDesktopManager().resizeFrame(frame, newX, newY, newW, newH);
    }

    /** 
     * Handles <code>mouseMoved</code> event. 
     * @see MouseInputAdapter#mouseMoved(MouseEvent)
     */
    public void mouseMoved(MouseEvent e)   
    {
        if (!frame.isResizable()) return;
    
        JComponent titleBar = frame.getTitleBar();
        if (e.getSource() == frame || e.getSource() == titleBar) {
            Point ep = new Point(e.getX(), e.getY());
            if (e.getSource() == titleBar) {
                Point np = titleBar.getLocation();
                ep.x += np.x;
                ep.y += np.y;
                frame.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
            }
            Insets i = frame.getInsets();
            if (ep.x <= i.left) {
                if (ep.y < resizeCornerSize+i.top)
                    frame.setCursor(Cursor.getPredefinedCursor(
                                    Cursor.NW_RESIZE_CURSOR));
                else if (ep.y > frame.getHeight()-resizeCornerSize-i.bottom)
                    frame.setCursor(Cursor.getPredefinedCursor(
                                    Cursor.SW_RESIZE_CURSOR));
                else frame.setCursor(Cursor.getPredefinedCursor(
                                    Cursor.W_RESIZE_CURSOR));
            } else if (ep.x >= frame.getWidth()-i.right) {
                if (e.getY() < resizeCornerSize+i.top)
                    frame.setCursor(Cursor.getPredefinedCursor(
                                    Cursor.NE_RESIZE_CURSOR));
                else if (ep.y > frame.getHeight()-resizeCornerSize-i.bottom)
                    frame.setCursor(Cursor.getPredefinedCursor(
                                    Cursor.SE_RESIZE_CURSOR));
                else                
                    frame.setCursor(Cursor.getPredefinedCursor(
                                    Cursor.E_RESIZE_CURSOR));
            } else if (ep.y <= i.top) {
                if (ep.x < resizeCornerSize+i.left)
                    frame.setCursor(Cursor.getPredefinedCursor(
                                    Cursor.NW_RESIZE_CURSOR));
                else if (ep.x > frame.getWidth()-resizeCornerSize-i.right)
                    frame.setCursor(Cursor.getPredefinedCursor(
                                    Cursor.NE_RESIZE_CURSOR));
                else                
                    frame.setCursor(Cursor.getPredefinedCursor(
                                    Cursor.N_RESIZE_CURSOR));
            } else if(ep.y >= frame.getHeight()-i.bottom) {
                if (ep.x < resizeCornerSize+i.left)
                    frame.setCursor(Cursor.getPredefinedCursor(
                                    Cursor.SW_RESIZE_CURSOR));
                else if (ep.x > frame.getWidth()-resizeCornerSize-i.right)
                    frame.setCursor(Cursor.getPredefinedCursor(
                                    Cursor.SE_RESIZE_CURSOR));
                else                
                    frame.setCursor(Cursor.getPredefinedCursor(
                                    Cursor.S_RESIZE_CURSOR));
            } else {
                frame.setCursor(Cursor.getPredefinedCursor(
                       Cursor.DEFAULT_CURSOR));
                return;
            }
        } 
    }

    /**
     * Sets the default cursor.
     * @see MouseInputAdapter#mouseExited(MouseEvent)
     */
    public void mouseExited(MouseEvent e)
    {
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); 
    }
    
    /**
     * Required by the interface but no-op implementation in our case.
     * @see MouseInputAdapter#mouseClicked(MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {}
    
}


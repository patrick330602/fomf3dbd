/*
 * org.openmicroscopy.shoola.agents.treeviewer.finder.FinderUI
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

package org.openmicroscopy.shoola.agents.treeviewer.finder;



//Java imports
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;


//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.treeviewer.IconManager;
import org.openmicroscopy.shoola.util.ui.HistoryDialog;

/** 
 * The UI delegate of the {@link Finder}.
 * 
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision$Date: )
 * </small>
 * @since OME2.2
 */
class FinderUI
	extends JPanel
{

    /** The message displayed when the phrase isn't found. */
    private static final String NO_PHRASE_MSG = "Phrase not found.";
    
    /** The message displayed when the phrase is found for than one time. */
    private static final String OCCURENCES_MSG = " occurences found";
    
    /** The message displayed when the phrase is found 0 or 1 time. */
    private static final String OCCURENCE_MSG = " occurence found";
    
    /** The horizontal space. */
    private static final Dimension H_SPACE_DIM = new Dimension(10, 5);
    
    /** The default width of the {@link #findArea}. */
    private static final int WIDTH = 100;
    
    /** The component that owns this UI delegate. */
    private Finder 			finder;
    
    /** Reference to the Controller. */
    private FinderControl	controller;
    
    /** Reference to the Model. */
    private FinderModel		model;
    
    /** The text area hosting the pattern to find. */
    private JTextField      findArea;
    
    /** Check box to match the case or not. */
    private JCheckBox		caseSensitive;
    
    /** The tool bar hosting the controls. */
    private JToolBar		controlsBar;
    
    /** The tool bar hosting the controls. */
    private JToolBar		leftBar;
    
    /** The popup menu. */
    private PopupMenu		popupMenu;
    
    /** The panel hosting information. */
    private JPanel			infoComponent;
    
    /** Initializes the components composing the display. */
    private void initComponents()
    {
        createMenuBar();
        createLeftMenuBar();
        caseSensitive = new JCheckBox("Match case");
        caseSensitive.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JCheckBox source = (JCheckBox) e.getSource();
                finder.setCaseSensitive(source.isSelected());
            }
        });
        infoComponent = new JPanel();
        findArea = new JTextField();
        findArea.setBorder(BorderFactory.createBevelBorder(
                			BevelBorder.LOWERED));
        findArea.setBackground(Color.WHITE);
        findArea.setOpaque(true);
        findArea.setEditable(true);
        int h = getFontMetrics(getFont()).getHeight()+4;
        findArea.setPreferredSize(new Dimension(WIDTH, h));
        findArea.addKeyListener(new KeyAdapter() {

            /** Finds the phrase. */
            public void keyPressed(KeyEvent e)
            {
                if ((e.getKeyCode() == KeyEvent.VK_ENTER)) {
                    Object source = e.getSource();
                    if (source instanceof JTextField) {
                        JTextField field = (JTextField) source;
                        if (field.getText() != null && 
                                field.getText().length() > 0)
                            finder.find();
                    }
                }
            }
        });
        findArea.getDocument().addDocumentListener(new DocumentListener() {
            
            /**
             * Sets the finder's controls enabled if no phrase entered.
             * @see DocumentListener#removeUpdate(DocumentEvent)
             */
            public void insertUpdate(DocumentEvent de)
            {
                Document d = de.getDocument();
                try {
                    finder.setTextUpdate(d.getText(0, d.getLength()));
                } catch (Exception e) {}
            }

            /**
             * Sets the finder's controls enabled if no phrase entered.
             * @see DocumentListener#removeUpdate(DocumentEvent)
             */
            public void removeUpdate(DocumentEvent de)
            { 
                Document d = de.getDocument();
                try {
                    finder.setTextUpdate(d.getText(0, d.getLength()));
                } catch (Exception e) {}
            }

            /** 
             * Required by I/F but no-op implementation in our case. 
             * @see DocumentListener#changedUpdate(DocumentEvent)
             */
            public void changedUpdate(DocumentEvent de) {}
            
        });
        findArea.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e)
            {
                if (findArea.getDocument().getLength() == 0) {
                    String[] h = model.getHistory();
                    if (h.length != 0) {
                        Rectangle r = findArea.getBounds();
                        HistoryDialog d = new HistoryDialog(h, r.width);
                        d.show(findArea, 0, r.height);
                        d.addPropertyChangeListener(
                                HistoryDialog.SELECTION_PROPERTY, controller);
                    }   
                }
            }
        });
    }
    
    /** Helper method to create the menu bar. */
    private void createMenuBar()
    {
        leftBar = new JToolBar();
        leftBar.setBorder(null);
        leftBar.setRollover(true);
        leftBar.setFloatable(false);
        leftBar.add(new JButton(controller.getAction(FinderControl.CLOSE)));
    }
    
    /** Helper method to create the menu bar. */
    private void createLeftMenuBar()
    {
        controlsBar = new JToolBar();
        controlsBar.setBorder(null);
        controlsBar.setRollover(true);
        controlsBar.setFloatable(false);
        JButton button = new JButton(
                controller.getAction(FinderControl.FIND_NEXT));	
        controlsBar.add(button);
        button = new JButton(
                controller.getAction(FinderControl.FIND_PREVIOUS));	
        controlsBar.add(button);
        button = new JButton(
                controller.getAction(FinderControl.HIGHLIGHT));	
        button = new JButton(
                controller.getAction(FinderControl.FILTER_MENU));
        button.addMouseListener(
            (FilterMenuAction) controller.getAction(FinderControl.FILTER_MENU));
        controlsBar.add(button);
    }
    
    /** Builds and lays out the GUI. */
    private void buildGUI()
    {
        JPanel p = new JPanel();
        p.add(leftBar);
        p.add(Box.createRigidArea(H_SPACE_DIM));
        p.add(new JLabel("Find: "));
        p.add(findArea);
        p.add(controlsBar);
        p.add(caseSensitive);
        p.add(infoComponent);
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        add(p);
    }
    
    /**
     * Creates a new instance. 
     * 
     * @param finder 	Reference to the component that owns this UI delegate.
     * 					Mustn't be <code>null</code>.
     * @param controller Reference to the Controller.
     * 					Mustn't be <code>null</code>.
     * @param model		Reference to the Model.
     * 					Mustn't be <code>null</code>.
     */
    FinderUI(Finder finder, FinderControl controller, FinderModel model)
    {
        if (finder == null)
            throw new IllegalArgumentException("No component.");
        if (controller == null)
            throw new IllegalArgumentException("No controller.");
        if (model == null)
            throw new IllegalArgumentException("No model.");
        this.model = model;
        this.finder = finder;
        this.controller = controller;
        initComponents();
        popupMenu = new PopupMenu(finder);
        buildGUI();
    }

    /**
     * Brings up the popup menu on top of the specified component at the
     * specified point.
     * 
     * @param c The component that requested the popup menu.
     * @param p The point at which to display the menu, relative to the
     *            <code>component</code>'s coordinates.
     *  
     */
    void showPopup(Component c, Point p) { popupMenu.show(c, p.x, p.y); }
    
    /**
     * Displays a message depending on the value of the specified parameter.
     * 
     * @param n The number of found nodes.
     */
    void setMessage(int n)
    {
        infoComponent.removeAll();
        if (n == 0) {
            IconManager im = IconManager.getInstance();
            JLabel icon = new JLabel(im.getIcon(IconManager.WARNING));
            infoComponent.add(icon);
            JLabel label = new JLabel(NO_PHRASE_MSG); 
            icon.setLabelFor(label);
            infoComponent.add(label);
        } else {
            IconManager im = IconManager.getInstance();
            JLabel icon = new JLabel(im.getIcon(IconManager.HIGHLIGHT));
            infoComponent.add(icon);
            JLabel label;
            if (n > 1) label = new JLabel(n+OCCURENCES_MSG); 
            else label = new JLabel(n+OCCURENCE_MSG);
            icon.setLabelFor(label);
            infoComponent.add(label);
        }
        validate();
        repaint();
    }
    
    /**
     * Sets the text to find.
     * 
     * @param text The text to find.
     */
    void setTextToFind(String text)
    {
        findArea.setText(text);
    }
    
}

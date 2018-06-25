/*
 * org.openmicroscopy.shoola.util.ui.colourpicker.ColourSlider
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
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;


//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.util.ui.colour.HSV;

/** 
 * ColourSlider is a derived class of JSlider which replaces the track with a 
 * colour spectrum, either from one RGB value to another or one HSV value to 
 * another. In this case the HSV normally just adjusts the V, to show a change
 * in value. 
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author	Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * 	<a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $ $Date: $)
 * </small>
 * @since OME2.2
 */

public class ColourSlider 
	extends JSlider
{	
	
	/**
	 * The UI delegate of the colour slider. This will draw the colour track
	 * overriding the {@link BasicSliderUI#paintTrack(Graphics g)} method.
	 */
	private ColourSliderUI 	ui;
	
	/**
	 * Static variable used to determine which colour space the user wishes to 
	 * use for the  slider. 
	 */
	final static int		HSV_COLOURSPACE = 1;

	/**
	 * Static variable used to determine which colour space the user wishes to 
	 * use for the  slider. 
	 */
	final static int		RGB_COLOURSPACE = 0;	
	
	/**
	 * Static variable used to determine which channel the user wishes to 
	 * use for the  gradient of the HSV slider. 
	 */
	final static int		HSV_CHANNEL_HUE = 0;
	
	/**
	 * Static variable used to determine which channel the user wishes to 
	 * use for the  gradient of the HSV slider. 
	 */
	final static int		HSV_CHANNEL_SATURATION = 1;
	
	/**
	 * Static variable used to determine which channel the user wishes to 
	 * use for the  gradient of the HSV slider. 
	 */
	final static int		HSV_CHANNEL_VALUE = 2;
	
	/**
	 * Initializes the slider.
	 * 
	 * @param min	The minimum value allowed.
	 * @param max	The maximum value allowed.
	 */
	private void initialize(int min, int max) 
	{
		setPaintTrack(true);
		setPaintTicks(true);
		setMinimum(min);
		setMaximum(max);
		setUI(ui);
		setFocusable(false);
	}
	
	/**
	 * Constructor of the ColourSlider. This will set the min and max values of
	 * the slider, as well as the start and end colours for the gradient fill 
	 * of the {@link ColourSliderUI#paintTrack(Graphics g)}. This method takes 
	 * RGB colours and so specifies {@link #RGB_COLOURSPACE} in the constructor.  
	 * 
	 * @param min minimum value allowed.
	 * @param max maximum value allowed.
	 * @param s   start colour.
	 * @param e   end colour.
	 */
	ColourSlider(int min, int max, Color s, Color e)
	{
		ui = new ColourSliderUI(this, s, e);
		//ui.setColourSpace(RGB_COLOURSPACE);
		initialize(min, max);
	}
	
	/**
	 * Constructor of the ColourSlider. This sets the min and max values of
	 * the slider, as well as the start and end colours for the gradient fill 
	 * of the {@link ColourSliderUI#paintTrack(Graphics g)}. This method 
	 * takes HSV colours and so specifies {@link #HSV_COLOURSPACE}
	 * in the constructor.  
	 * 
	 * @param min minimum value allowed.
	 * @param max maximum value allowed.
	 * @param s   start colour using the HSV helper class.
	 * @param e   end colour using the HSV helper class.
	 */
	ColourSlider(int min, int max, HSV s, HSV e)
	{
		ui = new ColourSliderUI(this, s, e);
		//ui.setColourSpace(HSV_COLOURSPACE);
		initialize(min, max);
	}
	
	/**
	 * Sets the current colourspace of the Slider. 
	 * 
	 * @param CS Colourspace, should be either {@link #RGB_COLOURSPACE} or
	 * {@link #HSV_COLOURSPACE}.
	 */
	public void setColourSpace(int CS) { ui.setColourSpace(CS); }

	/**
	 * Returns the current colourspace of the Slider. 
	 * 
	 * @return Colourspace, should be either {@link #RGB_COLOURSPACE} or
	 * {@link #HSV_COLOURSPACE}.
	 */
	public int getColourSpace() { return ui.getColourSpace(); }
		
	/**
	 * Sets the current start of the RGB Colour for gradient fill to c.
	 * 
	 * @param c Colour to be used for start of gradient fill. 
	 */
	public void setRGBStart(Color c) { ui.setRGBStart(c); }
	
	/**
	 * Sets the current end of the RGB Colour for gradient fill to c.
	 * 
	 * @param c Colour to be used for end of gradient fill. 
	 */
	public void setRGBEnd(Color c) { ui.setRGBEnd(c); }
	
	/**
	 * Sets the current start of the HSV Colour for gradient fill to c.
	 * 
	 * @param c HSV colour to be used for start of gradient fill. 
	 */
	public void setHSVStart(HSV c) { ui.setHSVStart(c); }
	
	/**
	 * Sets the current end of the HSV Colour for gradient fill to c.
	 * 
	 * @param c HSV Colour to be used for end of gradient fill. 
	 */
	public void setHSVEnd(HSV c) { ui.setHSVEnd(c); }
	
	/** 
	 * Sets the channel you wish the gradient to be set on; Hue, saturation or
	 * Value.
     * 
	 * @param channel The value to set.
	 */
	public void setChannel(int channel) { ui.setChannel(channel); }
	
	/**
	 * Sets the current start of the HSV Colour for gradient fill to c.
     * 
	 * @param c colour to be used for start of gradient fill. 
	 */
	public void setHSVStart(Color c) { ui.setHSVStart(c); }
	
	/**
	 * Sets the current end of the HSV Colour for gradient fill to c.
	 * 
	 * @param c colour to be used for end of gradient fill. 
	 */
	public void setHSVEnd(Color c) { ui.setHSVEnd(c); }
	
}

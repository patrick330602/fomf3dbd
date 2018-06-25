/*
 * org.openmicroscopy.shoola.agents.metadata.rnd.GraphicsPaneUI 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2009 University of Dundee. All rights reserved.
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
package org.openmicroscopy.shoola.agents.metadata.rnd;



//Java imports
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.metadata.IconManager;

/** 
 * Component displaying the plane histogram.
 * 
 * @author Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp; <a
 *         href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author Andrea Falconi &nbsp;&nbsp;&nbsp;&nbsp; <a
 *         href="mailto:a.falconi@dundee.ac.uk">a.falconi@dundee.ac.uk</a>
 * @author Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp; <a
 *         href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since 3.0-Beta4
 */
class GraphicsPaneUI 
	extends JPanel 
{
 
	/** Color used to draw a line to indicate the selected value. */
	private final static Color			LINECOLOUR = Color.BLACK;
	
	/** The color used to grey out the non selected area. */
	private final static Color			GREYCOLOUR = new Color(196, 196, 196, 
			128);

	/** The color of the filled background area. */
	private final static Color      	FILLCOLOUR = new Color(255, 255, 255);

	/** The color of the border of the histogram. */
	private final static Color      	BORDERCOLOUR = new Color(224, 209, 207);

	/** The curve's stroke. */
	private final static BasicStroke	STROKE1_5 = new BasicStroke(1.5f);

	/** The border stroke. */
	private final static BasicStroke 	STROKE2 = new BasicStroke(2.0f);

	/** A temporary image of a histogram */
	private ImageIcon       histogramImage;

	/** A reference to the model. */
	private RendererModel   model;

	/** A reference to the model. */
	private GraphicsPane   	view;

	/** 
	 * Creates a new instance. 
	 * 
	 * @param view 	Reference to the view.
	 * @param model Reference to the model. Mustn't be <code>null</code>.
	 */
	GraphicsPaneUI(GraphicsPane view, RendererModel model)
	{
		if (view == null) throw new IllegalArgumentException("No view.");
		if (model == null) throw new IllegalArgumentException("No model.");
		this.model = model;
		this.view = view;
		IconManager icons = IconManager.getInstance();
		histogramImage = icons.getImageIcon(IconManager.TEMPORARY_HISTOGRAM);
	}

	/**
	 * Overridden to paint the histogram image.
	 * @see JPanel#paintComponent(Graphics)
	 */
	public void paintComponent(Graphics og)
	{
		super.paintComponent(og);
		Graphics2D g = (Graphics2D) og;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		double width = getWidth();
		double height = getHeight();
		g.setColor(FILLCOLOUR);
		g.fillRect(1, 1, (int) width-1, (int) height-1);
		g.setColor(BORDERCOLOUR);
		g.setStroke(STROKE2);
		g.drawRect(0, 0, (int) width-1, (int) height-1);

		if (histogramImage != null)
			g.drawImage(histogramImage.getImage(), 0, 0, (int) width-1, 
					(int) height-1, null);

		double codomainMin = model.getCodomainStart();
		double codomainMax = model.getCodomainEnd();
		//double domainGlobalMin = model.getGlobalMin();
		//double domainGlobalMax = model.getGlobalMax();
		double domainGlobalMin = view.getPartialMinimum();
		double domainGlobalMax = view.getPartialMaximum();
		double domainMin = model.getWindowStart();
		double domainMax = model.getWindowEnd();
		//Added jmarie 03/10/07
		if (domainMin < domainGlobalMin) domainMin = domainGlobalMin;
		if (domainMax > domainGlobalMax) domainMax = domainGlobalMax;
		double domainMinScreenX = ((domainMin-domainGlobalMin)/
				(domainGlobalMax-domainGlobalMin))*width;
		double domainMaxScreenX = ((domainMax-domainGlobalMin)/
				(domainGlobalMax-domainGlobalMin))*width;
		double codomainMinScreenY = ((255-codomainMin)/255.0f)*height;
		double codomainMaxScreenY = ((255-codomainMax)/255.0f)*height;
		double domainRangeScreen = domainMaxScreenX-domainMinScreenX;
		double codomainRangeScreen = codomainMinScreenY-codomainMaxScreenY;

		g.setColor(GREYCOLOUR);

		g.fillRect(0, 0, (int) domainMinScreenX+1, (int) height);
		g.fillRect((int) domainMaxScreenX-1, 0, (int) width, (int) height);

		g.fillRect(0, 0, (int) domainMinScreenX, (int) height);
		g.fillRect((int) domainMaxScreenX, 0, (int) width, (int) height);

		g.fillRect(0, 0, (int) width, (int) codomainMaxScreenY);
		g.fillRect(0, (int) codomainMinScreenY, (int) width, (int) height);

		String family = model.getFamily();
		double k = model.getCurveCoefficient();

		double a = 0;
		if (family.equals(RendererModel.LINEAR)) 
			a = codomainRangeScreen/domainRangeScreen;
		else if (family.equals(RendererModel.POLYNOMIAL)) 
			a = codomainRangeScreen/Math.pow(domainRangeScreen,	k);
		else if (family.equals(RendererModel.EXPONENTIAL)) 
			a = codomainRangeScreen/Math.exp(Math.pow(domainRangeScreen, k));
		else if (family.equals(RendererModel.LOGARITHMIC)) {
			if (domainRangeScreen <= 1) domainRangeScreen = 1;
			a = codomainRangeScreen/Math.log(domainRangeScreen);
		}	

		double b = codomainMinScreenY;
		double currentX = domainMinScreenX-1;
		double currentY = b;
		double oldX, oldY;

		g.setColor(model.getChannelColor(model.getSelectedChannel()));
		g.setStroke(STROKE1_5);

		for (double x = 1; x < domainRangeScreen; x += 1) {
			oldX = currentX;
			oldY = currentY;
			currentX = x+domainMinScreenX-1;
			if (family.equals(RendererModel.LINEAR)) 
				currentY = b-a*x;
			else if (family.equals(RendererModel.EXPONENTIAL)) 
				currentY = b-a*Math.exp(Math.pow(x,	k));
			else if (family.equals(RendererModel.POLYNOMIAL)) 
				currentY = b-a*Math.pow(x, k);
			else if (family.equals(RendererModel.LOGARITHMIC)) 
				currentY = b-a*Math.log(x);
			g.drawLine((int) oldX, (int) oldY, (int) currentX,
					(int) currentY);
		}
		if (view.isPaintLine()) {
			int vertical = view.getVerticalLine();
			g.setColor(LINECOLOUR);
			double v;
			if (view.paintVertical()) {
				v = ((vertical-domainGlobalMin)/
						(domainGlobalMax-domainGlobalMin))*width;
				g.drawLine((int) v, 0, (int) v, (int) height);
			}
			int horizontal = view.getHorizontalLine();
			if (view.paintHorizontal()) {
				v = ((255-horizontal)/255.0f)*height;
				g.drawLine(0, (int) v, (int) width, (int) v);
			} 
		}
	}
	
}

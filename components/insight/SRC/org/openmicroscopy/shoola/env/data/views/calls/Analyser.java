/*
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2015 University of Dundee. All rights reserved.
 *
 *
 *  This program is free software; you can redistribute it and/or modify
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
package org.openmicroscopy.shoola.env.data.views.calls;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import omero.gateway.SecurityContext;

import org.openmicroscopy.shoola.env.data.views.BatchCall;
import org.openmicroscopy.shoola.env.data.views.BatchCallTree;
import org.openmicroscopy.shoola.env.rnd.roi.ROIAnalyser;
import org.openmicroscopy.shoola.util.roi.model.ROIShape;
import org.openmicroscopy.shoola.util.roi.model.util.Coord3D;

import omero.gateway.model.PixelsData;

/** 
 * Retrieves the raw pixels data and creates an analyser.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * @since OME3.0
 */
public class Analyser
	extends BatchCallTree
{

	/** The pixels set to analyze. */
	private final PixelsData pixels;
	
	/** Collection of active channels. */
	private final Collection channels; 
	
	/** The result of the call. */
    private Object result;
    
    /** Loads the specified experimenter groups. */
    private BatchCall loadCall;
    
    /** The plane to analyze the shapes for */
    private Coord3D plane;
    
    /**
     * Creates a {@link BatchCall} to analyze the specified shapes.
     * 
     * @param ctx The security context.
     * @param shapes Collection of shapes to analyze.
     * @return The {@link BatchCall}.
     */
    private BatchCall analyseShapes(final SecurityContext ctx,
    		final ROIShape[] shapes)
    {
    	return new BatchCall("Analysing shapes") {
    		            public void doCall() throws Exception
            {
            	ROIAnalyser analyser = new ROIAnalyser(context.getGateway(), pixels);
            	try {
            		result = analyser.analyze(ctx, shapes, channels, plane);
				} catch (Exception e) {
				}
            }
        };
    }
    
    /**
     * Adds the {@link #loadCall} to the computation tree.
     * @see BatchCallTree#buildTree()
     */
    protected void buildTree() { add(loadCall); }

    /**
     * Returns, in a <code>Map</code>.
     * 
     * @see BatchCallTree#getResult()
     */
    protected Object getResult() { return result; }
    
    /**
     * Creates a new instance.
     * 
     * @param ctx The security context.
     * @param pixels	The pixels set to analyze.
     * @param channels	Collection of active channels. 
     * 					Mustn't be <code>null</code>.
     * @param shapes	Collection of shapes to analyze. 
     * 					Mustn't be <code>null</code>.
     * @param plane     The plane to analyze the shapes for, can be <code>null</code>
     */
    public Analyser(SecurityContext ctx, PixelsData pixels, Collection channels,
    		List shapes, Coord3D plane)
    {
    	if (pixels == null) 
    		throw new IllegalArgumentException("No Pixels specified."); 
    	if (channels == null || channels.size() == 0)
			throw new IllegalArgumentException("No channels specified.");
		if (shapes == null || shapes.size() == 0)
			throw new IllegalArgumentException("No shapes specified.");
		this.pixels = pixels;
    	this.channels = channels;
    	this.plane = plane;
    	Iterator i = shapes.iterator();
    	ROIShape[] data = new ROIShape[shapes.size()];
    	int index = 0;
    	while (i.hasNext()) {
			data[index] = (ROIShape) i.next();
			index++;
		}
		loadCall = analyseShapes(ctx, data);
    }
    
}

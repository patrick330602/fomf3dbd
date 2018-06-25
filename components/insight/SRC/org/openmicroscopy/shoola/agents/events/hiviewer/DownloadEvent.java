/*
 *------------------------------------------------------------------------------
 *  Copyright (C) 2015-2016 University of Dundee. All rights reserved.
 *
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */
package org.openmicroscopy.shoola.agents.events.hiviewer;

import java.io.File;
import java.util.List;
import omero.gateway.model.DataObject;
import org.openmicroscopy.shoola.env.event.RequestEvent;


/**
 * Event posted to download image.
 * @author Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @since 5.1
 */
public class DownloadEvent
    extends RequestEvent
{

    /** The folder where to download the files.*/
    private File folder;

    /**  Flag indicating to override the existing file if it exists.*/
    private boolean override;

    /** The objects to download */
    private List<DataObject> selection;
    
    /**
     * Creates a new instance.
     *
     * @param folder
     *            where to download the objects
     * @param override
     *            Indicate to override or not the name if it already exits.
     * @param selection
     *            The objects to download (can be <code>null</code> in which case
     *            the receiver of this event has to figure it out itself)
     */
    public DownloadEvent(File folder, boolean override,
            List<DataObject> selection) {
        this.folder = folder;
        this.override = override;
        this.selection = selection;
    }

    /**
     * Returns <code>true</code> if the existing file can be overridden,
     * <code>false</code> otherwise.
     *
     * @return See above
     */
    public boolean isOverride() { return override; }

    /**
     * Returns the folder where to download the files.
     *
     * @return See above.
     */
    public File getFolder() { return folder; }

    /**
     * Get the objects to download
     * @return See above
     */
    public List<DataObject> getSelection() {
        return selection;
    }
    
    
}

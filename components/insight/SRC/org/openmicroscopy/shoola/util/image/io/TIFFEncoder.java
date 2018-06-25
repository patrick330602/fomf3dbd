/*
 * org.openmicroscopy.shoola.util.image.io.TIFFEncoder
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

package org.openmicroscopy.shoola.util.image.io;

//Java imports
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.DataOutputStream;
import java.io.IOException;

//Third-party libraries

//Application-internal dependencies

/** 
 * Saves a buffered image as an uncompressed, big-Endian TIFF.
 * We use the band interleaving model to retrieve the pixel value.

 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author  <br>Andrea Falconi &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:a.falconi@dundee.ac.uk">
 * 					a.falconi@dundee.ac.uk</a>
 * @version 2.2 
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
public class TIFFEncoder 
	extends Encoder
{

    /** The number of bits per sample. */
	private int 				bitsPerSample;
    
    /** The number of samples per pixel. */
    private int                 samplesPerPixel;
    
    /** The number of entries. */
    private int                 nEntries;
    
    /** 
     * The space for the photometric interpretation, default value is
     * <code>1</code>.
     */
	private int					photoInterp;
    
    /** The space of the <code>image File Directory</code>. */
    private int                 ifdSize;
    
    /** The value identifying the image's size. */
    private int                 imageSize;
    
    /**
     * Writes the 6 bytes of data required by the RGB BitsPerSample tag. 
     * 
     * @throws IOException Exception thrown if an error occurred during the
     * encoding process.
     */
    private void writeBitsPerPixel()
        throws IOException 
    {
        output.writeShort(8);
        output.writeShort(8);
        output.writeShort(8);
    }
    
    /**
     * Writes one Image File Directory. 
     * 
     * @throws IOException Exception thrown if an error occurred during the
     * encoding process.
     */
    private void writeIFD()
        throws IOException
    {
        int tagDataOffset = TIFFEncoderCst.HDR_SIZE + ifdSize;
        output.writeShort(nEntries);
        writeEntry(TIFFEncoderCst.NEW_SUBFILE_TYPE, 4, 1, 0);
        writeEntry(TIFFEncoderCst.IMAGE_WIDTH, 3, 1, imageWidth);
        writeEntry(TIFFEncoderCst.IMAGE_LENGTH, 3, 1, imageHeight);
        if (colorType == ColorSpace.TYPE_RGB) {
            writeEntry(TIFFEncoderCst.BITS_PER_SAMPLE,  3, 3, tagDataOffset);
            tagDataOffset += TIFFEncoderCst.BPS_DATA_SIZE;
        } else
            writeEntry(TIFFEncoderCst.BITS_PER_SAMPLE,  3, 1, bitsPerSample);

        writeEntry(TIFFEncoderCst.PHOTO_INTERP, 3, 1, photoInterp);
        writeEntry(TIFFEncoderCst.STRIP_OFFSETS, 4, 1,
                    TIFFEncoderCst.IMAGE_START);
        writeEntry(TIFFEncoderCst.SAMPLES_PER_PIXEL, 3, 1, samplesPerPixel);
        writeEntry(TIFFEncoderCst.ROWS_PER_STRIP,   3, 1, imageHeight);
        writeEntry(TIFFEncoderCst.STRIP_BYTE_COUNT, 4, 1, imageSize);
        
        //X resolution info, Y resolution info.. 
        writeEntry(TIFFEncoderCst.X_RESOLUTION, 5, 1, tagDataOffset);
        writeEntry(TIFFEncoderCst.Y_RESOLUTION, 5, 1, tagDataOffset+8);
        int unit = 2;   //TODO: support all unit
        writeEntry(TIFFEncoderCst.RESOLUTION_UNIT, 3, 1, unit);
        output.writeInt(0); // only one image
    }
    
    /**
     * Writes  one 12-byte IFD entry. 
     * 
     * @param tag The tag value.
     * @param fieldType The field type.
     * @param count
     * @param value
     * @throws IOException Exception thrown if an error occurred during the
     * encoding process.
     */
    private void writeEntry(int tag, int fieldType, int count, int value) 
        throws IOException
    {
        output.writeShort(tag);
        output.writeShort(fieldType);
        output.writeInt(count);
        if (count == 1 && fieldType == TIFFEncoderCst.SHORT)
            value <<= 16; //left justify 16-bit values
        output.writeInt(value); // may be an offset
    }
    
    /**
     * Writes the pixel value, band model. 
     * @throws IOException Exception thrown if an error occurred during the
     * encoding process.
     */
    private void writeRGBPixels()
        throws IOException
    {
        DataBuffer db = image.getRaster().getDataBuffer();
        if (db instanceof DataBufferByte)
            writeRGBDataBufferByte((DataBufferByte) db);
        else if (db instanceof DataBufferInt) 
            writeRGBDataBufferInt((DataBufferInt) db);
        
    }    
    
    /**
     * Writes the data for <code>int</code> values.
     * 
     * @param dbi   The buffer containing the data.
     * @throws IOException Exception thrown if an error occurred during the
     * encoding process.
     */
    private void writeRGBDataBufferInt(DataBufferInt dbi)
        throws IOException
    {
        int xres = image.getWidth();
        int yres = image.getHeight();
        int[] pixels = dbi.getData();
        byte[] bank = new byte[pixels.length];
        for (int iy = 0; iy < yres; iy++) {
            for (int ix = 0; ix < xres; ix++) {
                  int off = iy*xres+ix;
                  int pixel = pixels[off];
                  int r = (pixel>>16) & 0xff;
                  int g = (pixel>>8) & 0xff;
                  int b = pixel & 0xff;
                  pixel = (0xff<<24)|(r<<16)|(g<<8)|b;
                  bank[off] = (byte) pixel;
                }
        }
        output.write(bank, 0, bank.length);
    }
    
    /**
     * Writes the data for <code>byte</code> values.
     * 
     * @param bufferByte   The buffer containing the data.
     * @throws IOException Exception thrown if an error occurred during the
     * encoding process.
     */
    private void writeRGBDataBufferByte(DataBufferByte bufferByte)
        throws IOException
    {
        int bytesWritten = 0;
        int size = imageWidth*imageHeight*3;
        int count = imageWidth*24;      //3*8
        byte[] buffer = new byte[count];
        int i, j;
        byte[] red = bufferByte.getData(Encoder.RED_BAND);
        byte[] green = bufferByte.getData(Encoder.GREEN_BAND);
        byte[] blue = bufferByte.getData(Encoder.BLUE_BAND);
        while (bytesWritten < size) {
            if ((bytesWritten+count) > size)
                count = size-bytesWritten;
            j = bytesWritten/3;
            //TIFF save as BRG and not RGB.
            for (i = 0; i < count; i += 3) {
                buffer[i]   = red[j];
                buffer[i+1] = green[j];
                buffer[i+2] = blue[j];
                j++;
            }
            output.write(buffer, 0, count);
            bytesWritten += count;
        }
        writeColorMap(red, green, blue);
    }

    /**
     * Writes the color palette following the image. 
     * 
     * @param red The byte array for the red band.
     * @param green The byte array for the green band.
     * @param blue The byte array for the blue band.
     * @throws IOException Exception thrown if an error occurred during the
     * encoding process.
     */
    private void writeColorMap(byte[] red, byte[] green, byte[] blue)
        throws IOException
    {
        byte[] colorTable16 = new byte[TIFFEncoderCst.MAP_SIZE*2];
        int j = 0;
        int max = 251;
        //if (red.length < max) max = red.length;
        for (int i = 0 ; i < max; i++) {
            colorTable16[j] = red[i];
            colorTable16[512+j] = green[i];
            colorTable16[1024+j] = blue[i];
            j += 2;
        }
        output.write(colorTable16);
    }

    /**
     * Writes the scale. 
     *
     * @throws IOException Exception thrown if an error occurred during the
     * encoding process.
     */
    private void writeScale()
        throws IOException
    {
        double xscale = 1.0/imageWidth;
        double yscale = 1.0/imageHeight;
        double scale = 1000000.0;
        if (xscale > 1000.0) scale = 1000.0;
        output.writeInt((int) (xscale*scale));
        output.writeInt((int) scale);
        output.writeInt((int) (yscale*scale));
        output.writeInt((int) scale);
    }
    
    /**
     * Creates a new instance. 
     * 
     * @param image The image to encode. Mustn't be <code>null</code>.
     * @param output The output stream. Mustn't be <code>null</code>.
     */
    public TIFFEncoder(BufferedImage image, DataOutputStream output)
    {
        super(image, output);
    }
    
	/**
     * Writes an uncompressed big-endian TIFF. 
     * @see Encoder#write()
     */
	public void write()
		throws EncoderException
	{
        try {
            output.write(TIFFEncoderCst.header);
            writeIFD();
            int bpsSize = 0, scaleSize;
            if (colorType == ColorSpace.TYPE_RGB) {
                writeBitsPerPixel();
                bpsSize = TIFFEncoderCst.BPS_DATA_SIZE;
            }
            scaleSize = TIFFEncoderCst.SCALE_DATA_SIZE;
            writeScale();
            int size = TIFFEncoderCst.IMAGE_START-
                        (TIFFEncoderCst.HDR_SIZE+ifdSize+bpsSize+scaleSize);
            output.write(new byte[size]); // force image to start at offset 768
            writeRGBPixels();
        } catch (IOException e) {
            throw new EncoderException("Cannot encode the image.", e);
        }
	}
    
    /** 
     * Initializes the values needed by this encoder. 
     * @see Encoder#init()
     */
    protected void init()
    {
        bitsPerSample = 8;
        samplesPerPixel = 1;
        nEntries = 12;
        photoInterp = 1;
        ifdSize = 6+nEntries*12;
        int bytesPerPixel = 1;
        if (colorType  == ColorSpace.TYPE_RGB) {
            photoInterp = 2;
            samplesPerPixel = 3;
            bytesPerPixel = 3;
        }
        imageSize = imageWidth*imageHeight*bytesPerPixel;
    }
 
}

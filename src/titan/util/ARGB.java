/* traceformula@gmail.com */

package titan.util;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/* Class for getting pixels' values */
public class ARGB {
    private int width; //width of the image
    private int height; //height of the image
    private boolean hasAlpha; //whether the image has alpha channel
    private int noOfChannels; //number of channels
    private byte [] data;

    //generate from file path
    public static ARGB fromFilePath(String path) throws IOException{
        File file = new File(path);
        return fromFile(file);
    }

    //generate from file
    public static ARGB fromFile(File file) throws IOException{
        BufferedImage image = ImageIO.read(file);
        return new ARGB(image);
    }

    public ARGB(BufferedImage image){

        data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        width = image.getWidth(); //get width of the image
        height = image.getHeight(); //get height of the image

        hasAlpha = (image.getAlphaRaster() != null);
        if(hasAlpha)
            noOfChannels = 4; //Alpha, R, G, B
        else
            noOfChannels = 3;
    }

    public int getRGB(int posX, int posY, boolean includingAlpha){

        int p = (posY * width * noOfChannels) + (posX * noOfChannels);

        int argb = - 0xFFFFFF -1;

        if(includingAlpha){
            argb = ((int) data[p++] & 0xFF) << 24;
        }
        argb += ((int) data[p++] & 0xFF); //get blue value
        argb += ((int) data[p++] & 0xFF) << 8; //get green
        argb += ((int) data[p++] & 0xFF) <<16; //get red;
        return argb;
    }

    public int getRGB(int posX, int posY){
        return getRGB(posX, posY, hasAlpha);
    }

    //getter methods
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public byte[] getData(){
        return data;
    }
    public boolean hasAlpha(){
        return hasAlpha;
    }
}
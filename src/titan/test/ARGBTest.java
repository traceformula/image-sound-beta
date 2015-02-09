/* traceformula@gmail.com */
package titan.test;

import java.io.IOException;
import java.awt.image.BufferedImage;

public class ARGBTest implements TitanTest{

    public void run() throws IOException{
        titan.util.ARGB argb = titan.util.ARGB.fromFilePath("assets/download.jpg");
        System.out.println(argb.getRGB(argb.getWidth()/2, argb.getHeight()/2)); //-3372669
    }
}
/* traceformula@gmail.com */
package titan.controller;

import titan.util.ARGB;
import java.io.IOException;

public class Image {

    private ARGB data;
    private String imagePath = "";

    public Image (){

    }

    public void read(String path) throws IOException{
        
        imagePath = path;
        data = ARGB.fromFilePath(path);
    }

    public void imageToSound(){
        
    }
}
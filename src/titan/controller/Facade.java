/* traceformula@gmail.com */
package titan.controller;

/*
*   Facade class
*
*/
public class Facade {
    
    private Sound sound;
    private Image image;

    public Facade(){
        sound = new Sound();
        image = new Image();
    }

    /*
    *   Convert image to sound
    *
    */
    public void imageToSound(){
        
    }

    /*
    *   Check to see whether the tonal file input is correct
    *
    */
    public boolean checkTone(){

    }

    /*
    *   Get the image from path
    *
    */
    public void readImage(String path){
        image.read(path);
    }
}
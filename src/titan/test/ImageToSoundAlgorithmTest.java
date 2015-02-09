/* traceformula@gmail.com */

package titan.test;

import titan.algo.*;
import titan.util.ARGB;
import java.io.IOException;

public class ImageToSoundAlgorithmTest implements TitanTest{

    @Override
    public void run() throws IOException{
        TwelveToneAlgorithm a = new TwelveToneAlgorithm();
        ARGB image = ARGB.fromFilePath("assets/download.jpg");
        a.output(image, "");
        byte[] averages = a.getColAverages();
        byte[] deltas = a.getDeltas();
        int L = 1000;
        if(averages != null){
            for(int i = 0; i<averages.length; i++){
                if(i > L) break;
                System.out.print(averages[i] + " ");
            }
            System.out.println("");
            for(int i = 0; i<averages.length; i++){
                if(i > L) break;
                System.out.print(deltas[i] + " ");
            }
        }
    }
}
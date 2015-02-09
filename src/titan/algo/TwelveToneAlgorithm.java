/* traceformula@gmail.com */

package titan.algo;

public class TwelveToneAlgorithm extends ImageToSoundAlgorithm{

    private String name = "Twelve Tone";

    @Override
    public double deltaToNode(byte delta){
        if(delta < 4) return 4/3.0;
        if(delta < 35) return 1.0;
        if(delta < 66) return 2/3.0;
        if(delta < 97) return 0.5;
        if(delta < 128) return 1/3.0;
        if(delta < 159) return 0.25;
        if(delta < 190) return 1/6.0;
        if(delta < 221) return 0.125;
        if(delta < 252) return 1/12.0;
        return 0.0625;
    }

    @Override
    public int rgbToPitch(byte delta){
        return (int) Math.abs(Math.round((delta%60) + 36));
    }

    @Override
    public String getName(){
        return name;
    }
}
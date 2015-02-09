/* traceformul@gmail.com */

package titan.algo;

import titan.util.ARGB;
import titan.util.SoundUtil;

import javax.sound.midi.*;

public abstract class ImageToSoundAlgorithm {

    public static final int MP3 = 0;
    public static final int IN_MEMORY = 1; //store in memory, then play

    private byte[] deltas;
    private byte[] colAverages;
    private byte[] audioData;
    private Sequence sequence = null;

    //convert image to a sound file
    public void output(ARGB image, String path, int type){

        colAverages = colAverages(image);
        deltas = deltas(colAverages);

        convert(colAverages, deltas);
        //Convert to mp3 file
        if(type == MP3){
            toMP3(path);
        }
    }

    //default output sound file is mp3
    public void output(ARGB image, String path){
        output(image, path, IN_MEMORY);
    }

    public byte[] colAverages(ARGB image){

        int inc = image.hasAlpha()?4:3; //increment

        byte[] data = image.getData();
        byte[] averages = new byte[image.getWidth()];
        for(int i = 0; i<image.getWidth(); i++){

            long sumRed = 0;
            long sumGreen = 0;
            long sumBlue = 0;
            int s = i*inc;
            for(int j = 0; j<image.getHeight(); j++){
                sumRed += data[s];
                sumGreen += data[s+1];
                sumBlue += data[s+2];
            }
            averages[i] = 
             (byte) ((sumRed/image.getHeight() + sumGreen/image.getHeight() 
                +sumBlue/image.getHeight())/3);
        }

        return averages;
    }

    public byte[] deltas(byte[] averages){

        if(averages.length == 0) return null; //should rase exception
        byte[] deltas  = new byte[averages.length];

        deltas[0] = 0;
        for(int i = 1; i<deltas.length; i++){
            deltas[i] = (byte) Math.round(Math.abs(averages[i] - averages[i-1]));
        }
        return deltas;
    }

    //convert to sound data
    public void convert(byte[] averages, byte[] deltas){
        sequence = null;
        try
        {
            sequence = new Sequence(Sequence.PPQ, 1);
        }
        catch (InvalidMidiDataException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        Track   track = sequence.createTrack();

        int offset;
        int length;
        int tick = 0;
        for(int i = 0; i<colAverages.length; i++){
            offset = (int) (Math.abs(Math.round(rgbToPitch(colAverages[i]))));

            length = (int) (Math.round(deltaToNode(deltas[i]) * 64));
            track.add(SoundUtil.createNoteOnEvent(offset, tick++));
            
            //track.add(SoundUtil.createNoteOffEvent(offset, tick));
            
            System.out.print(offset + " ");
        }
    }

    public void toMP3(String path){
        //
    }

    public abstract double deltaToNode(byte delta);
    public abstract int rgbToPitch(byte average);
    public abstract String getName();

    public byte[] getColAverages(){
        return colAverages;
    }
    public byte[] getDeltas(){
        return deltas;
    }
    public byte[] getAudioData(){
        return audioData;
    }
    public Sequence getSequence(){
        return sequence;
    }
}
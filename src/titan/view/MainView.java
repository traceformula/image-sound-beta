/* traceformula@gmail.com */

package titan.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.*;
import java.nio.channels.*;
import java.nio.*;
import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.*;
import titan.algo.*;
import titan.util.*;
import javax.sound.midi.*;

public class MainView extends JFrame{

    final JButton generateBtn = new JButton("Generate");
    final JButton playBtn = new JButton("Play");
    final JButton stopBtn = new JButton("Stop");
    final JButton fileBtn = new JButton("Choose Image");
    final JLabel elapsedTimeMeter =
                              new JLabel("0000");
    final JLabel filePathLabel = new JLabel("...");                            
    static final String FRAME_NAME = "EarPaint"; //title of the frame

    private String filePath = null;
    //private String filePath = "E:\\projects\\freelancer\\20150205 Java Image to sound\\image-sound-conversion\\build\\packages\\assets";

    //for audio processing
    AudioFormat audioFormat;
    AudioInputStream audioInputStream;
    SourceDataLine sourceDataLine;

    //The following are audio format parameters.
    // They may be modified by the signal generator
    // at runtime.  Values allowed by Java
    // SDK 1.4.1 are shown in comments.
    float sampleRate = 16000.0F;
    //Allowable 8000,11025,16000,22050,44100
    int sampleSizeInBits = 16;
    //Allowable 8,16
    int channels = 1;
    //Allowable 1,2
    boolean signed = true;
    //Allowable true,false
    boolean bigEndian = true;
    //Allowable true,false

    //A buffer to hold two seconds monaural and one
    // second stereo data at 16000 samp/sec for
    // 16-bit samples
    byte audioData[] = new byte[16000*4];

    Sequence sequence = null;
    MidiPlayer player = null;
    public MainView(){

        final JPanel controlButtonPanel = new JPanel();
        controlButtonPanel.setBorder(
            BorderFactory.createEtchedBorder());

        final JPanel centerPanel = new JPanel();
        final JPanel outputButtonPanel = new JPanel();

        fileBtn.setEnabled(true);
        generateBtn.setEnabled(false);
        playBtn.setEnabled(false);
        stopBtn.setEnabled(true);
        
        fileBtn.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent event){
                    filePath = Util.open(MainView.this);
                    filePathLabel.setText(filePath);
                    generateBtn.setEnabled(true);
                }
            }
        );

        generateBtn.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent event){
                    fileBtn.setEnabled(false);
                    playBtn.setEnabled(false);

                    //audioData = new Sound().synthesize();
                    sequence = new Sound().createSequence();

                    fileBtn.setEnabled(true);
                    playBtn.setEnabled(true);
                }
            });

        playBtn.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent event){
                    //play();
                    playSequence();
                }
            });
        stopBtn.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent event){
                    stop();
                }
            });

        controlButtonPanel.add(generateBtn);
        controlButtonPanel.add(playBtn);
        controlButtonPanel.add(stopBtn);
        controlButtonPanel.add(elapsedTimeMeter);
        centerPanel.add(filePathLabel);
        outputButtonPanel.add(fileBtn);

        getContentPane().add(
          controlButtonPanel,BorderLayout.NORTH);
        getContentPane().add(centerPanel,
                                BorderLayout.CENTER);
        getContentPane().add(outputButtonPanel,
                                 BorderLayout.SOUTH);
       
        setTitle(FRAME_NAME);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(250,275);
        setVisible(true);
    }

    class Sound {
        ByteBuffer byteBuffer;
        ShortBuffer shortBuffer;
        int byteLength;

        public byte[] synthesize(){

            try{
                ARGB image = ARGB.fromFilePath(filePath);

                byte[] colAverages = image.getData();
                byte[] synDataBuffer = new byte[30000];
                for(int i = 0, j=0; i< synDataBuffer.length; i++, j++){
                    synDataBuffer[i] = colAverages[j];
                    if(j+1 == colAverages.length) j = 0;
                }

                //Prepare the ByteBuffer and the shortBuffer
                // for use
                byteBuffer = ByteBuffer.wrap(synDataBuffer);
                shortBuffer = byteBuffer.asShortBuffer();

                byteLength = synDataBuffer.length;

                return synDataBuffer;
            }catch(IOException ex){
                ex.printStackTrace();
            }

            return null;
        }

        public Sequence createSequence(){
            try{

                ARGB image = ARGB.fromFilePath(filePath);
                TwelveToneAlgorithm algo = new TwelveToneAlgorithm();
                algo.output(image, "");
                return algo.getSequence();

            }catch(IOException ex){
                ex.printStackTrace();
            }
            return null;
        }
    }

    //Inner class to play back the data that was
    // saved.
    class ListenThread extends Thread{
    //This is a working buffer used to transfer
    // the data between the AudioInputStream and
    // the SourceDataLine.  The size is rather
    // arbitrary.
    byte playBuffer[] = new byte[16384];

    public void run(){
        try{
            //Disable buttons while data is being
            // played.
            generateBtn.setEnabled(false);
            fileBtn.setEnabled(false);
            playBtn.setEnabled(false);

            //Open and start the SourceDataLine
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();

            int cnt;
            //Get beginning of elapsed time for
            // playback
            long startTime = new Date().getTime();

            //Transfer the audio data to the speakers
            while((cnt = audioInputStream.read( playBuffer, 0, playBuffer.length)) != -1){
                //Keep looping until the input read
                // method returns -1 for empty stream.
                if(cnt > 0){
                  //Write data to the internal buffer of
                  // the data line where it will be
                  // delivered to the speakers in real
                  // time
                  sourceDataLine.write(
                                     playBuffer, 0, cnt);
                }//end if
            }//end while

            //Block and wait for internal buffer of the
            // SourceDataLine to become empty.
            sourceDataLine.drain();


            //Get and display the elapsed time for
            // the previous playback.
            int elapsedTime =
            (int)(new Date().getTime() - startTime);
            elapsedTimeMeter.setText("" + elapsedTime);

            //Finish with the SourceDataLine
            sourceDataLine.stop();
            sourceDataLine.close();

            //Re-enable buttons for another operation
            generateBtn.setEnabled(true);
            fileBtn.setEnabled(true);
            playBtn.setEnabled(false);
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }//end catch

        }//end run
    }//end inner class ListenThread

    //this method is to play raw data without sequence/ track
    public void play(){
        try{
            InputStream byteArrayInputStream =
                            new ByteArrayInputStream(audioData);

            //Get the required audio format
            audioFormat = new AudioFormat( sampleRate, sampleSizeInBits, channels,
                                    signed, bigEndian);

            //Get an audio input stream from the
            // ByteArrayInputStream
            audioInputStream = new AudioInputStream( byteArrayInputStream, audioFormat,
                        audioData.length/audioFormat.getFrameSize());

            //Get info on the required data line
            DataLine.Info dataLineInfo = new DataLine.Info( SourceDataLine.class, audioFormat);

            //Get a SourceDataLine object
            sourceDataLine = (SourceDataLine) AudioSystem.getLine( dataLineInfo);
            
            //Create a thread to play back the data and
            // start it running.  It will run until all
            // the data has been played back
            new ListenThread().start();
        }catch(LineUnavailableException el){
                el.printStackTrace();
        }
    }

    public void playSequence(){

        if(sequence == null){
            System.out.println("Sequence is now. Stop.");
            return;
        }

        System.out.println(sequence.getMicrosecondLength());

        if(player!= null){
            player.stop();
        }

        player = new MidiPlayer();
        player.play(sequence, true);

    }

    public void stop(){
        if(player != null){
            player.stop();
        }
    }
}
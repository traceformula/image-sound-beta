/* traceformula@gmail.com */

package titan.util;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.Sequence;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.sound.midi.InvalidMidiDataException;

public class SoundUtil {

    private static final int    VELOCITY = 64;

    public static MidiEvent createNoteOnEvent(int nKey, long lTick)
    {
        return createNoteEvent(ShortMessage.NOTE_ON,
                               nKey,
                               VELOCITY,
                               lTick);
    }

    public static MidiEvent createNoteOnEvent(int nKey, long lTick, int velocity)
    {
        return createNoteEvent(ShortMessage.NOTE_ON,
                               nKey,
                               velocity,
                               lTick);
    }

    public static MidiEvent createNoteOffEvent(int nKey, long lTick)
    {
        return createNoteEvent(ShortMessage.NOTE_OFF,
                               nKey,
                               0,
                               lTick);
    }

    public static MidiEvent createNoteEvent(int nCommand,
                                             int nKey,
                                             int nVelocity,
                                             long lTick)
    {
        ShortMessage    message = new ShortMessage();
        try
        {
            message.setMessage(nCommand,
                               0,   // always on channel 1
                               nKey,
                               nVelocity);
        }
        catch (InvalidMidiDataException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        MidiEvent   event = new MidiEvent(message,
                                          lTick);
        return event;
    }
}
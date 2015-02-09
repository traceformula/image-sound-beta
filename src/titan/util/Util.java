/* traceformula@gmail.com */

package titan.util;

import java.awt.AWTError;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;

public class Util {

    public static String open(Container container){
       FileDialog fd = null;
    try {
        Container c = container;  // find frame that contains this panel
        do {
           Container p = c.getParent();
           if (p == null)
              break;
           c = p; 
        } while (true);

        if (!(c instanceof Frame))
           c = null;

        fd = new FileDialog((Frame)c,"Select File to Load",FileDialog.LOAD);
        fd.show();
      }
      catch (AWTError e) {  // thrown by Netscape 3.0 on attempt to use file dialog
        return null;
      }
      catch (RuntimeException re) { // illegal typecast, maybe?
        
        return null;
      }

      String fileName = fd.getFile();
      if (fileName == null)
         return null;
      String dir = fd.getDirectory();
      return joinPath(dir, fileName);
    }

    public static String joinPath(String path1, String path2){
        String path = new java.io.File(path1, path2).toString();
        return path;
    }
}
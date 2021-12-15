/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcImageIO;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author قصي حسين
 */
public class FileReader {
    
    FileInputStream file;
    protected  byte[] buffer = null;
    private boolean EOF=false;
    int bufferpos ;
    int buffervalid;
    
    public FileReader(FileInputStream file) {
        
        this.file=file;
        this.buffer=new byte[4096];
        bufferpos=buffervalid=0;
    }
    
    public void readBytes(int len, int off, byte[] buffer2) throws IOException 
    {
        while (len > 0) {

            int avail = buffervalid - bufferpos;

            if (avail == 0) {

                Fetch();

                continue;

            }

            int copy = (avail >= len) ? len : avail;

            

                System.arraycopy(buffer, bufferpos, buffer2, off, copy);  
            


            off += copy;

            len -= copy;

            bufferpos += copy;

        }
    }
    
    public int getU8() throws IOException
    {
         if (bufferpos == buffervalid) {

            Fetch();

        }

        return buffer[bufferpos++] & 255;
    }
    
    public int getU16() throws IOException
    {
        int t = getU8();
        return (t << 8) | getU8();
    }
    
    public void Fetch() throws IOException
    {
         
         bufferpos = 0;
         buffervalid = file.read(buffer);
         
            if (buffervalid <= 0)
            {
              EOF=true;
              file.close();
              throw new EOFException();
            }

        
    }
    
    public void Skip(int len) throws IOException
    {
      
        while (len > 0) {

            int inputBufferRemaining = buffervalid - bufferpos;

            if (len > inputBufferRemaining) {

                len -= inputBufferRemaining;

                Fetch();

            } else {

                bufferpos += len;
                len = 0;
            }

        }
    }

    public void processMarker() throws IOException , Unsupported{
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcImageIO;

import Extensions.FilePath;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 *
 * @author قصي حسين
 */
public class Image {
   public int Width;
   public int Height;

   public IntBuffer RGBA;


    public  Image(int imgHeight, int imgWidth) {
        
       this.Height=imgHeight;
       this.Width=imgWidth;
       RGBA=IntBuffer.allocate(Width*Height);


    }
   

}

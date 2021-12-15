/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;
import Extensions.Point;
import Extensions.Result_Image;
import android.graphics.Bitmap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;

/**
 *
 * @author قصي حسين
 */
public class Crop extends Tools {
    
    public static void crop(Point p1 , Point p2)
    {
   
    
        IntBuffer newimg=IntBuffer.allocate((((int)p2.getY()-(int)p1.getY())*((int)p2.getX()-(int)p1.getX())));


        int index=0;


        for(int r=(int)p1.getY() ; r<(int)p2.getY() ; r++)
       {
        
           for(int c=(int)p1.getX() ; c<(int)p2.getX() ; c++)
           {
               newimg.put( index++ , Result_Image.mBuffer.get( r*img.Width+c ) );

           }

       }

        Result_Image.INSTANCE.getMImage().Height=((int)p2.getY()-(int)p1.getY());
        Result_Image.INSTANCE.getMImage().Width=((int)p2.getX()-(int)p1.getX());
        Result_Image.INSTANCE.setMBuffer(newimg);
       img.Height=((int)p2.getY()-(int)p1.getY());
       img.Width=((int)p2.getX()-(int)p1.getX());
       img.RGBA=newimg;

    }










}

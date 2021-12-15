/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import Extensions.Result_Image;

import static Tools.Tools.bitmap;
import static Tools.Tools.img;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author قصي حسين
 */
public class HueFilter extends Filter{
    
    
    public static void huecolorize(float huerotate) {

        int index = 1;

        int rgb;

        byte[] RGB = new byte[3];
        float[] hsl = new float[3];

        ColorConversion.setHSL(hsl);
        ColorConversion.setRGB(RGB);

        for (int i = 0; i < img.Height; i++) {
            for (int j = 0; j < img.Width; j++) {


                 hsl[0] = huerotate ;
                 hsl[1]=HSL[index++];
                 hsl[2]=HSL[index++];
                 index++;
                ColorConversion.HSLtoRGB();

                rgb = 255 << 24 | (RGB[0] & 255) << 16 | (RGB[1] & 255) << 8 | (RGB[2] & 255);

                Result_Image.mBitmap.setPixel(j,i,rgb);


            }
        }
    }
    
    
        public static void huenotcolorize(float huerotate) {

        int index = 0;

        int rgb;

        byte[] RGB = new byte[3];
        float[] hsl = new float[3];

        ColorConversion.setHSL(hsl);
        ColorConversion.setRGB(RGB);

        for (int i = 0; i < img.Height; i++) {
            for (int j = 0; j < img.Width; j++) {


                 hsl[0] = huerotate -HSL[index++];
                 hsl[1]=HSL[index++];
                 hsl[2]=HSL[index++];
             
                ColorConversion.HSLtoRGB();

                rgb = 255 << 24 | (RGB[0] & 255) << 16 | (RGB[1] & 255) << 8 | (RGB[2] & 255);

                Result_Image.mBitmap.setPixel(j,i,rgb);

            }
        }
    }
    

}

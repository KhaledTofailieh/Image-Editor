/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;


import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 *
 * @author قصي حسين
 */
public class Rotation extends Tools {

    public static void rotate(RotateDirect dir) {
        
        switch (dir) {
            
            case LeftRotate: {
                 leftRotate();
                 break;
            }

            case RightRotate: {
                 rightRotate();
                 break;
            }
        }
       

    }

   
    public static void  leftRotate() {
        
        IntBuffer newimg =IntBuffer.allocate(img.Height*img.Width);

        int k = (img.Height*img.Width - img.Height);
        

        for (int c = 0; c <= img.Width - 1; c++) {

            int n = k;
            k -= img.Height;
            
            for (int i = 0; i < img.Height; i++) {

                /*if ((i * img.Width + c) % img.Width == 0) {
                    continue;
                }*/

                newimg.put(n++, img.RGBA.get(i * img.Width  + c ));

            }
        }

       img.RGBA=newimg;
       int w=img.Width;
       img.Width=img.Height;
       img.Height=w;
    }

    
    
    public static void rightRotate() {

        IntBuffer newimg =IntBuffer.allocate(img.Height*img.Width);

        int k = (img.Height * img.Width)  - 1;

        for (int c = img.Width - 1; c >= 0; c--) {

            for (int i = 0; i <= img.Height - 1; i++) {

                if( (i*img.Width+c) % img.Width==0)
                {
                    continue;
                }
                
                newimg.put(k--, img.RGBA.get(i * img.Width  + c ));

            }
        }

       img.RGBA=newimg;
       int w=img.Width;
       img.Width=img.Height;
       img.Height=w;
    }

}

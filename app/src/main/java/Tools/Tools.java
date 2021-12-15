/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import android.graphics.Bitmap;
import srcImageIO.Image;
public class Tools {
    
    protected static Image img;
    

    public  static Bitmap bitmap;

    public static void setimg(Image img2)
    {
        img=img2;
    }
    public  static  void setBitmap(Bitmap mbit)
    {
        bitmap=mbit;
    }


}

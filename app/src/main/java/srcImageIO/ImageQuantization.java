/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcImageIO;

import Extensions.Screen_Size;

/**
 *
 * @author قصي حسين
 */
public class ImageQuantization {
    
     static int columns, rows, index;
     static float sizerate, inversesizerate;

      static int screenheight = 500;
      static int screenwidth = 200;
   static float rate;
      static int blankHeight ;
      static int blankWidth;
      static int Width;
      static boolean setPixelInBlankImage() {

        if (index < blankHeight * blankWidth) {
            if (columns % (int) inversesizerate == 0 && rows % (int) inversesizerate == 0) {

                return true;
            } else {

                columns++;
                if (rows % (int) inversesizerate != 0 || columns == Width) {

                    columns = 0;
                }

                return false;
            }

        }
        return false;

    }

    static void computeSizerate(int Height, int Width ) {
              if (Height > Width) {
                  sizerate = Screen_Size.INSTANCE.getHeight() / Height;
                  inversesizerate = (Height / Screen_Size.INSTANCE.getHeight());
                  if(inversesizerate != 0)
                  {
                      rate =inversesizerate-(float) Math.floor((double) inversesizerate);
                      rate*=10;
                      rate=(float)Math.floor((double) rate);
                      rate/=10;
                      inversesizerate =(float) Math.floor((double) inversesizerate)+rate ;
                  }else {
                      inversesizerate=1.0f;
                  }

              } else {
                  sizerate = Screen_Size.INSTANCE.getWidth() / Width;
                  inversesizerate = Width / Screen_Size.INSTANCE.getWidth() ;
                  if(inversesizerate != 0)
                  {
                      rate =inversesizerate-(float) Math.floor((double) inversesizerate);
                      rate*=10;
                      rate=(float)Math.floor((double) rate);
                      rate/=10;
                      inversesizerate =(float) Math.floor((double) inversesizerate)+rate ;
                  }else {
                      inversesizerate=1.0f;
                  }
              }


    }

    static void reset(int blankHeight1 , int blankWidth1 , int Width1)
    {
        index=columns=rows=0;
        
        blankHeight=blankHeight1;
        blankWidth=blankWidth1;
        Width=Width1;
    }
    
    
}

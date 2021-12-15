package Tools;

import Extensions.Result_Image;

import java.nio.IntBuffer;

public class Flip extends  Tools{

    public static void HorizionalFlip()
    {
        IntBuffer im=IntBuffer.allocate(img.Height*img.Width);
        for(int i = 1; i<= Result_Image.mBitmap.getHeight() ; i++)
        {
            for(int j=i*Result_Image.mBitmap.getWidth() - 1 ; j>=(i-1)*img.Width  ; j--)
            {
                im.put(Result_Image.mBuffer.get(j));
            }
        }

        im.position(0);
        img.RGBA=null;
        img.RGBA=im;
        Result_Image.mBuffer=null;
        Result_Image.mBuffer=im;

    }

    public static void VerticalFlip()
    {
        Result_Image.mBuffer.position(0);
        IntBuffer im=IntBuffer.allocate(img.Height*img.Width);
        int index=0;
        for(int i=0 ; i<Result_Image.mBitmap.getHeight() ; i++)
        {
            index=(Result_Image.mBitmap.getHeight() - i -1)*Result_Image.mBitmap.getWidth();
            for(int j=0 ; j<Result_Image.mBitmap.getWidth() ;j++)
            {
                im.put(index++, Result_Image.mBuffer.get());
            }
        }

        im.position(0);
        img.RGBA=null;
        img.RGBA=im;
        Result_Image.mBuffer=null;
        Result_Image.mBuffer=im;
    }

}

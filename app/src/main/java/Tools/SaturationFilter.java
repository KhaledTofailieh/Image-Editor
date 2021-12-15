
package Tools;
public class SaturationFilter extends  Tools{
    
    
    public static void Saturationcolorize(float saturation)
    {
        int index = 0;

        int rgb;

        byte[] RGB = new byte[3];
        float[] HSL = new float[3];

        ColorConversion.setHSL(HSL);
        ColorConversion.setRGB(RGB);

        for (int i = 0; i < img.Height; i++) {
            for (int j = 0; j < img.Width; j++) {

                rgb = bitmap.getPixel(j,i);

                rgb<<=8;
                RGB[0] = (byte) (rgb >>> 24);
                rgb <<= 8;
                RGB[1] = (byte) (rgb >>> 24);
                rgb <<= 8;
                RGB[2] = (byte) (rgb >>> 24);

                ColorConversion.RGB2HSL();

                HSL[1] *= saturation  ;

                HSL[0]=360-HSL[0];
             
                ColorConversion.HSLtoRGB();

                rgb =  255<< 24 | (RGB[0] & 255) << 16 | (RGB[1] & 255) << 8 | (RGB[2] & 255);

                bitmap.setPixel(j,i,rgb);

            }
        }
        
        
    }

    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.nio.IntBuffer;
public class ColorConversion {

    private static float []HSL;
    private static byte [] RGB;
    public static void  setHSL(float [] hsl)
    {
        HSL=hsl;
    }
    
    public static void  setRGB(byte [] rgb)
    {
        RGB=rgb;
    }
    
    static void HSLtoRGB() {
        HSL[0]=HSL[0]%360.0f;
        HSL[0]/=360f;

        float q=0.0f;
        if(HSL[2]<0.5)
        {
            q=HSL[2]*(1+HSL[1]);
        }
        else 
        {
            q=(HSL[2]+HSL[1])-(HSL[1]*HSL[2]);
        }
        
        float p=2*HSL[2]-q;
        
        float r=Math.max(0, HueToRGB(p,q,HSL[0]+(1.0f/3.0f)));
        float g=Math.max(0, HueToRGB(p, q, HSL[0]));
        float b=Math.max(0, HueToRGB(p, q, HSL[0]-(1.0f/3.0f)));
        
        r=Math.min(r, 1.0f);
        g=Math.min(g, 1.0f);
        b=Math.min(b, 1.0f);
        
        RGB[0]=(byte) (r*255);
        RGB[1]=(byte) (g*255);
        RGB[2]=(byte) (b*255);
    }
    
    static void RGB2HSL()
    {
        float r1=(RGB[0]&255)/255.0f;

        float b1=(RGB[1]&255)/255.0f;

        float g1=(RGB[2]&255)/255.0f;
        
      
        
        float cmax= Math.max(r1, Math.max(g1, b1));
        
        float cmin=Math.min(r1, Math.min(g1, b1));
        
        float delta =cmax-cmin;
        
        
        if(delta==0)
        {
            HSL[0]=0.0f;
        }
        else if(cmax==r1)
        {
            //HSL[0]=60*(((g1-b1)/delta)%6);
            HSL[0]=((60*(g1-b1)/delta)+360)%360;
        }
        
        else if(cmax==g1)
        {
          // HSL[0]=60*((b1-r1/delta)+2); 
            HSL[0]=((60*(b1-r1)/delta))+120;
        }
        
        else if(cmax==b1)
        {
            //HSL[0]=60*((r1-g1)/delta +4);
            HSL[0]=((60*(r1-g1)/delta))+240;
        }
        
        HSL[2]=(cmax+cmin)/2;
        
        if(cmax==cmin)
        {
            HSL[1]=0.0f;
        }
        else if(HSL[2]<=0.5f)
        {
            HSL[1]=delta/(cmax+cmin);
        }
        
        else
        {
            HSL[1]=delta/(2-cmax-cmin);
        }
        
       /* HSL[1]=delta/(1-Math.abs(2*HSL[2]-1));

        if(HSL[1]<0.0)
        {
            HSL[1]=0.0f;
        }
        if(HSL[2]<0.0)
        {
            HSL[2]=0.0f;
        }
        if(HSL[1]>1.0)
        {
            HSL[1]=1.0f;
        }
        if(HSL[2]>1.0)
        {
            HSL[2]=1.0f;
        }*/
        
        
    }

    private static float HueToRGB(float p, float q, float h) {
        
        if(h<0) h+=1;
        if(h>1) h-=1;
        
        if(6*h<1)
        {
            return p+((q-p)*6*h);
        }
        
        if(2*h<1)
        {
            return q;
        }
        if(3*h<2)
        {
            return p+((q-p)*6*((2.0f/3.0f)-h));
        }
        return p;
    }
    
    public static float[] MHSL(IntBuffer RGBA)
    {
        int i=0 , index=0;
        int rgb ;
        float [] hsl =new float[3*RGBA.capacity()];
        RGB = new byte[3];
        HSL=new float [3];
        while(i<RGBA.capacity())
        {
            rgb=RGBA.get(i++);

            RGB[2] = (byte) (rgb >>> 24);

            RGB[1] = (byte) (rgb >>> 16);

            RGB[0] = (byte) (rgb >>> 8);

            RGB2HSL();
            hsl[index++]=HSL[0];
            hsl[index++]=HSL[1];
            hsl[index++]=HSL[2];

        }

        return hsl;
    }

}

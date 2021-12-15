/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;


import Extensions.Brush_Features;
import Extensions.Point;
import Extensions.Result_Image;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;

/**
 *
 * @author قصي حسين
 */



public class Pen extends Tools{
    

    static Properties pro;
    
    public static void setpro()
    {

        pro=new Properties(0,0);
        pro.color= Brush_Features.INSTANCE.getColor();
        pro.size=Brush_Features.INSTANCE.getSize()+4;

    }

    public static void setpro(int color , int size)
    {

        pro=new Properties(color,size);


    }

    public static void PenSquare(ArrayList<Point> points)
    {
        for(Point point :points) {
            int y = (int) point.getY();
            int x = (int) point.getX();

            y -= pro.size;
            x -= pro.size;

            if (y < 0) {
                y = 0;
            }
            int y1 = (int) point.getY() + pro.size;


            if (y1 > img.Height) {
                y1 = img.Height;
            }

            if (x < 0) {
                x = 0;
            }

            int x1 = (int) point.getX() + pro.size;

            if (x1 > img.Width) {
                x1 = img.Width;
            }

            for (int i = y; i < y1; i++) {
                for (int j = x; j < x1; j++) {
                    Result_Image.mBitmap.setPixel(j, i, pro.color);
                }
            }
        }
    }


    public static void PenRadius(ArrayList<Point> points)
    {
        for(Point point:points) {
            int y = (int) point.getY() - pro.size;
            int x = (int) point.getX() + pro.size;
            int y1 = (int) point.getY() + pro.size;
            int x1 = (int) point.getX() - pro.size;


            if (y < 0) {
                y = 0;
                x -= pro.size;
            }


            if (x >= img.Width) {
                y += x - img.Width + 1;
                x = img.Width - 1;
            }


            if (x1 < 0) {
                y1 += x1 + 1;

            }
            if (y1 > img.Height) {
                y1 = img.Height;
            }
            for (int i = y; i < y1; i++) {

                {
                    Result_Image.mBitmap.setPixel(x, i, pro.color);
                    x--;
                }
            }
        }
    }

    public static void Rupper(ArrayList<Point> points)
    {
        for(Point point:points) {
            int y = (int) point.getY();
            int x = (int) point.getX();

            y -= pro.size;
            x -= pro.size;

            if (y < 0) {
                y = 0;
            }
            int y1 = (int) point.getY() + pro.size;


            if (y1 > img.Height) {
                y1 = img.Height;
            }

            if (x < 0) {
                x = 0;
            }

            int x1 = (int) point.getX() + pro.size;

            if (x1 > img.Width) {
                x1 = img.Width;
            }

            byte r, g, b;
            int rgb;

            for (int i = y; i < y1; i++) {
                for (int j = x; j < x1; j++) {
                    rgb = img.RGBA.get(i * img.Width + j);
                    rgb <<= 8;
                    b = (byte) (rgb >>> 24);
                    rgb <<= 8;
                    g = (byte) (rgb >>> 24);
                    rgb <<= 8;
                    r = (byte) (rgb >>> 24);
                    rgb = 255 << 24 | (r & 255) << 16 | (g & 255) << 8 | (b & 255);

                    Result_Image.mBitmap.setPixel(j, i, rgb);
                }
            }
        }
    }

    public static void fill(Point p , int color)
    {
        ArrayList<Point> stack=new ArrayList<>();
        stack.add(p);
        boolean [] visit=new boolean [img.Width*img.Height];
        int i=0;
        while(i<stack.size())
        {
            p=stack.get(i++);
            int index=(int)p.getY()*img.Width+(int)p.getX();
            visit [(int)p.getY()*img.Width+(int)p.getX()]=true;
            int color1=img.RGBA.get(index);
            img.RGBA.put(index, color);
            int index1=((int)p.getY()-1)*img.Width+(int)p.getX();
            if(index1>=0 && img.RGBA.get(index1)==color1&&!visit[index1])
            {
                Point p1=new Point(i, i);
                p1.setX(p.getX()); p1.setY(p.getY()-1);
                stack.add(p1);
                visit[index1]=true;
            }
            index1=((int)p.getY()+1)*img.Width+(int)p.getX();
            if(index1<img.Height*img.Width && img.RGBA.get(index1)==color1&&!visit[index1])
            {
                Point p1=new Point(i, i);
                p1.setX(p.getX()); p1.setY(p.getY()+1);
                stack.add(p1);
                visit[index1]=true;
            }
            index1=(int)p.getY()*img.Width+(int)p.getX()-1;
            if(index1>=((int)p.getY())*img.Width&&index1<((int)p.getY()+1)*img.Width && img.RGBA.get(index1)==color1&&!visit[index1])
            {
                Point p1=new Point(i, i);
                p1.setX(p.getX()-1); p1.setY(p.getY());
                stack.add(p1);
                visit[index1]=true;
            }
            index1=(int)p.getY()*img.Width+(int)p.getX()+1;
            if(index1<((int)p.getY()+1)*img.Width && img.RGBA.get(index1)==color1&&!visit[index1])
            {
                Point p1=new Point(i, i);
                p1.setX(p.getX()+1); p1.setY(p.getY());
                stack.add(p1);
                visit[index1]=true;
            }
        }
        visit=null;
        stack=null;
    }







    @Nullable
    public static Properties getpro() {
        return pro;
    }


    public static class Properties {

        public int color;
        public PenType type;
        public int  size ;
        public Properties( int color , int size) {
            
            this.type=type;
            this.color=color;
            this.size=size;
        }
    }


    
    
    
}

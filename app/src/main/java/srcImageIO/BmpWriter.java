/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcImageIO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BmpWriter {

    FileOutputStream out;

    byte[] Header;

    public BmpWriter(FileOutputStream out) {
        this.out = out;
        Header = new byte[54];
        Header[0] = 66;
        Header[1] = 77;
    }

    public void write(Image img) throws IOException {
        writeHeader(img.Width, img.Height);
        writeData(img);
    }

    public  void writeHeader(int Width, int Height) throws IOException {
        int skip = 0;
        if (Width % 4 != 0) {
            skip = 4 - Width % 4;
        }
        int s = 54 + (Width * Height * 3) + (skip * Height);

        Header[5] = (byte) (s >> 24);
        s <<= 8;
        Header[4] = (byte) (s >> 24);
        s <<= 8;
        Header[3] = (byte) (s >> 24);
        s <<= 8;
        Header[2] = (byte) (s >> 24);
        int w = Width, h = Height;
        Header[10] = 54;
        Header[14] = 40;
        Header[21] = (byte) (w >> 24);
        w <<= 8;
        Header[20] = (byte) (w >> 24);
        w <<= 8;
        Header[19] = (byte) (w >> 24);
        w <<= 8;
        Header[18] = (byte) (w >> 24);

        Header[25] = (byte) (h >> 24);
        h <<= 8;
        Header[24] = (byte) (h >> 24);
        h <<= 8;
        Header[23] = (byte) (h >> 24);
        h <<= 8;
        Header[22] = (byte) (h >> 24);

        Header[26] = 1;
        Header[28] = 24;
        int bytsize = 27000000;
        Header[37] = (byte) (bytsize >> 24);
        bytsize <<= 8;
        Header[36] = (byte) (bytsize >> 24);
        bytsize <<= 8;
        Header[35] = (byte) (bytsize >> 24);
        bytsize <<= 8;
        Header[34] = (byte) (bytsize >> 24);

        out.write(Header);
    }

    private void writeData(Image img) throws IOException {
        


               
        byte[] rgb = new byte[img.Width * 3];
        
        int w = img.Width;
        int h = img.Height;

        int index=0;


        int skip = w % 4;

        byte[] zero = new byte[skip];

        int rgba;


        for(int j=h-1 ; j>=0 ; j--) {
            for (int i = j * w; i < (j + 1) * w; i++) {

                rgba = img.RGBA.get(i);
                rgba <<= 8;
                rgb[index ++] = (byte) (rgba >>> 24);
                rgba <<= 8;
                rgb[index ++] = (byte) (rgba >>> 24);
                rgba <<= 8;
                rgb[index++] = (byte) (rgba >>> 24);

            }
            out.write(rgb);

            out.write(zero);
            index=0;
        }
        out.close();
    }

}

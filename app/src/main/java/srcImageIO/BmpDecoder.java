/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcImageIO;

import Extensions.FilePath;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


/**
 *
 * @author قصي حسين
 */
public class BmpDecoder extends ImageDecoder {

    int Width , Height;
 
    public BmpDecoder(FileInputStream file) {
 
         reader=new FileReader(file);
    }

    public Image read() throws IOException, Unsupported {
       
        readHeadr();
        readData();
        return img;
    }

    public void readHeadr() throws IOException, Unsupported {
        reader.Skip(12);

        int f ;
        int s ;
        int t ;
        int fo;


        reader.Skip(4);

        f = reader.getU8();
        s = reader.getU8();
        t = reader.getU8();
        fo = reader.getU8();

         Width = (fo << 24) | (t << 16) | (s << 8) | f;
        f = reader.getU8();
        s = reader.getU8();
        t = reader.getU8();
        fo = reader.getU8();
         Height = (fo << 24) | (t << 16) | (s << 8) | f;

      
        ImageQuantization.computeSizerate(Height, Width);
        
        img = new Image( (int) (Height / ImageQuantization.inversesizerate), (int) (Width / ImageQuantization.inversesizerate));
    

        int n = reader.getU8();
        int ncom = (reader.getU8() << 8) | n;

        if (ncom != 1) {

            throw new Unsupported();
        }

        n = reader.getU8();
        ncom = (reader.getU8() << 8) | n;

        if (ncom != 24) {
            throw new Unsupported();
        }

        reader.Skip(24);
        

    }


    public void readData() throws IOException {
        int skip = Width % 4;

        int i;

        int j;

        byte[] bgr = new byte[Width * 3];

        int index2 = 0;
        int index = -1;
        int h = img.Height - 1;


        for (i = Height - 1; i >= 0; i--) {


            if (i % ImageQuantization.inversesizerate == 0 && h >= 0) {
            reader.readBytes(bgr.length, 0, bgr);



                index = img.Width * h;
                int rgb;
                int r;
                int b;
                int g;
                index2 = 0;
                for (j = 0; j < Width; j++) {
                    if (j % ImageQuantization.inversesizerate == 0) {

                        b = bgr[index2++] & 255;
                        g = bgr[index2++] & 255;
                        r = bgr[index2++] & 255;
                        rgb = 255<< 24 | (b & 255) << 16 | (g & 255) << 8 | (r & 255);
                        img.RGBA.put(index++, rgb);
                    } else {
                        index2 += 3;
                    }

                    if (index >= (h + 1) * img.Width) {
                        break;
                    }

                }


                h--;
            }


            else
            {
                reader.Skip(bgr.length);
            }

            reader.Skip(skip);


        }

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcImageIO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;




/**
 *
 * @author قصي حسين
 */
public class Imageio {
    static ImageType Type;
    static FileOutputStream Out;
    
    public static Image read( String Path) throws IOException , Unsupported,OutOfMemoryError
    {
        FileInputStream in=new FileInputStream(Path);
        
        ChekImageType(in);
        
        if(Type==ImageType.Bmp)
        {
           
            return readBmp(in);
        }
        
        else 
        {
            try 
            {
                return readJpeg(in);
            }
            finally  
            {
                in.close();
            }

        }
        
        
    }

    private static void ChekImageType(FileInputStream in) throws IOException , Unsupported
    {
        
        int FirstByte;
        int SecondByte;
        try
        {
           FirstByte=in.read();
           SecondByte=in.read();
        }
        catch(IOException ex)
        {
            in.close();
            throw new IOException();
        }

        
        if(FirstByte==0xFF  && SecondByte==0xD8)
        {
            Type=ImageType.Jpeg;
        }
        
        else if(FirstByte == 'B' && SecondByte== 'M' )
        {
            
            Type=ImageType.Bmp;
        }
        
        else 
        {
            in.close();
            throw new Unsupported();
        }
    }

    
    private static Image readBmp(FileInputStream in) throws IOException , Unsupported , OutOfMemoryError
    {
        return new BmpDecoder(in).read();
    }

    
    private static Image readJpeg(FileInputStream in) throws IOException , Unsupported ,OutOfMemoryError
    {
        
        return new JpegDecoder(in).Read();
        
    }
    
    
    public static void write(Image img , ImageType type , String path) throws FileNotFoundException, IOException
    {
        FileOutputStream out=new FileOutputStream(path);
        Out=out;
        if(type==ImageType.Bmp)
        {
            writeBmp(img , out);
        }
        
        else if(type==ImageType.Jpeg)
        {
            writeJpeg(img , out);
        }
    }
    public static FileOutputStream getFile()
    {
        return Out;
    }
    private static void writeBmp(Image img, FileOutputStream out) throws IOException {
        new BmpWriter(out).write(img);
    }

    private static void writeJpeg(Image img, FileOutputStream out) {
        new jpegEncoder().encode(img , out);
    }
}

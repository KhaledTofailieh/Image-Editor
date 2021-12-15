/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcImageIO;

import java.io.DataOutputStream;
import java.io.FileOutputStream;

/**
 *
 * @author قصي حسين
 */
public class jpegEncoder {
    JpegImage comimg;
    Image img;
    public jpegEncoder() {
        
    }
    
    public void initComponent()
    {
        JpegImage.Component comp=new JpegImage.Component(1);
        //Y component
       
       comp.blocksPerMCUHorz=comp.blocksPerMCUVert=2;
       comp.iddequant=0;
       comp.hac=comp.hdc=0;
       comp.huffDC=comimg.Huff[0];
       comp.huffAC=comimg.Huff[2];
       comimg.component[0]=comp;
       
       comp=null;
       
       //CB component

       comp=new JpegImage.Component(2);
       comp.blocksPerMCUHorz=comp.blocksPerMCUVert=1;
       comp.iddequant=1;
       comp.hac=comp.hdc=1;
       comp.huffDC=comimg.Huff[1];
       comp.huffAC=comimg.Huff[3];
       comimg.component[1]=comp;
       
       comp=null;
       
       //CR component

       comp=new JpegImage.Component(3);
       comp.blocksPerMCUHorz=comp.blocksPerMCUVert=1;
       comp.iddequant=1;
       comp.hac=comp.hdc=1;
       comp.huffDC=comimg.Huff[1];
       comp.huffAC=comimg.Huff[3];
       comimg.component[2]=comp;
       comp=null;
       
       comimg.Hmax=comimg.Vmax=2;
       
       comimg.ncomponents=3;
       comimg.mcuCountX=(comimg.imgWidth + comimg.Hmax*8 - 1) / (comimg.Hmax*8);
       comimg.mcuCountY=(comimg.imgHeight + comimg.Vmax*8 - 1) / (comimg.Vmax*8);
       for(int i=0 ; i<comimg.ncomponents ; i++)
       {
           comimg.component[i].width=(comimg.imgWidth * comimg.component[i].blocksPerMCUHorz
					+ comimg.Hmax - 1) / comimg.Hmax;
           comimg.component[i].height=(comimg.imgHeight * comimg.component[i].blocksPerMCUVert
					+ comimg.Vmax - 1) / comimg.Vmax;
           comimg.component[i].minReqWidth=(comimg.component[i].width+comimg.component[i].blocksPerMCUHorz*8-1);
           comimg.component[i].minReqWidth-=comimg.component[i].minReqWidth%(comimg.component[i].blocksPerMCUHorz*8);
           
           comimg.component[i].minReqHeight=(comimg.component[i].height+comimg.component[i].blocksPerMCUVert*8-1);
           comimg.component[i].minReqHeight-=comimg.component[i].minReqHeight%(comimg.component[i].blocksPerMCUVert*8);
       }
          
    }
    
    public void encode(Image img , FileOutputStream out)
    {
        this.img=img;
        convertcolor co=new convertcolor();
        co.ji=img;
        co.rgb_ycc_init();
        
        comimg=new JpegImage();
        comimg.imgWidth=img.Width;
        comimg.imgHeight=img.Height;
        
        initComponent();
        
        int rowsinmcu=8*comimg.Vmax;
        
        int expandwidth=img.Width+((img.Width%(8*comimg.Vmax)==0)?0:(8*comimg.Vmax)-img.Width%(8*comimg.Vmax));
        //convert to YUV ColorSpace
        
        short [][][]YUV=new short [3][rowsinmcu][expandwidth];
        short [][][]sampledimg=new short [3][rowsinmcu][expandwidth];
        
        
        
        jpgwriter wrt=new jpgwriter();
        
        Sampling sam=new Sampling();
        
        MCUs mcu=new MCUs();
        
        HuffEncode hh=new HuffEncode();
        
        hh.setjpg(comimg);
        
        hh.setout(out);
        hh.huff_init();
        mcu.sethuen(hh);
        
        wrt.setjpg(comimg);
        wrt.setout(out);
        
        wrt.write_file_header();
        wrt.write_scan_header();
           

        for(int i=0 ; i<img.Height ;i+=rowsinmcu)
        {
            co.get_rgb_ycc_rows( Math.min(rowsinmcu, img.Height - i), YUV);
           // RGB2YUV(i, YUV,i+rowsinmcu);
            sam.Expandedge( Math.min(rowsinmcu, img.Height - i), rowsinmcu,img.Width,expandwidth, 3, YUV);

            //downsampling
            for (int j = 0; j < comimg.ncomponents; j++) {
                int v=comimg.Vmax/comimg.component[j].blocksPerMCUVert;
                int h=comimg.Hmax/comimg.component[j].blocksPerMCUHorz;
                sam.DownSampling(v, h, comimg.component[j].width, comimg.component[j].blocksPerMCUVert*8, YUV[j], sampledimg[j]);
            }
            
            mcu.extractmcu(comimg, sampledimg);
        }
        hh.huff_term();
        wrt.write_file_trailer();
    }
    

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcImageIO;

/**
 *
 * @author قصي حسين
 */
public class MCUs {
        int []block;
        Fwddct dct;
        HuffEncode huen;
        public void sethuen(HuffEncode h)
        {
            this.huen=h;
        }
    	static final short ZZ[] =
	{
		 0,  1,  8, 16,  9,  2,  3, 10,
		17, 24, 32, 25, 18, 11,  4,  5,
		12, 19, 26, 33, 40, 48, 41, 34,
		27, 20, 13,  6,  7, 14, 21, 28,
		35, 42, 49, 56, 57, 50, 43, 36,
		29, 22, 15, 23, 30, 37, 44, 51,
		58, 59, 52, 45, 38, 31, 39, 46,
		53, 60, 61, 54, 47, 55, 62, 63
	};
        
         
        public void extractmcu(JpegImage jpg , short [][][] data )
        {
            block=new int[64];
            dct=new Fwddct();
            int blkn;
            short [][]mcudata=new short [10][64];
                for(int j=0 ; j<jpg.mcuCountX ; j++)
                {
                      
                    blkn=0;
                    for(int n=0 ; n<jpg.ncomponents ; n++)
                    {
                        JpegImage.Component comp=jpg.component[n];
                        short []quanttbl=jpg.QTables1[comp.iddequant];
                        for(int v=0 ; v<comp.blocksPerMCUVert ; v++)
                        {
                            for(int h=0 ; h<comp.blocksPerMCUHorz ; h++)
                            {
                             	extract_block(data[n], v*8,(j*comp.blocksPerMCUHorz+ h)*8, mcudata[blkn++], quanttbl);
                          
                            }
                        }
                    }
                    huen.encodeMCU(mcudata);
                }
            
        }

    private void extract_block(short[][] data, int sr, int sc, short[] outputdata, short[] quanttbl) {
        
        int in=0;
        for(int i=sr  ; i<sr+8 ; i++)
        {
            for(int j=sc ; j<sc+8 ; j++)
            {
                block[in++]=data[i][j]-128;
            }
        }
        
        dct.fwd_dct(block);
        
        for(int i=64 ; --i>=0 ; )
        {
            int temp=block[ZZ[i]];
            if(temp<0)
            {
            	temp = -temp;
		temp += (int)(quanttbl[i]>>1);
		temp /= (int)(quanttbl[i]);
         	temp = -temp;
            }
            else
            {
             	temp += quanttbl[i]>>1;
		temp /= quanttbl[i];
            }
            
            outputdata[i]=(short) temp;
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcImageIO;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author قصي حسين
 */
public class HuffEncode {
    JpegImage jpg;
    private int huff_put_bits , huff_put_buffer;
    FileOutputStream out;
    
    public void setout(FileOutputStream out)
    {
        this.out=out;
    }
    
    public void setjpg(JpegImage jpg)
    {
        this.jpg=jpg;
    }
    public  void encodeMCU(short [][] Mcudata)
    {
        short temp,blkn=0;
        
        temp=(short) (jpg.component[0].blocksPerMCUHorz*jpg.component[0].blocksPerMCUVert);
        // Blocks of Y component
         
        for(short i=0; i<temp ; i++)
        {
            DCupdate(Mcudata, blkn, (short)0);
            encodeOneBlock(Mcudata[blkn++],jpg.component[0].huffDC,jpg.component[0].huffAC);
        }
        
        temp=(short) (jpg.component[1].blocksPerMCUHorz*jpg.component[1].blocksPerMCUVert);
        //Blocks of CB component
        for(short i=0; i<temp ; i++)
        {
            DCupdate(Mcudata, blkn, (short)1);
            encodeOneBlock(Mcudata[blkn++],jpg.component[1].huffDC,jpg.component[1].huffAC);        }
         
        temp=(short) (jpg.component[2].blocksPerMCUHorz*jpg.component[2].blocksPerMCUVert);
        //Blocks of CR component
        for(short i=0; i<temp ; i++)
        {
            DCupdate(Mcudata, blkn, (short)2);
            encodeOneBlock(Mcudata[blkn++],jpg.component[2].huffDC,jpg.component[2].huffAC);
        }
    }
    
    public void DCupdate(short [][] Mcudata , short blkn , short ci)
    {
        short temp = Mcudata[blkn][0];
        Mcudata[blkn][0] -= jpg.last_dc_val[ci];
        jpg.last_dc_val[ci] = temp;
    }
    
    public void encodeOneBlock ( short [] block, JpegImage.Huffman dctbl, JpegImage.Huffman actbl)
	{
	  int temp, temp2;
	  int nbits;
	  int k, r, i;

	  temp = temp2 = block[0];

	  if (temp < 0) {
	    temp = -temp;		
	    temp2--;
	  }

	  nbits = 0;
	  while (temp != 0) {
	    nbits++;
	    temp >>= 1;
	  }
	  write_bits( dctbl.code[nbits], dctbl.size[nbits]);


	  if (nbits != 0)
	    write_bits((short) temp2, (byte) nbits);


	  r = 0;

	  for (k = 1; k < 64; k++) {
	    if ((temp = block[k]) == 0) {
	      r++;
	    } else {

	      while (r > 15) {
		write_bits( actbl.code[0xF0], actbl.size[0xF0]);
		r -= 16;
	      }

	      temp2 = temp;
	      if (temp < 0) {
		temp = -temp;
		temp2--;
	      }

	      nbits = 1;
	      while ((temp >>= 1) != 0)
					nbits++;

	      i = (r << 4) + nbits;
	      write_bits( actbl.code[i], actbl.size[i]);

	      write_bits((short) temp2, (byte) nbits);

	      r = 0;
	    }
	  }

	  if (r > 0)
	    write_bits( actbl.code[0], actbl.size[0]);
	}

    private void write_bits(int code, int size) {
	  int put_buffer = code;
	  int put_bits = huff_put_bits;



	  put_buffer &= (((int) 1) << size) - 1;

	  put_bits += size;

	  put_buffer <<= 24 - put_bits;

	  put_buffer |= huff_put_buffer;

	  while (put_bits >= 8) {
	    int c = (int) ((put_buffer >> 16) & 0xFF);

	    write_byte( c);
	    if (c == 0xFF) {
	      write_byte( 0);
	    }
	    put_buffer <<= 8;
	    put_bits -= 8;
	  }

	  huff_put_buffer = put_buffer;	// Update global variables 
	  huff_put_bits = put_bits;
    }

    private void write_byte(int i) {
        try {
            //i&=0x00ff;
            out.write(i);
        } catch (IOException ex) {
            Logger.getLogger(HuffEncode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    	public  void
	huff_term ()
	{
	  // Flush out the last data 
	  flush_bits();
	  flush_bytes();
	}
        
       void flush_bits ( )
	{
	  write_bits(  0x7F, 7);	// fill any partial byte with ones 
	  huff_put_buffer = 0;		// and reset bit-buffer to empty 
	  huff_put_bits = 0;
	}
       
       	void
	flush_bytes ( )
	{
		try {
			out.flush(); 
		} catch (IOException e){}
	}
        
        void
	huff_init ()
	{
	  short ci;
	  JpegImage.Component comp;

	  huff_put_buffer = 0;
	  huff_put_bits = 0;

	  for (ci = 0; ci < jpg.ncomponents; ci++) {
	    comp = jpg.component[ci];
	    init_huff_tbl(comp.huffDC);
	    init_huff_tbl(comp.huffAC);
	    jpg.last_dc_val[ci] = 0;
	  }
	}

    private void init_huff_tbl(JpegImage.Huffman htbl) {
        int p, i, l, lastp, si;
	  byte [] huffsize = new byte[257];
	  short[] huffcode = new short[257];
	  short code;

	  p = 0;
	  for (l = 1; l <= 16; l++) {
	    for (i = 1; i <= (int) htbl.count[l]; i++)
	      huffsize[p++] = (byte) l;
	  }
	  huffsize[p] = 0;
	  lastp = p;

	  code = 0;
	  si = huffsize[0];
	  p = 0;
	  while (huffsize[p] != 0) {
	    while (((int) huffsize[p]) == si) {
	      huffcode[p++] = code;
	      code++;
	    }
	    code <<= 1;
	    si++;
	  }


	  for (i=0; i<256; i++)
	  	htbl.size[i] = 0;

	  for (p = 0; p < lastp; p++) {
	    htbl.code[htbl.values1[p]] = huffcode[p];
	    htbl.size[htbl.values1[p]] = huffsize[p];
    }
}
}

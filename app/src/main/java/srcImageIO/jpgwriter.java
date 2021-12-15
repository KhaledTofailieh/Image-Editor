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
public class jpgwriter {
    FileOutputStream out;
    JpegImage jpg;
    public void setjpg(JpegImage jpg)
    {
        this.jpg=jpg;
    }
    public void setout(FileOutputStream out)
    {
        this.out=out;
    }
    	// Write a single byte 
        void write_byte(  int b){
		try{

			out.write(b);
		}catch(IOException e){}
	}
	

	
		
	 final short NUM_QUANT_TBLS=4;
	 final short DCTSIZE=8;
	 final short NUM_ARITH_TBLS=4;
	

	// JPEG marker codes 
	public  final byte	  
	  SOF0  = (byte)0xc0,

	  
	  DHT   = (byte)0xc4,
	  
	  
	  SOI   = (byte)0xd8,
	  EOI   = (byte)0xd9,
	  SOS   = (byte)0xda,
	  DQT   = (byte)0xdb ;


	void
	write_marker ( byte mark)
	// write a marker code 
	{
	  write_byte(0xFF);
	  write_byte( mark);
	}


	void
	write_2bytes ( int value)

	{
	  write_byte( (value >> 8) & 0xFF);
	  write_byte( value & 0xFF);
	}


        void
	write_dqt ( int index)

	{
	  int i;
	  write_marker( DQT);
	  
	  // table size
	  write_2bytes( DCTSIZE*DCTSIZE + 1 + 2);
	  
	  // precision and table id, precision always 0 (8bit)
	  write_byte( index);

          int i1;
            for( i1=0 ; i1<64 ; i1++)
            {
                write_byte(jpg.QTables1[index][i1]);
              
            }
 
	}


	void
	write_dht (int index, boolean is_ac)
	// write a DHT marker 
	{
	  JpegImage.Huffman htbl;
	  int length, i;
	  if(is_ac)
          {
              htbl = jpg.Huff[index+2];
              index += 0x10;
          }

          else
          {
              htbl = jpg.Huff[index];
          }

	  
	  if (! htbl.sent_table) 
          {
	    write_marker( DHT);
	    
	    length = 0;
	   for (i = 1; i <= 16; i++)
	      length+=htbl.count[i];
	    write_2bytes( length + 2 + 1 + 16);
	    write_byte( index);
 	    for (i = 1; i <= 16; i++)
	      write_byte(  htbl.count[i]);
	    
	    for (i = 0; i < length; i++)
	      write_byte(  htbl.values1[i]);

	    
	    htbl.sent_table = true;
	  }
	}


        void
	write_sof (byte code)
	// write a SOF marker 
	{
	  int i;
	  
	  write_marker( code);
	  
	  write_2bytes( 3 * jpg.ncomponents + 2 + 5 + 1); // length 

	  write_byte(8);
	  write_2bytes( (int) jpg.imgHeight);
	  write_2bytes( (int) jpg.imgWidth);

	  write_byte(jpg.ncomponents);

	  for (i = 0; i < jpg.ncomponents; i++) {
	    write_byte(i+1 );
	    write_byte((jpg.component[i].blocksPerMCUHorz << 4)
			     + jpg.component[i].blocksPerMCUVert);
	    write_byte(jpg.component[i].iddequant);
	  }
	}


        void
	write_sos ()
	// write a SOS marker 
	{
	  int i;
	  
	  write_marker( SOS);
	  
	  write_2bytes( 2 * jpg.ncomponents + 2 + 1 + 3); // length 
	  
	  write_byte(jpg.ncomponents);
	  
	  for (i = 0; i < jpg.ncomponents; i++) {
	    write_byte( i+1);
	    write_byte((jpg.component[i].hdc<< 4)
			     +jpg.component[i].hac);
	  }

	  write_byte( 0);		// Spectral selection start 
	  write_byte( DCTSIZE*DCTSIZE-1);	// Spectral selection end 
	  write_byte( 0);		// Successive approximation 
	}



	// Write the file header.
	 
	public  void
	write_file_header ()
	{
	  short qt_in_use[] = new short[NUM_QUANT_TBLS];
	  int i, prec;
	  boolean is_baseline;
	  
	  write_marker( SOI);	// first the SOI 

	  // write DQT for each quantization table. 

	  for (i = 0; i < NUM_QUANT_TBLS; i++)
	    qt_in_use[i] = 0;

	  for (i = 0; i < jpg.ncomponents; i++)
	    qt_in_use[jpg.component[i].iddequant] = 1;

	  prec = 0;
	  for (i = 0; i < NUM_QUANT_TBLS; i++) {
	    if (qt_in_use[i] != 0)
	      write_dqt( i);
	  }

	  is_baseline = true;


	    write_sof(SOF0);	// SOF code for baseline implementation 
	
	}





	public  void
	write_scan_header ()
	{
	  int i;
	  

	  for (i = 0; i < jpg.ncomponents; i++) {
	    write_dht(jpg.component[i].hdc, false);
	    write_dht(jpg.component[i].hac, true);
	  }

	  write_sos();
	}






	public  void
	write_file_trailer ()
	{
	  write_marker(EOI);
		try	{
			out.flush();
		} catch (IOException e){}
	}
    
}

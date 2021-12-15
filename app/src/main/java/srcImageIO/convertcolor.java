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
public class convertcolor {
    	short pixel_row[][];

	/** buffer to retieve JimiImage pixel data **/
	 int[]  jiIntBuf_;
	 byte[]  jiBufByt;
	
  // defines color spaces 
	static final short CS_UNKNOWN=0;
	static final short CS_GRAYSCALE=1;
	static final short CS_RGB=2;
	static final short CS_YCbCr=3;

	static final short SCALEBITS=16;
	static final int ONE_HALF=(1<<(SCALEBITS-1));

	static final short MAXJSAMPLE = 256;
	int rgb_ycc_tab[];	// => table for RGB to YCbCr conversion 
	static final int R_Y_OFF=0;			// offset to R => Y section 
	static final int G_Y_OFF=(1*MAXJSAMPLE);	// offset to G => Y section 
	static final int B_Y_OFF=(2*MAXJSAMPLE);	// etc. 
	static final int R_CB_OFF=(3*MAXJSAMPLE);
	static final int G_CB_OFF=(4*MAXJSAMPLE);
	static final int B_CB_OFF=(5*MAXJSAMPLE);
	static final int R_CR_OFF=B_CB_OFF;		// B=>Cb, R=>Cr are the same 
	static final int G_CR_OFF=(6*MAXJSAMPLE);
	static final int B_CR_OFF=(7*MAXJSAMPLE);
	static final int TABLE_SIZE=(8*MAXJSAMPLE);
        Image ji;
    public int row_idx_=0;
    public void get_rgb_ycc_rows ( int rows_to_read, short[][][] image_data)  
	{
		int r, g, b;
		int col;
		int row;
		int rowSource;
		
		int rowEnd = row_idx_ + rows_to_read;
		int width = ji.Width;
		int val;
		int[] buf = jiIntBuf_;
		int i;

		// local variable copy of reference
		int[] rgb_ycc_tabL = rgb_ycc_tab;	// small gain :)

		rowSource = row_idx_;
		for (row = 0; row < rows_to_read; ++row)
		{
			getChannel(ji, rowSource, buf, 0);

		    for (col = 0; col < width; col++)
			{
				val = buf[col];
				val<<=8;
				b = (val >>> 24) & 0xFF;
                                val<<=8;
				g = (val >>> 24) & 0xFF;
                                val<<=8;
				r = (val>>>24) & 0xFF;

				// Y 
				image_data[0][row][col] = (short)
						((rgb_ycc_tabL[r+R_Y_OFF] +
						  rgb_ycc_tabL[g+G_Y_OFF] +
						  rgb_ycc_tabL[b+B_Y_OFF]) >> SCALEBITS);
                                
				// Cb 
				image_data[1][row][col] = (short)
						((rgb_ycc_tabL[r+R_CB_OFF] + 
						  rgb_ycc_tabL[g+G_CB_OFF] +
						  rgb_ycc_tabL[b+B_CB_OFF]) >> SCALEBITS);
				// Cr 
				image_data[2][row][col] = (short)
						((rgb_ycc_tabL[r+R_CR_OFF] +
						  rgb_ycc_tabL[g+G_CR_OFF] +
						  rgb_ycc_tabL[b+B_CR_OFF]) >> SCALEBITS);
			}
			++rowSource;
		}

		row_idx_ += rows_to_read;
	}
	
	
	private void getChannel(Image image, int row, int[] pixels, int off)
	{
		int w = image.Width;
		int h = image.Height;
                for(int i=0;i<w;i++)
                {
                    pixels[i]=image.RGBA.get(row*w+i);
                }
		//image.getRGB(pixels, off, w, 0, row, w, 1);
	}

        public void rgb_ycc_init ()
	{
		int i;

		// Allocate a workspace for the result of get_input_row. 
		pixel_row = new short[3][ji.Width];

		// Allocate and fill in the conversion tables. 
		rgb_ycc_tab = new int[TABLE_SIZE];

		// original. using multiply for each array being initialised
		for (i = 0; i < MAXJSAMPLE; i++)
		{
		    rgb_ycc_tab[i+R_Y_OFF] = 19595 * i;
		    rgb_ycc_tab[i+G_Y_OFF] = 38470 * i;
		    rgb_ycc_tab[i+B_Y_OFF] = 7471 * i     + ONE_HALF;
		    rgb_ycc_tab[i+R_CB_OFF] = -11059 * i;
		    rgb_ycc_tab[i+G_CB_OFF] = -21710 * i;
		    rgb_ycc_tab[i+B_CB_OFF] = 32768 * i    + ONE_HALF*MAXJSAMPLE;

		    rgb_ycc_tab[i+G_CR_OFF] = -27439 * i;
		    rgb_ycc_tab[i+B_CR_OFF] = -5329 * i;
		}

        row_idx_ = 0;    // first row
		jiIntBuf_ = new int[ji.Width];
	}


        
}

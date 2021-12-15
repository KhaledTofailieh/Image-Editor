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
public class Sampling {
    public void DownSampling(int v,int h, int numcols , int numrows  ,short [][] input_data, short [][] output_data)
    {

        if(v==1&&h==1)
            h1v1(numcols,numrows,input_data,output_data);
        else if(v==2&&h==2)
            h2v2(numcols,numrows,input_data,output_data);
        else if(h==2&&v==1)
            h2v1(numcols,numrows,input_data,output_data);
    }

    private void h2v1(int numcols, int numrows, short[][] input_data, short[][] output_data) {
        int outrow;
	  int outcol,incol;

	  for (outrow = 0; outrow < numrows; outrow++) {
	    for (outcol = 0, incol=0; outcol < numcols; outcol++, incol+=2) {
	      output_data[outrow][outcol] = (short)
	      	((input_data[outrow][incol] + input_data[outrow][incol+1] + 1)/2);
	    }
	  }
        
    }

    private void h2v2(int numcols, int numrows, short[][] input_data, short[][] output_data) {
        
        int inrow, outrow;
	  int outcol, incol;

	  inrow = 0;
	  for (outrow = 0; outrow < numrows; outrow++) {
	    for (outcol = 0, incol = 0; outcol < numcols; outcol++, incol+=2) {
	    	output_data[outrow][outcol] = (short)((input_data[inrow][incol] + 
	    				input_data[inrow][incol+1] + input_data[inrow+1][incol] + 
	    				input_data[inrow+1][incol+1] + 2) / 4);
	    }
	    inrow += 2;
	  }
    }

    private void h1v1(int numcols, int numrows, short[][] input_data, short[][] output_data) {
        
        int outcol, outrow;
		
		for (outrow = 0; outrow < numrows; outrow++)
			for (outcol = 0; outcol < numcols; outcol++)
				output_data[outrow][outcol] = input_data[outrow][outcol];
    }
    

    public void  Expandedge(int input_rows, int output_rows ,int input_cols, int output_cols, int ncomponents,short [][][] image_data)
    {

         if (input_cols < output_cols) {
	    short pixval;
	    int count;
	    int row;
	    short ci;
	    int numcols = output_cols - input_cols;

	    for (ci = 0; ci < ncomponents; ci++) {
	      for (row = 0; row < input_rows; row++) {
					pixval = image_data[ci][row][input_cols-1];
					for (count = numcols; count > 0; count--)
					  image_data[ci][row][output_cols-count] = pixval;
	      }
	    }
	  }

        
	  if (input_rows < output_rows) {
	    int row, col;
	    short ci;

	    for (ci = 0; ci < ncomponents; ci++) {
	      for (row = input_rows; row < output_rows; row++) {
	      	for (col = 0; col < output_cols; col++) {
	      		image_data[ci][row][col] = image_data[ci][input_rows-1][col];
	      	}
	      }
	    }
	  }
    }
    }


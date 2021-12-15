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
public class Fwddct {
    static final int DCTSIZE = 8;
	 final int CONST_BITS = 13;
	 final short PASS1_BITS = 2;

	 final int CONST_SCALE = (1 << CONST_BITS);



	 final int FIX_0_298631336  =  2446;	 
	 final int FIX_0_390180644  =  3196;	 
	 final int FIX_0_541196100  =  4433;	 
	 final int FIX_0_765366865  =  6270;	 
	 final int FIX_0_899976223  =  7373;	 
	 final int FIX_1_175875602  =  9633;	 
	 final int FIX_1_501321110  =  12299;	 
	 final int FIX_1_847759065  =  15137;	 
	 final int FIX_1_961570560  =  16069;	 
	 final int FIX_2_053119869  =  16819;	 
	 final int FIX_2_562915447  =  20995;	 
	 final int FIX_3_072711026  =  25172;	 




	public  int deScale(int f, int n)
	{
		return (int)((f + (1 << (n-1)))>> n);
	}




	public  void fwd_dct (int [] data)
	{
		int tmp0, tmp1, tmp2, tmp3, tmp4, tmp5, tmp6, tmp7;
		int tmp10, tmp11, tmp12, tmp13;
		int z1, z2, z3, z4, z5;
		int rowctr;
		int row, col;

		row = 0;
		for (rowctr = DCTSIZE; --rowctr >= 0; )
		{
			tmp0 = data[row+0] + data[row+7];
			tmp7 = data[row+0] - data[row+7];
			tmp1 = data[row+1] + data[row+6];
			tmp6 = data[row+1] - data[row+6];
			tmp2 = data[row+2] + data[row+5];
			tmp5 = data[row+2] - data[row+5];
			tmp3 = data[row+3] + data[row+4];
			tmp4 = data[row+3] - data[row+4];


			tmp10 = tmp0 + tmp3;
			tmp13 = tmp0 - tmp3;
			tmp11 = tmp1 + tmp2;
			tmp12 = tmp1 - tmp2;

			data[row+0] = (tmp10 + tmp11) << PASS1_BITS;
			data[row+4] = (tmp10 - tmp11) << PASS1_BITS;

			z1 = (tmp12 + tmp13) * FIX_0_541196100;
			data[row+2] = deScale(z1 + (tmp13 * FIX_0_765366865),
						   CONST_BITS-PASS1_BITS);
			data[row+6] = deScale(z1 + (tmp12 * (-1) * FIX_1_847759065),
						   CONST_BITS-PASS1_BITS);


			z1 = tmp4 + tmp7;
			z2 = tmp5 + tmp6;
			z3 = tmp4 + tmp6;
			z4 = tmp5 + tmp7;
			z5 = (z3 + z4) * FIX_1_175875602;  

			tmp4 = tmp4 * FIX_0_298631336; 		
			tmp5 = tmp5 * FIX_2_053119869; 		
			tmp6 = tmp6 * FIX_3_072711026; 		
			tmp7 = tmp7 * FIX_1_501321110; 		 
			z1 = z1 * (-1) * FIX_0_899976223; 
			z2 = z2 * (-1) * FIX_2_562915447;  
			z3 = z3 * (-1) * FIX_1_961570560;  
			z4 = z4 * (-1) * FIX_0_390180644;  

			z3 += z5;
			z4 += z5;

			data[row+7] = deScale(tmp4 + z1 + z3, CONST_BITS-PASS1_BITS);
			data[row+5] = deScale(tmp5 + z2 + z4, CONST_BITS-PASS1_BITS);
			data[row+3] = deScale(tmp6 + z2 + z3, CONST_BITS-PASS1_BITS);
			data[row+1] = deScale(tmp7 + z1 + z4, CONST_BITS-PASS1_BITS);

			row += DCTSIZE;		 
	    }


		col = 0;
		for (rowctr = DCTSIZE; --rowctr >= 0; )
		{
			tmp0 = data[DCTSIZE*0+col] + data[DCTSIZE*7+col];
			tmp7 = data[DCTSIZE*0+col] - data[DCTSIZE*7+col];
			tmp1 = data[DCTSIZE*1+col] + data[DCTSIZE*6+col];
			tmp6 = data[DCTSIZE*1+col] - data[DCTSIZE*6+col];
			tmp2 = data[DCTSIZE*2+col] + data[DCTSIZE*5+col];
			tmp5 = data[DCTSIZE*2+col] - data[DCTSIZE*5+col];
			tmp3 = data[DCTSIZE*3+col] + data[DCTSIZE*4+col];
			tmp4 = data[DCTSIZE*3+col] - data[DCTSIZE*4+col];

		
			tmp10 = tmp0 + tmp3;
			tmp13 = tmp0 - tmp3;
			tmp11 = tmp1 + tmp2;
			tmp12 = tmp1 - tmp2;

			data[DCTSIZE*0+col] = deScale(tmp10 + tmp11, PASS1_BITS+3);
			data[DCTSIZE*4+col] = deScale(tmp10 - tmp11, PASS1_BITS+3);

			z1 = (tmp12 + tmp13) * FIX_0_541196100;
			data[DCTSIZE*2+col] = deScale(z1 + tmp13 * FIX_0_765366865,
							   CONST_BITS+PASS1_BITS+3);
			data[DCTSIZE*6+col] = deScale(z1 + tmp12 * (-1) * FIX_1_847759065,
							   CONST_BITS+PASS1_BITS+3);

			 
			z1 = tmp4 + tmp7;
			z2 = tmp5 + tmp6;
			z3 = tmp4 + tmp6;
			z4 = tmp5 + tmp7;
			z5 = (z3 + z4) * FIX_1_175875602; 

			tmp4 = tmp4 * FIX_0_298631336;  
			tmp5 = tmp5 * FIX_2_053119869;  
			tmp6 = tmp6 * FIX_3_072711026;  
			tmp7 = tmp7 * FIX_1_501321110;  
			z1 = z1 * (-1) * FIX_0_899976223;  
			z2 = z2 * (-1) * FIX_2_562915447;  
			z3 = z3 * (-1) * FIX_1_961570560;  
			z4 = z4 * (-1) * FIX_0_390180644; 

			z3 += z5;
			z4 += z5;

			data[DCTSIZE*7+col] = deScale(tmp4 + z1 + z3, CONST_BITS+PASS1_BITS+3);
			data[DCTSIZE*5+col] = deScale(tmp5 + z2 + z4, CONST_BITS+PASS1_BITS+3);
			data[DCTSIZE*3+col] = deScale(tmp6 + z2 + z3, CONST_BITS+PASS1_BITS+3);
			data[DCTSIZE*1+col] = deScale(tmp7 + z1 + z4, CONST_BITS+PASS1_BITS+3);

			col++;			 
		}
	}
}

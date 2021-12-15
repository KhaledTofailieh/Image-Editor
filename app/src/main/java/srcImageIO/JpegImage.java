/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcImageIO;

import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author قصي حسين
 */
class JpegImage {

    short[] last_dc_val;
    short[][] QTables1;
    int Vmax, Hmax;

    byte[][] QTables;

    Huffman[] Huff;

    Component[] component;

    int mcuCountY;

    int mcuCountX;

    int imgHeight, imgWidth;

    int ncomponents;

    int restartInterval;

    JpegImage(boolean decoder) {
        QTables = new byte[4][64];
        Huff = new Huffman[8];
        component = new Component[3];
        restartInterval = 0;
    }

    JpegImage() {
        Tables tbl=new Tables();
        component = new Component[3];
        QTables1=new short[2][64];
        tbl.getLuminanceQuantTable(QTables1[0]);
        tbl.getChrominanceQuantTable(QTables1[1]);
        Huff=new Huffman[4];
        Huff[0]=new Huffman(tbl.dc_luminance_bits,tbl.dc_luminance_val);
        Huff[1]=new Huffman(tbl.dc_chrominance_bits, tbl.dc_chrominance_val);
        Huff[2]=new Huffman(tbl.ac_luminance_bits, tbl.ac_luminance_val);
        Huff[3]=new Huffman(tbl.ac_chrominance_bits, tbl.ac_chrominance_val);
        this.last_dc_val=new short[3];
    }

    public static class Huffman {

        static final int FAST_BITS = 9;
        static final int FAST_MASK = (1 << FAST_BITS) - 1;
          boolean sent_table;
          int[] code;
          short[] values1;
        short[] fast;
         byte[] size;
         byte[] values;
         int[] maxCode;
         int[] delta;
         short[] count;

        public Huffman(byte[] count, int numSymbols) throws IOException {

            fast = new short[1 << FAST_BITS];
            size = new byte[numSymbols];
            values = new byte[numSymbols];
            maxCode = new int[18];
            delta = new int[17];

            for (int i = 0, k = 0; i < 16; i++) {
                for (int j = 0; j < count[i]; j++) {
                    size[k++] = (byte) (i + 1);
                }
            }

            final int[] code = new int[256];
            int i = 1, k = 0;

            for (int c = 0; i <= 16; i++) {
                delta[i] = (k - c);

                if (k < numSymbols && size[k] == i) {
                    do {
                        code[k++] = c++;

                    } while (k < numSymbols && size[k] == i);

                    if (c - 1 >= (1 << i)) {
                        throw new IOException();
                    }
                }
                maxCode[i] = (c << (16 - i));
                c <<= 1;
            }

            maxCode[i] = Integer.MAX_VALUE;

            Arrays.fill(fast, (short) 255);

            for (i = 0; i < values.length; i++) {
                int s = size[i];

                if (s <= FAST_BITS) {
                    int c = code[i] << (FAST_BITS - s);
                    int m = 1 << (FAST_BITS - s);

                    for (int j = 0; j < m; j++) {
                        fast[c + j] = (short) i;
                    }
                }

            }
        }

        public Huffman(short[] count , short []val)
        {
            this.count=new short[17];
            this.values1=new short[256];
            this.code=new int[256];
            this.size=new byte[256];
            this.sent_table=false;
            for(int i=0; i<count.length ;i++)
                this.count[i]=count[i];
            for(int i=0 ; i<val.length; i++)
                this.values1[i]=val[i];
        }

        int getNumSymbols() {
            return values.length;
        }

    }

    public static class Component {

        int hac , hdc;
        int upsampler ;
        int outPos;
        int blocksPerMCUHorz;
        int blocksPerMCUVert;
        int iddequant;
        Huffman huffDC;
        int dcPred = 0;
        Huffman huffAC;
        int id;
        int width;
        int height;
        int minReqWidth;
        int minReqHeight;

        Component(int id) {
            this.id = id;
        }

    }

}

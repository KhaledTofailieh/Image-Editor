/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcImageIO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 *
 * @author قصي حسين
 */
public class JpegDecoder extends ImageDecoder{

    private JpegImage comimg;

    

    private byte[][] decodeTmp;

    private byte[][] upsampleTmp;

    private int currentMCURow;

    private IDCT_2D idct_2d;

    private int codeBuffer, codeBits;

    private boolean nomore;

    String Path;

    private int todo;

    private int marker;

    int outPos;

    private short[] data;



    


    


    public JpegDecoder(FileInputStream in) {

        comimg = new JpegImage(true);
        reader = new JpegReader(in, comimg);
        idct_2d = new IDCT_2D();

        codeBuffer = 0;
        codeBits = 0;

        outPos = 0;
        currentMCURow = 0;
        this.nomore = false;
        this.marker = 0xFF;
        data = new short[64];

    }

    private void reset() {

        codeBits = 0;

        codeBuffer = 0;

        nomore = false;

        marker = 0xFF;

        if (comimg.restartInterval != 0) {

            todo = comimg.restartInterval;

        } else {

            todo = Integer.MAX_VALUE;

        }

        for (JpegImage.Component c : comimg.component) {

            c.dcPred = 0;

        }

    }

    private boolean checkRestart() throws IOException {

        if (codeBits < 24) {

            growBufferUnsafe();

        }

        if (marker >= 0xD0 && marker <= 0xD7) {

            reset();

            return true;

        }

        return false;

    }

    public Image Read() throws IOException, Unsupported, OutOfMemoryError {


        reader.processMarker();


        reset();


        ImageQuantization.computeSizerate(comimg.imgHeight , comimg.imgWidth);

        img = new Image((int) (comimg.imgHeight / ImageQuantization.inversesizerate), (int) (comimg.imgWidth / ImageQuantization.inversesizerate));


        ImageQuantization.reset((int)(comimg.imgHeight / ImageQuantization.inversesizerate), (int)(comimg.imgWidth / ImageQuantization.inversesizerate), comimg.imgWidth);







        decodeRGB();


        return img;
    }

    private void decodeRGB() throws IOException {

        final int YUVstride = comimg.mcuCountX * comimg.Hmax * 8;

        final boolean requiresUpsampling = allocateDecodeTmp(YUVstride);

        final byte[] YtoRGB = (comimg.component[0].upsampler != 0) ? upsampleTmp[0] : decodeTmp[0];

        final byte[] UtoRGB = (comimg.component[1].upsampler != 0) ? upsampleTmp[1] : decodeTmp[1];

        final byte[] VtoRGB = (comimg.component[2].upsampler != 0) ? upsampleTmp[2] : decodeTmp[2];

        for (int j = 0; j < comimg.mcuCountY; j++) {

            DecodeMcuRow();

            if (requiresUpsampling ) {

                doUpsampling(YUVstride);

            }

            int n = comimg.Vmax * 8;

            n = Math.min(comimg.imgHeight - (currentMCURow - 1) * n, n);

            for (int i = 0; i < n; i++) {

                if (ImageQuantization.rows%ImageQuantization.inversesizerate==0)
                {
                    YUVtoRGB(YtoRGB, UtoRGB, VtoRGB, i * YUVstride, comimg.imgWidth);
                }

                ImageQuantization.rows++;

                outPos = 0;

            }

            if (marker != 0xFF) {


                break;
            }

        }

    }

    private boolean allocateDecodeTmp(int YUVstride) {

        if (decodeTmp == null) {

            decodeTmp = new byte[3][];

        }

        boolean requiresUpsampling = false;

        for (int compIdx = 0; compIdx < 3; compIdx++) {

            JpegImage.Component c = comimg.component[compIdx];

            int reqSize = c.minReqWidth * c.blocksPerMCUVert * 8;
            if (decodeTmp[compIdx] == null || decodeTmp[compIdx].length < reqSize) {

                decodeTmp[compIdx] = new byte[reqSize];

            }

            if (c.upsampler != 0) {

                if (upsampleTmp == null) {

                    upsampleTmp = new byte[3][];

                }

                int upsampleReq = comimg.Vmax * 8 * YUVstride;

                if (upsampleTmp[compIdx] == null || upsampleTmp[compIdx].length < upsampleReq) {

                    upsampleTmp[compIdx] = new byte[upsampleReq];

                }

                requiresUpsampling = true;

            }

        }

        return requiresUpsampling;

    }

    private void DecodeMcuRow() throws IOException {
        ++currentMCURow;

        for (int i = 0; i < comimg.mcuCountX; i++) {

            for (int compIdx = 0; compIdx < 3; compIdx++) {

                JpegImage.Component c = comimg.component[compIdx];

                int outStride = c.minReqWidth;

                int outPosY = 8 * i * c.blocksPerMCUHorz;

                for (int y = 0; y < c.blocksPerMCUVert; y++, outPosY += 8 * outStride) {

                    for (int x = 0, outPos = outPosY; x < c.blocksPerMCUHorz; x++, outPos += 8) {

                        decodeblock(c, data);



                            idct_2d.compute(decodeTmp[compIdx], outPos, outStride, data);


                    }

                }

            }

            if (--todo <= 0) {

                if (!checkRestart()) {

                    break;

                }

            }

        }

    }

    private void YUVtoRGB(byte[] inY, byte[] inU, byte[] inV, int inPos, int count) {

    
        do {

  
            int y = (inY[inPos] & 255);

            int u = (inU[inPos] & 255) - 128;

            int v = (inV[inPos] & 255) - 128;

            int r = y + ((32768 + v * 91881) >> 16);

            int g = y + ((32768 - v * 46802 - u * 22554) >> 16);

            int b = y + ((32768 + u * 116130) >> 16);

            if (r > 255) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }

            if (g > 255) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }

            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }


            int rgb = ( 255 );
            rgb <<= 24;
            int gg = (b & 255);
            gg <<= 16;
            int rr = (g & 255);
            rr <<= 8;
            rgb = rgb | gg | rr | r;

            if(ImageQuantization.setPixelInBlankImage())
            {
                img.RGBA.put(ImageQuantization.index++, rgb);
                ImageQuantization.columns++;
            }
            
            
         

            outPos += 3;
            inPos++;

        } while (--count > 0);


    }

   
    
    private void decodeblock(JpegImage.Component cc, short[] data) throws IOException {

        final byte[] dq = comimg.QTables[cc.iddequant];

        Arrays.fill(data, (short) 0);

        {

            int t = 0;

            t = decodeFast(cc.huffDC);

            int dc = cc.dcPred;

            if (t > 0) {

                dc += extendReceive(t);

                cc.dcPred = dc;

            }

            data[0] = (short) (dc * (dq[0] & 0xFF));

        }

        final JpegImage.Huffman hac = cc.huffAC;

        int k = 1;

        do {

            int rs = 0;

            rs = decodeFast(hac);

            k += rs >> 4;

            int s = rs & 15;

            if (s != 0) {

                int v = extendReceive(s) * (dq[k] & 0xFF);

                data[dezigzag[k]] = (short) v;

            } else if (rs != 0xF0) {

                break;

            }

        } while (++k < 64);
    }

    private int decodeFast(JpegImage.Huffman h) throws IOException {

        if (codeBits < 16) {

            growBufferUnsafe();

        }

        int k = h.fast[codeBuffer >>> (32 - JpegImage.Huffman.FAST_BITS)] & 255;

        if (k < 0xFF) {

            int s = h.size[k];

            codeBuffer <<= s;

            codeBits -= s;

            return h.values[k] & 255;

        }

        return decodeSlow(h);

    }

    private int decodeSlow(JpegImage.Huffman h) {

        int temp = codeBuffer >>> 16;

        int s = JpegImage.Huffman.FAST_BITS + 1;

        while (temp >= h.maxCode[s]) {

            s++;

        }

        int k = (temp >>> (16 - s)) + h.delta[s];

        codeBuffer <<= s;

        codeBits -= s;

        return h.values[k] & 255;

    }

    private int extendReceive(int n) throws IOException {
        if (codeBits < 24) {

            growBufferUnsafe();

        }

        int k = codeBuffer >>> (32 - n);

        codeBuffer <<= n;

        codeBits -= n;

        int limit = 1 << (n - 1);

        if (k < limit) {

            k -= limit * 2 - 1;

        }

        return k;

    }

    private void growBufferUnsafe() throws IOException {

        do {

            int b = 0;

            if (!nomore) {

                b = reader.getU8();

                if (b == 0xFF) {

                    try {
                        growBufferCheckMarker();
                    } catch (IOException ex) {
                        break;
                    }

                }

            }

            codeBuffer |= b << (24 - codeBits);

            codeBits += 8;

        } while (codeBits <= 24);

    }

    private void growBufferCheckMarker() throws IOException {

        int c = reader.getU8();

        if (c != 0) {

            nomore = true;
            if (c >= 0xD0 && c <= 0xD7) {
                marker = c;
            } else {
                if (c != 0xD9) {
                    throw new RuntimeException();
                }

                throw new IOException();
            }
        }

    }

    private void doUpsampling(int YUVstride) {

        for (int compIdx = 0; compIdx < 3; compIdx++) {

            JpegImage.Component c = comimg.component[compIdx];

            int inStride = c.minReqWidth;

            int height = c.blocksPerMCUVert * 8;

            switch (c.upsampler) {

                case 1:

                    for (int i = 0; i < height; i++) {

                        upsampleH2(upsampleTmp[compIdx], i * YUVstride, decodeTmp[compIdx], i * inStride, c.width);

                    }

                    break;

                case 2:

                    for (int i = 0, inPos0 = 0, inPos1 = 0; i < height; i++) {

                        upsampleV2(upsampleTmp[compIdx], (i * 2) * YUVstride, decodeTmp[compIdx], inPos0, inPos1, c.width);

                        upsampleV2(upsampleTmp[compIdx], (i * 2 + 1) * YUVstride, decodeTmp[compIdx], inPos1, inPos0, c.width);

                        inPos0 = inPos1;

                        inPos1 += inStride;

                    }

                case 3:

                    for (int i = 0, inPos0 = 0, inPos1 = 0; i < height; i++) {

                        upsampleHV2(upsampleTmp[compIdx], (i * 2) * YUVstride, decodeTmp[compIdx], inPos0, inPos1, c.width);

                        upsampleHV2(upsampleTmp[compIdx], (i * 2 + 1) * YUVstride, decodeTmp[compIdx], inPos1, inPos0, c.width);

                        inPos0 = inPos1;

                        inPos1 += inStride;

                    }

                    break;

            }

        }

    }

    private static void upsampleH2(byte[] out, int outPos, byte[] in, int inPos, int width) {

        if (width == 1) {

            out[outPos] = out[outPos + 1] = in[inPos];

        } else {

            int i0 = in[inPos] & 255;

            int i1 = in[inPos + 1] & 255;

            out[outPos] = (byte) i0;

            out[outPos + 1] = (byte) ((i0 * 3 + i1 + 2) >> 2);

            for (int i = 2; i < width; i++) {

                int i2 = in[inPos + i] & 255;

                int n = i1 * 3 + 2;

                out[outPos + i * 2 - 2] = (byte) ((n + i0) >> 2);

                out[outPos + i * 2 - 1] = (byte) ((n + i2) >> 2);

                i0 = i1;

                i1 = i2;

            }

            out[outPos + width * 2 - 2] = (byte) ((i0 * 3 + i1 + 2) >> 2);

            out[outPos + width * 2 - 1] = (byte) i1;

        }

    }

    private static void upsampleV2(byte[] out, int outPos, byte[] in, int inPos0, int inPos1, int width) {

        for (int i = 0; i < width; i++) {

            out[outPos + i] = (byte) ((3 * (in[inPos0 + i] & 255) + (in[inPos1 + i] & 255) + 2) >> 2);

        }

    }

    private static void upsampleHV2(byte[] out, int outPos, byte[] in, int inPos0, int inPos1, int width) {

        if (width == 1) {

            int i0 = in[inPos0] & 255;

            int i1 = in[inPos1] & 255;

            out[outPos] = out[outPos + 1] = (byte) ((i0 * 3 + i1 + 2) >> 2);

        } else {

            int i1 = 3 * (in[inPos0] & 255) + (in[inPos1] & 255);

            out[outPos] = (byte) ((i1 + 2) >> 2);

            for (int i = 1; i < width; i++) {

                int i0 = i1;

                i1 = 3 * (in[inPos0 + i] & 255) + (in[inPos1 + i] & 255);

                out[outPos + i * 2 - 1] = (byte) ((3 * i0 + i1 + 8) >> 4);

                out[outPos + i * 2] = (byte) ((3 * i1 + i0 + 8) >> 4);

            }

            out[outPos + width * 2 - 1] = (byte) ((i1 + 2) >> 2);

        }

    }

  
    static final char dezigzag[] = ("\0\1\10\20\11\2\3\12"
            + "\21\30\40\31\22\13\4\5"
            + "\14\23\32\41\50\60\51\42"
            + "\33\24\15\6\7\16\25\34"
            + "\43\52\61\70\71\62\53\44"
            + "\35\26\17\27\36\45\54\63"
            + "\72\73\64\55\46\37\47\56"
            + "\65\74\75\66\57\67\76\77"
            + "\77\77\77\77\77\77\77\77"
            + "\77\77\77\77\77\77\77").toCharArray();

    private static class IDCT_2D {

        public IDCT_2D() {
        }

        final int[] tmp2D = new int[64];
        private static int c0 = f2f(0.541196100);
        private static int c1 = f2f(-1.847759065);
        private static int c2 = f2f(0.765366865);
        private static int c3 = f2f(1.175875602);
        private static int c4 = f2f(0.298631336);
        private static int c5 = f2f(2.053119869);
        private static int c6 = f2f(3.072711026);
        private static int c7 = f2f(1.501321110);
        private static int c8 = f2f(-0.899976223);
        private static int c9 = f2f(-2.562915447);
        private static int c10 = f2f(-1.961570560);
        private static int c11 = f2f(-0.390180644);

        private void computeV(short[] data) {
            final int[] tmp = tmp2D;
            int i = 0;
            do {
                int s0 = data[i];
                int s1 = data[i + 8];
                int s2 = data[i + 16];
                int s3 = data[i + 24];
                int s4 = data[i + 32];
                int s5 = data[i + 40];
                int s6 = data[i + 48];
                int s7 = data[i + 56];

                int p1, p2, p3, p4, p5;
                p1 = (s2 + s6) * c0;
                p2 = ((s0 + s4) << 12) + 512;
                p3 = ((s0 - s4) << 12) + 512;
                p4 = p1 + s6 * c1;
                p5 = p1 + s2 * c2;

                int x0 = p2 + p5;
                int x3 = p2 - p5;
                int x1 = p3 + p4;
                int x2 = p3 - p4;

                p1 = s7 + s1;
                p2 = s5 + s3;
                p3 = s7 + s3;
                p4 = s5 + s1;
                p5 = (p3 + p4) * c3;

                p1 = p5 + p1 * c8;
                p2 = p5 + p2 * c9;
                p3 = p3 * c10;
                p4 = p4 * c11;

                int t0 = s7 * c4 + p1 + p3;
                int t1 = s5 * c5 + p2 + p4;
                int t2 = s3 * c6 + p2 + p3;
                int t3 = s1 * c7 + p1 + p4;

                tmp[i] = (x0 + t3) >> 10;
                tmp[i + 56] = (x0 - t3) >> 10;
                tmp[i + 8] = (x1 + t2) >> 10;
                tmp[i + 48] = (x1 - t2) >> 10;
                tmp[i + 16] = (x2 + t1) >> 10;
                tmp[i + 40] = (x2 - t1) >> 10;
                tmp[i + 24] = (x3 + t0) >> 10;
                tmp[i + 32] = (x3 - t0) >> 10;
            } while (++i < 8);
        }

        public final void compute(ByteBuffer out, int outPos, int outStride, short[] data) {
            computeV(data);

            final int[] tmp = tmp2D;

            for (int i = 0; i < 64; i += 8) {
                int s0 = tmp[i] + (257 << 4);
                int s1 = tmp[i + 1];
                int s2 = tmp[i + 2];
                int s3 = tmp[i + 3];
                int s4 = tmp[i + 4];
                int s5 = tmp[i + 5];
                int s6 = tmp[i + 6];
                int s7 = tmp[i + 7];

                int p1, p2, p3, p4, p5;

                p1 = (s2 + s6) * c0;
                p2 = ((s0 + s4) << 12);
                p3 = ((s0 - s4) << 12);
                p4 = p1 + s6 * c1;
                p5 = p1 + s2 * c2;

                int x0 = p2 + p5;
                int x3 = p2 - p5;
                int x1 = p3 + p4;
                int x2 = p3 - p4;

                p1 = s7 + s1;
                p2 = s5 + s3;
                p3 = s7 + s3;
                p4 = s5 + s1;
                p5 = (p3 + p4) * c3;

                p1 = p5 + p1 * c8;
                p2 = p5 + p2 * c9;
                p3 = p3 * c10;
                p4 = p4 * c11;

                int t0 = s7 * c4 + p1 + p3;
                int t1 = s5 * c5 + p2 + p4;
                int t2 = s3 * c6 + p2 + p3;
                int t3 = s1 * c7 + p1 + p4;

                out.put(outPos, clampShift17(x0 + t3));
                out.put(outPos + 7, clampShift17(x0 - t3));
                out.put(outPos + 1, clampShift17(x1 + t2));
                out.put(outPos + 6, clampShift17(x1 - t2));
                out.put(outPos + 2, clampShift17(x2 + t1));
                out.put(outPos + 5, clampShift17(x2 - t1));
                out.put(outPos + 3, clampShift17(x3 + t0));
                out.put(outPos + 4, clampShift17(x3 - t0));

                outPos += outStride;
            }
        }

        public final void compute(byte[] out, int outPos, int outStride, short[] data) {
            computeV(data);

            final int[] tmp = tmp2D;

            for (int i = 0; i < 64; i += 8) {
                int s0 = tmp[i] + (257 << 4);
                int s1 = tmp[i + 1];
                int s2 = tmp[i + 2];
                int s3 = tmp[i + 3];
                int s4 = tmp[i + 4];
                int s5 = tmp[i + 5];
                int s6 = tmp[i + 6];
                int s7 = tmp[i + 7];

                int p1, p2, p3, p4, p5;

                p1 = (s2 + s6) * c0;
                p2 = ((s0 + s4) << 12);
                p3 = ((s0 - s4) << 12);
                p4 = p1 + s6 * c1;
                p5 = p1 + s2 * c2;

                int x0 = p2 + p5;
                int x3 = p2 - p5;
                int x1 = p3 + p4;
                int x2 = p3 - p4;

                p1 = s7 + s1;
                p2 = s5 + s3;
                p3 = s7 + s3;
                p4 = s5 + s1;
                p5 = (p3 + p4) * c3;

                p1 = p5 + p1 * c8;
                p2 = p5 + p2 * c9;
                p3 = p3 * c10;
                p4 = p4 * c11;

                int t0 = s7 * c4 + p1 + p3;
                int t1 = s5 * c5 + p2 + p4;
                int t2 = s3 * c6 + p2 + p3;
                int t3 = s1 * c7 + p1 + p4;

                out[outPos] = clampShift17(x0 + t3);
                out[outPos + 7] = clampShift17(x0 - t3);
                out[outPos + 1] = clampShift17(x1 + t2);
                out[outPos + 6] = clampShift17(x1 - t2);
                out[outPos + 2] = clampShift17(x2 + t1);
                out[outPos + 5] = clampShift17(x2 - t1);
                out[outPos + 3] = clampShift17(x3 + t0);
                out[outPos + 4] = clampShift17(x3 - t0);

                outPos += outStride;
            }
        }

        private static strictfp int f2f(double x) {
            return (int) Math.round(Math.scalb(x, 12));
        }

        private static byte clampShift17(int x) {
            if (x < 0) {
                return 0;
            }

            if (x > (255 << 17)) {
                return (byte) 255;
            }

            return (byte) (x >>> 17);
        }
    }



}

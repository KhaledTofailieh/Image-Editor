/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcImageIO;

import java.io.FileInputStream;
import java.io.IOException;


/**
 *
 * @author قصي حسين
 */
public class JpegReader extends FileReader {

    private int marker;
    private boolean insideSOS, insideSOF;
    private JpegImage comimg;

    public JpegReader(FileInputStream file, JpegImage comimg) {
        super(file);
        this.insideSOS = false;
        this.insideSOF = false;
        this.comimg = comimg;
    }

    
    public void processMarker() throws IOException , Unsupported{

        while (!insideSOS) {
            marker = getU8();
            assure(marker != 0xFF);
            marker = getU8();

            
            if (marker >= 0xE0 && marker <= 0xEF) {
                Skip(getU16() - 2);
                continue;
            } else if (marker >= 0xF0 && marker <= 0xFD) {
                Skip(getU16() - 2);
                continue;
            }
            switch (marker) {
                case 0xFE: {
                    //COM
                    Skip(getU16() - 2);
                    break;
                }

                case 0xDD: {
                    //DRI
                    assure(getU16() != 4);
                    comimg.restartInterval = getU16();
                    break;
                }

                case 0xC4: {
                    //DHT
                    readDHT();

                    break;
                }

                case 0xDB: {
                    //DQT
                    readDQT();
                    break;
                }
                case 0xC0: {
                    //SOF0
                    insideSOF = true;
                    
                    readSOF();

                    break;
                }


                case 0xDA: {
                    //SOS
                    assure(insideSOS);
                    readSOS();
                    insideSOS = true;
                    return;
                }
                case 0xD9: {
                    //EOI
                    assure(insideSOS);
                }
                default: {
                    assure(true);
                }
            }
        }
    }

    private void readDHT() throws Unsupported , IOException {

        int length = getU16();

        int bytesleft = length - 2;

        while (bytesleft > 0) {

            int firstbyte = getU8();

            byte[] counts = new byte[16];

            readBytes(16, 0, counts);

            bytesleft -= 17;

            assure(bytesleft < 0);

            int kind = firstbyte >> 4;

            assure(kind >= 2);

            int id = firstbyte & 15;

            assure(id >= 2);

            int codestotal = 0;

            for (int i = 0; i < 16; i++) {
                codestotal += counts[i];
            }

            JpegImage.Huffman Huff = new JpegImage.Huffman(counts, codestotal);

            readBytes(codestotal, 0, Huff.values);

            bytesleft -= codestotal;

            assure(bytesleft < 0);

            comimg.Huff[kind * 4 + id] = Huff;
        }

    }

    private void readDQT() throws Unsupported, IOException {

        int length = getU16();

        int bytesleft = length - 2;
        assure(bytesleft <= 0 || bytesleft % 65 != 0);

        int nTables = bytesleft / 65;

        for (int i = 0; i < nTables; i++) {

            int firstbyte = getU8();

            int precisionflag = firstbyte >> 4;

            assure(precisionflag != 0);

            int id = firstbyte & 15;

            assure(id >= 2);

            readBytes(64, 0, comimg.QTables[id]);
        }

    }

    private void readSOF() throws Unsupported , IOException {

        int length = getU16();
        int firstbyte = getU8();
        assure(firstbyte != 8);
        int imgheight = getU16();
        int imgwidth = getU16();

        assure(imgwidth <= 0 || imgwidth <= 0);

        int nComponents = getU8();

        int expectedlength = 8 + 3 * nComponents;

        assure(length != expectedlength);

        assure( nComponents != 3);
        comimg.ncomponents = nComponents;

        int hmax = 1, vmax = 1;

        for (int c = 0; c < nComponents; c++) {
            JpegImage.Component comp = new JpegImage.Component(getU8());
            byte[] buffer = new byte[2];
            readBytes(2, 0, buffer);

            //assure((buffer[0] & 0xFF) != c + 1);
            int H = (buffer[0] & 0xFF) >> 4;
            int V = (buffer[0] & 0xFF) & 15;

            assure((H != 1 && H != 2 && H != 4) && (V != 1 && V != 2 && V != 4));

            assure((buffer[1] & 0xFF) >= 2);

            vmax = Math.max(vmax, V);
            hmax = Math.max(hmax, H);

            comp.blocksPerMCUHorz = H;
            comp.blocksPerMCUVert = V;
          
            comp.iddequant = buffer[1] & 0xFF;
            comimg.component[c] = comp;
        }

        comimg.imgHeight = imgheight;
        comimg.imgWidth = imgwidth;
        
        
        comimg.Hmax = hmax;
        comimg.Vmax = vmax;

        int mcuW = hmax * 8;

        int mcuH = vmax * 8;

        int mcuCountX = (imgwidth + mcuW - 1) / mcuW;

        int mcuCountY = (imgheight + mcuH - 1) / mcuH;

        comimg.mcuCountX = mcuCountX;
        comimg.mcuCountY = mcuCountY;

        for (int i = 0; i < nComponents; i++) {

            JpegImage.Component c = comimg.component[i];

            c.width = (imgwidth * c.blocksPerMCUHorz + hmax - 1) / hmax;

            c.height = ( imgheight * c.blocksPerMCUVert + vmax - 1 ) / vmax;

            c.minReqWidth = ( mcuCountX * c.blocksPerMCUHorz * 8 );

            c.minReqHeight = mcuCountY * c.blocksPerMCUVert * 8;

            if ( c.blocksPerMCUHorz < hmax ) {

                c.upsampler |= 1;

            }

            if ( c.blocksPerMCUVert < vmax ) {

                c.upsampler |= 2;

            }

        }

    }

    private void readSOS() throws Unsupported , IOException {

        int length = getU16();
        int nComponents = getU8();

        assure(nComponents != comimg.ncomponents);

        assure(length != 6 + 2 * nComponents);

        int dcIds;
        int acIds;
        byte[] buffer = new byte[9];

        readBytes(9, 0, buffer);

        for (int c = 0; c < nComponents; ++c) {

            int id = buffer[2 * c] & 0xFF;

            assure(id != c + 1);

            dcIds = (buffer[2 * c + 1] & 0xFF) >> 4;
            acIds = (buffer[2 * c + 1] & 0xFF) & 0xF;

            assure(dcIds >= 2 || acIds >= 2);

            assure(comimg.Huff[dcIds] == null || comimg.Huff[acIds] == null);

            for (int i = 0; i < nComponents; i++) {
                JpegImage.Component comp = comimg.component[i];
                if (comp.id == id) {
                    comp.huffAC = comimg.Huff[acIds + 4];

                    comp.huffDC = comimg.Huff[dcIds];
                    break;
                }
            }

        }

    }

    public  void assure(boolean condition) throws Unsupported, IOException {

        if (condition) {
            file.close();
            throw new Unsupported();
        }

    }

}

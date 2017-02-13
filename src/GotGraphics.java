import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by gustavbodestad on 2017-02-03.
 * I/O class for our own format (GotGraphics)
 *
 */
public class GotGraphics {

    final static byte[] magic = "KingLorenz!".getBytes(StandardCharsets.US_ASCII);

    public final static class InvalidGotFileException extends IOException { }


    /**
     * Takes a BufferedImage and a filename, generates a GotGraphics file.
     * @param img
     * @param fnam
     * @throws IOException
     */
    public void write(BufferedImage img, String fnam) throws IOException {
        int width  = img.getWidth();
        int height = img.getHeight();
        int[] pxl = new int[3];
        Raster imgr  = img.getRaster();
        OutputStream out = new FileOutputStream(fnam);
        out.write(magic);
        write4bytes(width, out);
        write4bytes(height, out);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                imgr.getPixel(i, j, pxl);
                out.write(pxl[0]);
                out.write(pxl[1]);
                out.write(pxl[2]);
            }
        }
        out.close();
    }

    /**
     * Takes a filename, generates and returns a BufferedImage from a GotGraphics file.
     * @param fnam
     * @return
     * @throws IOException
     */
    public BufferedImage read(String fnam) throws IOException {
        InputStream in = new FileInputStream(fnam);

        // Check header.
        for (int i = 0; i < magic.length; i++) {
            if (in.read() != magic[i]) { throw new GotGraphics.InvalidGotFileException(); }
        }
        int width  = read4bytes(in);
        int height = read4bytes(in);
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        byte[] pxlBytes = new byte[3];
        int[] pxl = new int[3];
        WritableRaster imgr  = img.getRaster();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (in.read(pxlBytes) != 3) { throw new EOFException(); }
                pxl[0] = pxlBytes[0];
                pxl[1] = pxlBytes[1];
                pxl[2] = pxlBytes[2];
                imgr.setPixel(i, j, pxl);
            }
        }
        in.close();
        return img;
    }

    /** Writes an int as 4 bytes, big endian. (Copied from MegatronGraphics) */
    private static void write4bytes(int v, OutputStream out) throws IOException {
        out.write(v>>>3*8);
        out.write(v>>>2*8 & 255);
        out.write(v>>>1*8 & 255);
        out.write(v       & 255);
    }

    /** Reads an int as 4 bytes, big endian. (Copied from MegatronGraphics) */
    private static int read4bytes(InputStream in) throws IOException {
        int b, v;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v = b<<3*8;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v |= b<<2*8;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v |= b<<1*8;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v |= b;
        return v;
    }
}

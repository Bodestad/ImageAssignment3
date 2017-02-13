import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by gustavbodestad on 2017-02-03.
 * The FLoyd-Steinberg algorithm was found on Wikipedia.
 * https://en.wikipedia.org/wiki/Floyd%E2%80%93Steinberg_dithering
 */
public class CompressImg {

    /**
     * Creates a new color palette by using the inner Palette class.
     * Takes Buffered image and returns compressed image.
     * @param img
     * @return
     */
    public BufferedImage dithering(BufferedImage img) {
        Palette[] palette = new Palette[] {
                new Palette(  0,   0,   0),
                new Palette(  0,   0, 255),
                new Palette(  0, 255,   0),
                new Palette(  0, 255, 255),
                new Palette(255,   0,   0),
                new Palette(255,   0, 255),
                new Palette(255, 255,   0),
                new Palette(255, 255, 255)
        };

        /**
         * Retrieves the width and height from the incoming image.
         * Fetches the colour of pixels.
         */
        int width = img.getWidth();
        int height = img.getHeight();

        Palette[][] d = new Palette[height][width];

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                d[y][x] = new Palette(img.getRGB(x, y));

        /**
         * Replaces the old color pixel with a new value by using the color palette.
         * The nested for-loop goes through the image from top to bottom, left to right, pixel by pixel.
         * This method is received from https://en.wikipedia.org/wiki/Floyd%E2%80%93Steinberg_dithering
         */
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                Palette oldColor = d[y][x];
                Palette newColor = findClosestPaletteColor(oldColor, palette);
                img.setRGB(x, y, newColor.toColor().getRGB());

                Palette err = oldColor.sub(newColor);

                if (x+1 < width)         d[y  ][x+1] = d[y  ][x+1].add(err.mul(7./16));
                if (x-1>=0 && y+1<height) d[y+1][x-1] = d[y+1][x-1].add(err.mul(3./16));
                if (y+1 < height)         d[y+1][x  ] = d[y+1][x  ].add(err.mul(5./16));
                if (x+1<width && y+1<height)  d[y+1][x+1] = d[y+1][x+1].add(err.mul(1./16));
            }
        }

        return img;
    }

    /**
     * Fins closest color in the palette.
     * @param c
     * @param palette
     * @return
     */
    private static Palette findClosestPaletteColor(Palette c, Palette[] palette) {
        Palette closest = palette[0];

        for (Palette n : palette)
            if (n.diffusion(c) < closest.diffusion(c))
                closest = n;

        return closest;
    }

    /**
     * Class is used for creating a new color palette.
     */
    static class Palette {
        /**
         * Declares instance variables.
         */
        private int r, g, b;

        /**
         * Constructor that creates the palette.
         * @param c
         */
        public Palette(int c) {
            Color color = new Color(c);
            this.r = color.getRed();
            this.g = color.getGreen();
            this.b = color.getBlue();
        }

        /**
         * Constructor that declares three int values.
         * @param r
         * @param g
         * @param b
         */
        public Palette(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        /**
         * Method used in the algorithm.
         * @param o
         * @return
         */
        public Palette add(Palette o) {
            return new Palette(r + o.r, g + o.g, b + o.b);
        }

        /**
         * Method used in the algorithm to find the quantization error.
         * @param o
         * @return
         */
        public Palette sub(Palette o) {
            return new Palette(r - o.r, g - o.g, b - o.b);
        }

        /**
         * Method used in the algorithm.
         * @param d
         * @return
         */
        public Palette mul(double d) {
            return new Palette((int) (d * r), (int) (d * g), (int) (d * b));
        }

        /**
         * This method diffuses the error to the neighbouring pixels.
         * @param o
         * @return
         */
        public int diffusion(Palette o) {
            return Math.abs(r - o.r) +  Math.abs(g - o.g) +  Math.abs(b - o.b);
        }

        /**
         * This method uses toColor to generate a new rgb value.
         * @return
         */
        public int toRGB() {
            return toColor().getRGB();
        }


        public Color toColor() {
            return new Color(clamp(r), clamp(g), clamp(b));
        }

        /**
         * Returns a integer value.
         * @param c
         * @return
         */
        public int clamp(int c) {
            return Math.max(0, Math.min(255, c));
        }
    }
}



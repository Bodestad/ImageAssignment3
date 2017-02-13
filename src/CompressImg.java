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

        int w = img.getWidth();
        int h = img.getHeight();

        Palette[][] d = new Palette[h][w];

        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                d[y][x] = new Palette(img.getRGB(x, y));

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                Palette oldColor = d[y][x];
                Palette newColor = findClosestPaletteColor(oldColor, palette);
                img.setRGB(x, y, newColor.toColor().getRGB());

                Palette err = oldColor.sub(newColor);

                if (x+1 < w)         d[y  ][x+1] = d[y  ][x+1].add(err.mul(7./16));
                if (x-1>=0 && y+1<h) d[y+1][x-1] = d[y+1][x-1].add(err.mul(3./16));
                if (y+1 < h)         d[y+1][x  ] = d[y+1][x  ].add(err.mul(5./16));
                if (x+1<w && y+1<h)  d[y+1][x+1] = d[y+1][x+1].add(err.mul(1./16));
            }
        }

        return img;
    }

    private static Palette findClosestPaletteColor(Palette c, Palette[] palette) {
        Palette closest = palette[0];

        for (Palette n : palette)
            if (n.diff(c) < closest.diff(c))
                closest = n;

        return closest;
    }

    static class Palette {
        int r, g, b;

        public Palette(int c) {
            Color color = new Color(c);
            this.r = color.getRed();
            this.g = color.getGreen();
            this.b = color.getBlue();
        }
        public Palette(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public Palette add(Palette o) {
            return new Palette(r + o.r, g + o.g, b + o.b);
        }
        public Palette sub(Palette o) {
            return new Palette(r - o.r, g - o.g, b - o.b);
        }
        public Palette mul(double d) {
            return new Palette((int) (d * r), (int) (d * g), (int) (d * b));
        }
        public int diff(Palette o) {
            return Math.abs(r - o.r) +  Math.abs(g - o.g) +  Math.abs(b - o.b);
        }

        public int toRGB() {
            return toColor().getRGB();
        }
        public Color toColor() {
            return new Color(clamp(r), clamp(g), clamp(b));
        }
        public int clamp(int c) {
            return Math.max(0, Math.min(255, c));
        }
    }
}



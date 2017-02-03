import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by gustavbodestad on 2017-02-03.
 */
public class Main {

    public static void main(String[] args) {
        BufferedImage image;
        BufferedImage image2;
        BufferedImage image3;
        BufferedImage image4;
        MegatronGraphics mega = new MegatronGraphics();
        Compress compress = new Compress();
        GotGraphics gotGraphics = new GotGraphics();
        try {

            // Read Mtg-file
            image = mega.read("Images/cartoon.mtg");
            File outputFileNormal = new File("Images/original.png");
            ImageIO.write(image, "png", outputFileNormal);

            // Compress image
            image2 = compress.dithering(image);
            File outputfile = new File("Images/compressedImg.png");
            ImageIO.write(image2, "png", outputfile);

            // Convert GotGraphics to Mtg
            gotGraphics.write(image2, "Images/Compressed.gotGraphics");
            image3 = gotGraphics.read("Images/Compressed.gotGraphics");
            mega.write(image3, "Images/fromGotToMtg.mtg");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

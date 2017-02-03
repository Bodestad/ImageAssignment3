import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

/**
 * Created by gustavbodestad on 2017-02-03.
 */
public class Main {

    public static void main(String[] args) {
        BufferedImage image;
        BufferedImage image2;
        Got got = new Got();
        MegatronGraphics mega = new MegatronGraphics();
        Test test = new Test();
        try {
            image = mega.read("Images/cartoon.mtg");
            File outputFileNormal = new File("Images/savedNormal.png");
            ImageIO.write(image, "png", outputFileNormal);
            image2 = test.dithering(image);
            File outputfile = new File("Images/saved.png");
            ImageIO.write(image2, "png", outputfile);
            mega.write(image2, "Test.got");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

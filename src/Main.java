import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by gustavbodestad on 2017-02-03.
 */
public class Main {

    public static void main(String[] args) {
        BufferedImage image;
        Got got = new Got();
        MegatronGraphics mega = new MegatronGraphics();
        try {
            image = mega.read("Images/cartoon.mtg");
            mega.write(image, "Test.got");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

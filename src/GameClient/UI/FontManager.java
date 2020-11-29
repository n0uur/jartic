package GameClient.UI;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontManager {

    private static Font font;
    public static Font getFont(){
        if(font == null) {
            try {
                font = Font.createFont(Font.TRUETYPE_FONT,new File("resource/FCIconic.ttf")).deriveFont(24f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(font);
            } catch (FontFormatException | IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return font;
    }
}

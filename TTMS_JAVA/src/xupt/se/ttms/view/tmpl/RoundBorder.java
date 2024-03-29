package xupt.se.ttms.view.tmpl;

import java.awt.*;

public class RoundBorder {

    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, 0, 0);
    }
    public boolean isBorderOpaque() {
        return false;
    }
    public void paintBorder(Component c, Graphics g, int x, int y, int width,
                            int height) {
        g.setColor(Color.BLACK);
        g.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 10, 10);
    }
}

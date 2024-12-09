// File: src/RoundedPanel.java
import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private int cornerRadius;
    private Color borderColor;

    public RoundedPanel(int cornerRadius, Color borderColor) {
        this.cornerRadius = cornerRadius;
        this.borderColor = borderColor;
        setOpaque(false); // Make the panel transparent to allow rounded corners
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill the panel with background color, taking the corner radius into account
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Draw the border with rounded corners
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2)); // Set the border thickness
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
    }
}

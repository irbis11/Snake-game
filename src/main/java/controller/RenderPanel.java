package controller;

import javax.swing.*;
import java.awt.*;

public class RenderPanel extends JPanel {

    // Need to be transient as they don't implement serialization
    private transient GameController controller;
    private transient Font defaultFont = new Font("Verdana", Font.BOLD, 18);
    private int standardOffset = 35;
    RenderPanel(GameController ctrl) {
        controller = ctrl;
    }

    // Paint all required objects on jFrame
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        // Update font
        graphics.setFont(defaultFont);

        int width = controller.getScreenWidth();
        int height = controller.getScreenHeight();
        int scale = controller.getScale();

        // Background
        graphics.setColor(Color.GRAY);
        graphics.fillRect(0, 0, width, height);

        Point head = controller.getHead();
        Point target = controller.getTarget();
        // Snake tail
        graphics.setColor(Color.GREEN);
        for (Point point : controller.getSnakeParts()) {
            graphics.fillRect(point.x * scale, point.y * scale, scale, scale);
        }
        // Snake head
        graphics.setColor(Color.RED);
        graphics.fillRect(head.x * scale, head.y * scale, scale, scale);
        // Target
        graphics.setColor(Color.YELLOW);
        graphics.fillRect(target.x * scale, target.y * scale, scale, scale);
        // Score display
        graphics.setColor(Color.MAGENTA);
        graphics.drawString(controller.getScoreString(), (width / 2) - 100, 15);
        // Game pause
        if (controller.isGamePaused() && !controller.isGameOverOver()) {
            graphics.drawString("Paused!", (width / 2) - 35, height / 2 - standardOffset);
        }
        // Game over
        if (controller.isGameOverOver()) {
            graphics.drawString("Game over!", (width / 2) - 50, height / 2 - standardOffset);
        }
    }

}
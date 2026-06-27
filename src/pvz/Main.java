package pvz;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        double speedMultiplier = parseSpeedMultiplier(args);
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Plants vs Zombies - Java Edition");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new GamePanel(speedMultiplier));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }

    private static double parseSpeedMultiplier(String[] args) {
        if (args.length == 0) {
            return 1.0;
        }
        try {
            double parsed = Double.parseDouble(args[0]);
            return Math.max(0.25, Math.min(4.0, parsed));
        } catch (NumberFormatException ignored) {
            return 1.0;
        }
    }
}

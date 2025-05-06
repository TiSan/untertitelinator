package de.tisan.church.untertitelinator.gui.qrgame;
import com.google.zxing.WriterException;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

public class QRCodeGameV2 extends JFrame {

    private final JLabel qrLabel = new JLabel();
    private final JLabel pointsLabel = new JLabel();
    private final JLabel timerLabel = new JLabel();
    private final JLabel infoLabel = new JLabel();

    private final Random random = new Random();
    private final StringBuilder inputBuffer = new StringBuilder();

    private final int qrSize = 200;
    private final int gameDurationSeconds = 60;
    private int timeLeft = gameDurationSeconds;
    private int score = 0;

    private Timer gameTimer;
    private String currentCode;
    private String currentHint = "";

    // Für Reihenfolge-Logik
    private List<String> sequence = Arrays.asList("SEQ-1", "SEQ-2", "SEQ-3");
    private int currentSequenceIndex = 0;

    public QRCodeGameV2() {
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.BLACK);

        // QR-Code-Anzeige
        qrLabel.setSize(qrSize, qrSize);
        add(qrLabel);

        // Punktestand-Anzeige
        pointsLabel.setForeground(Color.GREEN);
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        pointsLabel.setBounds(20, 20, 300, 30);
        add(pointsLabel);

        // Zeit-Anzeige
        timerLabel.setForeground(Color.YELLOW);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerLabel.setBounds(20, 60, 300, 30);
        add(timerLabel);

        // Hinweistext
        infoLabel.setForeground(Color.CYAN);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 22));
        infoLabel.setBounds(20, 100, 800, 30);
        add(infoLabel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                if (ch == '\n') {
                    String input = inputBuffer.toString().trim();
                    inputBuffer.setLength(0);
                    handleScan(input);
                } else {
                    inputBuffer.append(ch);
                }
            }
        });

        setVisible(true);
        startGame();
    }

    private void startGame() {
        updateStats();
        nextQRCode();

        gameTimer = new Timer(1000, e -> {
            timeLeft--;
            updateStats();
            if (timeLeft <= 0) {
                ((Timer) e.getSource()).stop();
                endGame();
            }
        });
        gameTimer.start();
    }

    private void updateStats() {
        pointsLabel.setText("Punkte: " + score);
        timerLabel.setText("Zeit: " + timeLeft + "s");
        infoLabel.setText(currentHint);
    }

    private void nextQRCode() {
        try {
            // Zufällig entscheiden ob Bonus oder Sequenz
            int chance = random.nextInt(10);
            if (chance < 2) {
                currentCode = "BONUS";
                currentHint = "🎁 BONUS! +10s und +3 Punkte!";
            } else if (chance < 5) {
                currentCode = sequence.get(currentSequenceIndex);
                currentHint = "🔢 Reihenfolge! " + currentCode;
            } else {
                currentCode = UUID.randomUUID().toString().substring(0, 8);
                currentHint = "";
            }

            BufferedImage qrImage = QRGenerator.generateQRCode(currentCode, qrSize, qrSize);
            qrLabel.setIcon(new ImageIcon(qrImage));

            // Zufällige Position
            Dimension screen = getSize();
            int x = random.nextInt(Math.max(1, screen.width - qrLabel.getWidth()));
            int y = random.nextInt(Math.max(1, screen.height - qrLabel.getHeight()));
            qrLabel.setLocation(x, y);

            updateStats();

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void handleScan(String input) {
        if (input.equals(currentCode)) {
            if ("BONUS".equals(currentCode)) {
                timeLeft += 10;
                score += 3;
            } else if (currentCode.startsWith("SEQ")) {
                if (currentCode.equals(sequence.get(currentSequenceIndex))) {
                    currentSequenceIndex++;
                    score += 2;
                    if (currentSequenceIndex >= sequence.size()) {
                        score += 5; // Bonus für vollständige Sequenz
                        currentSequenceIndex = 0;
                        currentHint = "✅ Reihenfolge abgeschlossen!";
                    }
                } else {
                    currentHint = "❌ Falsche Reihenfolge!";
                }
            } else {
                score++;
            }
            nextQRCode();
        } else {
            currentHint = "❌ Ungültiger Scan!";
        }
        updateStats();
    }

    private void endGame() {
        JOptionPane.showMessageDialog(this,
                "Spiel beendet!\nPunkte: " + score,
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QRCodeGameV2::new);
    }
}

package de.tisan.church.untertitelinator.gui.qrgame;
import com.google.zxing.WriterException;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class QRCodeGameV3 extends JFrame {

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
    private final List<QRCodeData> activeCodes = new ArrayList<>();
    private int currentSequenceIndex = 0;
    private boolean inSequenceMode = false;

    public QRCodeGameV3() {
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.BLACK);

        pointsLabel.setForeground(Color.GREEN);
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        pointsLabel.setBounds(20, 20, 300, 30);
        add(pointsLabel);

        timerLabel.setForeground(Color.YELLOW);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerLabel.setBounds(20, 60, 300, 30);
        add(timerLabel);

        infoLabel.setForeground(Color.CYAN);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 22));
        infoLabel.setBounds(20, 100, 1000, 30);
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

    private void clearOldCodes() {
        for (QRCodeData qr : activeCodes) {
            remove(qr.label);
        }
        // Entferne auch alle JLabel-Markierungen mit Zahlen unter den QR-Codes
        Component[] components = getContentPane().getComponents();
        List<Component> toRemove = new ArrayList<>();
        for (Component comp : components) {
            if (comp instanceof JLabel && ((JLabel) comp).getText().matches("[1-9]")) {
                toRemove.add(comp);
            }
        }
        for (Component comp : toRemove) {
            remove(comp);
        }
        repaint();
    }


private void nextQRCode() {
    clearOldCodes();
    activeCodes.clear();
    currentHint = "";

    int chance = random.nextInt(10);
    if (chance < 2) {
        String bonusCode = "BONUS-" + UUID.randomUUID().toString().substring(0, 4);
        QRCodeData qr = new QRCodeData(bonusCode, true, false);
        placeQRCode(qr, new ArrayList<>());
        activeCodes.add(qr);
        currentHint = "🎁 BONUS! Scanne für +10s und +3 Punkte!";
        inSequenceMode = false;
    } else if (chance < 5) {
        inSequenceMode = true;
        currentSequenceIndex = 0;
        List<Rectangle> occupied = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            String code = "SEQ-" + i;
            QRCodeData qr = new QRCodeData(code, false, true);
            placeQRCode(qr, occupied);
            JLabel label = new JLabel("" + i);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 20));
            label.setBounds(qr.label.getX(), qr.label.getY() + qrSize, 50, 25);
            add(label);
            activeCodes.add(qr);
        }
        currentHint = "🔢 Reihenfolge: Scanne SEQ-1 → SEQ-2 → SEQ-3";
    } else {
        inSequenceMode = false;
        List<Rectangle> occupied = new ArrayList<>();
        int numCodes = 3 + random.nextInt(3); // 3–5 Codes
        for (int i = 0; i < numCodes; i++) {
            String code = UUID.randomUUID().toString().substring(0, 8);
            QRCodeData qr = new QRCodeData(code, false, false);
            placeQRCode(qr, occupied);
            activeCodes.add(qr);

            // Punkte sinken linear von 100 auf 0 über 5 Sekunden
            final int maxPoints = 100;
            final int displayDuration = 5000;
            long showTime = System.currentTimeMillis();
            Timer hideTimer = new Timer(50, e -> {
                long elapsed = System.currentTimeMillis() - showTime;
                if (elapsed >= displayDuration) {
                    remove(qr.label);
                    activeCodes.remove(qr);
                    repaint();
                    ((Timer) e.getSource()).stop();
                } else {
                    int value = (int) (maxPoints * (1.0 - (double) elapsed / displayDuration));
                    qr.label.setToolTipText("Noch " + value + " Punkte möglich");
                    qr.label.putClientProperty("points", value);
                }
            });
            hideTimer.start();
        }
    }

    updateStats();
}

private void placeQRCode(QRCodeData qr, List<Rectangle> occupied) {
    try {
        BufferedImage qrImage = QRGenerator.generateQRCode(qr.code, qrSize, qrSize);
        qr.label.setIcon(new ImageIcon(qrImage));
        qr.label.setSize(qrSize, qrSize);

        Rectangle newRect;
        boolean overlap;
        int attempts = 0;
        do {
            int x = random.nextInt(Math.max(1, getWidth() - qrSize));
            int y = random.nextInt(Math.max(1, getHeight() - qrSize - 50));
            newRect = new Rectangle(x, y, qrSize, qrSize + 30);
            Rectangle finalNewRect = newRect;
            overlap = occupied.stream().anyMatch(r -> r.intersects(finalNewRect));
            attempts++;
        } while (overlap && attempts < 100);

        qr.label.setLocation(newRect.x, newRect.y);
        add(qr.label);
        qr.label.setVisible(true);

        occupied.add(newRect);
    } catch (WriterException e) {
        e.printStackTrace();
    }
}

private void handleScan(String input) {
    Optional<QRCodeData> matched = activeCodes.stream()
            .filter(qr -> qr.code.equals(input))
            .findFirst();

    if (matched.isPresent()) {
        QRCodeData qr = matched.get();

        if (qr.isBonus) {
            timeLeft += 10;
            score += 3;
            currentHint = "✅ Bonus eingesammelt!";
            nextQRCode();
        } else if (qr.isSequence) {
            String expected = "SEQ-" + (currentSequenceIndex + 1);
            if (qr.code.equals(expected)) {
                currentSequenceIndex++;
                score += 2;
                currentHint = "✅ " + qr.code + " korrekt!";
                if (currentSequenceIndex >= 3) {
                    score += 5;
                    inSequenceMode = false;
                    currentHint = "🏁 Reihenfolge komplett!";
                    nextQRCode();
                }
            } else {
                currentHint = "❌ Falsche Reihenfolge: Erwartet " + expected;
            }
        } else {
            int dynamicPoints = qr.label.getClientProperty("points") instanceof Integer ? (Integer) qr.label.getClientProperty("points") : 1;
            score += dynamicPoints;
            currentHint = "✔️ Korrekt!";
            nextQRCode();
        }
    } else {
        currentHint = "❌ Ungültiger Code!";
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
    SwingUtilities.invokeLater(QRCodeGameV3::new);
}

private static class QRCodeData {
    String code;
    JLabel label;
    boolean isBonus;
    boolean isSequence;

    QRCodeData(String code, boolean isBonus, boolean isSequence) {
        this.code = code;
        this.isBonus = isBonus;
        this.isSequence = isSequence;
        this.label = new JLabel();
    }
}

private String currentHint = "";
}

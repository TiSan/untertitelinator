package de.tisan.church.untertitelinator.gui.qrgame;

import com.google.zxing.WriterException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.UUID;

public class QRCodeGame extends JFrame {

    private final JLabel qrLabel = new JLabel();
    private final Random random = new Random();
    private String currentCode = "";
    private final StringBuilder inputBuffer = new StringBuilder();
    private int score = 0;

    public QRCodeGame() {
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.BLACK);

        qrLabel.setSize(200, 200);
        add(qrLabel);

        generateAndShowNewQRCode();

        // Tastaturinput (Scanner tippt + ENTER)
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                if (ch == '\n') { // ENTER gedrückt
                    String input = inputBuffer.toString().trim();
                    inputBuffer.setLength(0); // leeren
                    checkCode(input);
                } else {
                    inputBuffer.append(ch);
                }
            }
        });

        setVisible(true);
    }

    private void checkCode(String input) {
        if (input.equals(currentCode)) {
            score++;
            System.out.println("✔️ Richtig gescannt! Punkte: " + score);
            generateAndShowNewQRCode();
        } else {
            System.out.println("❌ Falscher Code: " + input);
        }
    }

    private void generateAndShowNewQRCode() {
        try {
            currentCode = UUID.randomUUID().toString(); // z.B. zufälliger Code
            BufferedImage qrImage = QRGenerator.generateQRCode(currentCode, 200, 200);
            qrLabel.setIcon(new ImageIcon(qrImage));

            // Neue zufällige Position
            Dimension screen = getSize();
            int x = random.nextInt(Math.max(1, screen.width - qrLabel.getWidth()));
            int y = random.nextInt(Math.max(1, screen.height - qrLabel.getHeight()));
            qrLabel.setLocation(x, y);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QRCodeGame::new);
    }
}

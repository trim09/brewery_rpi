package cz.todr.brewery.hardware.impl.desktop;

import cz.todr.brewery.hardware.api.ButtonEnum;
import cz.todr.brewery.hardware.api.ButtonStateListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DesktopSwingHardware {

    private final JTextArea lcd = new JTextArea();
    private final JLabel heating = new JLabel();
    private final JLabel temp = new JLabel();
    private final Map<ButtonEnum, ButtonStateListener> listeners = new ConcurrentHashMap<>();

    public DesktopSwingHardware() {
        JFrame frame = new JFrame();

        JButton up = new JButton("up");
        JButton mid = new JButton("mid");
        JButton down = new JButton("down");
        up.addKeyListener(new MyKeyAdapter());
        mid.addKeyListener(new MyKeyAdapter());
        down.addKeyListener(new MyKeyAdapter());
        up.addMouseListener(new MyMouseAdapter(ButtonEnum.UP));
        mid.addMouseListener(new MyMouseAdapter(ButtonEnum.MID));
        down.addMouseListener(new MyMouseAdapter(ButtonEnum.DOWN));

        JPanel left = new JPanel();
        left.setLayout(new GridLayout(3,0));
        left.add(up);
        left.add(mid);
        left.add(down);
        frame.add(left, BorderLayout.WEST);
        frame.add(lcd,  BorderLayout.CENTER);
        frame.add(heating,  BorderLayout.NORTH);
        frame.add(temp,  BorderLayout.SOUTH);

        val font = lcd.getFont();
        lcd.setFont(new Font(font.getName(), font.getStyle(), font.getSize() + 16));

        frame.setSize(350, 200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addCloseOnEscapeKeyFeature(frame);

        heating.setText("N/A");
        heating.setOpaque(true);
        heating.setHorizontalAlignment(SwingConstants.CENTER);
        temp.setText("N/A");
        temp.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void addCloseOnEscapeKeyFeature(JFrame frame) {
        Action dispatchClosing = new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        };

        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);

        JRootPane rootPane = frame.getRootPane();
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "closeWindow");
        rootPane.getActionMap().put("closeWindow", dispatchClosing);
    }

    public void setLcdText(String firstRow, String secondRow) {
        SwingUtilities.invokeLater(() -> lcd.setText(String.format("%.16s\n%.16s", firstRow, secondRow)));
    }

    public void setTemp(float temp) {
        SwingUtilities.invokeLater(() -> this.temp.setText(temp + "°C"));
    }

    public void setHeating(boolean on) {
        SwingUtilities.invokeLater(() -> {
            if (on) {
                heating.setText("Heating ON");
                heating.setBackground(Color.RED);
            } else {
                heating.setText("Heating OFF");
                heating.setBackground(Color.CYAN);
            }
        });
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            testKey(e, true);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            testKey(e, false);
        }

        private void testKey(KeyEvent e, boolean pressed) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    buttonStatusUpdate(ButtonEnum.UP, pressed);
                    break;
                case KeyEvent.VK_S:
                    buttonStatusUpdate(ButtonEnum.MID, pressed);
                    break;
                case KeyEvent.VK_X:
                    buttonStatusUpdate(ButtonEnum.DOWN, pressed);
                    break;
            }
        }
    }

    @AllArgsConstructor
    private class MyMouseAdapter extends MouseAdapter {
        private final ButtonEnum button;

        public void mousePressed(MouseEvent e) {
            buttonStatusUpdate(button, true);
        }

        public void mouseReleased(MouseEvent e) {
            buttonStatusUpdate(button, false);
        }
    }

    private void buttonStatusUpdate(ButtonEnum button, boolean pressed) {
        val listener = listeners.getOrDefault(button, (b, p) -> log.info("No listener for button {}", b));
        listener.stateChanged(button, pressed);
    }

    public void registerButtonListener(ButtonEnum button, ButtonStateListener listener) {
        log.info("Registering {} button listener", button);
        listeners.put(button, listener);
    }
}

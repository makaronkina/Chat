package chat.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import static java.awt.BorderLayout.WEST;

public class ChatFrame extends JFrame {
    private StringBuilder stringBuilder;
    private JTextArea textArea;
    private ActionListener submitListener;

    public ChatFrame(Consumer<String> transmitter) {
        stringBuilder = new StringBuilder();
        setTitle("Chat Frame");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(150, 150, 400, 500);

        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        textArea = new JTextArea();
        top.add(textArea, BorderLayout.CENTER);
        add(top, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        JTextField textField = new JTextField();
        JButton button = new JButton("SUBMIT");
        bottom.add(textField, BorderLayout.CENTER);
        bottom.add(button, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        submitListener = new SubmitListener(textField, transmitter);
        textField.addActionListener(submitListener);
        button.addActionListener(submitListener);

        setVisible(true);
    }

    public void append(String message) {
        stringBuilder.append(textArea.getText()).append("\n").append(message);
        textArea.setText(stringBuilder.toString());
        stringBuilder.setLength(0);
    }

    public void close() {
        System.exit(0);
    }
}

package chat.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class SubmitListener implements ActionListener {
    private JTextField textField;
    private Consumer<String> callBack;

    public SubmitListener(JTextField textField, Consumer<String> callBack) {
        this.textField = textField;
        this.callBack = callBack;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String message = textField.getText();
        textField.setText("");
        callBack.accept(message);
    }
}

import javax.swing.*;
import java.awt.*;

public class Calculator {

    private final static JButton[] numButtons = new JButton[10];
    private final static JButton[] functionalButtons = {
            new JButton("+"),
            new JButton("-"),
            new JButton("*"),
            new JButton("/"),
            new JButton("="),
            new JButton("C"),
    };
    private static JTextField screen = new JTextField(10);
    private StringBuilder sb = new StringBuilder();

    public Calculator() {
        // buttons config
        addButtons();
        addActionListenerToNumButtons();
        addActionListenerToFunctionalButtons();

        // screen config
        screen.setText("0");

        // panel config
        JPanel main = new JPanel();
        main.setLayout(new GridLayout(4, 4));
        addButtonsToPanel(numButtons, main);
        addButtonsToPanel(functionalButtons, main);

        // frame config
        JFrame frame = new JFrame("Calculator");
        frame.setLayout(new BorderLayout());
        frame.add(main, BorderLayout.CENTER);
        frame.add(screen, BorderLayout.NORTH);
        frame.setVisible(true);
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void addButtons() {
        for (int i = 0; i < numButtons.length; i++) {
            numButtons[i] = new JButton(String.valueOf(i));
        }
    }

    private void addButtonsToPanel(JButton[] numButtons, JPanel panel) {
        for(JButton b : numButtons) {
            panel.add(b);
        }
    }

    private void addActionListenerToNumButtons() {
        for(JButton b : numButtons) {
            b.addActionListener(e -> {
                if(!(sb.isEmpty() && sb.length() == 0 && e.getActionCommand().equals("0"))) {
                    sb.append(e.getActionCommand());
                    screen.setText(sb.toString());
                }
            });
        }
    }

    private void addActionListenerToFunctionalButtons() {
        for(JButton b : functionalButtons) {
            b.addActionListener(e -> {
                switch (e.getActionCommand()) {
                    case "+":
                        break;
                    case "-":
                        break;
                    case "*":
                        break;
                    case "/":
                        break;
                    case "=":
                        break;
                    case "C":
                        break;
                }
            });
        }
    }


}

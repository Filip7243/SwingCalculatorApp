import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> arithmeticOperations = new ArrayList<>();
    private StringBuilder sb = new StringBuilder();
    private double result = 0.0;
    private double number = 0.0;
//    private String lastClicked = "";

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
        numButtons[numButtons.length - 1] = new JButton(".");
    }

    private void addButtonsToPanel(JButton[] numButtons, JPanel panel) {
        for (JButton b : numButtons) {
            panel.add(b);
        }
    }

    private void addActionListenerToNumButtons() {
        for (JButton b : numButtons) {
            b.addActionListener(e -> {
                if (!(sb.isEmpty() && sb.length() == 0 && e.getActionCommand().equals("0"))) {
                    sb.append(e.getActionCommand());
                    screen.setText(sb.toString());
                }
            });
        }
    }

    private void addActionListenerToFunctionalButtons() {
        for (JButton b : functionalButtons) {
            b.addActionListener(e -> {
                String clickedButton = e.getActionCommand();
                arithmeticOperations.add(sb.toString());
                arithmeticOperations.add(clickedButton);
                sb = new StringBuilder();
                screen.setText("");
                whenEqualsClicked(clickedButton);
                screen.setText(String.valueOf(result));
            });
        }
    }

    private void whenEqualsClicked(String clickedButton) {
        if (clickedButton.equals("=")) {
            String lastClicked = arithmeticOperations.get(arithmeticOperations.size() - 3);
            for (int i = 0; i < arithmeticOperations.size(); i++) {
                if (i % 2 != 0) {
                    String buttonClicked = arithmeticOperations.get(i);
                    makeOperationDependOnButtonClicked(buttonClicked);
                } else {
                    number = Double.parseDouble(arithmeticOperations.get(i));
                }
            }
            makeOperationDependOnButtonClicked(lastClicked);
        }
    }

    private void makeOperationDependOnButtonClicked(String buttonClicked) {
        switch (buttonClicked) {
            case "+" -> result += number;
            case "-" -> result -= number;
            case "/" -> result /= number;
            case "*" -> {
                if (result == 0.0) {
                    result = 1.0;
                }
                result *= number;
            }
        }
    }


}

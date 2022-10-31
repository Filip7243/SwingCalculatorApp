import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Calculator {

    private final static JButton[] numButtons = new JButton[12];
    private final static JButton[] functionalButtons = {
            new JButton("+"),
            new JButton("-"),
            new JButton("*"),
            new JButton("/"),
            new JButton("="),
    };
    private static JTextField screen = new JTextField(10);
    private List<String> arithmeticOperations = new ArrayList<>();
    private StringBuilder sb = new StringBuilder();
    private double result = 0.0;
    private double n1 = 0.0;
    private double n2 = 0.0;

    public Calculator() {
        // buttons config
        createButtons();
        addActionListenerToNumButtons();
        addActionListenerToFunctionalButtons();

        // screen config
        screen.setText("0");

        // panel config
        JPanel main = new JPanel();
        main.setLayout(new GridLayout(5, 5));
        addButtonsToPanel(numButtons, main);
        addButtonsToPanel(functionalButtons, main);

        // frame config
        JFrame frame = new JFrame("Calculator");
        frame.setLayout(new BorderLayout());
        frame.add(main, BorderLayout.CENTER);
        frame.add(screen, BorderLayout.NORTH);
        frame.setVisible(true);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void createButtons() {
        for (int i = 0; i < numButtons.length; i++) {
            numButtons[i] = new JButton(String.valueOf(i));
        }
        numButtons[numButtons.length - 2] = new JButton(".");
        numButtons[numButtons.length - 1] = new JButton("C");

    }

    private void addButtonsToPanel(JButton[] numButtons, JPanel panel) {
        for (JButton b : numButtons) {
            panel.add(b);
        }
    }

    private void addActionListenerToNumButtons() {
        for (JButton b : numButtons) {
            b.addActionListener(e -> {
                String buttonClicked = e.getActionCommand();
                if (sb.toString().contains(".") && buttonClicked.equals(".")) {
                    return;
                } else if (buttonClicked.equals("C") && sb.length() >= 1) {
                    sb = new StringBuilder(sb.substring(0, sb.length() - 1));
                    screen.setText(sb.toString());
                } else if (!(sb.isEmpty() && sb.length() == 0 && buttonClicked.equals("0")) && !buttonClicked.equals("C")) {
                    sb.append(buttonClicked);
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
            });
        }
    }

    private void whenEqualsClicked(String clickedButton) {
        if (clickedButton.equals("=")) {
            arithmeticOperations.remove(arithmeticOperations.size() - 1);
            for (int i = 0; i < arithmeticOperations.size(); i++) {
                try {
                    Double.parseDouble(arithmeticOperations.get(i));
                } catch (NumberFormatException e) { // when exception catched, it means functional operator
                    n1 = Double.parseDouble(arithmeticOperations.get(i - 1));
                    n2 = Double.parseDouble(arithmeticOperations.get(i + 1));
                    makeOperationDependOnButtonClicked(arithmeticOperations.get(i));
                    arithmeticOperations.remove(i - 1);
                    arithmeticOperations.remove(i);
                    arithmeticOperations.add(i, String.valueOf(result));
                }
            }
            screen.setText(String.valueOf(result));
            arithmeticOperations.clear();
            result = 0.0;
        }
    }

    private void makeOperationDependOnButtonClicked(String buttonClicked) {
        switch (buttonClicked) {
            case "+" -> result = n1 + n2;
            case "-" -> result = n1 - n2;
            case "/" -> result = n1 / n2;
            case "*" -> result = n1 * n2;
        }
    }

}

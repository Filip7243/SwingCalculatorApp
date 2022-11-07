import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Calculator {

    private static final JPanel main = new JPanel(new BorderLayout(5, 5));
    private static final JPanel buttonsPanel = new JPanel(new GridLayout(5, 3, 2, 2));
    private static final JTextArea screen = new JTextArea(3, 40);

    private static final JButton[] numButtons = new JButton[12];
    private static final JButton[] functionalButtons = {
            new JButton("+"),
            new JButton("-"),
            new JButton("*"),
            new JButton("/"),
            new JButton("="),
    };

    private static final List<String> buttons = new ArrayList<>(List.of(
            "1/x", "x^2", "+/-", "C",
            "9", "8", "7", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "=", "0", ".", "/"
    ));

    private static final List<String> numButtonss = new ArrayList<>(List.of(
            "9", "8", "7",
            "4", "5", "6",
            "1", "2", "3",
            "0", "C", "."
    ));

    private static final List<String> funButtons = new ArrayList<>(List.of(
            "*", "+", "/", "=", "-"
    ));

    private List<String> arithmeticOperations = new ArrayList<>();
    private StringBuilder sb = new StringBuilder();
    private double result = 0.0;
    private double n1 = 0.0;
    private double n2 = 0.0;

    public Calculator() {

        // screen config
        screen.setBackground(Color.BLACK);
        screen.setForeground(Color.WHITE);
        screen.setText("0");
        screen.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
        screen.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // panel config
        addButtonsToPanel();
        main.setBackground(Color.BLACK);
        buttonsPanel.setBackground(Color.BLACK);
        main.add(screen, BorderLayout.NORTH);
        main.add(buttonsPanel, BorderLayout.CENTER);

        // frame config
        JFrame frame = new JFrame("Calculator");
        frame.setLayout(new BorderLayout());
        frame.add(main, BorderLayout.CENTER);
        frame.add(screen, BorderLayout.NORTH);
        frame.setVisible(true);
        frame.setSize(340, 400);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void addButtonsToPanel() {
        for(String s : buttons) {
            JButton b = new JButton(s);
            b.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
            b.setOpaque(false);
            b.setBorderPainted(false);
            b.setBackground(Color.BLACK);
            b.setForeground(Color.WHITE);

            if(numButtonss.contains(b.getText())){
                addActionListenerToNumButton(b);
            } else if(funButtons.contains(b.getText())) {
                addActionListenerToFunctionalButton(b);
            } else {
                addActionListenerToOtherButtons(b);
            }
            buttonsPanel.add(b);
        }
    }

    private void addActionListenerToNumButton(JButton b) {
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

    private void addActionListenerToFunctionalButton(JButton b) {
        b.addActionListener(e -> {
            String clickedButton = e.getActionCommand();
            arithmeticOperations.add(sb.toString());
            arithmeticOperations.add(clickedButton);
            sb = new StringBuilder();
            screen.setText("");
            whenEqualsClicked(clickedButton);
        });
    }

    private void addActionListenerToOtherButtons(JButton b) {
        // "1/x", "x^2", "+/-"
        b.addActionListener(e -> {
            if(!(sb.isEmpty() && screen.getText().isEmpty())) {
                String clickedButton = e.getActionCommand();
                double numberOnScreen = Double.parseDouble(screen.getText());
                switch (clickedButton) {
                    case "1/x" -> {
                        numberOnScreen = 1/numberOnScreen;
                        changeNumberOnScreen(numberOnScreen);
                    }
                    case "x^2" -> {
                        numberOnScreen = Math.pow(numberOnScreen, 2);
                        changeNumberOnScreen(numberOnScreen);
                    }
                    case "+/-" -> {
                        numberOnScreen *= -1;
                        changeNumberOnScreen(numberOnScreen);
                    }
                }
            }
        });
    }

    private void changeNumberOnScreen(Double numberOnScreen) {
        sb = new StringBuilder(String.valueOf(numberOnScreen));
        screen.setText(sb.toString());
    }

    private void whenEqualsClicked(String clickedButton) {
        if (clickedButton.equals("=")) {
            arithmeticOperations.remove(arithmeticOperations.size() - 1); // removing equals
            List<String> op = orderArithmeticalOperations(arithmeticOperations); // list with ordered arithmetical operations, it contains only + and -
            op.forEach(System.out::println);
            for (int i = 0; i < op.size(); i++) {
                try {
                    if (op.size() == 1) { // if in op is only one number
                        result = Double.parseDouble(op.get(i));
                    } else {
                        Double.parseDouble(op.get(i));
                    }
                } catch (NumberFormatException e) { // when exception catched, it means functional operator
                    n1 = Double.parseDouble(op.get(i - 1));
                    n2 = Double.parseDouble(op.get(i + 1));
                    makeOperationDependOnButtonClicked(op.get(i));
                    op.remove(i - 1);
                    op.remove(i);
                    op.add(i, String.valueOf(result));
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
        }
    }

    private List<String> orderArithmeticalOperations(List<String> arithmeticOperations) { // "*" and "/" done here
        var temp = new ArrayList<>(arithmeticOperations);
        while (temp.contains("*") || temp.contains("/")) {
            for (int i = 0; i < temp.size(); i++) {
                if (i % 2 != 0) { // when i is odd, it means we are on arithmetical operator index(*, /, +, -)
                    multiplyOrDivide(temp, i);
                }
            }
        }
        return temp;
    }

    private static void multiplyOrDivide(ArrayList<String> temp, int i) {
        var operation = temp.get(i);
        switch (operation) {
            case "*" -> {
                double n1 = Double.parseDouble(temp.get(i - 1));
                double n2 = Double.parseDouble(temp.get(i + 1));
                remove(temp, i);
                temp.add(i - 1, String.valueOf(n1 * n2));
            }
            case "/" -> {
                double n1 = Double.parseDouble(temp.get(i - 1));
                double n2 = Double.parseDouble(temp.get(i + 1));
                remove(temp, i);
                temp.add(i - 1, String.valueOf(n1 / n2));
            }
        }
    }

    private static void remove(List<String> temp, int i) {
        temp.remove(i + 1);
        temp.remove(i - 1);
        temp.remove(i - 1);
    }

}

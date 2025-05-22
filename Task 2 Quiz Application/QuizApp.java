import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizApp extends JFrame {

    static class Question {
        String questionText;
        String[] options;
        char correctOption;

        Question(String questionText, String[] options, char correctOption) {
            this.questionText = questionText;
            this.options = options;
            this.correctOption = correctOption;
        }
    }

    private Question[] questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionsGroup;
    private JButton nextButton;
    private JPanel mainPanel;

    public QuizApp() {
        questions = new Question[] {
                new Question("What is the capital of France?",
                        new String[] { "Berlin", "Madrid", "Paris", "Lisbon" }, 'C'),

                new Question("Which language runs in a web browser?",
                        new String[] { "Java", "C", "Python", "JavaScript" }, 'D'),

                new Question("Who is the founder of Microsoft?",
                        new String[] { "Elon Musk", "Bill Gates", "Steve Jobs", "Mark Zuckerberg" }, 'B'),

                new Question("What is the default port of HTTP?",
                        new String[] { "80", "443", "21", "22" }, 'A'),

                new Question("Which keyword is used to inherit a class in Java?",
                        new String[] { "super", "this", "extends", "implements" }, 'C')
        };

        setTitle("Java Quiz Application");
        setSize(750, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        createUI();
        loadQuestion(currentQuestionIndex);
    }

    private void createUI() {
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(230, 240, 255));
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40)); // More padding around edges

        // Title
        JLabel titleLabel = new JLabel("🧠 Java Quiz");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Question Panel
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBackground(Color.WHITE);
        questionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 3),
                BorderFactory.createEmptyBorder(30, 30, 30, 30) // Increased padding inside box
        ));

        questionLabel = new JLabel("Question");
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        questionLabel.setForeground(new Color(25, 25, 112));
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        questionPanel.add(questionLabel);
        questionPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        optionButtons = new JRadioButton[4];
        optionsGroup = new ButtonGroup();

        // Inside createUI(), increase vertical spacing between options:
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Segoe UI", Font.PLAIN, 18));
            optionButtons[i].setBackground(Color.WHITE);
            optionButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            optionButtons[i].setFocusable(false);
            optionsGroup.add(optionButtons[i]);
            questionPanel.add(optionButtons[i]);
            questionPanel.add(Box.createRigidArea(new Dimension(0, 24))); // Increased from 18 to 24 px
        }

        mainPanel.add(questionPanel, BorderLayout.CENTER);

        // Buttons Panel with improved buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 240, 255));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        nextButton = new JButton("Next");
        styleButton(nextButton);
        nextButton.addActionListener(new NextButtonListener());

        buttonPanel.add(nextButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.BLUE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(140, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void loadQuestion(int index) {
        if (index < questions.length) {
            Question q = questions[index];
            questionLabel.setText("Q" + (index + 1) + ": " + q.questionText);
            char optionChar = 'A';
            optionsGroup.clearSelection(); // Clear previous selection properly
            for (int i = 0; i < 4; i++) {
                optionButtons[i].setText(optionChar + ". " + q.options[i]);
                optionButtons[i].setActionCommand(String.valueOf(optionChar));
                optionButtons[i].setSelected(false);
                optionChar++;
            }
        }
    }

    private class NextButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (optionsGroup.getSelection() == null) {
                JOptionPane.showMessageDialog(QuizApp.this,
                        "Please select an answer before proceeding.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            char selected = optionsGroup.getSelection().getActionCommand().charAt(0);
            Question currentQ = questions[currentQuestionIndex];
            if (selected == currentQ.correctOption) {
                score++;
            }

            currentQuestionIndex++;

            if (currentQuestionIndex < questions.length) {
                loadQuestion(currentQuestionIndex);
            } else {
                showResult();
            }
        }
    }

    private void showResult() {
        mainPanel.setVisible(false);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout(20, 20));
        resultPanel.setBackground(new Color(230, 240, 255));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel resultTitle = new JLabel("🎉 Quiz Completed!");
        resultTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        resultTitle.setForeground(new Color(0, 102, 204));
        resultTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel scoreLabel = new JLabel("Your Score: " + score + " / " + questions.length);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        scoreLabel.setForeground(new Color(0, 128, 0));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextArea answersArea = new JTextArea();
        answersArea.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        answersArea.setEditable(false);
        answersArea.setBackground(new Color(230, 240, 255));
        answersArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        answersArea.setLineWrap(true);
        answersArea.setWrapStyleWord(true);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < questions.length; i++) {
            Question q = questions[i];
            sb.append("Q").append(i + 1).append(": ").append(q.questionText).append("\n");
            sb.append("Correct Answer: ").append(q.correctOption).append(". ")
                    .append(q.options[q.correctOption - 'A']).append("\n\n");
        }
        answersArea.setText(sb.toString());

        JScrollPane scrollPane = new JScrollPane(answersArea);
        scrollPane.setPreferredSize(new Dimension(650, 200));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));

        JButton closeButton = new JButton("Close");
        styleButton(closeButton);
        closeButton.setPreferredSize(new Dimension(140, 45));
        closeButton.addActionListener(e -> System.exit(0));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(230, 240, 255));
        bottomPanel.add(closeButton);

        resultPanel.add(resultTitle, BorderLayout.NORTH);
        resultPanel.add(scoreLabel, BorderLayout.CENTER);
        resultPanel.add(scrollPane, BorderLayout.SOUTH);
        resultPanel.add(bottomPanel, BorderLayout.PAGE_END);

        add(resultPanel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            QuizApp app = new QuizApp();
            app.setVisible(true);
        });
    }
}

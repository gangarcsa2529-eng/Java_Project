import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
public class TypingSpeedAnalyzer extends JFrame implements ActionListener {

    private CardLayout cardLayout = new CardLayout();
    private JPanel container = new JPanel(cardLayout);

    private JPanel menuPanel, testPanel, leaderboardPanel;

    private JButton playBtn = new JButton("1. Play Typing Test");
    private JButton boardBtn = new JButton("2. View Leaderboard");
    private JButton exitBtn = new JButton("3. Exit");

    private JTextField nameField = new JTextField(12);
    private JComboBox<String> diffBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
    private JTextArea passageArea = new JTextArea(5, 45);
    private JTextArea typedArea = new JTextArea(6, 45);
    private JLabel statusLabel = new JLabel("Enter your name, choose difficulty, and click Start Test.");
    private JLabel clockLabel = new JLabel("Time: 0.0 s");
    private JButton startBtn = new JButton("Start Test");
    private JButton submitBtn = new JButton("Submit Result");
    private JButton backFromTestBtn = new JButton("Back to Menu");

    private JTextArea leaderboardArea = new JTextArea(15, 45);
    private JButton backFromBoardBtn = new JButton("Back to Menu");

    private String currentPassage = "";
    private long startTime = 0;
    private boolean isRunning = false;
    private Thread countdownThread = null;

    private javax.swing.Timer clockTimer = new javax.swing.Timer(100, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (isRunning) {
                double elapsed = (System.currentTimeMillis() - startTime) / 1000.0;
                clockLabel.setText("Time: " + (int)elapsed + " s");}
            }});

    public TypingSpeedAnalyzer() {//constructor
        super("Typing Speed & Accuracy Analyser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);

        buildMenuPanel();
        buildTestPanel();
        buildLeaderboardPanel();

        container.add(menuPanel, "menu");
        container.add(testPanel, "test");
        container.add(leaderboardPanel, "leaderboard");

        setContentPane(container);
        cardLayout.show(container, "menu");}

    private void buildMenuPanel() {
        menuPanel = new JPanel(new BorderLayout(20, 20));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JLabel title = new JLabel("Typing Speed & Accuracy Analyser", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));

        JLabel subTitle = new JLabel("Improve your speed. Track your accuracy.", JLabel.CENTER);
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 15));

        titlePanel.add(title);
        titlePanel.add(subTitle);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        playBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        boardBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        exitBtn.setFont(new Font("SansSerif", Font.BOLD, 16));

        playBtn.addActionListener(this);
        boardBtn.addActionListener(this);
        exitBtn.addActionListener(this);

        buttonPanel.add(playBtn);
        buttonPanel.add(boardBtn);
        buttonPanel.add(exitBtn);

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 60));
        centerWrapper.add(buttonPanel);

        menuPanel.add(titlePanel, BorderLayout.NORTH);
        menuPanel.add(centerWrapper, BorderLayout.CENTER);}

    private void buildTestPanel() {
        testPanel = new JPanel(new BorderLayout(10, 10));

        JPanel header = new JPanel(new BorderLayout());
        JLabel headerTitle = new JLabel("Typing Test");
        headerTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        backFromTestBtn.addActionListener(this);
        header.add(headerTitle, BorderLayout.WEST);
        header.add(backFromTestBtn, BorderLayout.EAST);

        JPanel center = new JPanel(new GridLayout(2, 1, 10, 10));

        JPanel passagePanel = new JPanel(new BorderLayout(5, 5));
        JPanel settings = new JPanel(new FlowLayout(FlowLayout.LEFT));
        settings.add(new JLabel("Player Name:"));
        settings.add(nameField);
        settings.add(new JLabel("Difficulty:"));
        settings.add(diffBox);

        passageArea.setLineWrap(true);
        passageArea.setWrapStyleWord(true);
        passageArea.setEditable(false);
        passageArea.setFont(new Font("Serif", Font.PLAIN, 17));
        passageArea.setBackground(new Color(245, 245, 245));

        passagePanel.add(settings, BorderLayout.NORTH);
        passagePanel.add(new JScrollPane(passageArea), BorderLayout.CENTER);

        JPanel typingPanel = new JPanel(new BorderLayout(5, 5));
        typingPanel.add(new JLabel("Type the passage here:"), BorderLayout.NORTH);
        typedArea.setLineWrap(true);
        typedArea.setWrapStyleWord(true);
        typedArea.setFont(new Font("SansSerif", Font.PLAIN, 15));
        typedArea.setEnabled(false);

        typedArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    if (submitBtn.isEnabled()) {
                        submitTest();}
                }}
        });

        typingPanel.add(new JScrollPane(typedArea), BorderLayout.CENTER);

        center.add(passagePanel);
        center.add(typingPanel);

        JPanel bottom = new JPanel(new BorderLayout());
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        startBtn.addActionListener(this);
        submitBtn.addActionListener(this);
        submitBtn.setEnabled(false);
        buttonRow.add(startBtn);
        buttonRow.add(submitBtn);

        JPanel footer = new JPanel(new BorderLayout());
        footer.add(statusLabel, BorderLayout.CENTER);
        footer.add(clockLabel, BorderLayout.EAST);

        bottom.add(buttonRow, BorderLayout.NORTH);
        bottom.add(footer, BorderLayout.SOUTH);

        testPanel.add(header, BorderLayout.NORTH);
        testPanel.add(center, BorderLayout.CENTER);
        testPanel.add(bottom, BorderLayout.SOUTH);}

    private void buildLeaderboardPanel() {
        leaderboardPanel = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel("Leaderboard — Top 10", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));

        leaderboardArea.setEditable(false);
        leaderboardArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        backFromBoardBtn.addActionListener(this);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backFromBoardBtn);

        leaderboardPanel.add(title, BorderLayout.NORTH);
        leaderboardPanel.add(new JScrollPane(leaderboardArea), BorderLayout.CENTER);
        leaderboardPanel.add(buttonPanel, BorderLayout.SOUTH);}

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == playBtn) {
            resetTestScreen();
            cardLayout.show(container, "test");} 
        else if (source == boardBtn) {
            updateLeaderboard();
            cardLayout.show(container, "leaderboard");} 
        else if (source == exitBtn) {
            int choice = JOptionPane.showConfirmDialog(this, "Exit the program?", "Exit", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                dispose();}} 
        else if (source == backFromTestBtn) {
            stopAllTimers();
            cardLayout.show(container, "menu");}
        else if (source == backFromBoardBtn) {
            cardLayout.show(container, "menu");} 
        else if (source == startBtn) {
            startTest();} 
        else if (source == submitBtn) {
            submitTest();}}

    private void startTest() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name first!");
            nameField.requestFocus();
            return;}

        currentPassage = PassageManager.getRandomPassage(diffBox.getSelectedIndex());
        passageArea.setText(currentPassage);

        typedArea.setText("");
        typedArea.setEnabled(false);
        nameField.setEnabled(false);
        diffBox.setEnabled(false);
        startBtn.setEnabled(false);
        submitBtn.setEnabled(false);

        if (countdownThread != null && countdownThread.isAlive()) {
            countdownThread.interrupt();}

        countdownThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 3; i > 0; i--) {
                        statusLabel.setText("Get ready: " + i);
                        clockLabel.setText("Starting in: " + i);
                        Thread.sleep(1000);
                    }
                    statusLabel.setText("GO! Type the passage, then press Enter to submit.");
                    clockLabel.setText("Time: 0.0 s");
                    typedArea.setEnabled(true);
                    typedArea.requestFocus();
                    submitBtn.setEnabled(true);

                    startTime = System.currentTimeMillis();
                    isRunning = true;
                    clockTimer.start();
                } catch (InterruptedException ex) {}}
        });
        countdownThread.start();}

    private void submitTest() {
        if (!isRunning) return;

        isRunning = false;
        clockTimer.stop();
        stopAllTimers();

        double seconds = (System.currentTimeMillis() - startTime) / 1000.0;
        if (seconds < 0.1) seconds = 0.1;

        Player player = new Player(nameField.getText());
        player.setResult(typedArea.getText(), currentPassage, seconds);

        Leaderboard.saveResult(player);

        String msg = "Player: " + player.getName() + "\n"
                   + "Time: " + (int)player.getTimeSeconds() + " seconds\n"
                   + "Speed: " + (int)player.getWpm() + " WPM\n"
                   + "Accuracy: " + (int)player.getAccuracy() + "%\n\n"
                   + "Result saved to leaderboard!";
        JOptionPane.showMessageDialog(this, msg, "Your Results", JOptionPane.INFORMATION_MESSAGE);

        typedArea.setEnabled(false);
        nameField.setEnabled(true);
        diffBox.setEnabled(true);
        startBtn.setEnabled(true);
        submitBtn.setEnabled(false);
        clockLabel.setText("Time: " + (int)seconds + " s");
        statusLabel.setText("Result saved. Click Start Test to try again!");
    }

    private void stopAllTimers() {
        if (countdownThread != null && countdownThread.isAlive()) {
            countdownThread.interrupt();}
        clockTimer.stop();
        isRunning = false;}

    private void resetTestScreen() {
        stopAllTimers();
        typedArea.setText("");
        typedArea.setEnabled(false);
        nameField.setEnabled(true);
        diffBox.setEnabled(true);
        startBtn.setEnabled(true);
        submitBtn.setEnabled(false);
        clockLabel.setText("Time: 0.0 s");
        statusLabel.setText("Enter your name, choose difficulty, and click Start Test.");}

    private void updateLeaderboard() {
        leaderboardArea.setText(Leaderboard.formatTopTen());}
    public static void main(String[] args) {
        TypingSpeedAnalyzer app = new TypingSpeedAnalyzer();
        app.setVisible(true);}}

abstract class Participant {
    private String name;
    public Participant(String name) {
        if (name == null || name.trim().isEmpty()) {
            this.name = "Player";} 
        else {
            this.name = name.trim();}}

    public String getName() {
        return name;}}

interface Scorable {
    void setResult(String typedText, String originalText, double timeSeconds);
    double getWpm();
    double getAccuracy();}

class Player extends Participant implements Scorable {
    private double timeSeconds, wpm, accuracy;
    public Player(String name) {
        super(name);}

    @Override
    public void setResult(String typedText, String originalText, double timeSeconds) {
        String typed = typedText;
        if (typed == null) {
            typed = "";}

        String original = originalText;
        if (original == null) {
            original = "";}

        this.timeSeconds = timeSeconds;
        if (this.timeSeconds < 0.1) this.timeSeconds = 0.1;

        double minutes = this.timeSeconds / 60.0;
        this.wpm = (typed.length() / 5.0) / minutes;

        int correct = 0;
        int compareLen = Math.min(typed.length(), original.length());
        for (int i = 0; i < compareLen; i++) {
            if (typed.charAt(i) == original.charAt(i)) {
                correct++;}}

        int totalLen = Math.max(typed.length(), original.length());
        if (totalLen > 0) {
            this.accuracy = (correct * 100.0) / totalLen;} 
        else {
            this.accuracy = 100.0;}

        if (this.accuracy > 100.0) this.accuracy = 100.0;
        if (this.accuracy < 0.0) this.accuracy = 0.0;}

    public double getTimeSeconds() { return timeSeconds; }
    @Override public double getWpm() { return wpm; }
    @Override public double getAccuracy() { return accuracy; }}

class PassageManager {
    public static String getRandomPassage(int level) {
        String[] fileNames = {"easy.txt", "medium.txt", "hard.txt"};
        if (level < 0 || level >= fileNames.length) {
            level = 0;}

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileNames[level]));
            String[] list = new String[10];
            int count = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && count < 10) {
                    list[count] = line;
                    count++;}}
            reader.close();

            if (count > 0) {
                int randomIndex = (int)(Math.random() * count);
                return list[randomIndex];}} 
        catch (FileNotFoundException e) {
            } 
            catch (IOException e) {}

        if (level == 0) {
            return "Practice every day to improve your typing speed and accuracy.";} 
        else if (level == 2) {
            return "Reliable software requires careful planning, meaningful testing, and consistent attention to detail.";} 
        else {
            return "A good programmer solves problems by breaking them into smaller and manageable steps.";}}

    public static String getRandomPassage(String difficulty) {
        int level = 0;
        if ("Medium".equals(difficulty)) {
            level = 1;
        } else if ("Hard".equals(difficulty)) {
            level = 2;
        }
        return getRandomPassage(level);}}

class Leaderboard {
    private static final String FILE_NAME = "typing_results.txt";

    public static void saveResult(Player p) {
        try {
            FileWriter fw = new FileWriter(FILE_NAME, true);
            PrintWriter pw = new PrintWriter(fw);
            String cleanName = p.getName().replace("|", " ");
            pw.println("Date|" + cleanName + "|" + (int)p.getWpm() + "|"
                    + (int)p.getAccuracy() + "|" + (int)p.getTimeSeconds());
            pw.close();} 
        catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Could not save result: " + ex.getMessage()); }}

    public static String formatTopTen() {
        Record[] top = new Record[10];
        int count = 0;

        try {
            BufferedReader in = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] a = line.split("\\|", -1);
                if (a.length != 5) continue;

                try {
                    String name = a[1];
                    double wpm = Double.parseDouble(a[2]);
                    double acc = Double.parseDouble(a[3]);
                    double sec = Double.parseDouble(a[4]);
                    Record newRec = new Record(name, wpm, acc, sec);

                    if (count < 10) {
                        top[count] = newRec;
                        count++;}
                    else if (newRec.wpm > top[9].wpm) {
                        top[9] = newRec;}

                    for (int i = 0; i < count - 1; i++) {
                        for (int j = 0; j < count - i - 1; j++) {
                            if (top[j].wpm < top[j + 1].wpm) {
                                Record temp = top[j];
                                top[j] = top[j + 1];
                                top[j + 1] = temp;}}}

                } catch (NumberFormatException ignored) {
                }
            }
            in.close();
        } catch (FileNotFoundException ex) {
            return "No results yet. Play a typing test to create the leaderboard.";
        } catch (IOException ex) {
            return "Could not read leaderboard: " + ex.getMessage();
}

        if (count == 0) {
            return "No valid results yet. Play a typing test first.";}

        String text = "";
        text = text + "Rank\tPlayer\t WPM\t\tAccuracy\tTime\n";
        text = text + "--------------------------------------------------------\n";

        for (int i = 0; i < count; i++) {
            Record r = top[i];
            text = text + (i + 1) + ".\t" + r.name + "\t" 
                        + (int)r.wpm + " WPM\t" 
                        + "\t"+(int)r.accuracy + "%\t\t" 
                        + (int)r.seconds + "s\n";}

        return text;}

    private static class Record {
        String name;
        double wpm, accuracy,seconds;

        Record(String name, double wpm, double accuracy, double seconds) {
            this.name = name;
            this.wpm = wpm;
            this.accuracy = accuracy;
            this.seconds = seconds;}}}
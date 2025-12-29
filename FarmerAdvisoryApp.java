import javax.swing.*;
import java.awt.*;


public class FarmerAdvisoryApp {

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            
            UIManager.put("control", new Color(245, 247, 250));
            UIManager.put("info", new Color(245, 247, 250));
            UIManager.put("nimbusBase", new Color(44, 122, 81));
            UIManager.put("nimbusOrange", new Color(139, 195, 74));
            UIManager.put("nimbusGreen", new Color(46, 204, 113));
            UIManager.put("nimbusRed", new Color(231, 76, 60));
            UIManager.put("nimbusSelectedText", Color.WHITE);
            UIManager.put("nimbusSelectionBackground", new Color(44, 122, 81));
            UIManager.put("nimbusLightBackground", Color.WHITE);
            UIManager.put("nimbusFocus", new Color(139, 195, 74));
            
            UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
            UIManager.put("Button[Default].background", new Color(44, 122, 81));
            UIManager.put("Button.arc", 10);
            
            UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));
            UIManager.put("Table.alternateRowColor", new Color(248, 249, 250));
            UIManager.put("Table.gridColor", new Color(220, 223, 230));
            
            UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 13));
            
            // Label and TextField customizations
            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 13));
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            showSplashScreen();
        });
    }

    private static void showSplashScreen() {
        JWindow splash = new JWindow();
        splash.setSize(500, 350);
        splash.setLocationRelativeTo(null);
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createLineBorder(new Color(44, 122, 81), 3));
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 30, 40));
        
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(44, 122, 81));
                g2.fillOval(60, 10, 80, 80);
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 32));
                g2.drawString("FAS", 75, 60);
            }
        };
        logoPanel.setPreferredSize(new Dimension(200, 100));
        logoPanel.setMaximumSize(new Dimension(500, 100));
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Farmer Crop Suggestion");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 122, 81));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Made By:- Azan Mehdi");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel versionLabel = new JLabel("Farmer Advisory");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(127, 140, 141));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(logoPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(subtitleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        centerPanel.add(versionLabel);
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setForeground(new Color(44, 122, 81));
        progressBar.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        
        JLabel loadingLabel = new JLabel("Loading application...");
        loadingLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        loadingLabel.setForeground(new Color(127, 140, 141));
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(loadingLabel, BorderLayout.NORTH);
        bottomPanel.add(progressBar, BorderLayout.CENTER);
        
        content.add(centerPanel, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);
        
        splash.setContentPane(content);
        splash.setVisible(true);
        
        Timer timer = new Timer(2000, e -> {
            splash.dispose();
            AdvisorySystemGUI gui = new AdvisorySystemGUI();
            gui.setVisible(true);
        });
        timer.setRepeats(false);
        timer.start();
    }
}

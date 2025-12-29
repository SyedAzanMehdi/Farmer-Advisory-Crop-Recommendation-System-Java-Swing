import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;


public class AdvisorySystemGUI extends JFrame {

    private static final Color PRIMARY_COLOR = new Color(34, 139, 87);         // Forest Green
    private static final Color PRIMARY_DARK = new Color(25, 100, 65);          // Darker Green
    private static final Color PRIMARY_LIGHT = new Color(46, 184, 116);        // Lighter Green
    private static final Color SECONDARY_COLOR = new Color(46, 204, 113);      // Emerald
    
    // UI Colors
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 252);    // Off White
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color SIDEBAR_COLOR = new Color(30, 41, 59);          // Slate Dark
    private static final Color SIDEBAR_HOVER = new Color(51, 65, 85);          // Slate
    
    // Status Colors
    private static final Color DANGER_COLOR = new Color(239, 68, 68);          // Red
    private static final Color WARNING_COLOR = new Color(245, 158, 11);        // Amber
    private static final Color INFO_COLOR = new Color(59, 130, 246);           // Blue
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);         // Green
    
    // Text Colors
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);           // Slate 900
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);      // Slate 500
    private static final Color TEXT_LIGHT = new Color(203, 213, 225);          // Slate 300
    
    // Border & Shadow
    private static final Color BORDER_COLOR = new Color(226, 232, 240);        // Slate 200
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 15);          // Subtle shadow
    private static final Color HOVER_OVERLAY = new Color(0, 0, 0, 8);          // Very subtle

    // Professional Fonts
    private static final Font HEADER_FONT = new Font("Segoe UI Semibold", Font.BOLD, 26);
    private static final Font TITLE_FONT = new Font("Segoe UI Semibold", Font.BOLD, 20);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI Semibold", Font.PLAIN, 16);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font BUTTON_FONT = new Font("Segoe UI Semibold", Font.BOLD, 13);
    private static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font MENU_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    // Spacing & Dimensions
    private static final int PADDING_LARGE = 32;
    private static final int PADDING_MEDIUM = 20;
    private static final int PADDING_SMALL = 12;
    private static final int BORDER_RADIUS = 8;
    private static final int CARD_GAP = 16;
    private static final int SIDEBAR_WIDTH = 240;

    // UI State
    private JDialog loadingDialog;
    private JPanel notificationPanel;
    private JLabel statusLabel;


    // Data Structures
    private ArrayList<Crop> crops = new ArrayList<>();
    private HashMap<String, Region> regions = new HashMap<>();
    private Queue<String> historyLogs = new LinkedList<>();
    private Set<String> cropNames = new HashSet<>();
    private ArrayList<User> users = new ArrayList<>();

    // Current session
    private User currentUser;

    // GUI Components
    private CardLayout cardLayout;
    private JPanel contentPanel;


    public AdvisorySystemGUI() {
        initializeData();
        showLoginScreen();
    }


    private void initializeData() {
        // Add default users
        users.add(new User("admin", "admin123", "ADMIN"));
        users.add(new User("farmer", "farmer123", "FARMER"));
        users.add(new User("ali", "ali123", "FARMER"));

        // Add Mianwali District regions (Tehsils)
        regions.put("Mianwali City", new Region("Mianwali City", "Semi-Arid", 380, "Wheat,Chickpea,Sugarcane"));
        regions.put("Piplan", new Region("Piplan", "Semi-Arid", 350, "Wheat,Maize,Groundnut"));
        regions.put("Isa Khel", new Region("Isa Khel", "Arid", 300, "Sorghum,Millet,Chickpea"));
        regions.put("Wan Bhachran", new Region("Wan Bhachran", "Semi-Arid", 320, "Wheat,Barley,Lentils"));
        regions.put("Kalabagh", new Region("Kalabagh", "Arid", 280, "Dates,Wheat,Cotton"));

        // Add 20 crops common in Mianwali region
        addCrop(new Crop("Wheat", "Rabi (Winter)", "Loamy", "Mianwali City", "Medium", 3.8));
        addCrop(new Crop("Chickpea", "Rabi (Winter)", "Sandy Loam", "Mianwali City", "Low", 1.2));
        addCrop(new Crop("Sugarcane", "Kharif (Summer)", "Clay Loam", "Mianwali City", "High", 55.0));
        addCrop(new Crop("Maize", "Kharif (Summer)", "Loamy", "Piplan", "Medium", 3.5));
        addCrop(new Crop("Groundnut", "Kharif (Summer)", "Sandy Loam", "Piplan", "Medium", 2.0));
        addCrop(new Crop("Sorghum", "Kharif (Summer)", "Sandy", "Isa Khel", "Low", 2.2));
        addCrop(new Crop("Millet", "Kharif (Summer)", "Sandy", "Isa Khel", "Low", 1.8));
        addCrop(new Crop("Barley", "Rabi (Winter)", "Loamy", "Wan Bhachran", "Low", 2.5));
        addCrop(new Crop("Lentils", "Rabi (Winter)", "Clay", "Wan Bhachran", "Low", 1.0));
        addCrop(new Crop("Cotton", "Kharif (Summer)", "Sandy Loam", "Kalabagh", "Medium", 2.8));
        addCrop(new Crop("Mustard", "Rabi (Winter)", "Loamy", "Mianwali City", "Low", 1.5));
        addCrop(new Crop("Sunflower", "Kharif (Summer)", "Sandy Loam", "Piplan", "Medium", 2.0));
        addCrop(new Crop("Rice", "Kharif (Summer)", "Clay", "Mianwali City", "High", 4.2));
        addCrop(new Crop("Onion", "Both Seasons", "Loamy", "Wan Bhachran", "Medium", 18.0));
        addCrop(new Crop("Tomato", "Both Seasons", "Sandy Loam", "Piplan", "Medium", 25.0));
        addCrop(new Crop("Potato", "Rabi (Winter)", "Sandy Loam", "Mianwali City", "Medium", 22.0));
        addCrop(new Crop("Mango", "Perennial", "Loamy", "Kalabagh", "Medium", 12.0));
        addCrop(new Crop("Citrus", "Perennial", "Sandy Loam", "Isa Khel", "Medium", 15.0));
        addCrop(new Crop("Fodder", "Both Seasons", "Clay Loam", "Wan Bhachran", "High", 40.0));
        addCrop(new Crop("Sesame", "Kharif (Summer)", "Sandy", "Kalabagh", "Low", 0.8));

        addHistory("System initialized - Mianwali District Agriculture Advisory");
    }



    private boolean addCrop(Crop crop) {
        if (cropNames.contains(crop.name.toLowerCase())) {
            return false;
        }
        crops.add(crop);
        cropNames.add(crop.name.toLowerCase());
        return true;
    }


    private void addHistory(String log) {
        historyLogs.offer("[" + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] " + log);
        if (historyLogs.size() > 100) historyLogs.poll();
    }


    private void showLoginScreen() {
        setTitle("Farmer Advisory System - Login");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Exit confirmation
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (showConfirmDialog("Exit", "Are you sure you want to exit?")) {
                    System.exit(0);
                }
            }
        });

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Login Card
        JPanel formCard = createCardPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setPreferredSize(new Dimension(400, 480));

        // Logo
        JPanel logoPanel = createLogoPanel(70);
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(logoPanel);
        formCard.add(Box.createRigidArea(new Dimension(0, 15)));

        // Title
        JLabel titleLabel = createStyledLabel("Farmer Advisory System", TITLE_FONT, PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(titleLabel);
        JLabel subtitleLabel = createStyledLabel("Mianwali District Agriculture Platform", SMALL_FONT, TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(subtitleLabel);
        formCard.add(Box.createRigidArea(new Dimension(0, 25)));

        // Form Fields
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setOpaque(false);
        fieldsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        fieldsPanel.setMaximumSize(new Dimension(320, 280));

        // Username
        fieldsPanel.add(createStyledLabel("Username", LABEL_FONT, TEXT_PRIMARY));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JTextField usernameField = createStyledTextField(20);
        usernameField.setMaximumSize(new Dimension(320, 40));
        fieldsPanel.add(usernameField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password
        fieldsPanel.add(createStyledLabel("Password", LABEL_FONT, TEXT_PRIMARY));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordField.setMaximumSize(new Dimension(320, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        fieldsPanel.add(passwordField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Role
        fieldsPanel.add(createStyledLabel("Login As", LABEL_FONT, TEXT_PRIMARY));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"ADMIN", "FARMER"});
        roleBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roleBox.setMaximumSize(new Dimension(320, 40));
        fieldsPanel.add(roleBox);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Login Button
        JButton loginBtn = createModernButton("Login", PRIMARY_COLOR, "Login to your account");
        loginBtn.setMaximumSize(new Dimension(320, 45));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        ActionListener loginAction = e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                showErrorBanner("Please enter username and password");
                return;
            }

            User user = users.stream()
                    .filter(u -> u.username.equals(username) && u.password.equals(password) && u.role.equals(role))
                    .findFirst().orElse(null);

            if (user != null) {
                currentUser = user;
                addHistory("User logged in: " + username + " (" + role + ")");
                getContentPane().removeAll();
                if ("ADMIN".equals(role)) {
                    showAdminPanel();
                } else {
                    showFarmerPanel();
                }
            } else {
                showErrorBanner("Invalid credentials or role mismatch");
                passwordField.setText("");
            }
        };

        loginBtn.addActionListener(loginAction);
        usernameField.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);
        fieldsPanel.add(loginBtn);

        formCard.add(fieldsPanel);
        formCard.add(Box.createRigidArea(new Dimension(0, 20)));

        // Version footer
        JLabel versionLabel = createStyledLabel("Version 1.0.0", SMALL_FONT, TEXT_SECONDARY);
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(versionLabel);

        mainPanel.add(formCard);
        add(mainPanel);
    }


    private void showAdminPanel() {
        setTitle("Admin Dashboard - Farmer Advisory System | Mianwali District");
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Add Menu Bar
        setJMenuBar(createMenuBar());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Professional Header with gradient
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, getWidth(), 0, PRIMARY_DARK);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(0, 90));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = createStyledLabel("Administrator Dashboard", HEADER_FONT, Color.WHITE);
        JLabel welcomeLabel = createStyledLabel("Welcome back, " + currentUser.username + " | " + 
            new java.text.SimpleDateFormat("EEEE, MMM dd yyyy").format(new Date()), SMALL_FONT, TEXT_LIGHT);
        
        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setOpaque(false);
        titleContainer.add(titleLabel);
        titleContainer.add(Box.createRigidArea(new Dimension(0, 4)));
        titleContainer.add(welcomeLabel);
        headerPanel.add(titleContainer, BorderLayout.WEST);

        JButton logoutBtn = createModernButton("   Sign Out", DANGER_COLOR, "Sign out of system");
        logoutBtn.setPreferredSize(new Dimension(130, 38));
        logoutBtn.addActionListener(e -> handleLogout());
        headerPanel.add(logoutBtn, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_LARGE, 0, PADDING_LARGE));

        // Stats Cards Panel - professional with icons
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, CARD_GAP, 0));
        statsPanel.setOpaque(false);
        statsPanel.add(createStatCard("Total Crops", String.valueOf(crops.size()), SUCCESS_COLOR));
        statsPanel.add(createStatCard("Regions", String.valueOf(regions.size()), INFO_COLOR));
        statsPanel.add(createStatCard("Active Users", String.valueOf(users.size()), new Color(139, 92, 246)));
        statsPanel.add(createStatCard("Actions", String.valueOf(historyLogs.size()), WARNING_COLOR));
        contentPanel.add(statsPanel, BorderLayout.NORTH);

        // Action Cards Grid
        JPanel actionsWrapper = new JPanel(new BorderLayout());
        actionsWrapper.setOpaque(false);
        actionsWrapper.setBorder(BorderFactory.createEmptyBorder(PADDING_MEDIUM, 0, PADDING_MEDIUM, 0));
        
        JLabel actionsTitle = createStyledLabel("Quick Actions", SUBTITLE_FONT, TEXT_PRIMARY);
        actionsWrapper.add(actionsTitle, BorderLayout.NORTH);

        JPanel actionsPanel = new JPanel(new GridLayout(2, 3, CARD_GAP, CARD_GAP));
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(PADDING_SMALL, 0, 0, 0));

        actionsPanel.add(createActionCard("Add Crop", "Register new crop variety", SUCCESS_COLOR, e -> showAddCropDialog()));
        actionsPanel.add(createActionCard("Update Crop", "Modify crop information", INFO_COLOR, e -> showUpdateCropDialog()));
        actionsPanel.add(createActionCard("Delete Crop", "Remove from database", DANGER_COLOR, e -> showDeleteCropDialog()));
        actionsPanel.add(createActionCard("Manage Regions", "View tehsil data", new Color(139, 92, 246), e -> showManageRegionsDialog()));
        actionsPanel.add(createActionCard("View Users", "Manage system users", new Color(6, 182, 212), e -> showViewUsersDialog()));
        actionsPanel.add(createActionCard("Reports", "Analytics & statistics", WARNING_COLOR, e -> showReportsDialog()));
        
        actionsWrapper.add(actionsPanel, BorderLayout.CENTER);
        contentPanel.add(actionsWrapper, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Professional Status Bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(248, 250, 252));
        statusBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        statusLabel = createStyledLabel("Ready | Database: " + crops.size() + " crops, " + regions.size() + " regions", SMALL_FONT, TEXT_SECONDARY);
        JLabel versionLabel = createStyledLabel("Farmer Advisory System v1.0.0", SMALL_FONT, TEXT_SECONDARY);
        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(versionLabel, BorderLayout.EAST);
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        add(mainPanel);
        revalidate();
        repaint();
    }

    // Create Menu Bar with Keyboard Shortcuts
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(MENU_FONT);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        
        JMenuItem refreshItem = new JMenuItem("Refresh Data");
        refreshItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        refreshItem.addActionListener(e -> { showSuccessBanner("Data refreshed!"); updateStatusBar(); });
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        exitItem.addActionListener(e -> { if (showConfirmDialog("Exit", "Exit application?")) System.exit(0); });
        
        fileMenu.add(refreshItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Data Menu
        JMenu dataMenu = new JMenu("Data");
        dataMenu.setFont(MENU_FONT);
        dataMenu.setMnemonic(KeyEvent.VK_D);
        
        JMenuItem addCropItem = new JMenuItem("Add Crop");
        addCropItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        addCropItem.addActionListener(e -> showAddCropDialog());
        
        JMenuItem viewRegionsItem = new JMenuItem("View Regions");
        viewRegionsItem.addActionListener(e -> showManageRegionsDialog());
        
        JMenuItem viewUsersItem = new JMenuItem("View Users");
        viewUsersItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK));
        viewUsersItem.addActionListener(e -> showViewUsersDialog());
        
        JMenuItem historyItem = new JMenuItem("View History");
        historyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK));
        historyItem.addActionListener(e -> showHistoryDialog());
        
        dataMenu.add(addCropItem);
        dataMenu.add(viewRegionsItem);
        dataMenu.add(viewUsersItem);
        dataMenu.addSeparator();
        dataMenu.add(historyItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setFont(MENU_FONT);
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        aboutItem.addActionListener(e -> showAboutDialog());
        
        JMenuItem versionItem = new JMenuItem("Version Info");
        versionItem.addActionListener(e -> showVersionDialog());
        
        helpMenu.add(aboutItem);
        helpMenu.add(versionItem);

        menuBar.add(fileMenu);
        menuBar.add(dataMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    // About Dialog
    private void showAboutDialog() {
        JDialog dialog = new JDialog(this, "About", true);
        dialog.setSize(400, 280);
        dialog.setLocationRelativeTo(this);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel logo = createLogoPanel(60);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logo);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel title = createStyledLabel("Farmer Advisory System", TITLE_FONT, PRIMARY_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);

        JLabel version = createStyledLabel("Version 1.0.0", LABEL_FONT, TEXT_SECONDARY);
        version.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(version);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel desc = createStyledLabel("Mianwali District Agriculture Platform", SMALL_FONT, TEXT_SECONDARY);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(desc);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton closeBtn = createModernButton("Close", PRIMARY_COLOR);
        closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeBtn.setMaximumSize(new Dimension(100, 35));
        closeBtn.addActionListener(e -> dialog.dispose());
        panel.add(closeBtn);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }



    // Create stat card for dashboard
    private JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout());
        
        JLabel valueLabel = createStyledLabel(value, new Font("Segoe UI", Font.BOLD, 36), accentColor);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = createStyledLabel(title, LABEL_FONT, TEXT_SECONDARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        return card;
    }

    // Create action card for dashboard
    private JPanel createActionCard(String title, String desc, Color color, ActionListener action) {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel titleLabel = createStyledLabel(title, SUBTITLE_FONT, TEXT_PRIMARY);
        JLabel descLabel = createStyledLabel(desc, SMALL_FONT, TEXT_SECONDARY);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(descLabel);
        
        JPanel colorBar = new JPanel();
        colorBar.setBackground(color);
        colorBar.setPreferredSize(new Dimension(5, 0));
        
        card.add(colorBar, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        // Click handler
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(new ActionEvent(card, ActionEvent.ACTION_PERFORMED, title));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(245, 245, 245));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_BACKGROUND);
            }
        });
        
        return card;
    }


    private void showFarmerPanel() {
        setTitle("Farmer Dashboard - Farmer Advisory System");
        setSize(1100, 750);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Modern Navigation Sidebar
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(44, 62, 80));
        navPanel.setPreferredSize(new Dimension(220, 0));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // User profile section
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setOpaque(false);
        profilePanel.setMaximumSize(new Dimension(190, 100));
        
        JPanel avatarPanel = createLogoPanel(50);
        avatarPanel.setBackground(new Color(44, 62, 80));
        avatarPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel nameLabel = createStyledLabel(currentUser.username, SUBTITLE_FONT, Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel roleLabel = createStyledLabel("Farmer", SMALL_FONT, new Color(150, 150, 150));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        profilePanel.add(avatarPanel);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        profilePanel.add(nameLabel);
        profilePanel.add(roleLabel);
        
        navPanel.add(profilePanel);
        navPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Navigation buttons
        JButton recommendationsBtn = createNavButton("Recommendations", true);
        JButton searchBtn = createNavButton("Search Crops", false);
        JButton soilBtn = createNavButton("Soil Suggestions", false);
        JButton logoutBtn = createNavButton("Logout", false);

        navPanel.add(recommendationsBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(searchBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(soilBtn);
        navPanel.add(Box.createVerticalGlue());
        
        // Version footer
        JLabel versionLabel = createStyledLabel("v1.0.0", SMALL_FONT, new Color(100, 100, 100));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        navPanel.add(versionLabel);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(logoutBtn);

        mainPanel.add(navPanel, BorderLayout.WEST);

        // Content Area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND_COLOR);

        contentPanel.add(createRecommendationsPanel(), "REC");
        contentPanel.add(createSearchPanel(), "SEARCH");
        contentPanel.add(createSoilPanel(), "SOIL");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Navigation actions
        recommendationsBtn.addActionListener(e -> {
            cardLayout.show(contentPanel, "REC");
            updateNavButtons(recommendationsBtn, searchBtn, soilBtn);
        });
        searchBtn.addActionListener(e -> {
            cardLayout.show(contentPanel, "SEARCH");
            updateNavButtons(searchBtn, recommendationsBtn, soilBtn);
        });
        soilBtn.addActionListener(e -> {
            cardLayout.show(contentPanel, "SOIL");
            updateNavButtons(soilBtn, recommendationsBtn, searchBtn);
        });
        logoutBtn.addActionListener(e -> handleLogout());

        add(mainPanel);
        revalidate();
        repaint();
    }

    private void updateNavButtons(JButton active, JButton... others) {
        active.setBackground(PRIMARY_COLOR);
        for (JButton b : others) {
            if (b.getText().contains("Logout")) continue;
            b.setBackground(new Color(52, 73, 94));
        }
    }
    
    /**
     * Handle logout action with confirmation
     */
    private void handleLogout() {
        if (showConfirmDialog("Logout", "Are you sure you want to logout?")) {
            addHistory("User logged out: " + currentUser.username);
            currentUser = null;
            getContentPane().removeAll();
            showLoginScreen();
            revalidate();
            repaint();
        }
    }



    private JPanel createRecommendationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(PADDING_MEDIUM, PADDING_MEDIUM));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING_LARGE, PADDING_LARGE, PADDING_LARGE, PADDING_LARGE));

        // Header
        JLabel title = createStyledLabel("Get Crop Recommendations", TITLE_FONT, TEXT_PRIMARY);
        JLabel subtitle = createStyledLabel("Find the best crops for your conditions", SMALL_FONT, TEXT_SECONDARY);
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.add(title);
        headerPanel.add(subtitle);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Form Card
        JPanel formCard = createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> regionBox = new JComboBox<>(regions.keySet().toArray(new String[0]));
        JComboBox<String> soilBox = new JComboBox<>(new String[]{"Loamy", "Clay", "Sandy", "Sandy Loam"});
        JComboBox<String> seasonBox = new JComboBox<>(new String[]{"Rabi", "Kharif", "Both", "Perennial"});

        gbc.gridx = 0; gbc.gridy = 0;
        formCard.add(createStyledLabel("Region:", LABEL_FONT, TEXT_PRIMARY), gbc);
        gbc.gridx = 1;
        formCard.add(regionBox, gbc);
        gbc.gridx = 2;
        formCard.add(createStyledLabel("Soil Type:", LABEL_FONT, TEXT_PRIMARY), gbc);
        gbc.gridx = 3;
        formCard.add(soilBox, gbc);
        gbc.gridx = 4;
        formCard.add(createStyledLabel("Season:", LABEL_FONT, TEXT_PRIMARY), gbc);
        gbc.gridx = 5;
        formCard.add(seasonBox, gbc);

        gbc.gridx = 6; gbc.gridy = 0;
        JButton getBtn = createModernButton("Get Recommendations", PRIMARY_COLOR, "Find matching crops");
        getBtn.setPreferredSize(new Dimension(180, 35));
        formCard.add(getBtn, gbc);

        // Results Table
        DefaultTableModel model = new DefaultTableModel(new String[]{"Crop Name", "Match Score", "Season", "Expected Yield (t/ha)"}, 0);
        JTable table = createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 223, 230)));

        getBtn.addActionListener(e -> {
            model.setRowCount(0);
            String region = (String) regionBox.getSelectedItem();
            String soil = (String) soilBox.getSelectedItem();
            String season = (String) seasonBox.getSelectedItem();

            List<Recommendation> recs = getRecommendations(soil, season, region);
            if (recs.isEmpty()) {
                showErrorBanner("No crops found matching your criteria");
            } else {
                for (Recommendation rec : recs) {
                    model.addRow(new Object[]{rec.crop.name, rec.score + "%", rec.crop.season, rec.crop.expectedYield});
                }
                showSuccessBanner("Found " + recs.size() + " matching crops!");
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout(0, PADDING_MEDIUM));
        centerPanel.setOpaque(false);
        centerPanel.add(formCard, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }


    /**
     * Modern Search Panel with real-time filtering
     */
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(PADDING_MEDIUM, PADDING_MEDIUM));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING_LARGE, PADDING_LARGE, PADDING_LARGE, PADDING_LARGE));

        // Header
        JLabel title = createStyledLabel("Search Crops", TITLE_FONT, TEXT_PRIMARY);
        JLabel subtitle = createStyledLabel("Find crops by name - results filter as you type", SMALL_FONT, TEXT_SECONDARY);
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.add(title);
        headerPanel.add(subtitle);

        // Search Card
        JPanel searchCard = createCardPanel();
        searchCard.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        JTextField searchField = createStyledTextField(25);
        searchField.setToolTipText("Type to search crops...");
        JButton clearBtn = createModernButton("Clear", TEXT_SECONDARY);
        clearBtn.setPreferredSize(new Dimension(80, 35));
        JLabel resultLabel = createStyledLabel("Showing all crops", SMALL_FONT, TEXT_SECONDARY);

        searchCard.add(createStyledLabel("Search:", LABEL_FONT, TEXT_PRIMARY));
        searchCard.add(searchField);
        searchCard.add(clearBtn);
        searchCard.add(Box.createHorizontalStrut(20));
        searchCard.add(resultLabel);

        JPanel topPanel = new JPanel(new BorderLayout(0, PADDING_SMALL));
        topPanel.setOpaque(false);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(searchCard, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.NORTH);

        // Table
        DefaultTableModel model = new DefaultTableModel(new String[]{"Crop Name", "Season", "Soil Type", "Region", "Yield (t/ha)"}, 0);
        JTable table = createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 223, 230)));

        // Load all crops initially
        for (Crop crop : crops) {
            model.addRow(new Object[]{crop.name, crop.season, crop.soilType, crop.region, crop.expectedYield});
        }
        resultLabel.setText("Showing " + crops.size() + " crops");

        // Real-time search with KeyListener
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText().toLowerCase();
                model.setRowCount(0);
                int count = 0;
                for (Crop crop : crops) {
                    if (crop.name.toLowerCase().contains(query) ||
                        crop.season.toLowerCase().contains(query) ||
                        crop.region.toLowerCase().contains(query)) {
                        model.addRow(new Object[]{crop.name, crop.season, crop.soilType, crop.region, crop.expectedYield});
                        count++;
                    }
                }
                resultLabel.setText("Found " + count + " crops");
            }
        });

        clearBtn.addActionListener(e -> {
            searchField.setText("");
            model.setRowCount(0);
            for (Crop crop : crops) {
                model.addRow(new Object[]{crop.name, crop.season, crop.soilType, crop.region, crop.expectedYield});
            }
            resultLabel.setText("Showing " + crops.size() + " crops");
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Modern Soil Suggestions Panel
     */
    private JPanel createSoilPanel() {
        JPanel panel = new JPanel(new BorderLayout(PADDING_MEDIUM, PADDING_MEDIUM));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING_LARGE, PADDING_LARGE, PADDING_LARGE, PADDING_LARGE));

        // Header
        JLabel title = createStyledLabel("Soil-Based Suggestions", TITLE_FONT, TEXT_PRIMARY);
        JLabel subtitle = createStyledLabel("Find crops suitable for your soil type", SMALL_FONT, TEXT_SECONDARY);
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.add(title);
        headerPanel.add(subtitle);

        // Selection Card
        JPanel selectCard = createCardPanel();
        selectCard.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        JComboBox<String> soilBox = new JComboBox<>(new String[]{"Loamy", "Clay", "Sandy", "Sandy Loam", "Clay Loam"});
        soilBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JButton getBtn = createModernButton("Get Suggestions", new Color(155, 89, 182), "Find crops for this soil");
        getBtn.setPreferredSize(new Dimension(150, 35));

        selectCard.add(createStyledLabel("Select Soil Type:", LABEL_FONT, TEXT_PRIMARY));
        selectCard.add(soilBox);
        selectCard.add(Box.createHorizontalStrut(10));
        selectCard.add(getBtn);

        JPanel topPanel = new JPanel(new BorderLayout(0, PADDING_SMALL));
        topPanel.setOpaque(false);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(selectCard, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.NORTH);

        // Table
        DefaultTableModel model = new DefaultTableModel(new String[]{"Crop Name", "Season", "Water Requirement", "Yield (t/ha)"}, 0);
        JTable table = createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 223, 230)));

        getBtn.addActionListener(e -> {
            model.setRowCount(0);
            String soil = (String) soilBox.getSelectedItem();
            int count = 0;
            for (Crop crop : crops) {
                if (crop.soilType.toLowerCase().contains(soil.toLowerCase())) {
                    model.addRow(new Object[]{crop.name, crop.season, crop.waterRequirement, crop.expectedYield});
                    count++;
                }
            }
            if (count > 0) {
                showSuccessBanner("Found " + count + " crops for " + soil + " soil");
            } else {
                showErrorBanner("No crops found for " + soil + " soil");
            }
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }



    private List<Recommendation> getRecommendations(String soilType, String season, String region) {
        List<Recommendation> recs = new ArrayList<>();

        for (Crop crop : crops) {
            int score = 0;

            // Soil match (40 points)
            if (crop.soilType.equalsIgnoreCase(soilType)) score += 40;
            else if (crop.soilType.toLowerCase().contains(soilType.toLowerCase())) score += 20;

            // Season match (35 points)
            if (crop.season.toLowerCase().contains(season.toLowerCase())) score += 35;

            // Region match (25 points)
            if (crop.region.equalsIgnoreCase(region)) score += 25;

            if (score > 0) {
                recs.add(new Recommendation(crop, score));
            }
        }

        recs.sort((a, b) -> Integer.compare(b.score, a.score));
        return recs;
    }

    // MODERN ADMIN DIALOGS

    private void showAddCropDialog() {
        JDialog dialog = new JDialog(this, "Add New Crop", true);
        dialog.setSize(480, 500);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM));

        JLabel titleLbl = createStyledLabel("Add New Crop", TITLE_FONT, PRIMARY_COLOR);
        mainPanel.add(titleLbl, BorderLayout.NORTH);

        JPanel formCard = createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JTextField nameField = createStyledTextField(20);
        JComboBox<String> seasonBox = new JComboBox<>(new String[]{"Rabi (Winter)", "Kharif (Summer)", "Both Seasons", "Perennial"});
        JComboBox<String> soilBox = new JComboBox<>(new String[]{"Loamy", "Clay", "Sandy", "Sandy Loam", "Clay Loam"});
        JComboBox<String> regionBox = new JComboBox<>(regions.keySet().toArray(new String[0]));
        JComboBox<String> waterBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        JTextField yieldField = createStyledTextField(20);

        String[] labels = {"Crop Name:", "Season:", "Soil Type:", "Region:", "Water Req:", "Yield (t/ha):"};
        Component[] fields = {nameField, seasonBox, soilBox, regionBox, waterBox, yieldField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.3;
            formCard.add(createStyledLabel(labels[i], LABEL_FONT, TEXT_PRIMARY), gbc);
            gbc.gridx = 1; gbc.weightx = 0.7;
            formCard.add(fields[i], gbc);
        }
        mainPanel.add(formCard, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setOpaque(false);
        JButton cancelBtn = createModernButton("Cancel", TEXT_SECONDARY);
        cancelBtn.setPreferredSize(new Dimension(100, 40));
        cancelBtn.addActionListener(e -> dialog.dispose());
        JButton saveBtn = createModernButton("Save Crop", SUCCESS_COLOR);
        saveBtn.setPreferredSize(new Dimension(120, 40));
        ActionListener saveAction = e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) { showErrorBanner("Crop name is required"); return; }
            try {
                double yield = Double.parseDouble(yieldField.getText().trim());
                Crop crop = new Crop(name, (String)seasonBox.getSelectedItem(), (String)soilBox.getSelectedItem(),
                        (String)regionBox.getSelectedItem(), (String)waterBox.getSelectedItem(), yield);
                if (addCrop(crop)) {
                    addHistory("Added crop: " + crop.name);
                    showSuccessBanner("Crop added successfully!");
                    dialog.dispose();
                } else { showErrorBanner("Crop already exists!"); }
            } catch (NumberFormatException ex) { showErrorBanner("Invalid yield value"); }
        };

        saveBtn.addActionListener(saveAction);
        nameField.addActionListener(saveAction);
        yieldField.addActionListener(saveAction);
        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
    }

    private void showUpdateCropDialog() {
        if (crops.isEmpty()) { showErrorBanner("No crops to update!"); return; }
        String[] names = crops.stream().map(c -> c.name).toArray(String[]::new);
        JComboBox<String> selectBox = new JComboBox<>(names);
        int result = JOptionPane.showConfirmDialog(this, selectBox, "Select Crop", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;
        String selected = (String) selectBox.getSelectedItem();
        Crop crop = crops.stream().filter(c -> c.name.equals(selected)).findFirst().orElse(null);
        if (crop == null) return;
        JDialog dialog = new JDialog(this, "Update: " + crop.name, true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM));
        mainPanel.add(createStyledLabel("Update: " + crop.name, TITLE_FONT, INFO_COLOR), BorderLayout.NORTH);
        JPanel formCard = createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JComboBox<String> seasonBox = new JComboBox<>(new String[]{"Rabi (Winter)", "Kharif (Summer)", "Both Seasons", "Perennial"});
        seasonBox.setSelectedItem(crop.season);
        JComboBox<String> soilBox = new JComboBox<>(new String[]{"Loamy", "Clay", "Sandy", "Sandy Loam", "Clay Loam"});
        soilBox.setSelectedItem(crop.soilType);
        JTextField yieldField = createStyledTextField(15);
        yieldField.setText(String.valueOf(crop.expectedYield));
        gbc.gridx = 0; gbc.gridy = 0; formCard.add(createStyledLabel("Season:", LABEL_FONT, TEXT_PRIMARY), gbc);
        gbc.gridx = 1; formCard.add(seasonBox, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formCard.add(createStyledLabel("Soil:", LABEL_FONT, TEXT_PRIMARY), gbc);
        gbc.gridx = 1; formCard.add(soilBox, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formCard.add(createStyledLabel("Yield:", LABEL_FONT, TEXT_PRIMARY), gbc);
        gbc.gridx = 1; formCard.add(yieldField, gbc);
        mainPanel.add(formCard, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        JButton updateBtn = createModernButton("Update", INFO_COLOR);
        updateBtn.setPreferredSize(new Dimension(100, 40));
        updateBtn.addActionListener(e -> {
            try {
                crop.season = (String) seasonBox.getSelectedItem();
                crop.soilType = (String) soilBox.getSelectedItem();
                crop.expectedYield = Double.parseDouble(yieldField.getText().trim());
                addHistory("Updated crop: " + crop.name);
                showSuccessBanner("Crop updated!");
                dialog.dispose();
            } catch (NumberFormatException ex) { showErrorBanner("Invalid yield"); }
        });
        btnPanel.add(updateBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
    }

    private void showDeleteCropDialog() {
        if (crops.isEmpty()) { showErrorBanner("No crops to delete!"); return; }
        String[] names = crops.stream().map(c -> c.name).toArray(String[]::new);
        JComboBox<String> selectBox = new JComboBox<>(names);
        int result = JOptionPane.showConfirmDialog(this, selectBox, "Select Crop to Delete", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;
        String selected = (String) selectBox.getSelectedItem();
        if (showConfirmDialog("Delete Crop", "Delete '" + selected + "'?")) {
            crops.removeIf(c -> c.name.equals(selected));
            cropNames.remove(selected.toLowerCase());
            addHistory("Deleted crop: " + selected);
            showSuccessBanner("Crop deleted!");
        }
    }

    private void showManageRegionsDialog() {
        JDialog dialog = new JDialog(this, "Manage Regions", true);
        dialog.setSize(700, 400);
        dialog.setLocationRelativeTo(this);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM));
        mainPanel.add(createStyledLabel("Region Management", TITLE_FONT, PRIMARY_COLOR), BorderLayout.NORTH);
        DefaultTableModel model = new DefaultTableModel(new String[]{"Region", "Climate", "Rainfall (mm)", "Common Crops"}, 0);
        for (Region r : regions.values()) { model.addRow(new Object[]{r.name, r.climate, r.avgRainfall, r.commonCrops}); }
        JTable table = createStyledTable(model);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton closeBtn = createModernButton("Close", PRIMARY_COLOR);
        closeBtn.setPreferredSize(new Dimension(100, 40));
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        btnPanel.add(closeBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
    }

    private void showHistoryDialog() {
        JDialog dialog = new JDialog(this, "System History", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM));
        mainPanel.add(createStyledLabel("System Activity History", TITLE_FONT, PRIMARY_COLOR), BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        area.setBackground(new Color(248, 249, 250));

        StringBuilder sb = new StringBuilder();
        for (String log : historyLogs) {
            sb.append(log).append("\n");
        }
        area.setText(sb.toString());

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        JButton closeBtn = createModernButton("Close", PRIMARY_COLOR);
        closeBtn.setPreferredSize(new Dimension(100, 40));
        closeBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(closeBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
    }


    private void showViewUsersDialog() {
        JDialog dialog = new JDialog(this, "User Management", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM));
        mainPanel.add(createStyledLabel("System Users", TITLE_FONT, new Color(139, 92, 246)), BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Username", "Role", "Status"}, 0);
        for (User u : users) {
            String status = (currentUser != null && currentUser.username.equals(u.username)) ? "Active" : "Inactive";
            model.addRow(new Object[]{u.username, u.role, status});
        }
        JTable table = createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setOpaque(false);
        
        JLabel countLabel = createStyledLabel("Total Users: " + users.size(), SMALL_FONT, TEXT_SECONDARY);
        btnPanel.add(countLabel);
        btnPanel.add(Box.createHorizontalStrut(20));
        
        JButton closeBtn = createModernButton("Close", PRIMARY_COLOR);
        closeBtn.setPreferredSize(new Dimension(100, 40));
        closeBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(closeBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
        addHistory("Viewed user list");
    }


    private void showVersionDialog() {
        JDialog dialog = new JDialog(this, "Version Information", true);
        dialog.setSize(400, 320);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel logo = createLogoPanel(60);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logo);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel title = createStyledLabel("Farmer Advisory System", TITLE_FONT, PRIMARY_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Version details
        String[][] versionInfo = {
            {"Build:", "2024.12.29"},
            {"Java:", System.getProperty("java.version")},
            {"Platform:", System.getProperty("os.name")},
            {"Region:", "Mianwali District, Pakistan"}
        };

        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 5));
        infoPanel.setOpaque(false);
        infoPanel.setMaximumSize(new Dimension(280, 120));
        for (String[] info : versionInfo) {
            infoPanel.add(createStyledLabel(info[0], LABEL_FONT, TEXT_SECONDARY));
            infoPanel.add(createStyledLabel(info[1], LABEL_FONT, TEXT_PRIMARY));
        }
        panel.add(infoPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton closeBtn = createModernButton("Close", PRIMARY_COLOR);
        closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeBtn.setMaximumSize(new Dimension(100, 35));
        closeBtn.addActionListener(e -> dialog.dispose());
        panel.add(closeBtn);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }


    private void showReportsDialog() {
        JDialog dialog = new JDialog(this, "System Reports", true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM));
        mainPanel.add(createStyledLabel("System Reports & Analytics", TITLE_FONT, WARNING_COLOR), BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(LABEL_FONT);

        // Crops by Region Tab
        JPanel regionTab = new JPanel(new BorderLayout());
        regionTab.setBackground(CARD_BACKGROUND);
        DefaultTableModel regionModel = new DefaultTableModel(new String[]{"Region", "Crop Count", "Crops"}, 0);
        for (String regionName : regions.keySet()) {
            StringBuilder cropList = new StringBuilder();
            int count = 0;
            for (Crop c : crops) {
                if (c.region.equals(regionName)) {
                    if (count > 0) cropList.append(", ");
                    cropList.append(c.name);
                    count++;
                }
            }
            regionModel.addRow(new Object[]{regionName, count, cropList.toString()});
        }
        regionTab.add(new JScrollPane(createStyledTable(regionModel)), BorderLayout.CENTER);
        tabbedPane.addTab("Crops by Region", regionTab);

        // Crops by Season Tab
        JPanel seasonTab = new JPanel(new BorderLayout());
        seasonTab.setBackground(CARD_BACKGROUND);
        DefaultTableModel seasonModel = new DefaultTableModel(new String[]{"Season", "Crop Count", "Crops"}, 0);
        HashMap<String, java.util.List<String>> seasonCrops = new HashMap<>();
        for (Crop c : crops) {
            seasonCrops.computeIfAbsent(c.season, k -> new ArrayList<>()).add(c.name);
        }
        for (String season : seasonCrops.keySet()) {
            java.util.List<String> list = seasonCrops.get(season);
            seasonModel.addRow(new Object[]{season, list.size(), String.join(", ", list)});
        }
        seasonTab.add(new JScrollPane(createStyledTable(seasonModel)), BorderLayout.CENTER);
        tabbedPane.addTab("Crops by Season", seasonTab);

        // Summary Tab
        JPanel summaryTab = new JPanel(new GridLayout(4, 2, 20, 15));
        summaryTab.setBackground(CARD_BACKGROUND);
        summaryTab.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        summaryTab.add(createStatCard("Total Crops", String.valueOf(crops.size()), SUCCESS_COLOR));
        summaryTab.add(createStatCard("Total Regions", String.valueOf(regions.size()), INFO_COLOR));
        summaryTab.add(createStatCard("Total Users", String.valueOf(users.size()), new Color(139, 92, 246)));
        summaryTab.add(createStatCard("Log Entries", String.valueOf(historyLogs.size()), WARNING_COLOR));
        tabbedPane.addTab("Summary", summaryTab);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        JButton closeBtn = createModernButton("Close", PRIMARY_COLOR);
        closeBtn.setPreferredSize(new Dimension(100, 40));
        closeBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(closeBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
        addHistory("Generated system reports");
    }


    private void updateStatusBar() {
        if (statusLabel != null) {
            statusLabel.setText("Ready | Database: " + crops.size() + " crops, " + regions.size() + " regions, " + users.size() + " users");
        }
    }


    private void logout() {
        addHistory("User logged out: " + currentUser.username);
        currentUser = null;
        getContentPane().removeAll();
        showLoginScreen();
        revalidate();
        repaint();
    }


    private JButton createModernButton(String text, Color bgColor) {
        return createModernButton(text, bgColor, null);
    }

    private JButton createModernButton(String text, Color bgColor, String tooltip) {
        JButton btn = new JButton(text);
        btn.setFont(BUTTON_FONT);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        if (tooltip != null) btn.setToolTipText(tooltip);
        return btn;
    }


    private JButton createNavButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(180, 45));
        btn.setBackground(isActive ? PRIMARY_COLOR : new Color(52, 73, 94));
        btn.setForeground(Color.WHITE);
        btn.setFont(BUTTON_FONT);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            Color originalColor = btn.getBackground();

            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(PRIMARY_COLOR.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(originalColor);
            }
        });

        return btn;
    }
    private JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 223, 230), 1),
                BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM)
        ));
        return card;
    }


    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 223, 230));
        table.setSelectionBackground(PRIMARY_COLOR.brighter());
        table.setSelectionForeground(Color.WHITE);

        // Custom header renderer
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                    c.setForeground(TEXT_PRIMARY);
                }
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return c;
            }
        });

        return table;
    }

    /**
     * Create styled label
     */
    private JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }


    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        // Focus border highlight
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                        BorderFactory.createEmptyBorder(7, 9, 7, 9)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
            }
        });

        return field;
    }


    private void showLoadingDialog(String message) {
        if (loadingDialog != null && loadingDialog.isVisible()) {
            return;
        }

        loadingDialog = new JDialog(this, "Loading", true);
        loadingDialog.setUndecorated(true);
        loadingDialog.setSize(300, 100);
        loadingDialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(LABEL_FONT);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setForeground(PRIMARY_COLOR);

        panel.add(label, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);

        loadingDialog.setContentPane(panel);

        // Show in separate thread
        new Thread(() -> loadingDialog.setVisible(true)).start();
    }


    private void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dispose();
            loadingDialog = null;
        }
    }


    private void showSuccessBanner(String message) {
        showBanner(message, SUCCESS_COLOR);
    }


    private void showErrorBanner(String message) {
        showBanner(message, DANGER_COLOR);
    }


    private void showBanner(String message, Color color) {
        // Create dialog first and set undecorated BEFORE making it displayable
        JDialog dialog = new JDialog(this);
        dialog.setUndecorated(true);
        dialog.setModal(false);
        
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(color);
        banner.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel label = new JLabel(message);
        label.setFont(LABEL_FONT);
        label.setForeground(Color.WHITE);
        banner.add(label, BorderLayout.CENTER);

        dialog.setContentPane(banner);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        // Auto-dismiss after 3 seconds
        javax.swing.Timer timer = new javax.swing.Timer(3000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
    }


    private boolean showConfirmDialog(String title, String message) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM));

        JLabel msgLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>");
        msgLabel.setFont(LABEL_FONT);
        msgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(CARD_BACKGROUND);

        final boolean[] result = {false};

        JButton yesBtn = createModernButton("Yes", SUCCESS_COLOR);
        yesBtn.setPreferredSize(new Dimension(100, 40));
        yesBtn.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });

        JButton noBtn = createModernButton("No", DANGER_COLOR);
        noBtn.setPreferredSize(new Dimension(100, 40));
        noBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(yesBtn);
        buttonPanel.add(noBtn);

        panel.add(msgLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.setVisible(true);

        return result[0];
    }


    private JPanel createLogoPanel(int size) {
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int centerX = (getWidth() - size) / 2;
                int centerY = (getHeight() - size) / 2;

                // Draw circle
                g2.setColor(PRIMARY_COLOR);
                g2.fillOval(centerX, centerY, size, size);

                // Draw initials
                g2.setColor(Color.WHITE);
                Font logoFont = new Font("Segoe UI", Font.BOLD, size / 2);
                g2.setFont(logoFont);
                FontMetrics fm = g2.getFontMetrics();
                String text = "FAS";
                int textX = centerX + (size - fm.stringWidth(text)) / 2;
                int textY = centerY + (size + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(text, textX, textY);
            }
        };
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setPreferredSize(new Dimension(size + 20, size + 20));
        return logoPanel;
    }


    class Crop {
        String name, season, soilType, region, waterRequirement;
        double expectedYield;

        Crop(String name, String season, String soilType, String region, String waterRequirement, double expectedYield) {
            this.name = name;
            this.season = season;
            this.soilType = soilType;
            this.region = region;
            this.waterRequirement = waterRequirement;
            this.expectedYield = expectedYield;
        }
    }

    class Region {
        String name, climate;
        double avgRainfall;
        String commonCrops;

        Region(String name, String climate, double avgRainfall, String commonCrops) {
            this.name = name;
            this.climate = climate;
            this.avgRainfall = avgRainfall;
            this.commonCrops = commonCrops;
        }
    }

    class User {
        String username, password, role;

        User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }

    class Recommendation {
        Crop crop;
        int score;

        Recommendation(Crop crop, int score) {
            this.crop = crop;
            this.score = score;
        }
    }
}

package dataapp;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.io.*;

/**
 * Class represents main application
 * All the user actions are written to the file
 * and read from file.
 * Users can filter trough the JTable
 * ICalculation is done based on user search criteria
 * The security issues remain here regarding the data file
 * The data file should be encrypted so only loged in users
 * have access to the database, current issue is that the file can be
 * simply opened in the file explorer
 * @author  Tomasz Walis-Walisiak
 * @version 1.5
 * @since   20-03-2017
 */
public class DataAppGUI extends JFrame implements ActionListener, KeyListener, WindowFocusListener  {

    private final String FILE_NAME = "1709_nhs-dent-stat-eng-15-16-q1-anx4-pseen.csv";
    private DataCalculation dataCalculation = new DataCalculation();
    private UserIActivityLog activity = new UserIActivityLog();
    private JTextField txtSearch, txtMin, txtMode, txtMax;
    private DataGraph dataGraph = new DataGraph();
    private JMenuItem activityLog, logOut, exit;
    private final JTable table;
    private JPanel graphPanel;
    private JButton btSearch;
    private int min, max, mode;
    private JMenuBar menuBar;


    private final int FRAME_WIDTH = 1200;
    private final int FRAME_HEIGHT = 600;
    private final int MAIN_PANEL_BORDER = 10;
    private final int SEARCH_PANEL_BORDER = 45;
    private final int GRID_SPACING = 10;
    private final int LABEL_WIDTH = 200;
    private final int LABEL_HEIGHT = 40;
    private final int Y_POSITION = 60;
    private final int TXT_WIDTH = 80;
    private final int TXT_HEIGHT = 35;


    /**
     * class constructor contains a application JFrame
     */
    public DataAppGUI() {

        super("Data App");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            /**
             * This method contains JOptionPane
             * When the frame is exited it asks user
             * for the confirmation
             */
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(null,
                        "Are you sure you want to exit application?",
                        "Exit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        FileOutputStream writer = new FileOutputStream("activity.txt");
                        writer.write(("").getBytes());
                        writer.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    System.exit(1);
                }
            }
        });
        setLocationRelativeTo(null);
        setVisible(true);
        addKeyListener(this);
        addWindowFocusListener(this);
        setResizable(false);

        Font f = new Font("sans-serif", Font.BOLD, 17);
        UIManager.put("Menu.font", f);
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
        activityLog = new JMenuItem("Activity Log", KeyEvent.VK_F);
        logOut = new JMenuItem("Log Out", KeyEvent.VK_N);
        exit = new JMenuItem("Exit", KeyEvent.VK_F);
        menu.add(activityLog);
        menu.add(logOut);
        menu.add(exit);
        add(menuBar);
        setJMenuBar(menuBar);

        JLabel lbCalculation = new JLabel("CALCULATION RESULT:");
        JLabel lbMinimum = new JLabel("MIN:");
        JLabel lbMode = new JLabel("MODE:");
        JLabel lbMaximum = new JLabel("MAX:");

        btSearch = new JButton("SEARCH");
        txtSearch = new JTextField();
        txtSearch.setHorizontalAlignment(SwingConstants.CENTER);
        txtMode = new JTextField();
        txtMode.setHorizontalAlignment(SwingConstants.CENTER);
        txtMode.setEditable(false);
        txtMax = new JTextField();
        txtMax.setHorizontalAlignment(SwingConstants.CENTER);
        txtMax.setEditable(false);
        txtMin = new JTextField();
        txtMin.setHorizontalAlignment(SwingConstants.CENTER);
        txtMin.setEditable(false);

        btSearch.addActionListener(this);

        activityLog.addActionListener(this);
        logOut.addActionListener(this);
        exit.addActionListener(this);

        logOut.addKeyListener(this);
        txtSearch.addKeyListener(this);

        JPanel mainPanel = new JPanel();
        add(mainPanel);
        mainPanel.setLayout(new GridLayout(2, 0, GRID_SPACING, GRID_SPACING));
        mainPanel.setBorder(new EmptyBorder(MAIN_PANEL_BORDER, MAIN_PANEL_BORDER, MAIN_PANEL_BORDER, MAIN_PANEL_BORDER));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(0, 2, GRID_SPACING, GRID_SPACING));
        mainPanel.add(topPanel);

        JPanel topPanelLeft = new JPanel();
        topPanelLeft.setLayout(new GridLayout(2, 0, GRID_SPACING, GRID_SPACING));
        topPanel.add(topPanelLeft);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(1, 0, TXT_HEIGHT, GRID_SPACING));
        searchPanel.setBorder(new EmptyBorder(SEARCH_PANEL_BORDER, SEARCH_PANEL_BORDER, SEARCH_PANEL_BORDER, SEARCH_PANEL_BORDER));
        topPanelLeft.add(searchPanel);

        searchPanel.add(txtSearch);
        searchPanel.add(btSearch);


        JPanel calculationPanel = new JPanel();
        calculationPanel.setLayout(null);
        topPanelLeft.add(calculationPanel);
        calculationPanel.add(lbCalculation);
        lbCalculation.setBounds(200, 5, LABEL_WIDTH, LABEL_HEIGHT);

        calculationPanel.add(lbMinimum);
        lbMinimum.setBounds(40, Y_POSITION, LABEL_WIDTH, LABEL_HEIGHT);
        calculationPanel.add(txtMin);
        txtMin.setBounds(70, Y_POSITION, TXT_WIDTH, TXT_HEIGHT);

        calculationPanel.add(lbMaximum);
        lbMaximum.setBounds(200, Y_POSITION, LABEL_WIDTH, LABEL_HEIGHT);
        calculationPanel.add(txtMax);
        txtMax.setBounds(235, Y_POSITION, TXT_WIDTH, TXT_HEIGHT);

        calculationPanel.add(lbMode);
        lbMode.setBounds(380, Y_POSITION, LABEL_WIDTH, LABEL_HEIGHT);
        calculationPanel.add(txtMode);
        txtMode.setBounds(425, Y_POSITION, TXT_WIDTH, 40);



        graphPanel = new JPanel();
        graphPanel.setLayout(new GridLayout(1, 0, TXT_HEIGHT, TXT_HEIGHT));
        graphPanel.setVisible(false);
        dataGraph.display(graphPanel);
        topPanel.add(graphPanel);



        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new GridLayout(0, 1, GRID_SPACING, GRID_SPACING));
        mainPanel.add(tablePanel);
        DataTable model = new DataTable();

        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(600, 800));
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);

        tablePanel.add(table);
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane);
        getFile();


    }

    /**
     * This method gets the value from the search text
     */
    public String getSearchTextField() {
        return txtSearch.getText();
    }

    /**
     * This method gets the data from file
     * and populates JTable rows with csv file
     */
    public void getFile() {
        ReadFile  reader = new ReadFile (this);
        DataTable dataTableModel = new DataTable();
        table.setModel(dataTableModel);
        File DataFile = new File(FILE_NAME);
        ArrayList<String[]> readRows = reader.ReadCsvFile(DataFile);
        dataTableModel.insertScvFile(readRows);
        dataCalculation.minMaxModeCalculation(table);

        updateCalculation();
        dataGraph.setGraphValues(min, max, mode);
        graphPanel.repaint();
        graphPanel.setVisible(true);

    }

    /**
     * This method updates the calculation result
     */
    private void updateCalculation() {

        min = dataCalculation.getMin();
        max = dataCalculation.getMax();
        mode = dataCalculation.getMode();
        txtMax.setText("" + max);
        txtMin.setText("" + min);
        txtMode.setText("" + mode);
        activity.saveToFile("Search result: " + " Max: " + max + "Min: " + min + "Mode " + mode);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btSearch) {
            getFile();

            activity.saveToFile("Search button clicked");
            activity.saveToFile("Value searched " + txtSearch.getText());
        } else if (e.getSource() == activityLog) {
            activity.saveToFile("Activity Log Menu clicked");
            UserIActivityLog a = new UserIActivityLog();
            a.showLog();

        } else if (e.getSource() == logOut) {
            activity.saveToFile("Log out menu clicked");
            try {
                FileOutputStream writer = new FileOutputStream("activity.txt");
                writer.write(("").getBytes());
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            new LoginForm().setVisible(true);
            this.dispose();
        } else if (e.getSource() == exit) {
            activity.saveToFile("Exit clicked");
            int confirm = JOptionPane.showOptionDialog(null,
                    "Are you sure you want to log out and exit application?",
                    "Exit", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(1);
            }


        }
    }


    /**
     * Considering HCI, users with disabilities
     * This method allows the user to navigate the registration form
     * only with keyboard.
     * ENTER Key to click on the Search button
     * ALT+F$ Key to exit application
     * CTRL+L to log out
     */
    @Override
    public void keyPressed(KeyEvent e) {

        if ((e.getKeyCode() == KeyEvent.VK_L) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
            logOut.doClick();
        }else if ((e.getKeyCode() == KeyEvent.VK_ALT) && ((e.getModifiers() & KeyEvent.VK_F4) != 0)){
            exit.doClick();
        }else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            btSearch.doClick();
        }
    }


    @Override
    public void windowGainedFocus(WindowEvent windowEvent) {
        txtSearch.requestFocusInWindow();
    }

    /**
     * Unused methods
     */
    public void windowLostFocus(WindowEvent windowEvent){}
    public void keyTyped(KeyEvent keyEvent){}
    public void keyReleased(KeyEvent keyEvent){}


}


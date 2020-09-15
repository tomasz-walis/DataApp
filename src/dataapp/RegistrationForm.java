package dataapp;
import javax.swing.border.*;
import java.util.regex.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * Class represents the Registration form
 * All the user information are written to the file
 * The registration page includes 6 JTextFields
 * Submit and Login buttons
 * The Submit button creates the user account
 * The login button redirects to the LoginForm
 *
 * @author  Tomasz Walis-Walisiak
 * @version 1.5
 * @since   20-03-2017
 */

public class RegistrationForm extends JFrame implements ActionListener, KeyListener, WindowFocusListener {


    private JLabel lbName, lbLastName, lbEmail, lbPasswordConf, lbUsername, lbPassword, lbMessage;
    private JTextField txtEnterName, txtLastName, txtEmail, txtEnterUsername;
    private JPasswordField txtEnterPassword, passwordConf;
    private JButton btSubmit, btLogin, btClearForm;
    private UserIActivityLog userActivityLog = new UserIActivityLog();
    private final int FRAME_WIDTH = 500;
    private final int FRAME_HEIGHT = 400;
    private final int GRID_SPACING = 10;
    private final int MAIN_PANEL_BORDER = 50;


    /**
     * class constructor contains a login JFrame
     * @exception HeadlessException Thrown when code is dependent on a keyboard
     */
    public RegistrationForm() throws HeadlessException {

        /*main JFrame*/
        super("Registration");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        addKeyListener(this);
        addWindowFocusListener(this);

        /*Panel with white background,
        grid layout used,
        empty border added to keep components of the edges*/
        JPanel mainPanel = new JPanel();
        add(mainPanel);
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new GridLayout(8, 0, GRID_SPACING, GRID_SPACING));
        mainPanel.setBorder(new EmptyBorder(MAIN_PANEL_BORDER, MAIN_PANEL_BORDER, MAIN_PANEL_BORDER, MAIN_PANEL_BORDER));

        /*create labels, text fields and buttons*/
        lbName = new JLabel("Name:");
        lbLastName = new JLabel("Last Name");
        lbEmail = new JLabel("Email:");
        lbUsername = new JLabel("Username:");
        lbPassword = new JLabel("Password:");
        lbPasswordConf = new JLabel("Confirm Password");
        lbMessage = new JLabel();

        txtEnterName = new JTextField();
        txtLastName = new JTextField();
        txtEmail = new JTextField();
        txtEnterUsername = new JTextField();
        txtEnterPassword = new JPasswordField();
        txtEnterPassword.addKeyListener(new KeyList());
        passwordConf = new JPasswordField();

        btSubmit = new JButton("Create Account");
        btLogin = new JButton("Login");
        btClearForm = new JButton("Clear Form");

        /*adding components to JPanel*/
        mainPanel.add(lbName);
        mainPanel.add(txtEnterName);
        mainPanel.add(lbLastName);
        mainPanel.add(txtLastName);
        mainPanel.add(lbEmail);
        mainPanel.add(txtEmail);
        mainPanel.add(lbUsername);
        mainPanel.add(txtEnterUsername);
        mainPanel.add(lbPassword);
        mainPanel.add(txtEnterPassword);
        mainPanel.add(lbPasswordConf);
        mainPanel.add(passwordConf);
        mainPanel.add(btSubmit);
        mainPanel.add(btLogin);
        mainPanel.add(btClearForm);
        mainPanel.add(lbMessage);

        /*action listeners*/
        btSubmit.addActionListener(this);
        btLogin.addActionListener(this);
        btClearForm.addActionListener(this);
        txtEnterName.addKeyListener(this);
        txtLastName.addKeyListener(this);
        txtEmail.addKeyListener(this);
        txtEnterUsername.addKeyListener(this);
        txtEnterPassword.addKeyListener(this);
        txtEnterPassword.addKeyListener(this);
        passwordConf.addKeyListener(this);
    }

    /**
     * Considering HCI, users with disabilities
     * This method allows the user to navigate the registration form
     * only with keyboard.
     * ENTER Key to click on the Create Account
     * ALT+L Key to go to login page
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            btSubmit.doClick();
        }else if ((e.getKeyCode() == KeyEvent.VK_L) && ((e.getModifiers() & KeyEvent.ALT_MASK) != 0)) {

           btLogin.doClick();
        }
    }

    @Override
    public void windowGainedFocus(WindowEvent windowEvent) {
        txtEnterName.requestFocusInWindow();
    }

    /**
     * Unused methods
     */
    public void keyTyped(KeyEvent keyEvent){}
    public void keyReleased(KeyEvent keyEvent){}
    public void windowLostFocus(WindowEvent windowEvent){}

    /**
     * This class extends key adapter class to rewrite the keyPressed method
     * from key adapter class. This method  gives possibility to check the password strength
     * when the key is pressed by using the checkStrength method
     */
    private class KeyList extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            String password = new String(txtEnterPassword.getPassword());
            String message= checkPasswordStrength(password);
            lbMessage.setForeground(Color.RED);
            lbMessage.setText(message + " password");
        }
    }

    /**
     * This method check if the input password is strong enough. In order for password to be strong
     * it should contain numbers and characters which are than 8 characters.
     * To identify if the password contains numbers and characters the regex
     * has been used: ([0-9][aA-zZ]|[aA-zZ][0-9])
     * Checking the password strength also addresses the security issue where the password
     * should contain not less than 8 characters and numeric characters
     * @param password This is the first parameter to checkPasswordStrength method
     */
    private String checkPasswordStrength(String password){
        Pattern pattern = Pattern.compile("([0-9][aA-zZ]|[aA-zZ][0-9])");
        Matcher matcher = pattern.matcher(password);
        if (matcher.find()){
            if (password.length()>8)
                 return "Strong";
            else return "Medium";
        }
        else return "Weak";
    }


    /**
     * This method enables the btSubmit and btLogin buttons to perform tasks when user clicks them.
     * When the btLogin button is clicked it opens the LoginForm
     * if the btSubmit is clicked first it checks if the username and passwords are not blank.
     * Then the username and password is searched within the text file to check if the account exists.
     * If account don't exist the new account is created
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == btSubmit) {
            String userName =  txtEnterUsername.getText();
            String password = new String(txtEnterPassword.getPassword());
            String passConf = new String(passwordConf.getPassword());
            String name = txtEnterName.getText();
            String lastName = txtLastName.getText();
            String email = txtEmail.getText();
            userActivityLog.saveToFile("Username: " + userName);
            userActivityLog.saveToFile("Submit button clicked");
            if (!checkEmpty(name, lastName, email,userName, password, passConf)) {
                if (!(password.equals(passConf))){
                    lbMessage.setText("Password doesn't match");
                    userActivityLog.saveToFile("Password doesn't match");
                }
                else if (!checkExist("accounts.txt", userName)) {
                    password = new String(encryptPassword(password));
                    String accountInfo = userName + "-" + password + "-" + name + "-" + lastName + "-" + email ;
                    saveToFile("accounts.txt", accountInfo);
                }
            }

        }else if (evt.getSource()==btLogin){
            userActivityLog.saveToFile("Login button clicked");
            new LoginForm().setVisible(true);
            this.dispose();

        }else if (evt.getSource()== btClearForm){
             txtEnterName.setText("");
             txtLastName.setText("");
             txtEmail.setText("");
             txtEnterUsername.setText("");
             txtEnterPassword.setText("");
             passwordConf.setText("");
        }
    }

    /**
     * This method saves the account information to the text file, the name, last name
     * email, username and account is written in a single line
     * @param filename This is the first parameter saveToFile method
     * @param text This is the second parameter saveToFile method
     */
    private void saveToFile(String filename, String text) {
        try {
            FileWriter fileWriter = new FileWriter(filename, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
            lbMessage.setForeground(Color.RED);
            lbMessage.setText("Account created!");
            userActivityLog.saveToFile("Account created");
        } catch (IOException ie) {
            System.out.println("Error in writing to file...");
        }
    }

    /**
     * This method checks for the JTextFields if they are left empty
     * @param userName This is the first parameter checkEmpty method
     * @param password This is the second parameter checkEmpty method
     * @param passConf This is the third parameter checkEmpty method
     */
    private boolean checkEmpty(String name, String lastName, String email, String userName, String password, String passConf) {
        boolean hasBlank = false;

        if (name.length() < 1) {
            lbName.setForeground(Color.RED);
            lbName.setText("Name is required.");
            userActivityLog.saveToFile("Name is required.");
            hasBlank = true;
        }


        if (lastName.length() < 1) {
            lbLastName.setForeground(Color.RED);
            lbLastName.setText("Last Name is required.");
            userActivityLog.saveToFile("LastName is required.");
            hasBlank = true;
        }

        if (email.length() < 1) {
            lbEmail.setForeground(Color.RED);
            lbEmail.setText("Email is required.");
            userActivityLog.saveToFile("Email is required.");
            hasBlank = true;
        }
        if (userName.length() < 1) {
            lbUsername.setForeground(Color.RED);
            lbUsername.setText("User name is required.");
            userActivityLog.saveToFile("User name is required.");
            hasBlank = true;
        }
        if (password.length() < 1) {
            lbPassword.setForeground(Color.RED);
            lbPassword.setText("Password is required.");
            userActivityLog.saveToFile("Password is required.");
            hasBlank = true;
        }
        if (passConf.length() < 1) {
            lbPasswordConf.setForeground(Color.RED);
            lbPasswordConf.setText("Confirmation is required.");
            userActivityLog.saveToFile("Confirmation is required.");
            hasBlank = true;
        }
        return hasBlank;
    }

    /**
     * This method checks if the account already exists in the file.
     * If username exists saving to the file again is not allowed
     * @param filename This is the first parameter checkEmpty method
     * @param userName This is the second parameter checkEmpty method
     */
    private boolean checkExist(String filename, String userName){
        FileReader fileReader;
        BufferedReader bufferedReader;
        String accInfo;
        boolean exist=false;
        try{
            fileReader=new FileReader(filename);
            bufferedReader=new BufferedReader(fileReader);
            while ((accInfo=bufferedReader.readLine())!=null){
                if(compareUser(accInfo,userName)) {
                    lbMessage.setText("Username already exists.");
                    userActivityLog.saveToFile("Username already exists.");
                    exist=true;
                    break;
                }
            }
            bufferedReader.close();
            fileReader.close();
        } catch(Exception ie)
        {System.out.println("Error!");}
        return exist;
    }


    /**
     * This method compares the user input with the username read from file
     * Return true if the user name is correct else return false
     * @param accInfo This is the first parameter check method
     * @param name This is the second parameter check method
     */
    private boolean compareUser(String accInfo, String name){
        String[] info=accInfo.split("-");
        String userName=info[0];
        if(userName.equals(name))
            return true;
        else return false;
    }

    /**
     * This method transforms password to a encrypted value before its saved to file
     * this is to make sure the account is secured. The password is encrypted
     * by changing all the bytes of the password and adding 1 to every byte
     * The security issue still remains at this point as the method encryps only user password
     * the remaining details username, name surname and email are not encrypted
     * this issue should be addressed unfortunately I have run out of time
     * @param password This is the first parameter check method
     */
    private byte[] encryptPassword(String password){
        byte[] byteSize=password.getBytes();
        for(int i=0;i<byteSize.length;i++)
            byteSize[i]=(byte)(byteSize[i]+1);
        return(byteSize);
    }
}


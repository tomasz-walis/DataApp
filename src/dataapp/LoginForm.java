package dataapp;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;


/**
 * This class represents a login Gui
 * The user is presented with the JFrame
 * that contains 2 JTextFields to
 * provide user name and password.
 * Also the button to log in in to the main application
 * and the register button to redirect to the registration page.
 *
 * @author  Tomasz Walis-Walisiak
 * @version 1.5
 * @since   20-03-2017
 */
public class LoginForm extends JFrame implements ActionListener, KeyListener, WindowFocusListener {

    private JLabel lbUsername, lbPassword, lbMessage;
    private JPasswordField passEnterPassword;
    private JTextField txtEnterUsername;
    private JButton btLogin, btRegister;
    private UserIActivityLog userActivityLog = new UserIActivityLog();
    private String userName;
    private final int FRAME_WIDTH = 500;
    private final int FRAME_HEIGHT = 230;
    private final int MAIN_PANEL_BORDER = 30;
    private final int GRID_SPACING = 10;



    /**
     * class constructor contains a login JFrame
     */
    public LoginForm()  {

        /*Set title, size and default operation on close*/
        setTitle("Login");
        setSize(FRAME_WIDTH,FRAME_HEIGHT);
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
        mainPanel.setLayout(new GridLayout (4,0,GRID_SPACING,GRID_SPACING));
        mainPanel.setBorder(new EmptyBorder(MAIN_PANEL_BORDER, MAIN_PANEL_BORDER, MAIN_PANEL_BORDER, MAIN_PANEL_BORDER));

        /*create labels, text fields and buttons*/
        lbUsername = new JLabel("Username:");
        lbPassword = new JLabel("Password:");
        txtEnterUsername = new JTextField();
        passEnterPassword = new JPasswordField();
        btLogin = new JButton("Login");
        btRegister = new JButton("Register");
        lbMessage = new JLabel();

        /*action and key listeners*/
        btLogin.addActionListener(this);
        btRegister.addActionListener(this);
        passEnterPassword.addKeyListener(this);
        txtEnterUsername.addKeyListener(this);

        /*adding components to JPanel*/
        mainPanel.add(lbUsername);
        mainPanel.add(txtEnterUsername);
        mainPanel.add(lbPassword);
        mainPanel.add(passEnterPassword);
        mainPanel.add(btLogin);
        mainPanel.add(btRegister);
        mainPanel.add(lbMessage);
    }


    /**
     * This method enables the btLogin and btRegister buttons to perform tasks when user clicks them.
     * When the btLogin is clicked the account information imputed in the txtEnterUsername and passEnterPassword
     * those text input in the boxes are searched through the text file. If the username and password are found the "valid login"
     * message is displayed in the lbMessage, else if user and password are not found the message "invalid login is displayed".
     * When the btRegister button is clicked it opens the RegistrationForm.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==btLogin){
            userName = txtEnterUsername.getText();
            String password=new String(passEnterPassword.getPassword());
            userActivityLog.saveToFile("Username : "+ userName);
            userActivityLog.saveToFile("Login button clicked");
            if(!checkEmptyTextFields(userName,password))
                verifyUser("accounts.txt",userName,password);

        }else if (e.getSource()==btRegister){
            userActivityLog.saveToFile("Registration button clicked");
            new RegistrationForm().setVisible(true);
            this.dispose();
        }
    }

    /**
     * This method checks if the username and password inputted are matching the text file
     * Method uses file reader and buffered reader to read the file, boolean flag is introduced
     * to check if user name and password are valid if the user has entered valid password
     * the application will be started. If the invalid user name and password is entered
     * user will see INVALID LOGIN message on screen.
     * Exception is thrown when file can not be read or dose not exist.
     * @param filename This is the first parameter to verifyUser method
     * @param userName This is the second parameter to verifyUser method
     * @param password This is the third parameter to verifyUser method
     */
    private void verifyUser(String filename, String userName, String password){
        FileReader fileReader;
        BufferedReader bufferedReader;
        boolean validUser=false;
        String accountInformation;
        try{
            fileReader=new FileReader(filename);
            bufferedReader=new BufferedReader(fileReader);
            while ((accountInformation=bufferedReader.readLine())!=null){
                if(checkUsernamePassword(accountInformation, userName,password)){
                    lbMessage.setForeground(Color.RED);
                    lbMessage.setText("VALID LOGIN !");

                    userActivityLog.saveToFile("Successful login");
                    validUser=true;
                    new DataAppGUI().setVisible(true);
                    this.dispose();
                    break;
                }
            }
            if(!validUser){
                lbMessage.setForeground(Color.RED);
                lbMessage.setText("INVALID USERNAME OR PASSWORD!");
                userActivityLog.saveToFile("Unsuccessful login");

            }

            bufferedReader.close();
            fileReader.close();
        } catch(IOException e) {
            JOptionPane.showMessageDialog(null, "UNABLE TO READ FILE");
        }
    }
    /**
     * This method checks for the user name and password if the text fields
     * are empty and user has not imputed any values for password and user name
     * The boolean flag isEmpty returns true if the fields are empty
     * the if statements checks for the length of user name and password
     * @param userName This is the first paramter to checkEmptyTextFields method
     * @param password This is the third paramter to checkEmptyTextFields method
     */
    private boolean checkEmptyTextFields(String userName, String password) {
        boolean isEmpty = false;
        if (userName.length() < 1) {
            lbUsername.setForeground(Color.RED);
            lbUsername.setText("USER NAME IS REQUIRED!");
            isEmpty = true;
        }
        if (password.length() < 1) {
            lbPassword.setForeground(Color.RED);
            lbPassword.setText("PASSWORD IS REQUIRED!");
            isEmpty = true;
        }
        return isEmpty;
    }

    /**
     * This method is used by verifyUser method to check and compare user name
     * and password entered by user.
     * Method also uses the passwordDecryption in order to decrypt the password
     * and return true if the user name and password match the file.
     * The if statement returns true or false when password is correct true is returned,
     * else return false.
     * @param accInfo This is the first parameter to checkUsernamePassword method
     * @param username This is the second parameter to checkUsernamePassword method
     * @param password This is the third parameter to checkUsernamePassword method
     */
    private boolean checkUsernamePassword(String accInfo, String username, String password){
        String[] info=accInfo.split("-");
        String userName = info[0];
        String pass=new String(passwordDecryption(info[1]));
        if(userName.equals(username) && pass.equals(password)){
            return true;
        }else{
            return false;
        }
      }

    /**
     * This method decrypts the password from txt file to its original length and value
     * The decryption happens before tha password is compared with password entered by the user
     * and password entered by user.
     * @param password This is the third parameter to checkUsernamePassword method
     */
    private byte[] passwordDecryption(String password){
        byte[] byteSize = password.getBytes();
        for(int i=0;i<byteSize.length;i++){
            byteSize[i]=(byte)(byteSize[i]-1);
         }
        return(byteSize);
    }

    /**
     * This is the main method which makes use of LoginForm
     * constructor and displays the JFrame to the user.
     * @param args Unused.
     */
    public static void main (String[] args){
        new LoginForm().setVisible(true);
    }

    /**
     * Considering HCI, users with disabilities
     * This method allows the user to navigate the login form
     * only with buttons.
     * ENTER Key to click on the Login
     * ALT+R Key to go to registration page
     */
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            btLogin.doClick();
        }else if ((e.getKeyCode() == KeyEvent.VK_R) && ((e.getModifiers() & KeyEvent.ALT_MASK) != 0)) {
            btRegister.doClick();
        }
    }

    /**
     * This method sets the focus on the first JText Field
     * The user is able to type in to the boxes straight
     * after the login page is loaded
     * this increases usability of the program as well
     * as HCI
     */
    @Override
    public void windowGainedFocus(WindowEvent windowEvent) {
        txtEnterUsername.requestFocusInWindow();
    }

    /**
     * Unused methods
     */
    public void windowLostFocus(WindowEvent windowEvent){}
    public void keyReleased(KeyEvent keyEvent) {}
    public void keyTyped(KeyEvent keyEvent){}

}





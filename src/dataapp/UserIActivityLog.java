package dataapp;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.*;
import java.util.*;

/**
 * Class represents the user activity Log
 * All the user actions are written to the file
 * and read from file
 * The activity log security issues remain in the program, the activity log
 * is not encrypted normally the Cipher encryption would be used
 * to address the encryption issue.
 * @author  Tomasz Walis-Walisiak
 * @version 1.5
 * @since   20-03-2017
 */
public class UserIActivityLog extends JFrame implements IActivityLog {


    private final String ACTIVITY_LOG = "activity.txt";
    private JTextArea log;
    private final int FRAME_WIDTH = 400;
    private final int FRAME_HEIGHT = 600;
    private final int GRID_SPACING = 0;

    /*default constructor*/
    public UserIActivityLog() {}

    /**
     * This method saves user actions to file
     * also the current date and time is saved to the text file
     * Exception is thrown when file can not be read or dose not exist.
     * @param text This is the first parameter to saveToFile method
     */

    @Override
    public void saveToFile(String text) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String logDate = dateFormat.format(today);
        try {

            FileWriter fileWriter = new FileWriter(ACTIVITY_LOG, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(logDate + ": " +text);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException ie) {
            JOptionPane.showMessageDialog(null, "UNABLE TO WRITE TO FILE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * ShowLog method displays the user activity log
     * in the JTextArea.
     * Method also reads the content of txt file
     * to display the activity log it to the user
     */
    @Override
    public void showLog() {

        JFrame frame = new JFrame();
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JPanel logPanel = new JPanel();
        logPanel.setLayout(new GridLayout(0, 1, GRID_SPACING, GRID_SPACING));
        frame.add(logPanel);

        log = new JTextArea();
        logPanel.add(log);

        JScrollPane scrollPane = new JScrollPane(log);
        logPanel.add(scrollPane);

        BufferedReader in = null;
        try {

            in  = new BufferedReader(new FileReader(ACTIVITY_LOG));
            String str;
            while ((str = in.readLine()) != null) {
                log.append("\n"+str);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "UNABLE TO READ FILE");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { in.close(); } catch (Exception ex) { }
        }
    }
}

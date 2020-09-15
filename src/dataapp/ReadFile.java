package dataapp;
import javax.swing.*;
import java.util.*;
import java.io.*;


/**
 * Class represents Data Csv file reader
 * @author  Tomasz Walis-Walisiak
 * @version 1.5
 * @since   20-03-2017
 */
public class ReadFile  {
       private final ArrayList<String[]> readToArray = new ArrayList<>();
        private final DataAppGUI gui;
        private String[] singleRow;
        private final String search;



    public ReadFile (DataAppGUI guiClass) {
            gui = guiClass;
            search = gui.getSearchTextField();
    }

        /**
         * This method reads the csv file and ads each row to ArrayList
         * @param dataFile This is the first parameter to saveToFile method
         * @return readToArray
         */
        public ArrayList<String[]> ReadCsvFile(File dataFile) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFile));
                bufferedReader.readLine();
                while (bufferedReader.ready()) {
                    String st = bufferedReader.readLine().replace("\"","");
                    singleRow = st.split(",");

                    if(st.contains(search.toUpperCase())) {
                        readToArray.add(singleRow);

                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "UNABLE TO READ FILE!!");
            }

            return readToArray;
        }
}

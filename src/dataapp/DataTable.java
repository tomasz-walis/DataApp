package dataapp;

import javax.swing.*;
import javax.swing.table.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Class represents Data Table Structure
 * @author  Tomasz Walis-Walisiak
 * @version 1.5
 * @since   20-03-2017
 */

public class DataTable extends AbstractTableModel {
    private final String FILE_NAME = "1709_nhs-dent-stat-eng-15-16-q1-anx4-pseen.csv";
    /*set column names*/
    private String[] columnNames;

   // public DataTable(){}

    private ArrayList<String[]> data = new ArrayList<>();


        /*method to add csv file in to table*/
        public void insertScvFile(ArrayList<String[]> DataIn) {
            this.data = DataIn;
            /*Checks if values in the table's rows changed.
            if numbers of rows changed JTable will be remade.*/
            this.fireTableDataChanged();

        }

         /*get number of columns in the table*/
        public int getColumnCount() {
            try{
                BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME));
                String text = bufferedReader.readLine().replace("\"","");
                String[] strFirstLine = text.split(",");
                columnNames = strFirstLine;
            }catch (Exception e){
                JOptionPane.showMessageDialog(null, "UNABLE TO READ COLUMNS NAMES!!");
            }

            return columnNames.length;
        }

        /*get number of rows in the table*/
            public int getRowCount() {
            return data.size();
        }

        /*get column name of table*/
        public String getColumnName(int col) {
            return columnNames[col];
        }

        /*gets value at the particular column or row*/
        public Object getValueAt(int row, int col) {
            try{
                return data.get(row)[col];
            }catch (Exception e) {
                data.clear();
                JOptionPane.showMessageDialog(new JFrame(), "FILE IS CORRUPTED", "Dialog",
                        JOptionPane.ERROR_MESSAGE);

            }
            return null;
        }

    }

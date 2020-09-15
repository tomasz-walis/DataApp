package dataapp;
import javax.swing.*;
import java.util.*;
/**
 * Class represents calculations
 * Minimum, Maximum, Mode
 * @author  Tomasz Walis-Walisiak
 * @version 1.5
 * @since   20-03-2017
 */
public class DataCalculation implements ICalculation {
    private int min;
    private int max;
    private int mode;


    /**
     * Default constructor
     */
    public DataCalculation(){}

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public int getMode() {
        return mode;
    }



    /**
     * Calculating minimum, maximum and mode values based on
     * user search criteria
     * @param table This is the first parameter to minMaxModeCalculation method
     */
    @Override
    public void minMaxModeCalculation(JTable table) {
        ArrayList<Integer> list = new ArrayList<>();
        try {
            for (int i = 0; i < table.getRowCount(); i++) {
                list.add(Integer.parseInt(table.getValueAt(i, 5).toString()));
            }
            max = Collections.max(list);
            min = Collections.min(list);

            mode = 0;
            int count = 0;
            for (int j=0; j<list.size(); j++){
                int element = list.get(j);
                int tempCount = 0;
                for (int p=0;p<list.size(); p++)
                    if (list.get(p)==element)
                        tempCount++;
                if (tempCount>count){
                    mode=element;
                    count=tempCount;
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), "INVALID CALCULATION VALUE!!", "Dialog",
                    JOptionPane.ERROR_MESSAGE);
        }


    }
}












package dataapp;

import javax.swing.*;

/**
 * Created by Tom on 04/04/2017.
 */
public interface ICalculation {

    int getMin();
    int getMax();
    int getMode();
    void minMaxModeCalculation(JTable table);
}

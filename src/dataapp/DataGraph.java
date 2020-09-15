package dataapp;
import javax.swing.*;
import java.awt.*;

/**
 * Class represents graph
 * Graph values are changed based
 * on user search criteria
 * @author  Tomasz Walis-Walisiak
 * @version 1.5
 * @since   20-03-2017
 */
public class DataGraph extends JPanel {

    private String[] graphLabels = new String[3];
    private double[] graphValue = new double[3];
    private double[] barValues;
    private String[] barNames;
    private int height;
    private int width;


    /**
     * Default constructor
     */
    public DataGraph(){}

    /**
     * Default constructor overloaded
     */
    public DataGraph(double[] value, String[] names) {
        barValues = value;
        barNames = names;
    }

    /**
     * Drawing the Graph
     * @param graph This is the first parameter to paintComponent method
     */
    public void paintComponent(Graphics graph) {

        super.paintComponent(graph);
        if (barValues == null || barValues.length == 0){
            return;
        }

        /*Minimum and maximum values for bar chart*/
        double minValue = 0;
        double maxValue = 573920;

        for (int i = 0; i < barValues.length; i++) {
            if (minValue > barValues[i]){
                minValue = barValues[i];
            }

            if (maxValue < barValues[i]){
                maxValue = barValues[i];
            }
        }

        Dimension dimension = getSize();
        width = dimension.width;
        height = dimension.height;
        /*Bar chart width*/
        int barWidth = width / barValues.length;


        /*The FontMetrics class defines a font  object, renders  font on the screen.*/
        Font label = new Font("Arial", Font.BOLD, 15);
        FontMetrics labelFont = graph.getFontMetrics(label);



        /*Method getHeight() gets height of text in the font. */
        int top = 20;
        int bottom = 20;
        if (maxValue == minValue){
            return;
        }

        /*Describes the scale of bar chart*/
        double scale = (height - top - bottom) / (maxValue - minValue);
        int yAxis = height - labelFont.getDescent();
        graph.setFont(label);

        for (int i = 0; i < barValues.length; i++) {
            int valueX = i * barWidth + 1;
            int valueY = top;
            int height = (int) (barValues[i] * scale);
            if (barValues[i] >= 0){
                valueY += (int) ((maxValue - barValues[i]) * scale);

            }else {

                valueY += (int) (maxValue * scale);
                height = -height;
            }

            try{
             /*set bar chart colour*/
                graph.setColor(Color.orange);
            /*fill the bar chart*/
                graph.fillRect(valueX, valueY, barWidth -120, height);
            /*set bar chart frame*/
                graph.setColor(Color.black);
            /*draw bar chart border*/
                graph.drawRect(valueX, valueY, barWidth - 120, height);
            /*draw the bar chart names*/
                int labelWidth = labelFont.stringWidth(barNames[i]);
                int xAxis = i * barWidth + (barWidth - labelWidth) /8;
                graph.drawString(barNames[i], xAxis, yAxis);
            }catch (NullPointerException e){
                JOptionPane.showMessageDialog(null, "UNABLE TO PLOT THE GRAPH");
                break;
            }

        }
    }

    /**
     * Set the graph values based on user search criteria
     * @param min This is the first parameter to setGraphValues method
     * @param max This is the first parameter to setGraphValues method
     * @param mode This is the first parameter to setGraphValues method
     */

    public void setGraphValues( int min, int max, int mode) {

        if(Math.abs(max-min)<1000){
            max *=2;
        }

        graphValue[0] = min ;
        graphLabels[0] = "MIN";
        graphValue[1] = max;
        graphLabels[1] = "MAX";
        graphValue[2] = mode;
        graphLabels[2] = "MODE";
    }

    /**
     * Print the graph in to JPanel in GUI class
     * @param graphPanel This is the first parameter to setGraphValues method
     */

    public void display(JPanel graphPanel){
        graphPanel.add(new DataGraph(graphValue, graphLabels));
    }
}




import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;


import javax.swing.border.EmptyBorder;

public class BMICal extends JFrame {
	private JTextField txtHeight;
	private JTextField txtWeight;
	private JTextArea lbResult;  
	private Preferences prefs;
	private JRadioButton rbInchesPounds;
	private JRadioButton rbCentimetersKilograms;
	private JProgressBar pbBMI;

    BMICal() {
    	 // Display a welcome message
    	JOptionPane.showMessageDialog(null, "Welcome to the BMI Calculator!");
    	
        // Ask for preference
        Object[] options = {"Inches/Pounds", "Centimeters/Kilograms"};
        int preference = JOptionPane.showOptionDialog(null, "What units would you like to use?", "Unit Preference",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        
        // Set up the main window
        setTitle("BMI Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load user preferences
        prefs = Preferences.userNodeForPackage(this.getClass());
        
        // Create the main panal and set its layout/
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10)); // Use GridLayout and add padding
        panel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add border for more padding
        
        //Add the height input field
        JLabel heightLabel = new JLabel("Please enter your height:");
        panel.add(heightLabel);

        txtHeight = new JTextField(prefs.get("height", ""), 2);
        panel.add(txtHeight);
        
        // Add the weight input field
        JLabel weightLabel = new JLabel("Please enter your weight:");
        panel.add(weightLabel);

        txtWeight = new JTextField(prefs.get("weight", ""), 2);
        panel.add(txtWeight);
        
        
        // Add the unit selection radio buttons
        rbInchesPounds = new JRadioButton("Inches/Pounds");
        rbCentimetersKilograms = new JRadioButton("Centimeters/Kilograms");
        
        ButtonGroup group = new ButtonGroup();
        group.add(rbInchesPounds);
        group.add(rbCentimetersKilograms);
        
        panel.add(rbInchesPounds);
        panel.add(rbCentimetersKilograms);
        
        
        // Add the calculate and clear buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCalculate = new JButton("Calculate BMI");
        btnCalculate.addActionListener(new CalculateButtonListener());
        buttonPanel.add(btnCalculate);

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ClearButtonListener());
        buttonPanel.add(btnClear);
        panel.add(buttonPanel);
        
        // Add the progress bar
        pbBMI = new JProgressBar(10, 50);
        pbBMI.setStringPainted(true);
        panel.add(pbBMI);
        
        
        // Add the BMI ranges text pane
        JTextPane bmiRanges = new JTextPane();
        bmiRanges.setContentType("text/html");
        bmiRanges.setText(
            "<html>" +
            "<b><font color='black'>BMI RANGES:</font></b><br/>" +
            "<font color='blue'>BELOW 18.5 = UNDERWEIGHT</font><br/>" +
            "<font color='green'>BETWEEN 18.5-24.9 = HEALTHY</font><br/>" +
            "<font color='orange'>BETWEEN 25-29.9 = OVERWEIGHT</font><br/>" +
            "<font color='red'>30 OR OVER = OBESE</font><br/>" +
            "</html>"
        );
        bmiRanges.setEditable(false);
        panel.add(bmiRanges);
        
        
        // Add the result text area
        lbResult = new JTextArea("BMI Result: ", 5,100);
        lbResult.setLineWrap(true);
        lbResult.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(lbResult);
        panel.add(scrollPane);
      
         //Set user's unit preference 
       if (preference == 0) {
            rbInchesPounds.setSelected(true);
        } else {
            rbCentimetersKilograms.setSelected(true);
        }
        
        // Add the main panel to the window and display it
        add(panel);
        pack();
        setSize(500,500); // Set a larger size
        setVisible(true);
    }
    //This methods returns a string explaining the user's BMI category
    private String getBmiExplanation(double bmi) {
        String explanation;
        if (bmi < 18.5) {
            explanation = "You are within the underweight range. It's recommended to gain weight.";
        } else if (bmi < 25) {
            explanation = "You are within the healthy range. Keep maintaining your current lifestyle.";
        } else if (bmi < 30) {
            explanation = "You are within the overweight range. It's recommended to lose some weight.";
        } else {
            explanation = "You are within the obesity range. It's strongly recommended to lose weight and consult with your personal doctor for more accurate recommendation.";
        }
        return explanation;
    }
 // This class handles the calculate button click event
    private class CalculateButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String heightText = txtHeight.getText();
                String weightText = txtWeight.getText();

                if (heightText.isEmpty()) {
                	txtHeight.setForeground(Color.RED);
                    txtHeight.setText("HEIGHT IS REQUIRED");
                    lbResult.setText("!ERROR: INVALID HEIGHT OR WEIGHT. PLEASE TRY AGAIN!");
                    return;
                }

                if (weightText.isEmpty()) {
                	txtWeight.setForeground(Color.RED);
                    txtWeight.setText("WEIGHT IS REQUIRED.");
                    lbResult.setText("!ERROR: INVALID HEIGHT OR WEIGHT. PLEASE TRY AGAIN!");
                    return;
                }

                double height = Double.parseDouble(heightText);
                double weight = Double.parseDouble(weightText);

                if (height <= 0) {
                	txtHeight.setForeground(Color.RED);
                    txtHeight.setText("POSITIVE NUMBER ONLY");
                    lbResult.setText("!ERROR: INVALID HEIGHT OR WEIGHT. PLEASE TRY AGAIN!");
                    return;
                }
                

                if (weight <= 0) {
                	txtWeight.setForeground(Color.RED);
                    txtWeight.setText("POSITIVE NUMBER ONLY");
                    lbResult.setText("!ERROR: INVALID HEIGHT OR WEIGHT. PLEASE TRY AGAIN!");
                    return;
                }
                //BMI calculation 
                double bmi;
                if (rbInchesPounds.isSelected()) {
                    bmi = weight / Math.pow(height, 2) * 703;
                } else {
                    height /= 100; 
                    bmi = weight / Math.pow(height, 2);
                }
                String category;
                Color color;
                if (bmi < 18.5) {
                    category = "underweight";
                    color = Color.BLUE;
                } else if (bmi < 25) {
                    category = "healthy";
                    color = Color.GREEN;
                } else if (bmi < 30) {
                    category = "overweight";
                    color = Color.ORANGE;
                } else {
                    category = "obese";
                    color = Color.RED;
                }
                lbResult.setForeground(color);
                String explanation = getBmiExplanation(bmi);
                lbResult.setText(String.format("BMI RESULT: %.2f (%s).\n%s", bmi, category, explanation));
                pbBMI.setValue((int) Math.round(bmi));
                pbBMI.setString(String.format("%.2f", bmi));
            } catch (NumberFormatException ex) {
                lbResult.setText("!ERROR: INVALID HEIGHT OR WEIGHT. PLEASE TRY AGAIN!");
            }
        }
    }
 // This class handles the clear button click event
    private class ClearButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	
        	//Clears the input and reset everything back
            txtHeight.setForeground(Color.BLACK);
            txtWeight.setForeground(Color.BLACK);
            txtHeight.setText("");
            txtWeight.setText("");
            lbResult.setForeground(Color.BLACK);
            lbResult.setText("BMI RESULT: ");
            prefs.remove("height");
            prefs.remove("weight");
            pbBMI.setValue(0);
            pbBMI.setString("0%");
        }
    }
    // The main method that launches the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BMICal());
    }
}

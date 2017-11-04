package com.jjjimenez.amata;

import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author JJJimenez
 */
public class InterfaceDFA extends javax.swing.JFrame {

    /**
     * Constructor that initializes swing components and the look and feel
     * design of the interface.
     */
    public InterfaceDFA() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            promptUser("Program encountered an error. Proceeding with default UI Look.");
        }
        initComponents(); //initialize swing components
    }
    //sets the input table rows based on the numbe of states the user inputted
    public void setTable() {
        emptyTable();
        model.addColumn("State");
        model.addColumn("Final State");
        model.addColumn("Input 0");
        model.addColumn("Input 1");
        for (int counter = 0; counter < numOfStates; counter++) {
            model.addRow(new Object[]{
                "State " + counter, "", "", ""
            });
        }
        promptUser("Fill out values for the table.\n\n YES or NO for FINAL STATE \n\nAny positive integers less than the "
                + "\nnumber of states for state destinations \n1 "
                + "and 0.");
    }
    //sets the input table empty
    public void emptyTable() {
        model.setRowCount(0);
        model.setColumnCount(0);
    }
    //prompts the user
    public static void promptUser(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
    //prevent user from producing more bugs
    public void edittingMode(int mode) {
        if (mode == 1) {//enter state mode
            tblInput.setEnabled(false);
            inputField.setEnabled(true);
            inputField.setText("");
            lblPrompt.setText("Enter # of states: ");
            btnGenerate.setText("Generate");
            btnGenerate.setEnabled(true);
            btnEdit.setEnabled(false);
            btnSave.setEnabled(false);
            btnReset.setEnabled(false);
            emptyTable();
        } else if (mode == 2) { //table editting mode
            tblInput.setEnabled(true);
            inputField.setEnabled(false);
            lblPrompt.setText("Enter input string: ");
            inputField.setText("");
            btnGenerate.setText("Validate");
            btnGenerate.setEnabled(false);
            btnReset.setEnabled(true);
            btnEdit.setEnabled(false);
            btnSave.setEnabled(true);
        } else if (mode == 3) { //table saved mode
            tblInput.setEnabled(false);
            inputField.setEnabled(true);
            inputField.setText("");
            btnGenerate.setText("Validate");
            btnGenerate.setEnabled(true);
            btnEdit.setEnabled(true);
            btnSave.setEnabled(false);
        }
    }
    //initialize user inputted values to the state object representation of the state class
    public void initializeStates() {
        states = new State[numOfStates];
        for (int index = 0; index < numOfStates; index++) {
            states[index] = new State();
        }

        //initialize values
        for (int row = 0; row < userInputs.getRowCount(); row++) {
            for (int column = 1; column <= 3; column++) {
                String holder = userInputs.getValueAt(row, column).toString();
                if (column == 1) { //final state
                    if (holder.equalsIgnoreCase("Yes")) {
                        states[row].setFinalState(Boolean.TRUE);
                    } else {
                        states[row].setFinalState(Boolean.FALSE);
                    }
                } else if (column == 2) { //destinationZero
                    states[row].setDestinationZero(Integer.parseInt(holder));
                } else if (column == 3) {
                    states[row].setDestinationOne(Integer.parseInt(holder));
                } else {
                    promptUser("Program code was altered.");
                    invalidInputIndicator();
                }
            }
        }
    }
    //eto din
    public void invalidInputIndicator() {
        jLabel1.setForeground(new java.awt.Color(255, 51, 0)); //red font color on amata indicates invalid input
    }
    //pang palit kulay hehe
    public void validInputIndicator() {
        jLabel1.setForeground(new java.awt.Color(0, 204, 0)); //green font color on amata indicates valid input
    }
    //eto yung method na nag aallow para mapaltan yung state names
    public String customNames() {
        String endingState = userInputs.getValueAt(currentState, 0).toString();
        return endingState;
    }
    //error handling and input validation of the program
    //notify user if input is invalid
    public boolean validateInputs() {
        for (int row = 0; row < userInputs.getRowCount(); row++) {
            //column 1 is final state, 2 is destination zero, 3 is destination one
            //check if inputs are empty
            String finalTemp = userInputs.getValueAt(row, 1).toString(); //store temporary values to string
            String destinationZeroTemp = userInputs.getValueAt(row, 2).toString(); //to prevent input mismatch exception
            String destinationOneTemp = userInputs.getValueAt(row, 3).toString();
            if (isEmpty(finalTemp)
                    || isEmpty(destinationOneTemp)
                    || isEmpty(destinationZeroTemp)) {
                promptUser("Fill out missing values");
                invalidInputIndicator();
                return false;
            } //check if destination 0 or 1 is a numeric character
            else if (!isDigit(userInputs.getValueAt(row, 2).toString())) {
                promptUser("Enter only a positive integer. Row " + (row + 1) + " Column 3");
                invalidInputIndicator();
                return false;
            }
            else if (!isDigit(userInputs.getValueAt(row, 3).toString())) {
                promptUser("Enter only a positive integer. Row " + (row + 1) + " Column 4");
                invalidInputIndicator();
                return false;
            }
            else {
                int zeroLength = destinationZeroTemp.length();
                int oneLength = destinationOneTemp.length();
                if (zeroLength > 1 || oneLength > 1) {
                    if (destinationZeroTemp.charAt(0) == '0') {
                        promptUser("Not a valid positive integer. Row " + (row + 1)+ " Column 3");
                        invalidInputIndicator();
                        return false;
                    }
                    else if (destinationOneTemp.charAt(0) == '0') {
                        promptUser("Not a valid positive integer. Row " + (row + 1) + " Column 4");
                        invalidInputIndicator();
                        return false;
                    }
                }
            }
            String isFinal = userInputs.getValueAt(row, 1).toString(); //if data type validation is done, declare them to variables
            int destinationZero = Integer.parseInt(userInputs.getValueAt(row, 2).toString());
            int destinationOne = Integer.parseInt(userInputs.getValueAt(row, 3).toString());
            //next, validate if inputs match the given format
            if (!(isFinal.equalsIgnoreCase("Yes") || isFinal.equalsIgnoreCase("No"))) { //final state validation
                promptUser("Only Yes or No are accepted inputs on final state.\n\nRow " + (row + 1) + " Column 1");
                invalidInputIndicator();
                return false;
            } else if (!(destinationOne < numOfStates)) { //destination one and zero validation
                promptUser("State does not exist. \nInvalid destination state at row " + (row + 1) + " Column 4");
                invalidInputIndicator();
                return false;
            }
            else if (!(destinationZero < numOfStates)) { //destination one and zero validation
                promptUser("State does not exist. \nInvalid destination state at Row " + (row + 1)+ " Column 3");
                invalidInputIndicator();
                return false;
            }
        }
        validInputIndicator();
        return true;
    }
    //checks if inputString is empty or null
    public boolean isEmpty(String inputString) {
        if (inputString.equals("")) {
            return true;
        } else {
            return false;
        }
    }
    //checks if input string is a digit or numerical character
    public boolean isDigit(String inputString) {
        inputString = inputString.replaceAll("\\s+", "").trim();
        for (int index = 0; index < inputString.length(); index++) { //reiterate the whole string to check if there are any alphabetic characters
            if (!Character.isDigit(inputString.charAt(index))) {
                return false;
            }
        }
        return true;
    }
    //checks if string consists of purely 1's and 0's
    public boolean isOneOrZero(String inputString) {
        for (int index = 0; index < inputString.length(); index++) {
            int character = Integer.parseInt(Character.toString(inputString.charAt(index)));
            if (character != 1 && character != 0) {
                return false;
            }
        }
        return true;
    }
    //checks if string consists of only alphabetical characters
    public boolean isLetter(String inputString) {
        inputString = inputString.replaceAll("\\s+", "").trim();
        for (int index = 0; index < inputString.length(); index++) { //reiterate the whole string to check if there are any alphabetic characters
            if (!Character.isAlphabetic(inputString.charAt(index))) {
                return false;
            }
        }
        return true;
    }
    //checks if the user inputted string is accepted by the dfa
    //main algorithm
    public boolean isAcceptedString(String inputString) {
        for (int charpos = 0; charpos < inputString.length(); charpos++) {
            int character = Integer.parseInt(Character.toString(inputString.charAt(charpos))); //extract chars in string one by one
            if (character == 1) //and convert them to int
            {
                currentState = states[currentState].getDestinationOne();
            } else if (character == 0) {
                currentState = states[currentState].getDestinationZero();
            }
        }
        if (states[currentState].isFinalState()) {
            return true;
        }
        return false;
    }
    //checks if starting state is a final state
    public boolean isAcceptingEmptyString(){
        currentState = 0;
        initializeStates();
        return states[currentState].isFinalState();
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblInput = new javax.swing.JTable();
        btnSave = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        lblPrompt = new javax.swing.JLabel();
        btnGenerate = new javax.swing.JButton();
        inputField = new javax.swing.JFormattedTextField();
        btnReset = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblInput.setModel(model);
        tblInput.setCellSelectionEnabled(true);
        tblInput.setEnabled(false);
        tblInput.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblInput);
        tblInput.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        btnSave.setText("Save");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnEdit.setText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        lblPrompt.setText("Enter # of States:");

        btnGenerate.setText("Generate");
        btnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateActionPerformed(evt);
            }
        });

        btnReset.setText("Reset");
        btnReset.setEnabled(false);
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jLabel1.setText("| 6AMATA |");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPrompt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputField, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGenerate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReset)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSave)
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnEdit)
                    .addComponent(lblPrompt)
                    .addComponent(btnGenerate)
                    .addComponent(inputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset)
                    .addComponent(jLabel1))
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    //method that handles on click events on generate action btn
    //triggers the main algorithm functions
    private void btnGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateActionPerformed
        String holder = inputField.getText().replaceAll("\\s+", "").trim();
        if (counter == 1) {
            if (holder.isEmpty() || !isDigit(holder)) {
                promptUser("Please input a valid positive integer.");
                invalidInputIndicator();
                edittingMode(1);
            } else {
                numOfStates = Integer.parseInt(holder);
                if (numOfStates > 0) {
                    edittingMode(2);
                    counter += 1;
                    tblInput.setEnabled(true);
                    setTable();
                    validInputIndicator();
                } else {
                    promptUser("Enter a positive integer greater than zero.");
                }
            }
        } else if (counter == 2) {
            if (holder.isEmpty()) {
                if(isAcceptingEmptyString()){
                    promptUser("It is accepted by the DFA.\nEnding state is: " + customNames()); 
                }
                else{
                   promptUser("Not accepted by the DFA.\nEnding state is: " + customNames() + ", not a final state."); 
                }
                validInputIndicator();
            }
            else if (!isDigit(holder) || !isOneOrZero(holder)) {
                promptUser("Please input a string consisting of 1's or 0's.");
                invalidInputIndicator();
            } else {
                currentState = 0;
                initializeStates();
                if (isAcceptedString(holder)) {
                    promptUser("It is accepted by the DFA.\nEnding state is: " + customNames());
                } else {
                    promptUser("Not accepted by the DFA.\nEnding state is: " + customNames() + ", not a final state.");
                }
                System.out.println("");
                validInputIndicator();
            }
        } else {
            promptUser("Program code was altered.");
            invalidInputIndicator();
        }
    }//GEN-LAST:event_btnGenerateActionPerformed
    //method that handles on click events on save action btn
    //allows user to save the modified input table
    //disables the input table afterwards
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        tblInput.editCellAt(-1, -1); //remove focus to any cell to prevent null values
        //check if user input is correct, if correct proceed to editting mode3
        userInputs = (DefaultTableModel) tblInput.getModel();
        if (validateInputs()) {
            edittingMode(3);
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    //method that handles on click events on edit action btn
    //allows user to edit the table once more
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        edittingMode(2);
    }//GEN-LAST:event_btnEditActionPerformed
    //method that handles on click events on reset action btn
    //resets everything back to square one
    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        counter = 1;
        edittingMode(1);
        jLabel1.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnResetActionPerformed

    /**
     * Main method to instantiate the GUI of DFA Input Validator.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InterfaceDFA GUI = new InterfaceDFA();
                GUI.setDefaultCloseOperation(EXIT_ON_CLOSE);
                GUI.setTitle("Dfa Input Validator");
                GUI.setLocationRelativeTo(null);
                GUI.setVisible(true);
                promptUser("This program identifies if the input string"
                        + "\n is accepted by the user defined DFA.\n\nAlcaraz | Gonzales | Jimenez | Nulud | Zablan"
                        );
            }
        });
    }

    private int currentState = 0; //this is the starting state.
    private int numOfStates;
    private int counter = 1;
    private State[] states; //create an array of objects representing the "states" of the dfa
    private DefaultTableModel model = new DefaultTableModel(); //model of the inputTbl
    private DefaultTableModel userInputs; //all user inputs goes here
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnGenerate;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.JFormattedTextField inputField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPrompt;
    private javax.swing.JTable tblInput;
    // End of variables declaration//GEN-END:variables
}

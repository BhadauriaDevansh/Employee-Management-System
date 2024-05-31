import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.sql.*;

public class EMSUI extends JFrame {
    private EMSMain emsMain;
    private JButton btnAddData, btnDeleteData, btnExit;
    private JTextField txtEID, txtName, txtSalary;
    private JComboBox<String> cmbPosition;
    private JTable table,attendanceTable,payrollTable;
    private DefaultTableModel tableModel,attendanceTableModel,payrollTableModel;
    private Connection connection;
    private PanelType panelType;
    private JTabbedPane tabbedPane;
    private JPanel employeePanel;
    private JPanel attendancePanel;
    private JPanel payrollPanel;

    public enum PanelType {
        EMPLOYEE,
        ATTENDANCE,
        PAYROLL
    }

    public EMSUI(EMSMain emsMain, Connection connection, PanelType panelType) {
        this.emsMain = emsMain;
        this.connection = connection;
        this.panelType = panelType;


        // Call initializeUI() to set up the UI components
        initializeUI();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);


        add(tabbedPane);


        switch (panelType) {
            case EMPLOYEE:
                loadEmployeeData();
                break;
            case ATTENDANCE:
                loadAttendanceData(connection);
                break;
            case PAYROLL:
                loadPayrollData(connection);
        }

    }

    private void initializeUI() {
        tabbedPane = new JTabbedPane();

        employeePanel = createEmployeePanel();
        attendancePanel = createAttendancePanel();
        payrollPanel = createPayrollPanel();

        tabbedPane.addTab("Employee", employeePanel);
        tabbedPane.addTab("Attendance", attendancePanel);
        tabbedPane.addTab("Payroll", payrollPanel);


        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Check which tab is selected
                int tabIndex = tabbedPane.getSelectedIndex();
                switch (tabIndex) {
                    case 0:
                        loadEmployeeData();
                        break;
                    case 1:
                        loadAttendanceData(connection);
                        break;
                    case 2:
                        loadPayrollData(connection);

                }
            }});}

        //Created Employee Panel

        private JPanel createEmployeePanel() {
        JPanel panelForm = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblEID = new JLabel("EID:");
        JLabel lblName = new JLabel("Name:");
        JLabel lblPosition = new JLabel("Position:");
        JLabel lblSalary = new JLabel("Salary:");

        txtEID = new JTextField(10);
        txtName = new JTextField(10);
        cmbPosition = new JComboBox<>(new String[]{"HR", "IT", "FINANCE", "MARKETING"});
        txtSalary = new JTextField(10);


        ((AbstractDocument) txtSalary.getDocument()).setDocumentFilter(new NumericDocumentFilter());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(lblName, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(lblPosition, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(cmbPosition, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(lblSalary, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(txtSalary, gbc);

        btnAddData = new JButton("Add");
        btnDeleteData = new JButton("Delete");
        btnExit = new JButton("Exit");

        JPanel panelButtons = new JPanel(new GridLayout(1, 5, 10, 0));
        panelButtons.add(btnAddData);
        panelButtons.add(btnDeleteData);
        panelButtons.add(btnExit);

        String[] columnNames = {"EID","Employee ID","Name", "Position", "Salary","Action"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        EMSUI emsUIInstance = this; // Assuming 'this' refers to the instance of your current class
        final JTable tableInstance = table;

        ButtonRenderer buttonRenderer = new ButtonRenderer();
        ButtonEditor buttonEditor = new ButtonEditor(new JCheckBox(), table, this, emsMain);

        table.getColumnModel().getColumn(5).setCellRenderer(buttonRenderer);
        table.getColumnModel().getColumn(5).setCellEditor(buttonEditor);

        JComboBox<String> positionComboBox = new JComboBox<>(new String[]{"HR", "IT", "FINANCE", "MARKETING"});
        TableColumn positionColumn = table.getColumnModel().getColumn(3);
        positionColumn.setCellEditor(new DefaultCellEditor(positionComboBox));

        JTextField salaryField = new JTextField();
        ((AbstractDocument) salaryField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        TableColumn salaryColumn = table.getColumnModel().getColumn(4);
        salaryColumn.setCellEditor(new DefaultCellEditor(salaryField));

        JScrollPane tableScrollPane = new JScrollPane(table);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(250, 750));
        leftPanel.add(inputPanel, BorderLayout.NORTH);

        panelForm.add(leftPanel, BorderLayout.WEST);
        panelForm.add(tableScrollPane, BorderLayout.CENTER);
        panelForm.add(panelButtons, BorderLayout.SOUTH);

        JButton btnExit = new JButton("Exit");
        panelButtons.add(btnExit);

        btnAddData.addActionListener(e -> addEmployeeData(connection));
        btnDeleteData.addActionListener(e -> deleteEmployeeData(connection));
        btnExit.addActionListener(e -> System.exit(0));


        return panelForm;
    }

    //Created Attendance Panel

    private JPanel createAttendancePanel() {
        JPanel panelForm = new JPanel(new BorderLayout());

        String[] columnNames = {"EID","Employee ID", "Employee Name", "Employee Department", "Date", "Hours Worked", "Action"};
        attendanceTableModel = new DefaultTableModel(columnNames, 0);
        attendanceTable = new JTable(attendanceTableModel);
        TableColumn actionColumn = attendanceTable.getColumn("Action");

        EMSUI emsUIInstance = this; //
        final JTable tableInstance = attendanceTable;

        AttendanceButtonRenderer attendanceButtonRenderer = new AttendanceButtonRenderer();
        AttendanceButtonEditor attendanceButtonEditor = new AttendanceButtonEditor(new JCheckBox(), attendanceTable, this, connection);


        attendanceTable.getColumnModel().getColumn(6).setCellRenderer(attendanceButtonRenderer);
        attendanceTable.getColumnModel().getColumn(6).setCellEditor(attendanceButtonEditor);
         // Pass the table model

        JScrollPane tableScrollPane = new JScrollPane(attendanceTable);

        attendanceTable.setModel(attendanceTableModel);


        JPanel panelButtons = new JPanel(new GridLayout(1, 5, 10, 0));
        JButton btnExit = new JButton("Exit");
        panelButtons.add(btnExit);

        btnExit.addActionListener(e -> System.exit(0));

        panelForm.add(tableScrollPane, BorderLayout.CENTER);
        panelForm.add(panelButtons, BorderLayout.SOUTH);

        return panelForm;
    }

    //Created Payroll Panel

    private JPanel createPayrollPanel() {
        JPanel panelForm = new JPanel(new BorderLayout());
        String[] columnNames = {"EID", "Employee ID", "Employee Name", "Position", "Total Hours Worked", "Pay Rate", "Month", "Year", "Action"};
        payrollTableModel = new DefaultTableModel(columnNames, 0);
        payrollTable = new JTable(payrollTableModel);
        TableColumn actionColumn = payrollTable.getColumn("Action");

        PayrollButtonRenderer payrollButtonRenderer = new PayrollButtonRenderer();
        PayrollButtonEditor payrollButtonEditor = new PayrollButtonEditor(new JCheckBox(), payrollTable, this, connection);

        payrollTable.getColumnModel().getColumn(8).setCellRenderer(payrollButtonRenderer);
        payrollTable.getColumnModel().getColumn(8).setCellEditor(payrollButtonEditor);

        EMSUI emsUIInstance = this;
        final JTable tableInstance = payrollTable;

        JScrollPane tableScrollPane = new JScrollPane(payrollTable);

        payrollTable.setModel(payrollTableModel);
        JPanel panelButtons = new JPanel(new GridLayout(1, 8, 10, 0));

        // ActionListener for Generate Payroll button
        JButton generatePayrollButton = new JButton("Generate Payroll");
        generatePayrollButton.addActionListener(e -> generatePayroll(connection, panelForm)); // Pass the panel instance
        panelButtons.add(generatePayrollButton);

        panelButtons.add(btnExit);

        btnExit.addActionListener(e -> System.exit(0));

        panelForm.add(tableScrollPane, BorderLayout.CENTER);
        panelForm.add(panelButtons, BorderLayout.SOUTH);

        return panelForm;
    }




    /// Employee refresh table call
    private void loadEmployeeData() {
        try {
            tableModel.setRowCount(0);

            // Create a CallableStatement to call the stored procedure
            CallableStatement callableStatement = connection.prepareCall("{call DisplayEmployee}");  /// Sql injection
            ResultSet resultSet = callableStatement.executeQuery();

            // Iterate through the result set and populate the table model
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    // Check if the column type is FLOAT and retrieve it as double
                    if (metaData.getColumnType(i + 1) == Types.FLOAT) {
                        rowData[i] = resultSet.getDouble(i + 1);
                    } else {
                        rowData[i] = resultSet.getObject(i + 1);
                    }
                }
                tableModel.addRow(rowData);
            }

            // Close resources
            resultSet.close();
            callableStatement.close();

            // Hide the EID column
            hideEIDColumn(table);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void hideEIDColumn(JTable table) {
        TableColumn eidColumn = table.getColumnModel().getColumn(0);
        eidColumn.setResizable(false);
        eidColumn.setMaxWidth(0);
        eidColumn.setMinWidth(0);
        eidColumn.setPreferredWidth(0);
        eidColumn.setResizable(false);
        eidColumn.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                return new JLabel();// Return an empty label to hide the cell content
            }
        });
    }

    /// Attendance refresh table call

    private void loadAttendanceData(Connection connection) {
        try {
            // Clear existing rows
            attendanceTableModel.setRowCount(0);

            // Create a CallableStatement to call the stored procedure
            CallableStatement callableStatement = connection.prepareCall("{call DisplayAttendance}");  /// Sql injection
            ResultSet resultSet = callableStatement.executeQuery();

            // Iterate through the result set and populate the table model
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }
                attendanceTableModel.addRow(rowData);
            }

            // Set the cell editor for the "No of Hours" column
            TableColumnModel columnModel = attendanceTable.getColumnModel();
            TableColumn hoursColumn = columnModel.getColumn(5); // Index of "No of Hours" column
            hoursColumn.setCellEditor(new DefaultCellEditor(new JTextField())); // Use JTextField as cell editor

            // Make the table not editable by default
            attendanceTable.setDefaultEditor(Object.class, null);

            hideEIDColumn(attendanceTable);
            // Close resources
            resultSet.close();
            callableStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /// Payroll refresh table call

    private void loadPayrollData(Connection connection) {
        try {
            // Clear existing rows
            payrollTableModel.setRowCount(0);

            // Prepare the CallableStatement to call the stored procedure
            CallableStatement callableStatement = connection.prepareCall("{call DisplayPayroll}"); /// Sql injection
            ResultSet resultSet = callableStatement.executeQuery();

            // Iterate through the result set and populate the table model
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }
                payrollTableModel.addRow(rowData);
            }

            // Set the cell editor for the "HoursWorked" column
            TableColumnModel columnModel = payrollTable.getColumnModel();
            TableColumn payRateColumn = columnModel.getColumn(5); // Index of "HoursWorked" column
            payRateColumn.setCellEditor(new DefaultCellEditor(new JTextField())); // Use JTextField as cell editor

            // Make the table not editable by default
            payrollTable.setDefaultEditor(Object.class, null);

            hideEIDColumn(payrollTable);


            resultSet.close();
            callableStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQL exception here, such as displaying an error message dialog
            JOptionPane.showMessageDialog(this, "Error loading payroll data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    //// Employee pannel method call
    public void addEmployeeData(Connection connection) {
        String name = txtName.getText();
        String position = (String) cmbPosition.getSelectedItem();
        String salaryStr = txtSalary.getText();

        // Convert salary to Double
        double salary = 0.0;
        try {
            salary = Double.parseDouble(salaryStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid salary.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (name == null || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Create a CallableStatement for calling the stored procedure
            CallableStatement callableStatement = connection.prepareCall("{call InsertEmployee(?,?,?)}");

            // Set parameters for the CallableStatement
            callableStatement.setString(1, name);
            callableStatement.setString(2, position);
            callableStatement.setDouble(3, salary);

            // Execute the stored procedure
            callableStatement.execute();

            // Close the CallableStatement
            callableStatement.close();

            // If no exception is thrown, assume success
            JOptionPane.showMessageDialog(this, "Employee added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearEmployeeForm();
            loadEmployeeData();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding employee data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void updateEmployeeData(Connection connection, int rowIndex) {
        // Get the data from the table model
        int eid = (int) tableModel.getValueAt(rowIndex, 0);
        String name = (String) tableModel.getValueAt(rowIndex, 2);
        String position = (String) tableModel.getValueAt(rowIndex, 3);
        Object salaryObj = tableModel.getValueAt(rowIndex, 4);

        // If salary is not null and not an instance of String, convert it to String
        String salaryStr = null;
        if (salaryObj instanceof String) {
            salaryStr = (String) salaryObj;
        } else if (salaryObj instanceof Double) {
            salaryStr = String.valueOf(salaryObj);
        }

        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this employee?", "Confirm Update", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                // Create a CallableStatement using the provided connection
                CallableStatement callableStatement = connection.prepareCall("{call UpdateEmployee(?, ?, ?, ?)}");

                // Set parameters for the CallableStatement
                callableStatement.setInt(1, eid);
                callableStatement.setString(2, name);
                callableStatement.setString(3, position);
                if (salaryStr != null) {
                    double salary = Double.parseDouble(salaryStr);
                    callableStatement.setDouble(4, salary);
                } else {
                    callableStatement.setNull(4, Types.DECIMAL);
                }

                // Execute the CallableStatement
                ResultSet resultSet = callableStatement.executeQuery();

                // Process the result
                while (resultSet.next()) {
                    String resultMessage = resultSet.getString("Result");
                    if (resultMessage.startsWith("Error")) {
                        JOptionPane.showMessageDialog(this, resultMessage, "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        tableModel.setValueAt(name, rowIndex, 2);
                        tableModel.setValueAt(position, rowIndex, 3);
                        if (salaryStr != null) {
                            tableModel.setValueAt(salaryStr, rowIndex, 4);
                        }
                        JOptionPane.showMessageDialog(this, resultMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                // Close resources
                resultSet.close();
                callableStatement.close();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating employee data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public void deleteEmployeeData(Connection connection) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            // Get the employee ID from the table model
            int eid = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                // Create a CallableStatement using the provided connection
                CallableStatement callableStatement = connection.prepareCall("{call DeleteEmployee(?)}");

                // Set the employee ID parameter for the CallableStatement
                callableStatement.setInt(1, eid);

                // Execute the CallableStatement
                ResultSet resultSet = callableStatement.executeQuery();

                // Process the result
                while (resultSet.next()) {
                    String resultMessage = resultSet.getString("Result");
                    if (resultMessage.startsWith("Error")) {
                        JOptionPane.showMessageDialog(this, resultMessage, "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // If deletion is successful, remove the row from the table model
                        tableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(this, resultMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                // Close resources
                resultSet.close();
                callableStatement.close();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting employee data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No row selected for deletion.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //// Attendance pannel method call

    public void addAttendance(Connection connection, int row) {
        int eid = (int) attendanceTableModel.getValueAt(row, 0); // Get EID from the table model
        int noOfHrs = Integer.parseInt(attendanceTableModel.getValueAt(row, 5).toString());

        try {
            // Create a CallableStatement using the provided connection
            CallableStatement callableStatement = connection.prepareCall("{call AddAttendance(?, ?)}");

            // Set parameters for the CallableStatement
            callableStatement.setInt(1, eid);
            callableStatement.setInt(2, noOfHrs);

            // Execute the CallableStatement
            ResultSet resultSet = callableStatement.executeQuery();

            // Process the result
            while (resultSet.next()) {
                String resultMessage = resultSet.getString("Result");
                if (resultMessage.startsWith("Error")) {
                    JOptionPane.showMessageDialog(this, resultMessage, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, resultMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
                     // reload attendance panel
                }
            }
            // Close resources
            resultSet.close();
            callableStatement.close();
            loadAttendanceData(connection);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding attendance: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    //////// Payroll pannel method call

    public void updatePayroll(Connection connection, int rowIndex) {
        try {
            int eid = (int) payrollTableModel.getValueAt(rowIndex, 0); // Get EID from the table model
            String result = (payrollTableModel.getValueAt(rowIndex, 4).toString() != "") ? "Not null" : "Null";
            int noOfHrs = Integer.parseInt(payrollTableModel.getValueAt(rowIndex, 4).toString());
            Object payRateObj = payrollTableModel.getValueAt(rowIndex, 5);
            String payRateStr = payRateObj.toString();

            // Check if payRate field is empty
            if (payRateStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid PayRate.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Don't proceed further
            }

            float payRate = Float.parseFloat(payRateStr);
            String month = (String) payrollTableModel.getValueAt(rowIndex, 6);
            int year = Integer.parseInt(payrollTableModel.getValueAt(rowIndex, 7).toString());

            // Create a CallableStatement using the provided connection
            CallableStatement callableStatement = connection.prepareCall("{call UpdatePayroll (?, ?, ?, ?, ?)}");

            // Set parameters
            callableStatement.setInt(1, eid);
            callableStatement.setInt(2, noOfHrs);
            callableStatement.setFloat(3, payRate); // PayRate is passed as is
            callableStatement.setString(4, month);
            callableStatement.setInt(5, year);

            // Execute the CallableStatement
            callableStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Payroll updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Close resources
            callableStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating payroll: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    public void generatePayroll(Connection connection, JPanel panel) {
        try {
            // Create a CallableStatement using the provided connection
            CallableStatement callableStatement = connection.prepareCall("{call GeneratePayroll}");

            // Execute the CallableStatement
            callableStatement.executeUpdate();

            // If the execution completes without throwing an exception,
            // it indicates success
            JOptionPane.showMessageDialog(panel, "Payroll generated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Reload data or take any other necessary actions

            // Close resources
            callableStatement.close();
            loadPayrollData(connection);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel, "Error generating payroll: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void clearEmployeeForm() {
        txtName.setText("");
        cmbPosition.setSelectedIndex(0);
        txtSalary.setText("");
    }

}

class NumericDocumentFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (isNumeric(string)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (isNumeric(text)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d*");
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
        setText("Update");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

// Inner class for editing Employee Pannel Update button
class ButtonEditor extends DefaultCellEditor {
    private final EMSMain emsMain;
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private EMSUI emsui;
    private Connection connection;

    public ButtonEditor(JCheckBox checkBox, JTable table, EMSUI emsui, EMSMain emsMain) {
        super(checkBox);
        this.table = table;
        this.emsui = emsui;
        this.emsMain = emsMain; // Assign EMSMain instance to emsMain field
        this.connection = emsMain.getConnection(); // Get the connection from EMSMain
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> {
            int modelRowIndex = table.convertRowIndexToModel(table.getSelectedRow());
            emsui.updateEmployeeData(connection, modelRowIndex);
            button.setText("Update");
        });
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        button.setText("Update");
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            JOptionPane.showMessageDialog(null, "Update row " + label);
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}

class AttendanceButtonRenderer extends JButton implements TableCellRenderer {
    public AttendanceButtonRenderer() {
        setOpaque(true);
        setText("Update"); // Change the button text to "Add" for adding attendance
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

// Inner class for editing Attendance Pannel Update button

class AttendanceButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private EMSUI emsui;
    private Connection connection;

    public AttendanceButtonEditor(JCheckBox checkBox, JTable table, EMSUI emsui, Connection connection) {
        super(checkBox);
        this.table = table;
        this.emsui = emsui;
        this.connection = connection;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> {
            int modelRowIndex = table.convertRowIndexToModel(table.getSelectedRow());
            Object noOfHrsObj = table.getValueAt(modelRowIndex, 5);
            if (noOfHrsObj == null) {
                JOptionPane.showMessageDialog(null, "Please enter the valid hours worked", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String noOfHrsStr = noOfHrsObj.toString();
            if (noOfHrsStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter the valid hours worked", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            emsui.addAttendance(connection, modelRowIndex);
            button.setText("Update");
        });
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        button.setText("Update");
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            JOptionPane.showMessageDialog(null, "Add attendance for row " + label);
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}

// Inner class for editing Payroll Pannel Update button

class PayrollButtonRenderer extends JButton implements TableCellRenderer {
    public PayrollButtonRenderer() {
        setOpaque(true);
        setText("Update"); // Change the button text to "Add" for adding attendance
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

class PayrollButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private EMSUI emsui;
    private Connection connection;

    public PayrollButtonEditor(JCheckBox checkBox, JTable table, EMSUI emsui, Connection connection) {
        super(checkBox);
        this.table = table;
        this.emsui = emsui;
        this.connection = connection;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> {
            int modelRowIndex = table.convertRowIndexToModel(table.getSelectedRow());
            Object payRateObj = table.getValueAt(modelRowIndex, 5);
            if (payRateObj == null) {
                JOptionPane.showMessageDialog(null, "Please enter a valid Pay rate", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String payRateStr = payRateObj.toString();
            if (payRateStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid Pay rate", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            emsui.updatePayroll(connection, modelRowIndex);
            button.setText("Update");
        });
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        button.setText("Update");
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            JOptionPane.showMessageDialog(null, "update payroll for row " + label);
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}


import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
public class BankApp extends Frame implements ActionListener {
    TextField nameField, balanceField, accField, amountField;
    Button createBtn, depositBtn, withdrawBtn, checkBtn;
    TextArea output;

    // Track last clicked button to avoid unnecessary clears
    Button lastActionButton = null;

    public BankApp() {
        setTitle("Bank Management System");
        setSize(900, 600);
        setLayout(null);
        setLocationRelativeTo(null);
        setBackground(new Color(245, 245, 245));

        Font labelFont = new Font("SansSerif", Font.PLAIN, 20);
        Font btnFont = new Font("SansSerif", Font.BOLD, 18);

        Label heading = new Label("Bank Management System", Label.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 28));
        heading.setBounds(250, 40, 400, 40);
        add(heading);

        Label createTitle = new Label("Create New Account");
        createTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        createTitle.setBounds(100, 90, 300, 25);
        add(createTitle);

        Label nameLabel = new Label("Customer Name:");
        nameLabel.setFont(labelFont);
        nameLabel.setBounds(100, 120, 180, 30);
        add(nameLabel);

        nameField = new TextField();
        nameField.setFont(labelFont);
        nameField.setBounds(300, 120, 200, 30);
        add(nameField);

        Label balanceLabel = new Label("Balance:");
        balanceLabel.setFont(labelFont);
        balanceLabel.setBounds(100, 170, 180, 30);
        add(balanceLabel);

        balanceField = new TextField();
        balanceField.setFont(labelFont);
        balanceField.setBounds(300, 170, 200, 30);
        add(balanceField);

        createBtn = new Button("Create Account");
        createBtn.setFont(btnFont);
        createBtn.setBounds(300, 220, 200, 40);
        add(createBtn);
        createBtn.addActionListener(this);

        Label actionTitle = new Label("Account Actions");
        actionTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        actionTitle.setBounds(100, 280, 300, 25);
        add(actionTitle);

        Label accLabel = new Label("Account Number:");
        accLabel.setFont(labelFont);
        accLabel.setBounds(100, 310, 180, 30);
        add(accLabel);

        accField = new TextField();
        accField.setFont(labelFont);
        accField.setBounds(300, 310, 200, 30);
        add(accField);

        Label amtLabel = new Label("Amount:");
        amtLabel.setFont(labelFont);
        amtLabel.setBounds(100, 360, 180, 30);
        add(amtLabel);

        amountField = new TextField();
        amountField.setFont(labelFont);
        amountField.setBounds(300, 360, 200, 30);
        add(amountField);

        depositBtn = new Button("Deposit");
        withdrawBtn = new Button("Withdraw");
        checkBtn = new Button("Check Balance");

        depositBtn.setFont(btnFont);
        withdrawBtn.setFont(btnFont);
        checkBtn.setFont(btnFont);

        depositBtn.setBounds(100, 430, 150, 40);
        withdrawBtn.setBounds(270, 430, 150, 40);
        checkBtn.setBounds(440, 430, 180, 40);

        add(depositBtn);
        add(withdrawBtn);
        add(checkBtn);

        depositBtn.addActionListener(this);
        withdrawBtn.addActionListener(this);
        checkBtn.addActionListener(this);

        output = new TextArea();
        output.setFont(new Font("Courier New", Font.PLAIN, 16));
        output.setBounds(650, 120, 400, 350);
        output.setEditable(false);
        add(output);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        Button clicked = (Button) ae.getSource();
        clearOutputIfNewAction(clicked);

        try (Connection con = DBConnection.getConnection()) {
            if (clicked == createBtn) {
                String name = nameField.getText().trim();
                double bal = Double.parseDouble(balanceField.getText().trim());

                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO accounts(name, balance) VALUES (?, ?)");
                ps.setString(1, name);
                ps.setDouble(2, bal);
                ps.executeUpdate();

                output.append("✅ Account created for: " + name + "\n");
                clearInputs();

            } else if (clicked == depositBtn) {
                int acc = Integer.parseInt(accField.getText().trim());
                double amt = Double.parseDouble(amountField.getText().trim());

                PreparedStatement ps = con.prepareStatement(
                        "UPDATE accounts SET balance = balance + ? WHERE acc_no = ?");
                ps.setDouble(1, amt);
                ps.setInt(2, acc);
                int rows = ps.executeUpdate();

                output.append(rows > 0
                        ? "✅ ₹" + amt + " deposited.\n"
                        : "❌ Account not found.\n");
                clearInputs();

            } else if (clicked == withdrawBtn) {
                int acc = Integer.parseInt(accField.getText().trim());
                double amt = Double.parseDouble(amountField.getText().trim());

                PreparedStatement check = con.prepareStatement(
                        "SELECT balance FROM accounts WHERE acc_no = ?");
                check.setInt(1, acc);
                ResultSet rs = check.executeQuery();

                if (rs.next()) {
                    double bal = rs.getDouble("balance");
                    if (bal >= amt) {
                        PreparedStatement ps = con.prepareStatement(
                                "UPDATE accounts SET balance = balance - ? WHERE acc_no = ?");
                        ps.setDouble(1, amt);
                        ps.setInt(2, acc);
                        ps.executeUpdate();
                        output.append("✅ ₹" + amt + " withdrawn.\n");
                    } else {
                        output.append("❌ Insufficient balance.\n");
                    }
                } else {
                    output.append("❌ Account not found.\n");
                }
                clearInputs();

            } else if (clicked == checkBtn) {
                int acc = Integer.parseInt(accField.getText().trim());

                PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM accounts WHERE acc_no = ?");
                ps.setInt(1, acc);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    output.append("\n📄 Account Info:\n");
                    output.append("Account No: " + rs.getInt("acc_no") + "\n");
                    output.append("Name      : " + rs.getString("name") + "\n");
                    output.append("Balance   : ₹" + rs.getDouble("balance") + "\n");
                } else {
                    output.append("❌ Account not found.\n");
                }
                clearInputs();
            }

        } catch (Exception ex) {
            output.append("⚠️ Error: " + ex.getMessage() + "\n");
        }

        lastActionButton = clicked;
    }

    // 🔄 Clear all input fields
    private void clearInputs() {
        nameField.setText("");
        balanceField.setText("");
        accField.setText("");
        amountField.setText("");
    }

    // ✨ Clear output if switching buttons
    private void clearOutputIfNewAction(Button clicked) {
        if (lastActionButton != clicked) {
            output.setText("");
        }
    }

    public static void main(String[] args) {
        new BankApp();
    }
}

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.Locale;

public class Dashboard {

    private final BudgetManager manager = new BudgetManager();

    private JFrame frame;
    private JTable transactionTable;
    private DefaultTableModel tableModel;

    private JLabel balanceAmount;
    private JLabel incomeAmount;
    private JLabel expenseAmount;
    private JLabel welcomeLabel;

    // Lavender and Royal Purple color palette
    private final Color backgroundColor = new Color(246, 243, 255);
    private final Color royalPurple = new Color(76, 42, 133);
    private final Color purple = new Color(117, 77, 218);
    private final Color lightPurple = new Color(237, 229, 255);
    private final Color darkText = new Color(43, 34, 66);
    private final Color mutedText = new Color(119, 111, 137);
    private final Color white = Color.WHITE;
    private final Color mint = new Color(224, 249, 237);
    private final Color mintText = new Color(24, 130, 88);
    private final Color peach = new Color(255, 234, 226);
    private final Color coral = new Color(221, 87, 100);
    private final Color borderColor = new Color(232, 225, 245);

    private final NumberFormat moneyFormat =
            NumberFormat.getCurrencyInstance(Locale.US);

    public void showDashboard() {

        String name = JOptionPane.showInputDialog(
                null,
                "What should we call you?",
                "Welcome to PennyWise",
                JOptionPane.QUESTION_MESSAGE
        );

        if (name == null || name.trim().isEmpty()) {
            name = "there";
        } else {
            name = name.trim();
        }

        frame = new JFrame("PennyWise | Your Money, Your Goals");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 760);
        frame.setMinimumSize(new Dimension(850, 650));
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 22));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        );

        // Build the different sections of the dashboard.
        mainPanel.add(createHeader(name), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 22));
        centerPanel.setBackground(backgroundColor);

        centerPanel.add(createSummarySection(), BorderLayout.NORTH);
        centerPanel.add(createTransactionsSection(), BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(createFooter(), BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);

        refreshTransactionTable();
        updateSummaryCards();

        frame.setVisible(true);
    }

    // Creates the dark purple welcome banner.
    private JPanel createHeader(String name) {

        JPanel header = new JPanel(new BorderLayout(15, 10));
        header.setBackground(royalPurple);
        header.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(royalPurple, 1),
                        BorderFactory.createEmptyBorder(22, 25, 22, 25)
                )
        );

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel brandLabel = new JLabel("PennyWise");
        brandLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        brandLabel.setForeground(white);
        brandLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        welcomeLabel = new JLabel(
                "Welcome back, " + name + "! Let's make your money count."
        );
        welcomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        welcomeLabel.setForeground(new Color(229, 219, 255));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tagline = new JLabel(
                "Spend smarter. Stress less. Save for what matters."
        );
        tagline.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tagline.setForeground(new Color(205, 190, 245));
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(brandLabel);
        textPanel.add(Box.createVerticalStrut(6));
        textPanel.add(welcomeLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(tagline);

        JLabel logo = new JLabel("PW", SwingConstants.CENTER);
        logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        logo.setForeground(royalPurple);
        logo.setOpaque(true);
        logo.setBackground(new Color(229, 216, 255));
        logo.setPreferredSize(new Dimension(62, 62));
        logo.setBorder(
                BorderFactory.createLineBorder(
                        new Color(229, 216, 255), 1
                )
        );

        header.add(textPanel, BorderLayout.CENTER);
        header.add(logo, BorderLayout.EAST);

        return header;
    }

    // Creates the three financial summary cards.
    private JPanel createSummarySection() {

        JPanel section = new JPanel(new BorderLayout(0, 12));
        section.setOpaque(false);

        JLabel sectionTitle = new JLabel("Your financial snapshot");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 19));
        sectionTitle.setForeground(darkText);

        JLabel sectionSubtitle = new JLabel(
                "A little clarity goes a long way."
        );
        sectionSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sectionSubtitle.setForeground(mutedText);

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(sectionTitle);
        heading.add(Box.createVerticalStrut(4));
        heading.add(sectionSubtitle);

        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0));
        cards.setOpaque(false);

        JPanel balanceCard = createSummaryCard(
                "TOTAL BALANCE",
                "Your money at a glance",
                lightPurple,
                royalPurple,
                true
        );

        JPanel incomeCard = createSummaryCard(
                "TOTAL INCOME",
                "Money coming in",
                mint,
                mintText,
                false
        );

        JPanel expenseCard = createSummaryCard(
                "TOTAL EXPENSES",
                "Money going out",
                peach,
                coral,
                false
        );

        balanceAmount = (JLabel) balanceCard.getClientProperty("amountLabel");
        incomeAmount = (JLabel) incomeCard.getClientProperty("amountLabel");
        expenseAmount = (JLabel) expenseCard.getClientProperty("amountLabel");

        cards.add(balanceCard);
        cards.add(incomeCard);
        cards.add(expenseCard);

        section.add(heading, BorderLayout.NORTH);
        section.add(cards, BorderLayout.CENTER);

        return section;
    }

    // Helper method so all summary cards have a consistent design.
    private JPanel createSummaryCard(
            String title,
            String description,
            Color cardColor,
            Color accentColor,
            boolean featured
    ) {

        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(white);
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderColor, 1),
                        BorderFactory.createEmptyBorder(17, 17, 17, 17)
                )
        );

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        titleLabel.setForeground(mutedText);

        JLabel dot = new JLabel("●");
        dot.setFont(new Font("SansSerif", Font.BOLD, 17));
        dot.setForeground(accentColor);

        top.add(titleLabel, BorderLayout.WEST);
        top.add(dot, BorderLayout.EAST);

        JLabel amountLabel = new JLabel("$0.00");
        amountLabel.setFont(
                new Font("SansSerif", Font.BOLD, featured ? 25 : 23)
        );
        amountLabel.setForeground(darkText);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        descriptionLabel.setForeground(mutedText);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.add(amountLabel);
        bottom.add(Box.createVerticalStrut(7));
        bottom.add(descriptionLabel);

        JPanel accentStrip = new JPanel();
        accentStrip.setBackground(cardColor);
        accentStrip.setPreferredSize(new Dimension(5, 1));

        card.add(top, BorderLayout.NORTH);
        card.add(bottom, BorderLayout.CENTER);
        card.add(accentStrip, BorderLayout.WEST);

        card.putClientProperty("amountLabel", amountLabel);

        return card;
    }

    // Creates the transaction table and its action buttons.
    private JPanel createTransactionsSection() {

        JPanel section = new JPanel(new BorderLayout(0, 14));
        section.setBackground(white);
        section.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderColor, 1),
                        BorderFactory.createEmptyBorder(20, 20, 18, 20)
                )
        );

        JPanel heading = new JPanel(new BorderLayout(10, 5));
        heading.setOpaque(false);

        JPanel headingText = new JPanel();
        headingText.setOpaque(false);
        headingText.setLayout(new BoxLayout(headingText, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Recent transactions");
        title.setFont(new Font("SansSerif", Font.BOLD, 19));
        title.setForeground(darkText);

        JLabel subtitle = new JLabel(
                "Keep track of every dollar, one transaction at a time."
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(mutedText);

        headingText.add(title);
        headingText.add(Box.createVerticalStrut(5));
        headingText.add(subtitle);

        JButton addButton = createButton("+ Add transaction", purple, white);
        addButton.addActionListener(e -> addTransaction());

        heading.add(headingText, BorderLayout.CENTER);
        heading.add(addButton, BorderLayout.EAST);

        String[] columns = {"TYPE", "DESCRIPTION", "AMOUNT"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(tableModel);
        transactionTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        transactionTable.setForeground(darkText);
        transactionTable.setBackground(white);
        transactionTable.setRowHeight(36);
        transactionTable.setShowVerticalLines(false);
        transactionTable.setShowHorizontalLines(true);
        transactionTable.setGridColor(new Color(241, 237, 248));
        transactionTable.setSelectionBackground(lightPurple);
        transactionTable.setSelectionForeground(royalPurple);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setIntercellSpacing(new Dimension(8, 0));
        transactionTable.setAutoCreateRowSorter(true);

        JTableHeader tableHeader = transactionTable.getTableHeader();
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 11));
        tableHeader.setForeground(royalPurple);
        tableHeader.setBackground(new Color(246, 242, 255));
        tableHeader.setPreferredSize(new Dimension(100, 38));
        tableHeader.setReorderingAllowed(false);

        // Center the transaction type and right-align amounts.
        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        transactionTable.getColumnModel()
                .getColumn(0).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer amountRenderer =
                new DefaultTableCellRenderer();
        amountRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        transactionTable.getColumnModel()
                .getColumn(2).setCellRenderer(amountRenderer);

        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(400);
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(white);

        JLabel emptyHint = new JLabel(
                "Your money story starts here — add your first transaction!"
        );
        emptyHint.setFont(new Font("SansSerif", Font.ITALIC, 12));
        emptyHint.setForeground(mutedText);
        emptyHint.setHorizontalAlignment(SwingConstants.CENTER);
        emptyHint.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JButton deleteButton = createButton(
                "Delete selected", coral, white
        );
        deleteButton.addActionListener(e -> deleteTransaction());

        JPanel tableBottom = new JPanel(new BorderLayout(0, 8));
        tableBottom.setOpaque(false);
        tableBottom.add(emptyHint, BorderLayout.CENTER);
        tableBottom.add(deleteButton, BorderLayout.EAST);

        // Update the empty-state hint whenever the table changes.
        tableModel.addTableModelListener(e -> {
            emptyHint.setVisible(tableModel.getRowCount() == 0);
        });

        section.add(heading, BorderLayout.NORTH);
        section.add(scrollPane, BorderLayout.CENTER);
        section.add(tableBottom, BorderLayout.SOUTH);

        return section;
    }

    // Creates consistently styled buttons.
    private JButton createButton(
            String text,
            Color background,
            Color foreground
    ) {

        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(
                BorderFactory.createEmptyBorder(11, 16, 11, 16)
        );
        button.setOpaque(true);

        Color originalColor = background;
        Color hoverColor = background.darker();

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }
        });

        return button;
    }

    // Adds a transaction after checking the user's input.
    private void addTransaction() {

        String[] types = {"Income", "Expense"};

        JComboBox<String> typeBox = new JComboBox<>(types);
        typeBox.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JTextField descriptionField = new JTextField(18);
        JTextField amountField = new JTextField(18);

        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        form.add(new JLabel("Transaction type:"));
        form.add(typeBox);
        form.add(new JLabel("Description:"));
        form.add(descriptionField);
        form.add(new JLabel("Amount (e.g. 25.50):"));
        form.add(amountField);

        int result = JOptionPane.showConfirmDialog(
                frame,
                form,
                "Add a transaction",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String type = (String) typeBox.getSelectedItem();
        String description = descriptionField.getText().trim();
        String amountText = amountField.getText().trim();

        if (description.isEmpty()) {
            showError("Please enter a description.");
            return;
        }

        // Keep CSV data safe by not allowing commas in descriptions.
        if (description.contains(",")) {
            showError("Please remove commas from the description.");
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException ex) {
            showError("Please enter a valid amount, such as 25.50.");
            return;
        }

        if (!Double.isFinite(amount) || amount <= 0) {
            showError("The amount must be greater than zero.");
            return;
        }

        Transaction transaction =
                new Transaction(type, description, amount);

        // BudgetManager saves the transaction to transactions.csv.
        manager.addTransaction(transaction);

        refreshTransactionTable();
        updateSummaryCards();

        JOptionPane.showMessageDialog(
                frame,
                "Transaction added successfully!",
                "PennyWise",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Deletes the selected transaction.
    private void deleteTransaction() {

        int selectedViewRow = transactionTable.getSelectedRow();

        if (selectedViewRow == -1) {
            showError("Please select a transaction to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to delete this transaction?",
                "Confirm deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Convert the visible row to the original data row in case
        // the user has sorted the table by a column.
        int selectedModelRow =
                transactionTable.convertRowIndexToModel(selectedViewRow);

        Transaction transaction =
                manager.getTransactions().get(selectedModelRow);

        // BudgetManager removes the transaction and saves the CSV.
        manager.removeTransaction(transaction);

        refreshTransactionTable();
        updateSummaryCards();
    }

    // Reloads all transactions into the table.
    private void refreshTransactionTable() {

        tableModel.setRowCount(0);

        for (Transaction transaction : manager.getTransactions()) {

            Object[] row = {
                    transaction.getType(),
                    transaction.getDescription(),
                    moneyFormat.format(transaction.getAmount())
            };

            tableModel.addRow(row);
        }
    }

    // Recalculates and displays all three summary amounts.
    private void updateSummaryCards() {

        balanceAmount.setText(
                moneyFormat.format(manager.getBalance())
        );

        incomeAmount.setText(
                moneyFormat.format(manager.getTotalIncome())
        );

        expenseAmount.setText(
                moneyFormat.format(manager.getTotalExpenses())
        );

        // Make a negative balance easy to notice.
        if (manager.getBalance() < 0) {
            balanceAmount.setForeground(coral);
        } else {
            balanceAmount.setForeground(royalPurple);
        }
    }

    // Creates the small footer at the bottom of the window.
    private JPanel createFooter() {

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JLabel leftText = new JLabel(
                "PennyWise  •  Small steps, smarter spending."
        );
        leftText.setFont(new Font("SansSerif", Font.PLAIN, 11));
        leftText.setForeground(mutedText);

        JLabel rightText = new JLabel("Made for your money journey.");
        rightText.setFont(new Font("SansSerif", Font.PLAIN, 11));
        rightText.setForeground(mutedText);

        footer.add(leftText, BorderLayout.WEST);
        footer.add(rightText, BorderLayout.EAST);

        return footer;
    }

    // Shows a consistent error message.
    private void showError(String message) {

        JOptionPane.showMessageDialog(
                frame,
                message,
                "Check your input",
                JOptionPane.ERROR_MESSAGE
        );
    }
}








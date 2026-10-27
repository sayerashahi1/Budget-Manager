import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BudgetManager {

    private ArrayList<Transaction> transactions;

    private final String FILE_NAME = "transactions.csv";

    // Initialize the list and load saved transactions
    public BudgetManager() {
        transactions = new ArrayList<>();
        loadTransactions();
    }

    // Add a transaction and save it
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        saveTransactions();
    }

    // Delete a transaction and save the change
    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        saveTransactions();
    }

    // Return all transactions
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    // Calculate total income
    public double getTotalIncome() {
        double total = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase("Income")) {
                total += transaction.getAmount();
            }
        }

        return total;
    }

    // Calculate total expenses
    public double getTotalExpenses() {
        double total = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase("Expense")) {
                total += transaction.getAmount();
            }
        }

        return total;
    }

    // Calculate remaining balance
    public double getBalance() {
        return getTotalIncome() - getTotalExpenses();
    }

    // Save transactions to a CSV file
    private void saveTransactions() {

        try (BufferedWriter writer =
                 new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Transaction transaction : transactions) {

                // Replace commas to keep the CSV format simple
                String description =
                    transaction.getDescription().replace(",", " ");

                writer.write(
                    transaction.getType() + "," +
                    description + "," +
                    transaction.getAmount()
                );

                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println(
                "Could not save transactions: " + e.getMessage()
            );
        }
    }

    // Load saved transactions from the CSV file
    private void loadTransactions() {

        try (BufferedReader reader =
                 new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",", 3);

                if (parts.length == 3) {

                    try {
                        String type = parts[0];
                        String description = parts[1];
                        double amount = Double.parseDouble(parts[2]);

                        boolean validType =
                            type.equalsIgnoreCase("Income")
                            || type.equalsIgnoreCase("Expense");

                        if (validType
                                && !description.trim().isEmpty()
                                && Double.isFinite(amount)
                                && amount > 0) {

                            transactions.add(
                                new Transaction(type, description, amount)
                            );
                        }

                    } catch (NumberFormatException e) {
                        System.out.println(
                            "Skipping an invalid saved transaction."
                        );
                    }
                }
            }

        } catch (IOException e) {
            // The file may not exist the first time the app runs
            System.out.println(
                "No previous transactions found. Starting fresh."
            );
        }
    }
}



    

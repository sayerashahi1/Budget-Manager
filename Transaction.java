public class Transaction {

    private String type;
    private String description;
    private double amount;

    // Create a transaction
    public Transaction(String type, String description, double amount) {
        this.type = type;
        this.description = description;
        this.amount = amount;
    }

    // Return the transaction type
    public String getType() {
        return type;
    }

    // Return the description
    public String getDescription() {
        return description;
    }

    // Return the transaction amount
    public double getAmount() {
        return amount;
    }
} 




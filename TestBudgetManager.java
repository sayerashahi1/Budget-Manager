
public class TestBudgetManager {

    public static void main(String[] args) {

        BudgetManager manager = new BudgetManager();

        Transaction salary = new Transaction("Income", "Part-time job", 1800.00);
        Transaction groceries = new Transaction("Expense", "Groceries", 45.50);
        Transaction gas = new Transaction("Expense", "Gas", 30.00);

        manager.addTransaction(salary);
        manager.addTransaction(groceries);
        manager.addTransaction(gas);

        System.out.println("Total Income: $" + manager.getTotalIncome());
        System.out.println("Total Expenses: $" + manager.getTotalExpenses());
        System.out.println("Remaining Balance: $" + manager.getBalance());
    }
}


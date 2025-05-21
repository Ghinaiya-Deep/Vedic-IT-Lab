import java.util.*;

class Expense {
    private String date;
    private String description;
    private double amount;
    private String category;

    public Expense(String date, String description, double amount, String category) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return String.format("%-12s | %-20s | %-10s | ₹ %.2f", date, description, category, amount);
    }
}

public class ExpenseTrackerApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Expense> expenses = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== Expense Tracker App ===");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Expense");
            System.out.println("2. List All Expenses");
            System.out.println("3. Show Summary");
            System.out.println("4. Exit");
            System.out.print("Choose an option (1-4): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addExpense();
                    break;
                case "2":
                    listExpenses();
                    break;
                case "3":
                    showSummary();
                    break;
                case "4":
                    System.out.println("Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice! Please select between 1 to 4.");
            }
        }
    }

    private static void addExpense() {
        try {
            System.out.print("Enter date (YYYY-MM-DD): ");
            String date = scanner.nextLine().trim();

            System.out.print("Enter description: ");
            String description = scanner.nextLine().trim();

            System.out.print("Enter category (e.g., Food, Travel, Bills): ");
            String category = scanner.nextLine().trim();

            System.out.print("Enter amount: ₹ ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            if (amount < 0) {
                System.out.println("Amount cannot be negative. Expense not added.");
                return;
            }

            expenses.add(new Expense(date, description, amount, category));
            System.out.println("Expense added successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered. Please try again.");
        }
    }

    private static void listExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        System.out.println("\nDate         | Description          | Category   | Amount");
        System.out.println("------------------------------------------------------------");
        for (Expense expense : expenses) {
            System.out.println(expense);
        }
    }

    private static void showSummary() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses to summarize.");
            return;
        }

        double total = 0;
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Expense expense : expenses) {
            total += expense.getAmount();
            categoryTotals.put(
                expense.getCategory(),
                categoryTotals.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount()
            );
        }

        System.out.printf("\nTotal Expenses: ₹ %.2f%n", total);
        System.out.println("Category-wise Summary:");
        System.out.println("----------------------");
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            System.out.printf("%-12s : ₹ %.2f%n", entry.getKey(), entry.getValue());
        }
    }
}

package expensetracker;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ExpenseTracker {
    private static final String[] INCOME_CATEGORIES = {"Salary", "Business"};
    private static final String[] EXPENSE_CATEGORIES = {"Food", "Rent", "Travel"};
    private TransactionManager manager = new TransactionManager();
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ExpenseTracker app = new ExpenseTracker();
        if (args.length > 0) {
            try {
                app.manager.loadFromFile(args[0]);
                System.out.println("Loaded data from " + args[0]);
            } catch (Exception e) {
                System.out.println("Failed to load file: " + e.getMessage());
            }
        }
        app.run();
    }

    private void run() {
        while (true) {
            System.out.println("\n1. Add Income\n2. Add Expense\n3. View Monthly Summary\n4. Save to File\n5. Exit");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> addTransaction(TransactionType.INCOME);
                case "2" -> addTransaction(TransactionType.EXPENSE);
                case "3" -> viewMonthlySummary();
                case "4" -> saveToFileOption();
                case "5" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addTransaction(TransactionType type) {
        String[] categories = (type == TransactionType.INCOME) ? INCOME_CATEGORIES : EXPENSE_CATEGORIES;
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }

        int choice = -1;
        while (choice < 1 || choice > categories.length) {
            System.out.print("Choose category (1-" + categories.length + "): ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException ignored) {}
        }

        String category = categories[choice - 1];
        double amount = -1;
        while (amount <= 0) {
            System.out.print("Enter amount: ");
            try {
                amount = Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException ignored) {}
        }

        LocalDate date = LocalDate.now();
        System.out.print("Enter date (YYYY-MM-DD) or leave empty: ");
        String dateInput = scanner.nextLine().trim();
        if (!dateInput.isEmpty()) {
            try {
                date = LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Using today's date.");
            }
        }

        manager.addTransaction(new Transaction(type, category, amount, date));
        System.out.println(type + " added.");
    }

    private void viewMonthlySummary() {
        System.out.print("Enter month (YYYY-MM) or leave empty for current: ");
        String input = scanner.nextLine().trim();
        YearMonth month = input.isEmpty() ? YearMonth.now() : YearMonth.parse(input);

        List<Transaction> monthTransactions = manager.getMonthlyTransactions(month);
        double totalIncome = 0, totalExpense = 0;

        for (Transaction t : monthTransactions) {
            System.out.printf("%s | %s | %.2f | %s%n", t.getTransactionType(), t.getCategory(), t.getAmount(), t.getDate());
            if (t.getTransactionType() == TransactionType.INCOME) {
                totalIncome += t.getAmount();
            } else {
                totalExpense += t.getAmount();
            }
        }
        System.out.printf("Total Income: %.2f | Total Expense: %.2f | Net: %.2f%n", totalIncome, totalExpense, totalIncome - totalExpense);
    }
    private void saveToFileOption() {
        System.out.print("Enter filename: ");
        String filename = scanner.nextLine().trim();
        try {
            manager.saveToFile(filename);
            System.out.println("Saved to " + filename);
        } catch (Exception e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }
}

package expensetracker;

import java.time.LocalDate;

public class Transaction {
    private TransactionType transactionType;
    private String category;
    private double amount;
    private LocalDate date;

    public Transaction(TransactionType transactionType, String category, double amount, LocalDate date) {
        this.transactionType = transactionType;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public String toCSVLine() {
        return transactionType + "," + category + "," + amount + "," + date.toString();
    }

    public static Transaction fromCSVLine(String line) throws IllegalArgumentException {
        String[] parts = line.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid line format");
        }
        TransactionType type = TransactionType.valueOf(parts[0]);
        String category = parts[1];
        double amount = Double.parseDouble(parts[2]);
        LocalDate date = LocalDate.parse(parts[3]);
        return new Transaction(type, category, amount, date);
    }

    public TransactionType getTransactionType() { return transactionType; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
}

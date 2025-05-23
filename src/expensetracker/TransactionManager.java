package expensetracker;

import java.io.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Transaction transaction : transactions) {
                writer.write(transaction.toCSVLine());
                writer.newLine();
            }
        }
    }

    public void loadFromFile(String filename) throws IOException {
        transactions.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    Transaction transaction = Transaction.fromCSVLine(line);
                    transactions.add(transaction);
                } catch (Exception e) {
                    System.out.println("Skipping invalid entry at line " + lineNumber + ": " + line);
                }
            }
        }
    }

    public List<Transaction> getMonthlyTransactions(YearMonth yearMonth) {
        List<Transaction> monthly = new ArrayList<>();
        for (Transaction t : transactions) {
            if (YearMonth.from(t.getDate()).equals(yearMonth)) {
                monthly.add(t);
            }
        }
        return monthly;
    }
}

enum TransactionType { income, expense }

class FinanceTransaction {
  const FinanceTransaction({required this.id, required this.type, required this.category, required this.amount, required this.description, required this.timestamp});
  final int id;
  final TransactionType type;
  final String category;
  final double amount;
  final String description;
  final DateTime timestamp;
}

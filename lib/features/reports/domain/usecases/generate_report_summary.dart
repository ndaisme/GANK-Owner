import '../../../payments/domain/entities/finance_transaction.dart';
import '../entities/report_summary.dart';

class GenerateReportSummary {
  const GenerateReportSummary();

  ReportSummary call(List<FinanceTransaction> transactions) {
    final income = transactions.where((t) => t.type == TransactionType.income).fold<double>(0, (sum, t) => sum + t.amount);
    final expense = transactions.where((t) => t.type == TransactionType.expense).fold<double>(0, (sum, t) => sum + t.amount);
    return ReportSummary(income: income, expense: expense, profit: income - expense, transactionCount: transactions.length);
  }
}

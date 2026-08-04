import '../../inventory/domain/sparepart_item.dart';
import '../../payments/domain/finance_transaction.dart';
import '../../services/domain/service_order.dart';
import 'dashboard_summary.dart';

class GetDashboardSummary {
  const GetDashboardSummary();

  DashboardSummary call({required List<ServiceOrder> services, required List<SparepartItem> spareparts, required List<FinanceTransaction> transactions}) {
    final income = transactions.where((t) => t.type == TransactionType.income).fold<double>(0, (sum, t) => sum + t.amount);
    final expense = transactions.where((t) => t.type == TransactionType.expense).fold<double>(0, (sum, t) => sum + t.amount);
    return DashboardSummary(
      totalIncome: income,
      activeServices: services.where((s) => s.status != ServiceStatus.pickedUp && s.status != ServiceStatus.cancelled).length,
      readyForPickup: services.where((s) => s.status == ServiceStatus.completed).length,
      lowStock: spareparts.where((s) => s.isLowStock).length,
      profit: income - expense,
    );
  }
}

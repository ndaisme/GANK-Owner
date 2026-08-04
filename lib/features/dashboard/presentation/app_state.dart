import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../customers/domain/entities/customer.dart';
import '../../dashboard/domain/entities/dashboard_summary.dart';
import '../../dashboard/domain/usecases/get_dashboard_summary.dart';
import '../../inventory/domain/entities/sparepart_item.dart';
import '../../payments/domain/entities/finance_transaction.dart';
import '../../services/domain/entities/service_order.dart';
import '../../services/domain/usecases/advance_service_status.dart';
import '../../services/domain/usecases/generate_service_number.dart';
import '../../services/domain/usecases/validate_pickup.dart';
import '../../reports/domain/entities/report_summary.dart';
import '../../reports/domain/usecases/generate_report_summary.dart';

class GankState {
  const GankState({required this.services, required this.spareparts, required this.transactions, required this.customers});
  final List<ServiceOrder> services;
  final List<SparepartItem> spareparts;
  final List<FinanceTransaction> transactions;
  final List<Customer> customers;
}

class GankController extends StateNotifier<GankState> {
  GankController() : super(GankState(services: _seedServices, spareparts: _seedSpareparts, transactions: _seedTransactions, customers: _seedCustomers));

  void advanceService(ServiceOrder service) {
    final validationMessage = const ValidatePickup()(service);
    if (service.status == ServiceStatus.completed && validationMessage != null) return;

    final result = const AdvanceServiceStatus()(service, nextTransactionId: state.transactions.length + 1, timestamp: DateTime.now());
    state = GankState(
      services: [for (final item in state.services) if (item.id == service.id) result.service else item],
      spareparts: state.spareparts,
      transactions: result.settlement == null ? state.transactions : [...state.transactions, result.settlement!],
      customers: state.customers,
    );
  }

  String previewNextServiceNumber() => const GenerateServiceNumber()(date: DateTime.now(), sequence: state.services.length + 101);

  void updateStock(int id, int change) {
    state = GankState(
      services: state.services,
      spareparts: [for (final item in state.spareparts) if (item.id == id) item.copyWith(stock: (item.stock + change).clamp(0, 9999)) else item],
      transactions: state.transactions,
      customers: state.customers,
    );
  }

}

final gankControllerProvider = StateNotifierProvider<GankController, GankState>((ref) => GankController());
final dashboardSummaryProvider = Provider<DashboardSummary>((ref) {
  final state = ref.watch(gankControllerProvider);
  return const GetDashboardSummary()(services: state.services, spareparts: state.spareparts, transactions: state.transactions);
});

final reportSummaryProvider = Provider<ReportSummary>((ref) {
  final transactions = ref.watch(gankControllerProvider).transactions;
  return const GenerateReportSummary()(transactions);
});

final _seedServices = [
  ServiceOrder(id: 1, number: 'GS-2026-101', customerName: 'Andi', customerPhone: '08123456789', deviceModel: 'iPhone 12', complaint: 'LCD pecah', estimatedCost: 950000, downPayment: 200000, capitalCost: 500000, status: ServiceStatus.repair, createdAt: DateTime.now()),
  ServiceOrder(id: 2, number: 'GS-2026-102', customerName: 'Sari', customerPhone: '08987654321', deviceModel: 'Samsung A52', complaint: 'Baterai drop', estimatedCost: 350000, downPayment: 100000, capitalCost: 180000, status: ServiceStatus.completed, createdAt: DateTime.now()),
];
final _seedSpareparts = [
  const SparepartItem(id: 1, barcode: 'LCD-IP12', name: 'LCD iPhone 12', category: 'LCD', stock: 2, minStock: 3, purchasePrice: 500000, sellingPrice: 850000, rackLocation: 'Rak A-01'),
  const SparepartItem(id: 2, barcode: 'BAT-A52', name: 'Baterai Samsung A52', category: 'Baterai', stock: 8, minStock: 4, purchasePrice: 180000, sellingPrice: 300000, rackLocation: 'Rak B-02'),
];
final _seedTransactions = [
  FinanceTransaction(id: 1, type: TransactionType.income, category: 'DP Servis', amount: 300000, description: 'DP servis masuk', timestamp: DateTime.now()),
  FinanceTransaction(id: 2, type: TransactionType.expense, category: 'Belanja Sparepart', amount: 180000, description: 'Beli baterai', timestamp: DateTime.now()),
];
final _seedCustomers = [const Customer(id: 1, name: 'Andi', phone: '08123456789', totalServices: 2, totalSpending: 1250000), const Customer(id: 2, name: 'Sari', phone: '08987654321', totalServices: 1, totalSpending: 350000)];

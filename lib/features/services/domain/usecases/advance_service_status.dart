import '../../../payments/domain/entities/finance_transaction.dart';
import '../entities/service_order.dart';

class ServiceAdvanceResult {
  const ServiceAdvanceResult({required this.service, this.settlement});

  final ServiceOrder service;
  final FinanceTransaction? settlement;
}

class AdvanceServiceStatus {
  const AdvanceServiceStatus();

  ServiceAdvanceResult call(ServiceOrder service, {required int nextTransactionId, required DateTime timestamp}) {
    final next = nextStatus(service.status);
    return ServiceAdvanceResult(
      service: service.copyWith(status: next),
      settlement: next == ServiceStatus.pickedUp
          ? FinanceTransaction(
              id: nextTransactionId,
              type: TransactionType.income,
              category: 'Pelunasan Servis',
              amount: service.estimatedCost - service.downPayment,
              description: 'Pelunasan ${service.number}',
              timestamp: timestamp,
            )
          : null,
    );
  }

  ServiceStatus nextStatus(ServiceStatus status) => switch (status) {
        ServiceStatus.checkIn => ServiceStatus.diagnosis,
        ServiceStatus.diagnosis => ServiceStatus.waitingApproval,
        ServiceStatus.waitingApproval => ServiceStatus.repair,
        ServiceStatus.repair => ServiceStatus.qualityControl,
        ServiceStatus.qualityControl => ServiceStatus.completed,
        ServiceStatus.completed => ServiceStatus.pickedUp,
        ServiceStatus.pickedUp || ServiceStatus.cancelled => status,
      };
}

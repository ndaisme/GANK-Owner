import '../../../services/domain/entities/service_order.dart';

class PaymentStatus {
  const PaymentStatus({required this.paid, required this.remaining, required this.isPaidOff});
  final double paid;
  final double remaining;
  final bool isPaidOff;
}

class GetPaymentStatus {
  const GetPaymentStatus();

  PaymentStatus call(ServiceOrder service) {
    final remaining = (service.estimatedCost - service.downPayment).clamp(0, double.infinity).toDouble();
    return PaymentStatus(paid: service.downPayment, remaining: remaining, isPaidOff: remaining == 0 || service.status == ServiceStatus.pickedUp);
  }
}

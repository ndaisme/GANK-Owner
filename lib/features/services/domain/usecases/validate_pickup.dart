import '../entities/service_order.dart';

class ValidatePickup {
  const ValidatePickup();

  String? call(ServiceOrder service) {
    if (service.status != ServiceStatus.completed) return 'Servis harus berstatus selesai sebelum pickup.';
    if (service.estimatedCost < service.downPayment) return 'DP tidak boleh melebihi estimasi biaya.';
    return null;
  }
}

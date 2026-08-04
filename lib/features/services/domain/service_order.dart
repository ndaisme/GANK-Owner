enum ServiceStatus { checkIn, diagnosis, waitingApproval, repair, qualityControl, completed, pickedUp, cancelled }

extension ServiceStatusLabel on ServiceStatus {
  String get label => switch (this) {
        ServiceStatus.checkIn => 'CHECK IN',
        ServiceStatus.diagnosis => 'DIAGNOSA',
        ServiceStatus.waitingApproval => 'MENUNGGU APPROVAL',
        ServiceStatus.repair => 'DALAM PERBAIKAN',
        ServiceStatus.qualityControl => 'QUALITY CONTROL',
        ServiceStatus.completed => 'SELESAI',
        ServiceStatus.pickedUp => 'SUDAH DIAMBIL',
        ServiceStatus.cancelled => 'DIBATALKAN',
      };
}

class ServiceOrder {
  const ServiceOrder({required this.id, required this.number, required this.customerName, required this.customerPhone, required this.deviceModel, required this.complaint, required this.estimatedCost, required this.downPayment, required this.capitalCost, required this.status, required this.createdAt});
  final int id;
  final String number;
  final String customerName;
  final String customerPhone;
  final String deviceModel;
  final String complaint;
  final double estimatedCost;
  final double downPayment;
  final double capitalCost;
  final ServiceStatus status;
  final DateTime createdAt;

  ServiceOrder copyWith({ServiceStatus? status}) => ServiceOrder(id: id, number: number, customerName: customerName, customerPhone: customerPhone, deviceModel: deviceModel, complaint: complaint, estimatedCost: estimatedCost, downPayment: downPayment, capitalCost: capitalCost, status: status ?? this.status, createdAt: createdAt);
}

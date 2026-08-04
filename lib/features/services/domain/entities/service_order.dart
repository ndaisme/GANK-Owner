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

class ServiceStatusTimelineEntry {
  const ServiceStatusTimelineEntry({required this.status, required this.createdAt, this.note = '', this.actorName = 'Sistem'});

  final ServiceStatus status;
  final DateTime createdAt;
  final String note;
  final String actorName;
}

class ServiceOrder {
  const ServiceOrder({
    required this.id,
    required this.number,
    required this.customerName,
    required this.customerPhone,
    required this.deviceModel,
    required this.complaint,
    required this.estimatedCost,
    required this.downPayment,
    required this.capitalCost,
    required this.status,
    required this.createdAt,
    this.timeline = const [],
    this.isDeleted = false,
    this.deletedAt,
  });

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
  final List<ServiceStatusTimelineEntry> timeline;
  final bool isDeleted;
  final DateTime? deletedAt;

  ServiceOrder copyWith({ServiceStatus? status, List<ServiceStatusTimelineEntry>? timeline, bool? isDeleted, DateTime? deletedAt}) => ServiceOrder(
        id: id,
        number: number,
        customerName: customerName,
        customerPhone: customerPhone,
        deviceModel: deviceModel,
        complaint: complaint,
        estimatedCost: estimatedCost,
        downPayment: downPayment,
        capitalCost: capitalCost,
        status: status ?? this.status,
        createdAt: createdAt,
        timeline: timeline ?? this.timeline,
        isDeleted: isDeleted ?? this.isDeleted,
        deletedAt: deletedAt ?? this.deletedAt,
      );
}

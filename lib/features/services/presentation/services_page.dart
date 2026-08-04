import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../core/theme/gank_theme.dart';
import '../../../core/widgets/neo_brutalist.dart';
import '../../dashboard/presentation/app_state.dart';
import '../domain/entities/service_order.dart';
import '../domain/usecases/advance_service_status.dart';
import '../domain/usecases/filter_services.dart';

class ServicesPage extends ConsumerWidget {
  const ServicesPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final state = ref.watch(gankControllerProvider);
    final controller = ref.read(gankControllerProvider.notifier);
    return ServicesView(
      services: state.visibleServices,
      query: state.serviceQuery,
      nextNumber: controller.previewNextServiceNumber(),
      onQueryChanged: controller.updateServiceQuery,
      onCreateService: controller.createService,
      onUpdateStatus: controller.updateServiceStatus,
      onDeleteService: controller.softDeleteService,
    );
  }
}

class ServicesView extends StatelessWidget {
  const ServicesView({super.key, required this.services, required this.query, required this.nextNumber, required this.onQueryChanged, required this.onCreateService, required this.onUpdateStatus, required this.onDeleteService});

  final List<ServiceOrder> services;
  final ServiceQuery query;
  final String nextNumber;
  final ValueChanged<ServiceQuery> onQueryChanged;
  final void Function({required String customerName, required String customerPhone, required String deviceModel, required String complaint, required double estimatedCost, double downPayment, double capitalCost}) onCreateService;
  final void Function(ServiceOrder service, ServiceStatus status, {String note, String actorName}) onUpdateStatus;
  final ValueChanged<ServiceOrder> onDeleteService;

  @override
  Widget build(BuildContext context) {
    return ListView.separated(
      padding: const EdgeInsets.all(16),
      itemCount: services.length + 2,
      separatorBuilder: (_, __) => const SizedBox(height: 12),
      itemBuilder: (context, index) {
        if (index == 0) {
          return Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
            SectionHeader('ANTRIAN SERVIS', subtitle: 'Tambah, cari, filter, sortir, dan update status servis'),
            const SizedBox(height: 12),
            NeoButton(label: 'Tambah Servis ($nextNumber)', onPressed: () => _showCreateServiceSheet(context)),
            const SizedBox(height: 12),
            _ServiceFilters(query: query, onChanged: onQueryChanged),
          ]);
        }
        if (index == 1 && services.isEmpty) return const NeoCard(child: Text('Belum ada servis sesuai filter.'));
        final service = services[index - 2];
        return _ServiceTile(service: service, onTap: () => _showServiceDetail(context, service), onUpdateStatus: onUpdateStatus, onDelete: () => onDeleteService(service));
      },
    );
  }

  void _showCreateServiceSheet(BuildContext context) {
    final name = TextEditingController();
    final phone = TextEditingController();
    final device = TextEditingController();
    final complaint = TextEditingController();
    final estimate = TextEditingController();
    final dp = TextEditingController(text: '0');
    final capital = TextEditingController(text: '0');
    showModalBottomSheet<void>(context: context, isScrollControlled: true, builder: (context) => Padding(
      padding: EdgeInsets.fromLTRB(16, 16, 16, MediaQuery.of(context).viewInsets.bottom + 16),
      child: SingleChildScrollView(child: Column(mainAxisSize: MainAxisSize.min, children: [
        const SectionHeader('TAMBAH SERVIS', subtitle: 'Pelanggan otomatis dicocokkan dari nomor HP'),
        _field(name, 'Nama pelanggan'), _field(phone, 'Nomor HP'), _field(device, 'Model perangkat'), _field(complaint, 'Keluhan'), _field(estimate, 'Estimasi biaya', keyboardType: TextInputType.number), _field(dp, 'DP', keyboardType: TextInputType.number), _field(capital, 'Modal servis', keyboardType: TextInputType.number),
        const SizedBox(height: 12),
        NeoButton(label: 'Simpan Servis', onPressed: () { onCreateService(customerName: name.text, customerPhone: phone.text, deviceModel: device.text, complaint: complaint.text, estimatedCost: double.tryParse(estimate.text) ?? 0, downPayment: double.tryParse(dp.text) ?? 0, capitalCost: double.tryParse(capital.text) ?? 0); Navigator.pop(context); }),
      ])),
    ));
  }

  void _showServiceDetail(BuildContext context, ServiceOrder service) => showModalBottomSheet<void>(context: context, isScrollControlled: true, builder: (context) => Padding(
    padding: const EdgeInsets.all(16),
    child: SingleChildScrollView(child: Column(crossAxisAlignment: CrossAxisAlignment.start, mainAxisSize: MainAxisSize.min, children: [
      SectionHeader(service.number, subtitle: '${service.customerName} • ${service.customerPhone}'),
      const SizedBox(height: 12),
      Text(service.deviceModel, style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 18)),
      Text(service.complaint),
      const SizedBox(height: 8),
      Text('Estimasi: Rp${service.estimatedCost.toStringAsFixed(0)} • DP: Rp${service.downPayment.toStringAsFixed(0)} • Modal: Rp${service.capitalCost.toStringAsFixed(0)}'),
      const SizedBox(height: 12),
      DropdownButtonFormField<ServiceStatus>(value: service.status, decoration: const InputDecoration(labelText: 'Update status'), items: [for (final status in ServiceStatus.values) DropdownMenuItem(value: status, child: Text(status.label))], onChanged: (status) { if (status != null) onUpdateStatus(service, status); Navigator.pop(context); }),
      const SizedBox(height: 12),
      const Text('Timeline Status', style: TextStyle(fontWeight: FontWeight.w900)),
      for (final item in service.timeline) ListTile(contentPadding: EdgeInsets.zero, leading: const Icon(Icons.history), title: Text(item.status.label), subtitle: Text('${item.note} • ${item.actorName} • ${item.createdAt}')),
    ])),
  ));

  Widget _field(TextEditingController controller, String label, {TextInputType? keyboardType}) => Padding(padding: const EdgeInsets.only(top: 10), child: TextField(controller: controller, keyboardType: keyboardType, decoration: InputDecoration(labelText: label, border: const OutlineInputBorder())));
}

class _ServiceFilters extends StatelessWidget {
  const _ServiceFilters({required this.query, required this.onChanged});
  final ServiceQuery query;
  final ValueChanged<ServiceQuery> onChanged;
  @override
  Widget build(BuildContext context) => NeoCard(child: Column(children: [
    TextField(decoration: const InputDecoration(prefixIcon: Icon(Icons.search), labelText: 'Cari nomor, pelanggan, HP, perangkat, keluhan'), onChanged: (value) => onChanged(query.copyWith(search: value))),
    Row(children: [
      Expanded(child: DropdownButton<ServiceStatus?>(isExpanded: true, value: query.status, hint: const Text('Semua status'), items: [const DropdownMenuItem<ServiceStatus?>(value: null, child: Text('Semua status')), for (final status in ServiceStatus.values) DropdownMenuItem<ServiceStatus?>(value: status, child: Text(status.label))], onChanged: (value) => onChanged(query.copyWith(status: value, clearStatus: value == null)))),
      const SizedBox(width: 12),
      Expanded(child: DropdownButton<ServiceSortOption>(isExpanded: true, value: query.sort, items: const [DropdownMenuItem(value: ServiceSortOption.newest, child: Text('Terbaru')), DropdownMenuItem(value: ServiceSortOption.oldest, child: Text('Terlama')), DropdownMenuItem(value: ServiceSortOption.numberAsc, child: Text('Nomor A-Z')), DropdownMenuItem(value: ServiceSortOption.customerAsc, child: Text('Pelanggan A-Z')), DropdownMenuItem(value: ServiceSortOption.statusAsc, child: Text('Status'))], onChanged: (value) => onChanged(query.copyWith(sort: value)))),
    ]),
  ]));
}

class _ServiceTile extends StatelessWidget {
  const _ServiceTile({required this.service, required this.onTap, required this.onUpdateStatus, required this.onDelete});
  final ServiceOrder service;
  final VoidCallback onTap;
  final void Function(ServiceOrder service, ServiceStatus status, {String note, String actorName}) onUpdateStatus;
  final VoidCallback onDelete;
  @override
  Widget build(BuildContext context) => NeoCard(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
    ListTile(contentPadding: EdgeInsets.zero, onTap: onTap, title: Text('${service.number} • ${service.deviceModel}', style: const TextStyle(fontWeight: FontWeight.w900)), subtitle: Text('${service.customerName} (${service.customerPhone})\n${service.complaint}'), trailing: const Icon(Icons.chevron_right)),
    Row(children: [Chip(label: Text(service.status.label)), const Spacer(), TextButton(onPressed: onDelete, child: const Text('Hapus')), NeoButton(label: 'Update Status', onPressed: () => onUpdateStatus(service, const AdvanceServiceStatus().nextStatus(service.status)))]),
  ]));
}

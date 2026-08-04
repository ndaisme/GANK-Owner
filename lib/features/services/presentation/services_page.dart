import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../core/theme/gank_theme.dart';
import '../../../core/widgets/neo_brutalist.dart';
import '../domain/entities/service_order.dart';
import '../../dashboard/presentation/app_state.dart';

class ServicesPage extends ConsumerWidget {
  const ServicesPage({super.key});
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final services = ref.watch(gankControllerProvider).services;
    return ServicesView(services: services, onAdvanceService: ref.read(gankControllerProvider.notifier).advanceService);
  }
}

class ServicesView extends StatelessWidget {
  const ServicesView({super.key, required this.services, required this.onAdvanceService});

  final List<ServiceOrder> services;
  final ValueChanged<ServiceOrder> onAdvanceService;

  @override
  Widget build(BuildContext context) {
    return ListView.separated(padding: const EdgeInsets.all(16), itemCount: services.length + 1, separatorBuilder: (_, __) => const SizedBox(height: 12), itemBuilder: (context, index) {
      if (index == 0) return const SectionHeader('ANTRIAN SERVIS', subtitle: 'Status lifecycle servis HP');
      final service = services[index - 1];
      return NeoCard(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
        Text('${service.number} • ${service.deviceModel}', style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 16)),
        Text('${service.customerName} (${service.customerPhone})'),
        Text(service.complaint, style: const TextStyle(color: GankColors.steel)),
        const SizedBox(height: 8),
        Row(children: [Chip(label: Text(service.status.label)), const Spacer(), NeoButton(label: 'Majukan Status', onPressed: () => onAdvanceService(service))]),
      ]));
    });
  }
}

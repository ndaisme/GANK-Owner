import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';

import '../../../core/widgets/neo_brutalist.dart';
import '../../dashboard/presentation/app_state.dart';

class CustomersPage extends ConsumerWidget {
  const CustomersPage({super.key});
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final money = NumberFormat.currency(locale: 'id_ID', symbol: 'Rp ', decimalDigits: 0);
    final customers = ref.watch(gankControllerProvider).customers;
    return ListView(padding: const EdgeInsets.all(16), children: [const SectionHeader('DIREKTORI PELANGGAN', subtitle: 'Database dan riwayat pelanggan'), for (final customer in customers) Padding(padding: const EdgeInsets.only(top: 12), child: NeoCard(child: ListTile(leading: const Icon(Icons.person), title: Text(customer.name), subtitle: Text('WA/HP: ${customer.phone}'), trailing: Text('${customer.totalServices}x\n${money.format(customer.totalSpending)}'))))]);
  }
}

import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';

import '../../../core/widgets/neo_brutalist.dart';
import '../../dashboard/presentation/app_state.dart';

class PaymentsPage extends ConsumerWidget {
  const PaymentsPage({super.key});
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final money = NumberFormat.currency(locale: 'id_ID', symbol: 'Rp ', decimalDigits: 0);
    final transactions = ref.watch(gankControllerProvider).transactions;
    return ListView(padding: const EdgeInsets.all(16), children: [const SectionHeader('BUKU KAS', subtitle: 'DP, pelunasan, dan biaya sparepart'), for (final tx in transactions) Padding(padding: const EdgeInsets.only(top: 12), child: NeoCard(child: ListTile(title: Text(tx.category), subtitle: Text(tx.description), trailing: Text(money.format(tx.amount), style: const TextStyle(fontWeight: FontWeight.w900)))))]);
  }
}

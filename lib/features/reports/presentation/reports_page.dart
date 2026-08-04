import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';

import '../../../core/widgets/neo_brutalist.dart';
import '../../dashboard/presentation/app_state.dart';
import '../domain/entities/report_summary.dart';

class ReportsPage extends ConsumerWidget {
  const ReportsPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return ReportsView(summary: ref.watch(reportSummaryProvider));
  }
}

class ReportsView extends StatelessWidget {
  const ReportsView({super.key, required this.summary});

  final ReportSummary summary;

  @override
  Widget build(BuildContext context) {
    final money = NumberFormat.currency(locale: 'id_ID', symbol: 'Rp ', decimalDigits: 0);
    return ListView(
      padding: const EdgeInsets.all(16),
      children: [
        const SectionHeader('LAPORAN', subtitle: 'Ringkasan profit dan kas dari use case laporan'),
        const SizedBox(height: 12),
        NeoCard(child: Text('Transaksi: ${summary.transactionCount}\nPendapatan: ${money.format(summary.income)}\nPengeluaran: ${money.format(summary.expense)}\nProfit: ${money.format(summary.profit)}')),
      ],
    );
  }
}

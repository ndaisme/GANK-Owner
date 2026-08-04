import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';

import '../../../core/theme/gank_theme.dart';
import '../../../core/widgets/neo_brutalist.dart';
import 'app_state.dart';
import 'home_shell.dart';

class DashboardPage extends ConsumerWidget {
  const DashboardPage({super.key, required this.onNavigate});
  final ValueChanged<MainTab> onNavigate;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final summary = ref.watch(dashboardSummaryProvider);
    final money = NumberFormat.currency(locale: 'id_ID', symbol: 'Rp ', decimalDigits: 0);
    return ListView(padding: const EdgeInsets.all(16), children: [
      const NeoCard(color: GankColors.ink, child: Text('GANK SERVICE\nRepair • Manage • Grow — ERP Servis HP', style: TextStyle(color: GankColors.yellow, fontWeight: FontWeight.w900, fontSize: 20))),
      const SizedBox(height: 16),
      NeoCard(child: Row(children: [Icon(summary.lowStock > 0 ? Icons.warning : Icons.check_circle, color: summary.lowStock > 0 ? GankColors.danger : GankColors.success), const SizedBox(width: 12), Expanded(child: Text(summary.lowStock > 0 ? 'PENGINGAT: ${summary.lowStock} stok sparepart menipis' : 'STOK SPAREPART AMAN', style: const TextStyle(fontWeight: FontWeight.w900))), NeoButton(label: 'Stok', onPressed: () => onNavigate(MainTab.inventory))])),
      const SizedBox(height: 16),
      const SectionHeader('RINGKASAN OPERASIONAL', subtitle: 'Kondisi toko terkini'),
      Wrap(spacing: 12, runSpacing: 12, children: [
        _Metric(title: 'Pendapatan Total', value: money.format(summary.totalIncome), color: GankColors.white),
        _Metric(title: 'Servis Diproses', value: '${summary.activeServices} Unit', color: GankColors.yellow),
        _Metric(title: 'Siap Diambil', value: '${summary.readyForPickup} Unit', color: GankColors.blue),
        _Metric(title: 'Profit', value: money.format(summary.profit), color: GankColors.white),
      ]),
      const SizedBox(height: 16),
      const SectionHeader('GRAFIK KAS MVP'),
      SizedBox(height: 180, child: NeoCard(child: BarChart(BarChartData(barGroups: [BarChartGroupData(x: 0, barRods: [BarChartRodData(toY: summary.totalIncome)]), BarChartGroupData(x: 1, barRods: [BarChartRodData(toY: summary.profit)])], titlesData: const FlTitlesData())))),
    ]);
  }
}

class _Metric extends StatelessWidget {
  const _Metric({required this.title, required this.value, required this.color});
  final String title;
  final String value;
  final Color color;
  @override
  Widget build(BuildContext context) => SizedBox(width: 180, child: NeoCard(color: color, child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [Text(title, style: const TextStyle(fontWeight: FontWeight.bold)), Text(value, style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 18))])));
}

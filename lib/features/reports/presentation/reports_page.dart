import 'package:flutter/material.dart';

import '../../../core/widgets/neo_brutalist.dart';

class ReportsPage extends StatelessWidget {
  const ReportsPage({super.key});
  @override
  Widget build(BuildContext context) => const ListView(padding: EdgeInsets.all(16), children: [SectionHeader('LAPORAN', subtitle: 'Placeholder MVP untuk ekspor dan analitik'), SizedBox(height: 12), NeoCard(child: Text('Laporan profit, stok, teknisi, dan pelanggan akan membaca data dari repository/use case.'))]);
}

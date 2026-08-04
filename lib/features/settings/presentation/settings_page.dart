import 'package:flutter/material.dart';

import '../../../core/widgets/neo_brutalist.dart';

class SettingsPage extends StatelessWidget {
  const SettingsPage({super.key});
  @override
  Widget build(BuildContext context) => const ListView(padding: EdgeInsets.all(16), children: [SectionHeader('PENGATURAN', subtitle: 'Preferensi toko dan integrasi WhatsApp'), SizedBox(height: 12), NeoCard(child: Text('SharedPreferences disiapkan untuk onboarding, backup JSON, dan token integrasi.'))]);
}

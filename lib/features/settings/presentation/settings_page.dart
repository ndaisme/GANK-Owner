import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../core/widgets/neo_brutalist.dart';
import '../../auth/domain/auth_session.dart';
import '../../auth/presentation/auth_controller.dart';

class SettingsPage extends ConsumerStatefulWidget {
  const SettingsPage({super.key});

  @override
  ConsumerState<SettingsPage> createState() => _SettingsPageState();
}

class _SettingsPageState extends ConsumerState<SettingsPage> {
  final _ownerPinController = TextEditingController();
  final _technicianPinController = TextEditingController();

  @override
  void dispose() {
    _ownerPinController.dispose();
    _technicianPinController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) => ListView(
        padding: const EdgeInsets.all(16),
        children: [
          const SectionHeader('PENGATURAN', subtitle: 'Preferensi toko, sesi login, dan PIN lokal'),
          const SizedBox(height: 12),
          NeoCard(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                const Text('Konfigurasi PIN disimpan di database lokal dan sesi aktif disimpan memakai SharedPreferences.'),
                const SizedBox(height: 16),
                TextField(controller: _ownerPinController, obscureText: true, keyboardType: TextInputType.number, decoration: const InputDecoration(labelText: 'PIN Owner baru')),
                const SizedBox(height: 8),
                NeoButton(label: 'SIMPAN PIN OWNER', onPressed: () => _updatePin(AuthRole.owner, _ownerPinController)),
                const SizedBox(height: 16),
                TextField(controller: _technicianPinController, obscureText: true, keyboardType: TextInputType.number, decoration: const InputDecoration(labelText: 'PIN Teknisi baru')),
                const SizedBox(height: 8),
                NeoButton(label: 'SIMPAN PIN TEKNISI', onPressed: () => _updatePin(AuthRole.teknisi, _technicianPinController)),
              ],
            ),
          ),
        ],
      );

  Future<void> _updatePin(AuthRole role, TextEditingController controller) async {
    final pin = controller.text.trim();
    if (pin.length < 4) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('PIN minimal 4 digit.')));
      return;
    }
    await ref.read(authControllerProvider.notifier).updatePin(role: role, pin: pin);
    controller.clear();
    if (mounted) ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('PIN ${role.label} tersimpan.')));
  }
}

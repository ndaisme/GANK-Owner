import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../core/theme/gank_theme.dart';
import '../../../core/widgets/neo_brutalist.dart';
import '../domain/entities/auth_session.dart';
import 'auth_controller.dart';

class AuthPage extends ConsumerStatefulWidget {
  const AuthPage({super.key});

  @override
  ConsumerState<AuthPage> createState() => _AuthPageState();
}

class _AuthPageState extends ConsumerState<AuthPage> {
  final _pinController = TextEditingController();
  AuthRole _role = AuthRole.owner;
  String? _error;

  @override
  void dispose() {
    _pinController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final authState = ref.watch(authControllerProvider);
    final isLoading = authState.isLoading;
    return Scaffold(
      body: Center(
        child: ConstrainedBox(
          constraints: const BoxConstraints(maxWidth: 420),
          child: NeoCard(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                const SectionHeader('LOGIN PIN', subtitle: 'Owner akses penuh, teknisi akses operasional terbatas'),
                const SizedBox(height: 16),
                SegmentedButton<AuthRole>(
                  segments: AuthRole.values.map((role) => ButtonSegment(value: role, label: Text(role.label))).toList(),
                  selected: {_role},
                  onSelectionChanged: isLoading ? null : (roles) => setState(() => _role = roles.first),
                ),
                const SizedBox(height: 12),
                TextField(
                  controller: _pinController,
                  keyboardType: TextInputType.number,
                  obscureText: true,
                  decoration: InputDecoration(labelText: 'PIN ${_role.label}', errorText: _error),
                  onSubmitted: (_) => _submit(),
                ),
                const SizedBox(height: 16),
                NeoButton(label: isLoading ? 'MEMERIKSA...' : 'MASUK', color: GankColors.yellow, onPressed: isLoading ? () {} : _submit),
                const SizedBox(height: 8),
                const Text('PIN awal Owner: 123456 • Teknisi: 0000', textAlign: TextAlign.center, style: TextStyle(color: GankColors.steel, fontSize: 12)),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Future<void> _submit() async {
    final ok = await ref.read(authControllerProvider.notifier).signIn(role: _role, pin: _pinController.text.trim());
    if (!ok && mounted) setState(() => _error = 'PIN tidak sesuai untuk ${_role.label}');
  }
}

import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../core/theme/gank_theme.dart';
import '../../../core/widgets/neo_brutalist.dart';
import '../../auth/domain/usecases/auth_permissions.dart';
import '../../auth/presentation/auth_controller.dart';
import '../../customers/presentation/customers_page.dart';
import '../../inventory/presentation/inventory_page.dart';
import '../../payments/presentation/payments_page.dart';
import '../../reports/presentation/reports_page.dart';
import '../../services/presentation/services_page.dart';
import '../../settings/presentation/settings_page.dart';
import 'dashboard_page.dart';

enum MainTab { dashboard, services, inventory, payments, customers, reports, settings }

class HomeShell extends ConsumerStatefulWidget {
  const HomeShell({super.key});

  @override
  ConsumerState<HomeShell> createState() => _HomeShellState();
}

class _HomeShellState extends ConsumerState<HomeShell> {
  MainTab tab = MainTab.dashboard;

  @override
  Widget build(BuildContext context) {
    final session = ref.watch(authControllerProvider).valueOrNull;
    final role = session?.role;
    final allowedTabs = role?.allowedTabs ?? {MainTab.dashboard};
    if (!allowedTabs.contains(tab)) tab = allowedTabs.first;
    final visibleTabs = MainTab.values.where(allowedTabs.contains).toList();
    final pages = {
      MainTab.dashboard: DashboardPage(onNavigate: (value) => setState(() => tab = value)),
      MainTab.services: const ServicesPage(),
      MainTab.inventory: const InventoryPage(),
      MainTab.payments: const PaymentsPage(),
      MainTab.customers: const CustomersPage(),
      MainTab.reports: const ReportsPage(),
      MainTab.settings: const SettingsPage(),
    };
    return Scaffold(
      appBar: AppBar(
        title: Text('GANK SERVICE • ${tab.name.toUpperCase()} • ${role?.label.toUpperCase() ?? ''}'),
        backgroundColor: GankColors.ink,
        foregroundColor: GankColors.white,
        actions: [TextButton.icon(onPressed: () => ref.read(authControllerProvider.notifier).signOut(), icon: const Icon(Icons.logout, color: GankColors.white), label: const Text('Keluar', style: TextStyle(color: GankColors.white)))],
      ),
      body: pages[tab] ?? const Center(child: NeoCard(child: Text('Menu tidak diizinkan untuk role ini.'))),
      floatingActionButton: (tab == MainTab.dashboard || tab == MainTab.services) ? FloatingActionButton(backgroundColor: GankColors.yellow, foregroundColor: GankColors.ink, onPressed: () => setState(() => tab = MainTab.services), child: const Icon(Icons.add)) : null,
      bottomNavigationBar: NavigationBar(
        selectedIndex: visibleTabs.indexOf(tab).clamp(0, visibleTabs.length - 1),
        onDestinationSelected: (index) => setState(() => tab = visibleTabs[index]),
        destinations: [for (final item in visibleTabs) _destinationFor(item)],
      ),
    );
  }
}

NavigationDestination _destinationFor(MainTab tab) => switch (tab) {
      MainTab.dashboard => const NavigationDestination(icon: Icon(Icons.dashboard), label: 'Dashboard'),
      MainTab.services => const NavigationDestination(icon: Icon(Icons.build), label: 'Servis'),
      MainTab.inventory => const NavigationDestination(icon: Icon(Icons.inventory), label: 'Stok'),
      MainTab.payments => const NavigationDestination(icon: Icon(Icons.payments), label: 'Kas'),
      MainTab.customers => const NavigationDestination(icon: Icon(Icons.people), label: 'Pelanggan'),
      MainTab.reports => const NavigationDestination(icon: Icon(Icons.bar_chart), label: 'Laporan'),
      MainTab.settings => const NavigationDestination(icon: Icon(Icons.settings), label: 'Setelan'),
    };

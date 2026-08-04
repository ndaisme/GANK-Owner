import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../core/theme/gank_theme.dart';
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
      appBar: AppBar(title: Text('GANK SERVICE • ${tab.name.toUpperCase()}'), backgroundColor: GankColors.ink, foregroundColor: GankColors.white),
      body: pages[tab],
      floatingActionButton: (tab == MainTab.dashboard || tab == MainTab.services) ? FloatingActionButton(backgroundColor: GankColors.yellow, foregroundColor: GankColors.ink, onPressed: () => setState(() => tab = MainTab.services), child: const Icon(Icons.add)) : null,
      bottomNavigationBar: NavigationBar(
        selectedIndex: (MainTab.values.indexOf(tab).clamp(0, MainTab.values.length - 1) as int),
        onDestinationSelected: (index) => setState(() => tab = MainTab.values[index]),
        destinations: const [
          NavigationDestination(icon: Icon(Icons.dashboard), label: 'Dashboard'),
          NavigationDestination(icon: Icon(Icons.build), label: 'Servis'),
          NavigationDestination(icon: Icon(Icons.inventory), label: 'Stok'),
          NavigationDestination(icon: Icon(Icons.payments), label: 'Kas'),
          NavigationDestination(icon: Icon(Icons.people), label: 'Pelanggan'),
          NavigationDestination(icon: Icon(Icons.bar_chart), label: 'Laporan'),
          NavigationDestination(icon: Icon(Icons.settings), label: 'Setelan'),
        ],
      ),
    );
  }
}

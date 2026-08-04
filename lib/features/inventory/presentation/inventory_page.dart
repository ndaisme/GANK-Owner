import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../core/theme/gank_theme.dart';
import '../../../core/widgets/neo_brutalist.dart';
import '../../dashboard/presentation/app_state.dart';

class InventoryPage extends ConsumerWidget {
  const InventoryPage({super.key});
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final items = ref.watch(gankControllerProvider).spareparts;
    return ListView(padding: const EdgeInsets.all(16), children: [
      const SectionHeader('INVENTORY SPAREPART', subtitle: 'Kontrol stok minimum dan lokasi rak'),
      for (final item in items) Padding(padding: const EdgeInsets.only(top: 12), child: NeoCard(color: item.isLowStock ? GankColors.yellow : GankColors.white, child: Row(children: [Expanded(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [Text(item.name, style: const TextStyle(fontWeight: FontWeight.w900)), Text('${item.category} • ${item.rackLocation} • minimum ${item.minStock}') ])), IconButton(onPressed: () => ref.read(gankControllerProvider.notifier).updateStock(item.id, -1), icon: const Icon(Icons.remove)), Text('${item.stock}'), IconButton(onPressed: () => ref.read(gankControllerProvider.notifier).updateStock(item.id, 1), icon: const Icon(Icons.add))]))),
    ]);
  }
}

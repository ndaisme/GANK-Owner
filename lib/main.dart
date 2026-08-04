import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import 'core/routing/app_router.dart';
import 'core/theme/gank_theme.dart';

void main() {
  runApp(const ProviderScope(child: GankOwnerApp()));
}

class GankOwnerApp extends ConsumerWidget {
  const GankOwnerApp({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final router = ref.watch(appRouterProvider);
    return MaterialApp.router(
      title: 'GANK Service Owner',
      theme: buildGankTheme(),
      routerConfig: router,
      debugShowCheckedModeBanner: false,
    );
  }
}

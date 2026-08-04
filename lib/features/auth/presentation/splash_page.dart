import 'package:flutter/material.dart';

import '../../../core/theme/gank_theme.dart';

class SplashPage extends StatelessWidget {
  const SplashPage({super.key});

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      backgroundColor: GankColors.yellow,
      body: Center(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(Icons.home_repair_service, size: 64, color: GankColors.ink),
            SizedBox(height: 16),
            Text('GANK SERVICE', style: TextStyle(fontSize: 28, fontWeight: FontWeight.w900, color: GankColors.ink)),
            SizedBox(height: 12),
            CircularProgressIndicator(color: GankColors.ink),
          ],
        ),
      ),
    );
  }
}

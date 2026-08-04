import 'package:flutter/material.dart';

class GankColors {
  static const ink = Color(0xFF111111);
  static const paper = Color(0xFFF6F0DF);
  static const white = Color(0xFFFFFFFF);
  static const yellow = Color(0xFFFFD23F);
  static const blue = Color(0xFF23B5D3);
  static const danger = Color(0xFFE84855);
  static const success = Color(0xFF2EAD4B);
  static const steel = Color(0xFF5D6470);
}

ThemeData buildGankTheme() {
  return ThemeData(
    colorScheme: ColorScheme.fromSeed(seedColor: GankColors.yellow, surface: GankColors.paper),
    scaffoldBackgroundColor: GankColors.paper,
    fontFamily: 'Roboto',
    useMaterial3: true,
  );
}

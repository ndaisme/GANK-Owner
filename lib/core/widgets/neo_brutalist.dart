import 'package:flutter/material.dart';

import '../theme/gank_theme.dart';

class NeoCard extends StatelessWidget {
  const NeoCard({super.key, required this.child, this.color = GankColors.white, this.padding = const EdgeInsets.all(16)});

  final Widget child;
  final Color color;
  final EdgeInsetsGeometry padding;

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(color: GankColors.ink, borderRadius: BorderRadius.circular(10)),
      padding: const EdgeInsets.only(right: 5, bottom: 5),
      child: Container(
        width: double.infinity,
        padding: padding,
        decoration: BoxDecoration(
          color: color,
          border: Border.all(color: GankColors.ink, width: 3),
          borderRadius: BorderRadius.circular(10),
        ),
        child: child,
      ),
    );
  }
}

class NeoButton extends StatelessWidget {
  const NeoButton({super.key, required this.label, required this.onPressed, this.color = GankColors.yellow});

  final String label;
  final VoidCallback onPressed;
  final Color color;

  @override
  Widget build(BuildContext context) {
    return FilledButton(
      style: FilledButton.styleFrom(
        backgroundColor: color,
        foregroundColor: GankColors.ink,
        side: const BorderSide(color: GankColors.ink, width: 3),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
        textStyle: const TextStyle(fontWeight: FontWeight.w900),
      ),
      onPressed: onPressed,
      child: Text(label),
    );
  }
}

class SectionHeader extends StatelessWidget {
  const SectionHeader(this.title, {super.key, this.subtitle});

  final String title;
  final String? subtitle;

  @override
  Widget build(BuildContext context) {
    return Row(children: [
      Container(width: 8, height: 28, decoration: BoxDecoration(color: GankColors.yellow, border: Border.all(width: 2))),
      const SizedBox(width: 8),
      Expanded(child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
        Text(title, style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 18)),
        if (subtitle != null) Text(subtitle!, style: const TextStyle(color: GankColors.steel, fontSize: 12)),
      ])),
    ]);
  }
}

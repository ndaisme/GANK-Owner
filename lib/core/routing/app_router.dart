import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../features/dashboard/presentation/home_shell.dart';

final appRouterProvider = Provider<GoRouter>((ref) => GoRouter(routes: [GoRoute(path: '/', builder: (context, state) => const HomeShell())]));

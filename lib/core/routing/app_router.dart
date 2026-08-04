import 'package:flutter/foundation.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../features/auth/domain/entities/auth_session.dart';
import '../../features/auth/presentation/auth_controller.dart';
import '../../features/auth/presentation/auth_page.dart';
import '../../features/auth/presentation/splash_page.dart';
import '../../features/dashboard/presentation/home_shell.dart';

final appRouterProvider = Provider<GoRouter>((ref) {
  final refresh = GoRouterRefreshNotifier(ref);
  ref.onDispose(refresh.dispose);

  return GoRouter(
    initialLocation: '/splash',
    refreshListenable: refresh,
    redirect: (context, state) {
      final auth = ref.read(authControllerProvider);
      final isSplash = state.matchedLocation == '/splash';
      final isLogin = state.matchedLocation == '/login';

      if (auth.isLoading) return isSplash ? null : '/splash';

      final session = auth.valueOrNull;
      if (session == null) return isLogin ? null : '/login';
      if (isSplash || isLogin) return '/';
      return null;
    },
    routes: [
      GoRoute(path: '/splash', builder: (context, state) => const SplashPage()),
      GoRoute(path: '/login', builder: (context, state) => const AuthPage()),
      GoRoute(path: '/', builder: (context, state) => const HomeShell()),
    ],
  );
});

class GoRouterRefreshNotifier extends ChangeNotifier {
  GoRouterRefreshNotifier(this._ref) {
    _subscription = _ref.listen(authControllerProvider, (_, __) => notifyListeners(), fireImmediately: true);
  }

  final Ref _ref;
  late final ProviderSubscription<AsyncValue<AuthSession?>> _subscription;

  @override
  void dispose() {
    _subscription.close();
    super.dispose();
  }
}

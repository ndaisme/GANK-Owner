import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../data/auth_repository.dart';
import '../domain/auth_session.dart';

final authControllerProvider = AsyncNotifierProvider<AuthController, AuthSession?>(AuthController.new);

class AuthController extends AsyncNotifier<AuthSession?> {
  @override
  Future<AuthSession?> build() => ref.watch(authRepositoryProvider).readSession();

  Future<bool> signIn({required AuthRole role, required String pin}) async {
    state = const AsyncLoading();
    final repository = ref.read(authRepositoryProvider);
    final session = await repository.signIn(role: role, pin: pin);
    state = AsyncData(session);
    return session != null;
  }

  Future<void> signOut() async {
    await ref.read(authRepositoryProvider).signOut();
    state = const AsyncData(null);
  }

  Future<void> updatePin({required AuthRole role, required String pin}) => ref.read(authRepositoryProvider).updatePin(role: role, pin: pin);
}

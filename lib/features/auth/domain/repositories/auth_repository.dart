import '../entities/auth_session.dart';

abstract class AuthRepository {
  Future<AuthSession?> readSession();
  Future<AuthSession?> signIn({required AuthRole role, required String pin});
  Future<void> signOut();
  Future<void> updatePin({required AuthRole role, required String pin});
}

import 'dart:convert';

import 'package:drift/drift.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../../../core/database/app_database.dart';
import '../../domain/repositories/auth_repository.dart';
import '../../domain/entities/auth_session.dart';

final appDatabaseProvider = Provider<AppDatabase>((ref) {
  final db = AppDatabase();
  ref.onDispose(db.close);
  return db;
});

final sharedPreferencesProvider = FutureProvider<SharedPreferences>((ref) => SharedPreferences.getInstance());

final authRepositoryProvider = Provider<AuthRepository>((ref) {
  return DriftAuthRepository(ref.watch(appDatabaseProvider), ref.watch(sharedPreferencesProvider.future));
});

class DriftAuthRepository implements AuthRepository {
  DriftAuthRepository(this._db, this._preferencesFuture);

  static const _sessionKey = 'gank.active_session';
  static const _defaultOwnerPin = '123456';
  static const _defaultTechnicianPin = '0000';

  final AppDatabase _db;
  final Future<SharedPreferences> _preferencesFuture;

  Future<AuthSession?> readSession() async {
    final preferences = await _preferencesFuture;
    final rawSession = preferences.getString(_sessionKey);
    if (rawSession == null) return null;
    final decoded = jsonDecode(rawSession);
    if (decoded is! Map<String, Object?>) return null;
    return AuthSession.fromJson(decoded);
  }

  Future<AuthSession?> signIn({required AuthRole role, required String pin}) async {
    final settings = await _ensureLocalAuthSettings();
    final expectedHash = switch (role) {
      AuthRole.owner => settings.ownerPinHash,
      AuthRole.teknisi => settings.technicianPinHash,
    };
    if (_hashPin(pin) != expectedHash) return null;
    final session = AuthSession(role: role, signedInAt: DateTime.now());
    await _saveSession(session);
    return session;
  }

  Future<void> signOut() async {
    final preferences = await _preferencesFuture;
    await preferences.remove(_sessionKey);
  }

  Future<void> updatePin({required AuthRole role, required String pin}) async {
    final settings = await _ensureLocalAuthSettings();
    final companion = StoreSettingsCompanion(
      id: Value(settings.id),
      storeName: Value(settings.storeName),
      phone: Value(settings.phone),
      address: Value(settings.address),
      receiptFooter: Value(settings.receiptFooter),
      defaultWarrantyDays: Value(settings.defaultWarrantyDays),
      ownerPinHash: role == AuthRole.owner ? Value(_hashPin(pin)) : Value(settings.ownerPinHash),
      technicianPinHash: role == AuthRole.teknisi ? Value(_hashPin(pin)) : Value(settings.technicianPinHash),
      updatedAt: Value(DateTime.now()),
    );
    await _db.into(_db.storeSettings).insertOnConflictUpdate(companion);
  }

  Future<StoreSetting> _ensureLocalAuthSettings() async {
    final existing = await (_db.select(_db.storeSettings)
          ..where((t) => t.isDeleted.equals(false))
          ..orderBy([(t) => OrderingTerm.desc(t.updatedAt)])
          ..limit(1))
        .getSingleOrNull();
    if (existing != null) return existing;

    final id = await _db.into(_db.storeSettings).insert(StoreSettingsCompanion.insert(
          ownerPinHash: Value(_hashPin(_defaultOwnerPin)),
          technicianPinHash: Value(_hashPin(_defaultTechnicianPin)),
        ));
    return (_db.select(_db.storeSettings)..where((t) => t.id.equals(id))).getSingle();
  }

  Future<void> _saveSession(AuthSession session) async {
    final preferences = await _preferencesFuture;
    await preferences.setString(_sessionKey, jsonEncode(session.toJson()));
  }

  String _hashPin(String pin) => base64Url.encode(utf8.encode('gank:$pin'));
}

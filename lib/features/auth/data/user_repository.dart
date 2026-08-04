import 'package:drift/drift.dart';

import '../../../core/database/app_database.dart';

class UserRepository {
  UserRepository(this._db);

  final AppDatabase _db;

  Future<List<AppUser>> listUsers({bool includeDeleted = false}) {
    final query = _db.select(_db.appUsers)..orderBy([(t) => OrderingTerm.asc(t.name)]);
    if (!includeDeleted) query.where((t) => t.isDeleted.equals(false));
    return query.get();
  }

  Stream<List<AppUser>> watchUsers({bool includeDeleted = false}) {
    final query = _db.select(_db.appUsers)..orderBy([(t) => OrderingTerm.asc(t.name)]);
    if (!includeDeleted) query.where((t) => t.isDeleted.equals(false));
    return query.watch();
  }

  Future<int> upsertUser(AppUsersCompanion user) => _db.into(_db.appUsers).insertOnConflictUpdate(user);

  Future<int> softDeleteUser(int id) => (_db.update(_db.appUsers)..where((t) => t.id.equals(id))).write(AppUsersCompanion(isDeleted: const Value(true), deletedAt: Value(DateTime.now()), updatedAt: Value(DateTime.now())));
}

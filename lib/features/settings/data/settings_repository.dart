import 'package:drift/drift.dart';

import '../../../core/database/app_database.dart';

class SettingsRepository {
  SettingsRepository(this._db);

  final AppDatabase _db;

  Future<StoreSetting?> getSettings({bool includeDeleted = false}) {
    final query = _db.select(_db.storeSettings)..orderBy([(t) => OrderingTerm.desc(t.updatedAt)])..limit(1);
    if (!includeDeleted) query.where((t) => t.isDeleted.equals(false));
    return query.getSingleOrNull();
  }

  Stream<StoreSetting?> watchSettings({bool includeDeleted = false}) {
    final query = _db.select(_db.storeSettings)..orderBy([(t) => OrderingTerm.desc(t.updatedAt)])..limit(1);
    if (!includeDeleted) query.where((t) => t.isDeleted.equals(false));
    return query.watchSingleOrNull();
  }

  Future<int> upsertSettings(StoreSettingsCompanion settings) => _db.into(_db.storeSettings).insertOnConflictUpdate(settings);
}

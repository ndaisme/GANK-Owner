import 'package:drift/drift.dart';

import '../../../core/database/app_database.dart';

class InventoryRepository {
  InventoryRepository(this._db);

  final AppDatabase _db;

  Future<List<InventoryItem>> listItems({bool includeDeleted = false}) {
    final query = _db.select(_db.inventoryItems)..orderBy([(t) => OrderingTerm.asc(t.name)]);
    if (!includeDeleted) query.where((t) => t.isDeleted.equals(false));
    return query.get();
  }

  Stream<List<InventoryItem>> watchItems({bool includeDeleted = false}) {
    final query = _db.select(_db.inventoryItems)..orderBy([(t) => OrderingTerm.asc(t.name)]);
    if (!includeDeleted) query.where((t) => t.isDeleted.equals(false));
    return query.watch();
  }

  Future<int> upsertItem(InventoryItemsCompanion item) => _db.into(_db.inventoryItems).insertOnConflictUpdate(item);

  Future<int> softDeleteItem(int id) => (_db.update(_db.inventoryItems)..where((t) => t.id.equals(id))).write(InventoryItemsCompanion(isDeleted: const Value(true), deletedAt: Value(DateTime.now()), updatedAt: Value(DateTime.now())));
}

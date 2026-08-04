import '../../../core/database/app_database.dart';

class DashboardRepository {
  DashboardRepository(this._db);

  final AppDatabase _db;

  Future<int> countActiveServices() => (_db.selectOnly(_db.serviceOrders)..where(_db.serviceOrders.isDeleted.equals(false))..addColumns([_db.serviceOrders.id.count()])).map((row) => row.read(_db.serviceOrders.id.count()) ?? 0).getSingle();

  Future<int> countLowStockItems() => (_db.selectOnly(_db.inventoryItems)..where(_db.inventoryItems.isDeleted.equals(false) & _db.inventoryItems.stock.isSmallerOrEqual(_db.inventoryItems.minStock))..addColumns([_db.inventoryItems.id.count()])).map((row) => row.read(_db.inventoryItems.id.count()) ?? 0).getSingle();
}

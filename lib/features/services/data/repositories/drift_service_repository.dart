import 'package:drift/drift.dart';

import '../../../../core/database/app_database.dart';
import '../../domain/repositories/service_repository.dart';

class DriftServiceRepository implements ServiceRepository {
  DriftServiceRepository(this._db);

  final AppDatabase _db;

  Future<List<ServiceOrder>> listServices({bool includeDeleted = false}) {
    final query = _db.select(_db.serviceOrders)
      ..orderBy([(t) => OrderingTerm.desc(t.createdAt)]);
    if (!includeDeleted) {
      query.where((t) => t.isDeleted.equals(false));
    }
    return query.get();
  }

  Stream<List<ServiceOrder>> watchServices({bool includeDeleted = false}) {
    final query = _db.select(_db.serviceOrders)
      ..orderBy([(t) => OrderingTerm.desc(t.createdAt)]);
    if (!includeDeleted) {
      query.where((t) => t.isDeleted.equals(false));
    }
    return query.watch();
  }

  Future<int> upsertService(ServiceOrdersCompanion service) => _db.into(_db.serviceOrders).insertOnConflictUpdate(service);

  Future<int> softDeleteService(int id) => (_db.update(_db.serviceOrders)..where((t) => t.id.equals(id))).write(ServiceOrdersCompanion(isDeleted: const Value(true), deletedAt: Value(DateTime.now()), updatedAt: Value(DateTime.now())));

  Future<List<ServiceStatusTimelineData>> listTimeline(int serviceId, {bool includeDeleted = false}) {
    final query = _db.select(_db.serviceStatusTimeline)
      ..where((t) => t.serviceId.equals(serviceId))
      ..orderBy([(t) => OrderingTerm.asc(t.createdAt)]);
    if (!includeDeleted) {
      query.where((t) => t.isDeleted.equals(false));
    }
    return query.get();
  }

  Future<int> addTimeline(ServiceStatusTimelineCompanion entry) => _db.into(_db.serviceStatusTimeline).insert(entry);

  Future<ServiceInitialCheck?> getInitialCheck(int serviceId) => (_db.select(_db.serviceInitialChecks)
        ..where((t) => t.serviceId.equals(serviceId) & t.isDeleted.equals(false)))
      .getSingleOrNull();

  Future<int> upsertInitialCheck(ServiceInitialChecksCompanion check) => _db.into(_db.serviceInitialChecks).insertOnConflictUpdate(check);

  Future<ServiceFinalCheck?> getFinalCheck(int serviceId) => (_db.select(_db.serviceFinalChecks)
        ..where((t) => t.serviceId.equals(serviceId) & t.isDeleted.equals(false)))
      .getSingleOrNull();

  Future<int> upsertFinalCheck(ServiceFinalChecksCompanion check) => _db.into(_db.serviceFinalChecks).insertOnConflictUpdate(check);
}

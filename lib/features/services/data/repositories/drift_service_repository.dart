import 'package:drift/drift.dart';

import '../../../../core/database/app_database.dart';
import '../../domain/repositories/service_repository.dart';

class DriftServiceRepository implements ServiceRepository {
  DriftServiceRepository(this._db);

  final AppDatabase _db;

  Future<List<ServiceOrder>> listServices({bool includeDeleted = false, String search = '', String? status, bool newestFirst = true}) {
    final query = _db.select(_db.serviceOrders)
      ..orderBy([(t) => newestFirst ? OrderingTerm.desc(t.createdAt) : OrderingTerm.asc(t.createdAt)]);
    if (search.trim().isNotEmpty) {
      final keyword = '%${search.trim()}%';
      query.where((t) => t.serviceNumber.like(keyword) | t.customerName.like(keyword) | t.customerPhone.like(keyword) | t.deviceModel.like(keyword) | t.complaint.like(keyword));
    }
    if (status != null && status.isNotEmpty) {
      query.where((t) => t.status.equals(status));
    }
    if (!includeDeleted) {
      query.where((t) => t.isDeleted.equals(false));
    }
    return query.get();
  }

  Stream<List<ServiceOrder>> watchServices({bool includeDeleted = false, String search = '', String? status, bool newestFirst = true}) {
    final query = _db.select(_db.serviceOrders)
      ..orderBy([(t) => newestFirst ? OrderingTerm.desc(t.createdAt) : OrderingTerm.asc(t.createdAt)]);
    if (search.trim().isNotEmpty) {
      final keyword = '%${search.trim()}%';
      query.where((t) => t.serviceNumber.like(keyword) | t.customerName.like(keyword) | t.customerPhone.like(keyword) | t.deviceModel.like(keyword) | t.complaint.like(keyword));
    }
    if (status != null && status.isNotEmpty) {
      query.where((t) => t.status.equals(status));
    }
    if (!includeDeleted) {
      query.where((t) => t.isDeleted.equals(false));
    }
    return query.watch();
  }

  Future<int> upsertService(ServiceOrdersCompanion service) => _db.into(_db.serviceOrders).insertOnConflictUpdate(service);

  Future<int> softDeleteService(int id) => (_db.update(_db.serviceOrders)..where((t) => t.id.equals(id))).write(ServiceOrdersCompanion(isDeleted: const Value(true), deletedAt: Value(DateTime.now()), updatedAt: Value(DateTime.now())));

  Future<List<ServiceStatusTimelineData>> listTimeline(int serviceId, {bool includeDeleted = false}) {
    final query = _db.select(_db.serviceStatusTimelines)
      ..where((t) => t.serviceId.equals(serviceId))
      ..orderBy([(t) => OrderingTerm.asc(t.createdAt)]);
    if (!includeDeleted) {
      query.where((t) => t.isDeleted.equals(false));
    }
    return query.get();
  }

  Future<int> addTimeline(ServiceStatusTimelinesCompanion entry) => _db.into(_db.serviceStatusTimelines).insert(entry);

  Future<ServiceInitialCheck?> getInitialCheck(int serviceId) => (_db.select(_db.serviceInitialChecks)
        ..where((t) => t.serviceId.equals(serviceId) & t.isDeleted.equals(false)))
      .getSingleOrNull();

  Future<int> upsertInitialCheck(ServiceInitialChecksCompanion check) => _db.into(_db.serviceInitialChecks).insertOnConflictUpdate(check);

  Future<ServiceFinalCheck?> getFinalCheck(int serviceId) => (_db.select(_db.serviceFinalChecks)
        ..where((t) => t.serviceId.equals(serviceId) & t.isDeleted.equals(false)))
      .getSingleOrNull();

  Future<int> upsertFinalCheck(ServiceFinalChecksCompanion check) => _db.into(_db.serviceFinalChecks).insertOnConflictUpdate(check);
}

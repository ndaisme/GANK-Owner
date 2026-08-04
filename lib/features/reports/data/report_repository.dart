import 'package:drift/drift.dart';

import '../../../core/database/app_database.dart';

class ReportRepository {
  ReportRepository(this._db);

  final AppDatabase _db;

  Future<List<Payment>> listPaymentsBetween(DateTime start, DateTime end, {bool includeDeleted = false}) {
    final query = _db.select(_db.payments)
      ..where((t) => t.timestamp.isBiggerOrEqualValue(start) & t.timestamp.isSmallerThanValue(end))
      ..orderBy([(t) => OrderingTerm.desc(t.timestamp)]);
    if (!includeDeleted) query.where((t) => t.isDeleted.equals(false));
    return query.get();
  }
}

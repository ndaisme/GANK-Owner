import 'package:drift/drift.dart';

import '../../../../core/database/app_database.dart';
import '../../domain/repositories/payment_repository.dart';

class DriftPaymentRepository implements PaymentRepository {
  DriftPaymentRepository(this._db);

  final AppDatabase _db;

  Future<List<Payment>> listPayments({bool includeDeleted = false}) {
    final query = _db.select(_db.payments)..orderBy([(t) => OrderingTerm.desc(t.timestamp)]);
    if (!includeDeleted) query.where((t) => t.isDeleted.equals(false));
    return query.get();
  }

  Stream<List<Payment>> watchPayments({bool includeDeleted = false}) {
    final query = _db.select(_db.payments)..orderBy([(t) => OrderingTerm.desc(t.timestamp)]);
    if (!includeDeleted) query.where((t) => t.isDeleted.equals(false));
    return query.watch();
  }

  Future<int> upsertPayment(PaymentsCompanion payment) => _db.into(_db.payments).insertOnConflictUpdate(payment);

  Future<int> softDeletePayment(int id) => (_db.update(_db.payments)..where((t) => t.id.equals(id))).write(PaymentsCompanion(isDeleted: const Value(true), deletedAt: Value(DateTime.now()), updatedAt: Value(DateTime.now())));
}

import 'package:drift/drift.dart';

import '../../../../core/database/app_database.dart';
import '../../domain/repositories/customer_repository.dart';

class DriftCustomerRepository implements CustomerRepository {
  DriftCustomerRepository(this._db);

  final AppDatabase _db;

  Future<List<Customer>> listCustomers({bool includeDeleted = false}) {
    final query = _db.select(_db.customers)..orderBy([(t) => OrderingTerm.asc(t.name)]);
    if (!includeDeleted) query.where((t) => t.isDeleted.equals(false));
    return query.get();
  }

  Stream<List<Customer>> watchCustomers({bool includeDeleted = false}) {
    final query = _db.select(_db.customers)..orderBy([(t) => OrderingTerm.asc(t.name)]);
    if (!includeDeleted) query.where((t) => t.isDeleted.equals(false));
    return query.watch();
  }

  Future<int> upsertCustomer(CustomersCompanion customer) => _db.into(_db.customers).insertOnConflictUpdate(customer);

  Future<int> softDeleteCustomer(int id) => (_db.update(_db.customers)..where((t) => t.id.equals(id))).write(CustomersCompanion(isDeleted: const Value(true), deletedAt: Value(DateTime.now()), updatedAt: Value(DateTime.now())));
}

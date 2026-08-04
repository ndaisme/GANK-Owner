import 'package:drift/drift.dart';
import 'package:drift_flutter/drift_flutter.dart';

part 'app_database.g.dart';

mixin SoftDeleteColumns on Table {
  BoolColumn get isDeleted => boolean().withDefault(const Constant(false))();
  DateTimeColumn get deletedAt => dateTime().nullable()();
}

class ServiceOrders extends Table with SoftDeleteColumns {
  IntColumn get id => integer().autoIncrement()();
  TextColumn get serviceNumber => text().unique()();
  TextColumn get customerName => text()();
  TextColumn get customerPhone => text()();
  TextColumn get deviceModel => text()();
  TextColumn get imei => text().withDefault(const Constant(''))();
  TextColumn get complaint => text()();
  TextColumn get diagnosis => text().withDefault(const Constant(''))();
  RealColumn get estimatedCost => real().withDefault(const Constant(0))();
  RealColumn get downPayment => real().withDefault(const Constant(0))();
  TextColumn get status => text().withDefault(const Constant('CHECK_IN'))();
  TextColumn get technicianName => text().withDefault(const Constant('Teknisi Utama'))();
  IntColumn get warrantyDays => integer().withDefault(const Constant(30))();
  DateTimeColumn get createdAt => dateTime().clientDefault(DateTime.now)();
  DateTimeColumn get updatedAt => dateTime().clientDefault(DateTime.now)();
  RealColumn get capitalCost => real().withDefault(const Constant(0))();
}

class Customers extends Table with SoftDeleteColumns {
  IntColumn get id => integer().autoIncrement()();
  TextColumn get name => text()();
  TextColumn get phone => text().unique()();
  TextColumn get address => text().withDefault(const Constant(''))();
  IntColumn get totalServices => integer().withDefault(const Constant(1))();
  RealColumn get totalSpending => real().withDefault(const Constant(0))();
  DateTimeColumn get createdAt => dateTime().clientDefault(DateTime.now)();
  DateTimeColumn get updatedAt => dateTime().clientDefault(DateTime.now)();
}

class InventoryItems extends Table with SoftDeleteColumns {
  IntColumn get id => integer().autoIncrement()();
  TextColumn get barcode => text().unique()();
  TextColumn get name => text()();
  TextColumn get category => text()();
  IntColumn get stock => integer()();
  IntColumn get minStock => integer().withDefault(const Constant(5))();
  RealColumn get purchasePrice => real()();
  RealColumn get sellingPrice => real()();
  TextColumn get rackLocation => text().withDefault(const Constant('Rak A-1'))();
  DateTimeColumn get createdAt => dateTime().clientDefault(DateTime.now)();
  DateTimeColumn get updatedAt => dateTime().clientDefault(DateTime.now)();
}

class Payments extends Table with SoftDeleteColumns {
  IntColumn get id => integer().autoIncrement()();
  IntColumn get serviceId => integer().nullable().references(ServiceOrders, #id)();
  TextColumn get type => text()();
  TextColumn get category => text()();
  RealColumn get amount => real()();
  TextColumn get description => text()();
  DateTimeColumn get timestamp => dateTime().clientDefault(DateTime.now)();
  DateTimeColumn get updatedAt => dateTime().clientDefault(DateTime.now)();
}

class ServiceStatusTimelines extends Table with SoftDeleteColumns {
  IntColumn get id => integer().autoIncrement()();
  IntColumn get serviceId => integer().references(ServiceOrders, #id)();
  TextColumn get status => text()();
  TextColumn get note => text().withDefault(const Constant(''))();
  TextColumn get actorName => text().withDefault(const Constant(''))();
  DateTimeColumn get createdAt => dateTime().clientDefault(DateTime.now)();
}

class ServiceInitialChecks extends Table with SoftDeleteColumns {
  IntColumn get id => integer().autoIncrement()();
  IntColumn get serviceId => integer().unique().references(ServiceOrders, #id)();
  BoolColumn get powerOn => boolean().withDefault(const Constant(false))();
  BoolColumn get screenOk => boolean().withDefault(const Constant(false))();
  BoolColumn get cameraOk => boolean().withDefault(const Constant(false))();
  BoolColumn get speakerOk => boolean().withDefault(const Constant(false))();
  BoolColumn get microphoneOk => boolean().withDefault(const Constant(false))();
  BoolColumn get chargingOk => boolean().withDefault(const Constant(false))();
  TextColumn get notes => text().withDefault(const Constant(''))();
  DateTimeColumn get checkedAt => dateTime().clientDefault(DateTime.now)();
}

class ServiceFinalChecks extends Table with SoftDeleteColumns {
  IntColumn get id => integer().autoIncrement()();
  IntColumn get serviceId => integer().unique().references(ServiceOrders, #id)();
  BoolColumn get powerOn => boolean().withDefault(const Constant(false))();
  BoolColumn get screenOk => boolean().withDefault(const Constant(false))();
  BoolColumn get cameraOk => boolean().withDefault(const Constant(false))();
  BoolColumn get speakerOk => boolean().withDefault(const Constant(false))();
  BoolColumn get microphoneOk => boolean().withDefault(const Constant(false))();
  BoolColumn get chargingOk => boolean().withDefault(const Constant(false))();
  TextColumn get notes => text().withDefault(const Constant(''))();
  DateTimeColumn get checkedAt => dateTime().clientDefault(DateTime.now)();
}

class StoreSettings extends Table with SoftDeleteColumns {
  IntColumn get id => integer().autoIncrement()();
  TextColumn get storeName => text().withDefault(const Constant('GANK Service'))();
  TextColumn get phone => text().withDefault(const Constant(''))();
  TextColumn get address => text().withDefault(const Constant(''))();
  TextColumn get receiptFooter => text().withDefault(const Constant('Terima kasih'))();
  TextColumn get ownerPinHash => text().withDefault(const Constant('Z2FuazoxMjM0NTY='))();
  TextColumn get technicianPinHash => text().withDefault(const Constant('Z2FuazowMDAw'))();
  IntColumn get defaultWarrantyDays => integer().withDefault(const Constant(30))();
  DateTimeColumn get updatedAt => dateTime().clientDefault(DateTime.now)();
}

class AppUsers extends Table with SoftDeleteColumns {
  IntColumn get id => integer().autoIncrement()();
  TextColumn get name => text()();
  TextColumn get role => text().withDefault(const Constant('owner'))();
  TextColumn get pinHash => text()();
  BoolColumn get isPinEnabled => boolean().withDefault(const Constant(true))();
  DateTimeColumn get createdAt => dateTime().clientDefault(DateTime.now)();
  DateTimeColumn get updatedAt => dateTime().clientDefault(DateTime.now)();
}

@DriftDatabase(tables: [ServiceOrders, Customers, InventoryItems, Payments, ServiceStatusTimelines, ServiceInitialChecks, ServiceFinalChecks, StoreSettings, AppUsers])
class AppDatabase extends _$AppDatabase {
  AppDatabase([QueryExecutor? executor]) : super(executor ?? openGankDatabase());

  @override
  int get schemaVersion => 3;

  @override
  MigrationStrategy get migration => MigrationStrategy(
        onUpgrade: (m, from, to) async {
          if (from < 2) {
            await m.addColumn(storeSettings, storeSettings.ownerPinHash);
            await m.addColumn(storeSettings, storeSettings.technicianPinHash);
          }
          if (from < 3) {
            await m.createTable(serviceStatusTimelines);
          }
        },
      );
}

QueryExecutor openGankDatabase() => driftDatabase(name: 'gank_owner');

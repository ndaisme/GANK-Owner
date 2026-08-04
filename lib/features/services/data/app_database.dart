import 'package:drift/drift.dart';
import 'package:drift_flutter/drift_flutter.dart';

class ServiceOrders extends Table {
  IntColumn get id => integer().autoIncrement()();
  TextColumn get number => text()();
  TextColumn get customerName => text()();
  TextColumn get customerPhone => text()();
  TextColumn get deviceModel => text()();
  TextColumn get complaint => text()();
  RealColumn get estimatedCost => real().withDefault(const Constant(0))();
  TextColumn get status => text()();
  DateTimeColumn get createdAt => dateTime()();
}

QueryExecutor openGankDatabase() => driftDatabase(name: 'gank_owner');

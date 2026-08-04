import '../entities/service_order.dart';

enum ServiceSortOption { newest, oldest, numberAsc, numberDesc, customerAsc, statusAsc }

class ServiceQuery {
  const ServiceQuery({this.search = '', this.status, this.sort = ServiceSortOption.newest, this.includeDeleted = false});

  final String search;
  final ServiceStatus? status;
  final ServiceSortOption sort;
  final bool includeDeleted;

  ServiceQuery copyWith({String? search, ServiceStatus? status, bool clearStatus = false, ServiceSortOption? sort, bool? includeDeleted}) => ServiceQuery(
        search: search ?? this.search,
        status: clearStatus ? null : status ?? this.status,
        sort: sort ?? this.sort,
        includeDeleted: includeDeleted ?? this.includeDeleted,
      );
}

class FilterServicesUseCase {
  const FilterServicesUseCase();

  List<ServiceOrder> call(List<ServiceOrder> services, ServiceQuery query) {
    final keyword = query.search.trim().toLowerCase();
    final filtered = services.where((service) {
      if (!query.includeDeleted && service.isDeleted) return false;
      if (query.status != null && service.status != query.status) return false;
      if (keyword.isEmpty) return true;
      return [service.number, service.customerName, service.customerPhone, service.deviceModel, service.complaint].any((value) => value.toLowerCase().contains(keyword));
    }).toList();

    filtered.sort((a, b) => switch (query.sort) {
          ServiceSortOption.newest => b.createdAt.compareTo(a.createdAt),
          ServiceSortOption.oldest => a.createdAt.compareTo(b.createdAt),
          ServiceSortOption.numberAsc => a.number.compareTo(b.number),
          ServiceSortOption.numberDesc => b.number.compareTo(a.number),
          ServiceSortOption.customerAsc => a.customerName.compareTo(b.customerName),
          ServiceSortOption.statusAsc => a.status.index.compareTo(b.status.index),
        });
    return filtered;
  }
}

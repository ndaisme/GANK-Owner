class Customer {
  const Customer({required this.id, required this.name, required this.phone, this.address = '', this.totalServices = 1, this.totalSpending = 0});
  final int id;
  final String name;
  final String phone;
  final String address;
  final int totalServices;
  final double totalSpending;
}

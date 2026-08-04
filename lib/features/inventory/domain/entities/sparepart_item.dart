class SparepartItem {
  const SparepartItem({required this.id, required this.barcode, required this.name, required this.category, required this.stock, required this.minStock, required this.purchasePrice, required this.sellingPrice, required this.rackLocation});
  final int id;
  final String barcode;
  final String name;
  final String category;
  final int stock;
  final int minStock;
  final double purchasePrice;
  final double sellingPrice;
  final String rackLocation;
  bool get isLowStock => stock <= minStock;
  SparepartItem copyWith({int? stock}) => SparepartItem(id: id, barcode: barcode, name: name, category: category, stock: stock ?? this.stock, minStock: minStock, purchasePrice: purchasePrice, sellingPrice: sellingPrice, rackLocation: rackLocation);
}

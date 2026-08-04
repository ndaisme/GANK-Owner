class GenerateServiceNumber {
  const GenerateServiceNumber();

  String call({required DateTime date, required int sequence}) {
    final year = date.year.toString().padLeft(4, '0');
    final number = sequence.toString().padLeft(3, '0');
    return 'GS-$year-$number';
  }
}

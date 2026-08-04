class GenerateServiceNumberUseCase {
  const GenerateServiceNumberUseCase();

  String call({required DateTime localDate, required int dailySequence}) {
    final year = localDate.year.toString().padLeft(4, '0');
    final month = localDate.month.toString().padLeft(2, '0');
    final day = localDate.day.toString().padLeft(2, '0');
    final number = dailySequence.toString().padLeft(3, '0');
    return 'SV-$year$month$day-$number';
  }
}

@Deprecated('Use GenerateServiceNumberUseCase instead.')
typedef GenerateServiceNumber = GenerateServiceNumberUseCase;

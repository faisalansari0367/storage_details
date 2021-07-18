import 'dart:io';

class Storage {
  final int free;
  final int total;
  final int used;
  final String path;

  Storage({
    required this.path,
    required this.used,
    required this.free,
    required this.total,
  });

  static List<Storage> fromMap(List list) {
    return List.generate(list.length, (index) {
      final Directory dir = Directory(list[index]['path']);
      final String path = dir.parent.parent.parent.parent.path;
      final data = list[index];
      return Storage(
        total: int.parse(data['total']),
        free: int.parse(data['free']),
        used: int.parse(data['total']) - int.parse(data['free']),
        path: path,
      );
    });
  }
}

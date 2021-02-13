
import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

class StorageDetails {
  static const MethodChannel _channel = const MethodChannel('storage_details');
  static const String _getStorageDetails = 'getStorageDetails';

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
  /// this method provides you the path of internal and removable sd card and it also provides the used, free, available space
  static Future<List<Storage>> get getspace async {
    var data;
    try {
      data = await _channel.invokeMethod(_getStorageDetails);
    } catch (e) {
      throw Exception(e);
    }
    final List<Storage> storage = Storage.fromMap(data);
    return storage;
  }


}


class Storage {
  final int free;
  final int total;
  final int used;
  final String path;
  Storage({this.path, this.used, this.free, this.total});

  static List<Storage> fromMap(List list) {
    return List.generate(list.length, (index) {
      final Directory dir = Directory(list[index]['path']);
      final String path = dir.parent.parent.parent.parent.path;
      return Storage(
        total: int.parse(list[index]['total']),
        free: int.parse(list[index]['free']),
        used: int.parse(list[index]['total']) - int.parse(list[index]['free']),
        path: path,
      );
    });
  }
}

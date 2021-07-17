import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

class StorageDetails {
  static const MethodChannel _channel = const MethodChannel('storage_details');
  static const String _getStorageDetails = 'getStorageDetails';
  static const EventChannel _eventChannel = const EventChannel('event_channel');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  /// this method provides you the path of internal and removable sd card and it also provides the used, free, available space
  static Future<List<Storage>> get getspace async {
    try {
      final object = await _channel.invokeMethod(_getStorageDetails);
      final List<Storage> storage = Storage.fromMap(object);
      return storage;
    } catch (e) {
      throw Exception(e);
    }
  }

  static Future<bool?> deleteWhenError(List<String> paths) async {
    try {
      var result = await _channel.invokeMethod(
        'deleteWhenError',
        <String, List<String>>{'paths': paths},
      );
      return result;
    } catch (e) {
      throw Exception(e);
    }
  }

  static Stream<dynamic> get watchFilesForChanges async* {
    await for (var event in _eventChannel.receiveBroadcastStream()) {
      yield event;
    }
  }
}

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

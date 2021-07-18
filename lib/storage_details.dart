import 'dart:async';

import 'package:flutter/services.dart';
import 'package:storage_details/storage.dart';
export '../storage.dart';

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

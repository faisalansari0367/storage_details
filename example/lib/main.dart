import 'package:flutter/material.dart';
import 'dart:async';

import 'package:storage_details/storage_details.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  List<Storage> data = [];

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    final _data = await StorageDetails.getspace;
    data = _data;
    if (!mounted) return;
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: ListView.builder(
            itemCount: data.length,
            itemBuilder: (context, index) {
              final storage = data[index];
              return Column(
                children: [
                  Text("Path ${storage.path}"),
                  Text("Free space in Bytes ${storage.free}"),
                  Text("Total space in Bytes ${storage.total}"),
                  Text("Used space in Bytes  ${storage.used}"),
                ],
              );
            },
          ),
        ),
      ),
    );
  }
}

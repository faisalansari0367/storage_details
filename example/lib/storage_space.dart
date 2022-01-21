import 'package:flutter/material.dart';
import 'package:storage_details/storage_details.dart';

class MyStorageSpace extends StatefulWidget {
  const MyStorageSpace({Key key}) : super(key: key);

  @override
  State<MyStorageSpace> createState() => _MyStorageSpaceState();
}

class _MyStorageSpaceState extends State<MyStorageSpace> {
  List<Storage> data = [];

  @override
  void initState() {
    _init();
    super.initState();
  }

  void _init() async {
    final _data = await StorageDetails.getspace;
    data = _data;
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
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
    );
  }
}

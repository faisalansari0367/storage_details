import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:storage_details/storage_details.dart';

class MyVideosFiles extends StatefulWidget {
  const MyVideosFiles({Key key}) : super(key: key);

  @override
  _MyVideosFilesState createState() => _MyVideosFilesState();
}

class _MyVideosFilesState extends State<MyVideosFiles> {
  List data = [];
  @override
  void initState() {
    _init();
    super.initState();
  }

  void _init() async {
    await Permission.storage.request();
    final videos = await StorageDetails.getVideos();
    data = videos;
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: data.length,
      itemBuilder: (BuildContext context, int index) {
        final item = data.elementAt(index);
        return ListTile(
          leading: Image.memory(item['thumbnail']),
          title: Text(item['name']),
          subtitle: Text(item['size']),
          trailing: Text(item['resolution']),
        );
      },
    );
  }
}

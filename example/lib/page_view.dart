import 'package:flutter/material.dart';
import 'package:storage_details_example/storage_space.dart';
import 'package:storage_details_example/videos_files.dart';

class MyPageView extends StatelessWidget {
  const MyPageView({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return PageView(
      children: [
        MyStorageSpace(),
        MyVideosFiles(),
      ],
    );
  }
}

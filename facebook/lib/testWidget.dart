
import 'package:facebook/toanCuc.dart';
import 'package:flutter/material.dart';

class TestWidget extends StatefulWidget{
  const TestWidget({Key? key}): super(key: key);

  @override
  State<TestWidget> createState() => TestWidgetState();
}

class TestWidgetState extends State<TestWidget>{
  String data = 'test123';

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: StreamBuilder(
        stream: Global.myStream!.testStream,
        builder: (context, snapshot) => Text(
          snapshot.hasData ? snapshot.data.toString() : "0",
          style: Theme.of(context).textTheme.headline4,
        ),
      ),
    );
  }
}
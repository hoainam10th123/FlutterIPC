import 'dart:async';
import 'dart:math';

import 'package:facebook/testWidget.dart';
import 'package:facebook/toanCuc.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:localstorage/localstorage.dart';

import 'LifecycleEventHandler.dart';
import 'MyStream.dart';
import 'controllers/testController.dart';
import 'package:get/get.dart';

void main() {
  Global.myStream = MyStream();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      key: ValueKey('locale.name'),
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.orange,
      ),
      home: const MyHomePage(title: 'Flutter Demo Facebook'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage>{
  static const platform = MethodChannel('app.server.facebook');
  String data = 'Test';
  String temp = '';


  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    platform.setMethodCallHandler(didRecieveTranscript);
  }

  Future<void> didRecieveTranscript(MethodCall call) async {
    final String utterance = call.arguments;
    await Future.delayed(const Duration(seconds: 1));
    print('data = $utterance');
    callA(utterance);
  }

  void callA(String value){
    Global.myStream!.send(value);
  }

  @override
  Widget build(BuildContext context) {
    print("build run");
    return Scaffold(
      appBar: AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          children: [
            TestWidget()
          ],
        ),
      ),
    );
  }
}

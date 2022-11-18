
import 'dart:async';

class MyStream {

  StreamController testController = StreamController<String>.broadcast();
  Stream get testStream => testController.stream;

  void send(String value){
    testController.sink.add(value);
  }

}
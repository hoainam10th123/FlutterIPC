import 'package:get/get.dart';

class TestController extends GetxController {
  var data = 'test'.obs;

  updateData(String dat){
    data.value = dat;
  }
}
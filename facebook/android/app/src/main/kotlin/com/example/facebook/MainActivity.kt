package com.example.facebook


import android.os.Bundle
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterActivity() {
    val CHANNEL = "app.server.facebook"
    var data = ""
    var _channel: MethodChannel? = null

    override fun onStart() {
        super.onStart()
        val client = RecentClient.client

        if(client != null){
            Log.i("app.server.data", client!!.clientData.toString())
            Log.i("app.server.ProcessId", client!!.clientProcessId.toString())
            Log.i("app.server.ipcMethod", client!!.ipcMethod)
            data = client?.clientData.toString()

            _channel?.let { it.invokeMethod("methodFromKoltin", data) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var flutterEngine = FlutterEngine(this)

        flutterEngine
            .dartExecutor
            .executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )

        var channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
        _channel = channel
    }
}

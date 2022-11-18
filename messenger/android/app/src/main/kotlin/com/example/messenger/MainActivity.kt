package com.example.messenger


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import io.flutter.embedding.android.FlutterActivity
import androidx.annotation.NonNull
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity: FlutterActivity(), ServiceConnection {

    // Is bound to the service of remote process
    private var isBound: Boolean = false

    // Messenger on the server
    private var serverMessenger: Messenger? = null

    // Messenger on the client
    private var clientMessenger: Messenger? = null

    // Handle messages from the remote service
    var handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            // Update UI with remote process info
            val bundle = msg.data
            /*viewBinding.linearLayoutClientInfo.visibility = View.VISIBLE
            viewBinding.btnConnect.text = getString(R.string.disconnect)
            viewBinding.txtServerPid.text = bundle.getInt(PID).toString()
            viewBinding.txtServerConnectionCount.text = bundle.getInt(CONNECTION_COUNT).toString()*/
        }
    }

    private val CHANNEL = "app.client.messenger"
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
                call, result ->
            if (call.method == "sendData") {
                if(isBound){
                    doUnbindService()
                } else {
                    doBindService()
                }
            } else {
                result.notImplemented()
            }
        }

    }

    private fun doBindService() {
        clientMessenger = Messenger(handler)
        Intent("messengerexample").also { intent ->
            intent.`package` = "com.example.facebook"
            activity?.applicationContext?.bindService(intent, this, Context.BIND_AUTO_CREATE)
        }
        isBound = true
    }

    private fun doUnbindService() {
        if (isBound) {
            activity?.applicationContext?.unbindService(this)
            isBound = false
        }
    }

    override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
        serverMessenger = Messenger(service)
        // Ready to send messages to remote service
        sendMessageToServer()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        serverMessenger = null
    }

    var count = 0

    private fun sendMessageToServer() {
        if (!isBound) return
        val message = Message.obtain(handler)
        val bundle = Bundle()
        bundle.putString(DATA, (++count).toString())
        bundle.putString(PACKAGE_NAME, context?.packageName)
        bundle.putInt(PID, Process.myPid())
        message.data = bundle
        message.replyTo = clientMessenger // we offer our Messenger object for communication to be two-way
        try {
            serverMessenger?.send(message)
        } catch (e: RemoteException) {
            e.printStackTrace()
        } finally {
            message.recycle()
        }
    }


}

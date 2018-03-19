package com.jtao.app.myapplication.mqtt

import android.util.Log
import android.widget.Toast
import com.jtao.app.myapplication.EventBus.MqttConnectionLostEvent
import com.jtao.app.myapplication.ui.HomeActivity
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject

/**
 * Created by taoo on 18-3-18.
 */
class MqttCallbackBus:MqttCallback {
    override fun messageArrived(topic: String?, message: MqttMessage?) {
        Log.i("mqtt callback", "$topic:${message.toString()}")
        val json = JSONObject().put("topic", topic).put("message", message)
        EventBus.getDefault().post(json)
    }

    override fun connectionLost(cause: Throwable) {
        Log.i("mqtt callback", cause.message)
        EventBus.getDefault().post(MqttConnectionLostEvent(cause.message!!))
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {

    }
}
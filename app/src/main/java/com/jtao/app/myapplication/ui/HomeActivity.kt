package com.jtao.app.myapplication.ui

import android.annotation.SuppressLint
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.jtao.app.myapplication.DemoActivity
import com.jtao.app.myapplication.DemoApplication
import com.jtao.app.myapplication.EventBus.MqttConnectionLostEvent
import com.jtao.app.myapplication.R
import com.jtao.app.myapplication.mqtt.MqttManager
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by taoo on 18-3-17.
 */
class HomeActivity:DemoActivity() {
    @Inject
    lateinit var locationManager:LocationManager

    var url = "tcp://111.230.197.145:1883"
    val username = "username"
    val password = "password"
    val clientId = "clientId"
    @SuppressLint("SimpleDateFormat")
    val format = SimpleDateFormat("HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        (application as DemoApplication).component.inject(this)

        set_mqtt_server.hint = url

        Log.i("main0","here")

        button_connect.onClick {
            doAsync {
                Log.d("button_connect","onclick")
                url = if (set_mqtt_server.text.toString() == "") "${set_mqtt_server.hint}" else "${set_mqtt_server.text}"
                Log.i("mqtt url", url)
                MqttManager.getInstance().creatConnect(url, username, password, clientId)
                Log.i("mqtt", "connect")

                uiThread { toast("mqtt connect to $url")}
            }
        }

        button_disconnect.onClick {
            doAsync {
                MqttManager.mInstance?.disConnect()
                Log.i("mqtt", "disconnect ")
                uiThread {toast("mqtt disconnect")}
            }
        }

        button_subscribe.onClick {
            doAsync {
                val topic:String = if (sub_topic.text.toString()=="") getString(R.string.topic_example) else sub_topic.text.toString()
                val b = MqttManager.mInstance?.subscribe(topic, 2)
                Log.i("mqtt", "subscribe $topic, $b")
                uiThread { toast("mqtt subscribe $topic") }
            }
        }

        button_publish.onClick {
            doAsync {
                val topic:String = if (push_topic.text.toString()=="") getString(R.string.topic_example) else push_topic.text.toString()
                val message = if (push_message.text.toString()=="") getString(R.string.push_message_example) else push_message.text.toString()
                val b = MqttManager.mInstance?.publish(topic,2, message.toByteArray())
                Log.i("mqtt", "publish $b")
                uiThread { toast("publish topic: $topic, message: $message") }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onEvent(json:JSONObject){
        doAsync {
            uiThread {
                mqtt_receive.text = "${format.format(Date())} \"${json.get("topic")}\": ${json.get("message")}\r\n${mqtt_receive.text}"
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event:MqttConnectionLostEvent){
        doAsync {
            uiThread {
                toast(event.message)
            }
        }
    }
}
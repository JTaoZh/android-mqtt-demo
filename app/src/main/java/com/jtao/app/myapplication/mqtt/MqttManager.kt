package com.jtao.app.myapplication.mqtt

import android.util.Log
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence
import org.eclipse.paho.client.mqttv3.MqttMessage


/**
 * Created by taoo on 18-3-18.
 */
class MqttManager {
    companion object {
        var mInstance:MqttManager?=null

        fun getInstance(): MqttManager {
            if(null == mInstance){
                mInstance = MqttManager()
            }
            return mInstance as MqttManager
        }

        fun release(){
            try {
                if(mInstance == null){
                    mInstance?.disConnect()
                    mInstance = null
                }
            } catch (e:Exception){}
        }
    }

    var mCallback:MqttCallback = MqttCallbackBus()
    lateinit var conOpt:MqttConnectOptions
    var client:MqttClient?=null
    var clean = true

    @Throws(MqttException::class)
    fun creatConnect(brokerUrl: String, userName: String?, password: String?, clientId: String): Boolean {
        var flag = false
        val tmpDir = System.getProperty("java.io.tmpdir")
        val dataStore = MqttDefaultFilePersistence(tmpDir)


        // Construct the connection options object that contains connection parameters
        // such as cleanSession and LWT
        conOpt = MqttConnectOptions()
        conOpt.mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1_1
        conOpt.isCleanSession = clean
        if (password != null) {
            conOpt.password = password.toCharArray()
        }
        if (userName != null) {
            conOpt.userName = userName
        }

        // Construct an MQTT blocking mode client
        client = MqttClient(brokerUrl, clientId, dataStore)

        // Set this wrapper as the callback handler
        client!!.setCallback(mCallback)
        flag = doConnect()


        return flag
    }

    fun doConnect(): Boolean {
        var flag = false
        if (client != null) {
            try {
                client?.connect(conOpt)
                Log.i("mqtt manager", """Connected to ${client!!.serverURI} with client ID ${client!!.clientId}""")
                flag = true
            } catch (e: Exception) {}
        }
        return flag
    }

    fun publish(topicName: String, qos: Int, payload: ByteArray): Boolean {

        var flag = false

        if (client != null && client!!.isConnected) {

            Log.i("mqtt manager","Publishing to topic \"$topicName\" qos $qos")

            // Create and configure a message
            val message = MqttMessage(payload)
            message.qos = qos

            // Send the message to the server, control is not returned until
            // it has been delivered to the server meeting the specified
            // quality of service.
            try {
                client!!.publish(topicName, message)
                flag = true
            } catch (e: MqttException) {

            }

        }

        return flag
    }

    fun subscribe(topicName: String, qos: Int): Boolean {

        var flag = false

        if (client != null && client!!.isConnected) {
            // Subscribe to the requested topic
            // The QoS specified is the maximum level that messages will be sent to the client at.
            // For instance if QoS 1 is specified, any messages originally published at QoS 2 will
            // be downgraded to 1 when delivering to the client but messages published at 1 and 0
            // will be received at the same level they were published at.
            Log.i("mqtt manager","Subscribing to topic \"$topicName\" qos $qos")
            try {
                client!!.subscribe(topicName, qos)
                flag = true
            } catch (e: MqttException) {

            }
        }

        return flag

    }


    @Throws(MqttException::class)
    fun disConnect() {
        if (client != null && client!!.isConnected) {
            client!!.disconnect()
            client = null
        }
    }
}
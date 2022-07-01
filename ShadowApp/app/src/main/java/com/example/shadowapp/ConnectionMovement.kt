package com.example.shadowapp

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.eclipse.paho.client.mqttv3.*


open class ConnectionMovement : AppCompatActivity() {

        val TAG = "ShadowApp"
        val EXTERNAL_MQTT_BROKER = "aerostun.dev"
        val MQTT_SERVER = "tcp://$EXTERNAL_MQTT_BROKER:1883"
        val THROTTLE_CONTROL = "/smartcar/control/throttle"
        val STEERING_CONTROL = "/smartcar/control/steering"
        val SPEED_TOPIC = "/smartcar/control/speed"
        val TURNING_TOPIC = "/smartcar/control/turning"
        val LIMITER = -1000
        val MOVEMENT_SPEED = 60
        val REVERSE_CAR_MOVEMENT = -1
        val IDLE_SPEED = 0
        val STRAIGHT_ANGLE = 0
        val STEERING_ANGLE = 30
        val QOS = 1
        val IMAGE_WIDTH = 320 //320
        val IMAGE_HEIGHT = 240 //240
        var mMqttClient: MqttClient? = null
        var isConnected = false
        var mCameraView: ImageView? = null
        var JoystickPage: JoystickPage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.controllerpage)
            mMqttClient = MqttClient(applicationContext, MQTT_SERVER, TAG)
            mCameraView = findViewById(R.id.imageView)

            this.JoystickPage = JoystickPage()
        }
        fun forward() {
            drive(MOVEMENT_SPEED, STRAIGHT_ANGLE, "Forward!")
        }
        fun left() {
            drive(MOVEMENT_SPEED, -STEERING_ANGLE, "Lefty!")
        }
        fun stop() {
            drive(IDLE_SPEED, STRAIGHT_ANGLE, "Stopped :(")
        }
        fun right() {
            drive(MOVEMENT_SPEED, STEERING_ANGLE, "Righty!")
        }
        fun reverse() {
            drive(-MOVEMENT_SPEED, STRAIGHT_ANGLE, "Reverse")
        }







        override fun onResume() {

            super.onResume()
            connectToMqttBroker()
        }



        fun connectToMqttBroker() {
            if (!isConnected)
            {
                mMqttClient?.connect(TAG, "", object: IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken) {
                        isConnected = true
                        val successfulConnection = "Connected to MQTT broker"
                        Log.i(TAG, successfulConnection)
                        Toast.makeText(applicationContext, successfulConnection, Toast.LENGTH_SHORT)?.show()
                        mMqttClient?.subscribe("/smartcar/ultrasound/front", QOS, null)
                        mMqttClient?.subscribe("/smartcar/camera", QOS, null)
                    }

                    override fun onFailure(asyncActionToken: IMqttToken, exception:Throwable) {
                        val failedConnection = "Failed to connect to MQTT broker"
                        Log.e(TAG, failedConnection)
                        Toast.makeText(applicationContext, failedConnection, Toast.LENGTH_SHORT)?.show()
                    }
                }, object: MqttCallback {
                    override fun connectionLost(cause:Throwable) {
                        isConnected = false
                        val connectionLost = "Connection to MQTT broker lost"
                        Log.w(TAG, connectionLost)
                        Toast.makeText(applicationContext, connectionLost, Toast.LENGTH_SHORT)?.show()
                    }

                    @Throws(Exception::class)
                    override fun messageArrived(topic:String, message: MqttMessage) {
                        if (topic == "/smartcar/camera") {
                            val bm = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888)
                            val payload = message.payload
                            val colors = IntArray(IMAGE_WIDTH * IMAGE_HEIGHT)
                            for (ci in colors.indices) {
                                val r = payload[3 * ci]
                                val g = payload[3 * ci + 1]
                                val b = payload[3 * ci + 2]
                                colors[ci] = Color.rgb(r.toInt(), g.toInt(), b.toInt())
                            }
                            bm.setPixels(colors, 0, IMAGE_WIDTH, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT)
                            mCameraView?.setImageBitmap(bm)
                        } else {
                            Log.i(TAG, "[MQTT] Topic: $topic | Message: $message")
                        }
                    }

                    override fun deliveryComplete(token: IMqttDeliveryToken) {
                        Log.d(TAG, "Message delivered")
                    }
                })
            }
        }
        fun drive(throttleSpeed:Int?, steeringAngle:Int?, actionDescription:String) {
            if (!isConnected)
            {
                val notConnected = "Not connected (yet)"
                Log.e(TAG, notConnected)
                Toast.makeText(applicationContext, notConnected, Toast.LENGTH_SHORT).show()
                return
            }
            Log.i(TAG, actionDescription)
            mMqttClient?.publish(THROTTLE_CONTROL, throttleSpeed.toString(), QOS, null)
            mMqttClient?.publish(STEERING_CONTROL, steeringAngle.toString(), QOS, null)
        }

    }

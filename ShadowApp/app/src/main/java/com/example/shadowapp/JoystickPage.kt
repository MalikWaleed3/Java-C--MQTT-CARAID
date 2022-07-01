package com.example.shadowapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.joystick_view.*
import org.eclipse.paho.client.mqttv3.*

         @SuppressLint("StaticFieldLeak")
         val mTextViewAngleLeft: TextView? = null
        @SuppressLint("StaticFieldLeak")
        val mTextViewStrengthLeft: TextView? = null
       val leftJoystick: JoystickView? = null




        class JoystickPage : ConnectionMovement() {


                @SuppressLint("SetTextI18n")
                override fun onCreate(savedInstanceState: Bundle?) {
                        super.onCreate(savedInstanceState)
                        setContentView(R.layout.joystick_view)

                       val mTextViewAngleLeft = findViewById<View>(R.id.textView_angle_left) as TextView //joystick
                       val mTextViewStrengthLeft = findViewById<View>(R.id.textView_strength_left) as TextView //joystick

                        connectToMqttBroker()



                        val joystick = findViewById<View>(R.id.joystickView_left) as JoystickView //joystick
                        joystick.setOnMoveListener { angle, strength ->
                                mTextViewAngleLeft.text = "$angle°"
                                mTextViewStrengthLeft.text = "$strength%"



                                        val adjustedAngle = (angleChange(angle))
                                        val adjustedSpeed = ((strength / 100) * MOVEMENT_SPEED)
                                    drive(adjustedSpeed,adjustedAngle,"moving")


                        }
                }

                private fun angleChange(angle: Int): Int {
                        val adjustedAngle: Int
                        adjustedAngle = if (angle >= 90 && angle <= 180) { // left
                                90 - angle
                        } else if (angle < 90 && angle >= 0) { // right
                                90 - angle
                        } else if (angle > 0 && angle >= 270) { // back right
                                angle - 270
                        } else { // back left
                                angle - 270
                        }
                        return adjustedAngle
                }

                private fun turnCar(
                        adjustedSpeed: Int,
                        adjustedAngle: Int,
                        previousAngle: Int,
                        previousSpeed: Int
                ) {
                        var adjustedAngle = adjustedAngle
                        if (adjustedAngle != previousAngle || adjustedSpeed != previousSpeed) {
                                if (adjustedSpeed == 0) adjustedAngle = 0
                                mMqttClient?.publish(TURNING_TOPIC,
                                        adjustedAngle.toString(), QOS, null)
                                mMqttClient?.publish(SPEED_TOPIC,
                                        adjustedSpeed.toString(), QOS, null)
                        }
                }







        }



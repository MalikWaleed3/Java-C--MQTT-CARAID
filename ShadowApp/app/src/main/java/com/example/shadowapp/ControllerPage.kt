package com.example.shadowapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.controllerpage.*

class ControllerPage : ConnectionMovement() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.controllerpage)
        mMqttClient = MqttClient(applicationContext, MQTT_SERVER, TAG)
        mCameraView = findViewById(R.id.imageView)
        connectToMqttBroker()

        val returnb = findViewById<View>(R.id.button3) as Button
        returnb.setOnClickListener(View.OnClickListener { openActivity2() })

        forward.setOnClickListener { forward() }
        reverse.setOnClickListener { reverse() }
        right.setOnClickListener {right()}
        left.setOnClickListener { left() }
        stop.setOnClickListener { stop() }
    }

    fun openActivity2() {
        val intent = Intent(this, OptionPage::class.java)
        startActivity(intent)
    }

}



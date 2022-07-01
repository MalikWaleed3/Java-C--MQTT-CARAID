#include <vector>

#include <MQTT.h>
#include <WiFi.h>
#ifdef __SMCE__
#include <OV767X.h>
#endif

#include <Smartcar.h>

#ifndef __SMCE__
WiFiClient net;
#endif
MQTTClient mqtt;


const int fSpeed   = 50;  // 70% of the full speed forward
const int bSpeed   = -50; // 70% of the full speed backward
const int lDegrees = -75; // degrees to turn left
const int rDegrees = 75;  // degrees to turn right

const auto oneSecond = 1000UL;
const int TRIGGER_PIN           = 6; // D6
const int ECHO_PIN              = 7; // D7
const unsigned int MAX_DISTANCE = 100;

const int FRONT_IR_PIN = 0;
const int LEFT_IR_PIN  = 1;
const int RIGHT_IR_PIN = 2;
const int BACK_IR_PIN  = 3;



ArduinoRuntime arduinoRuntime;
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);
SR04 front(arduinoRuntime, TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);


GP2Y0A21 frontIR(arduinoRuntime, FRONT_IR_PIN);
GP2Y0A21 leftIR(arduinoRuntime, LEFT_IR_PIN);
GP2Y0A21 rightIR(arduinoRuntime, RIGHT_IR_PIN);
GP2Y0A21 backIR(arduinoRuntime,BACK_IR_PIN);



SimpleCar car(control);
std::vector<char> frameBuffer;

void setup()
{
    Serial.begin(9600);

     #ifdef __SMCE__
  Camera.begin(QVGA, RGB888, 15);
  frameBuffer.resize(Camera.width() * Camera.height() * Camera.bytesPerPixel());
  mqtt.begin("aerostun.dev", 1883, WiFi);
  
#else
  mqtt.begin(net);
#endif
  if (mqtt.connect("arduino", "public", "public")) {
    mqtt.subscribe("/smartcar/control/#", 1);
    mqtt.onMessage([](String topic, String message) {
      if (topic == "/smartcar/control/throttle") {
        car.setSpeed(message.toInt());
      } else if (topic == "/smartcar/control/steering") {
        car.setAngle(message.toInt());
      } else {
        Serial.println(topic + " " + message);
      }
    });
  }

}

void loop()
{
    handleInput();
    activeMovement();
    Objectavoid();

     if (mqtt.connected()) {
    mqtt.loop();
    const auto currentTime = millis();
#ifdef __SMCE__
    static auto previousFrame = 0UL;
    if (currentTime - previousFrame >= 65) {
      previousFrame = currentTime;
      Camera.readFrame(frameBuffer.data());
      mqtt.publish("/smartcar/camera", frameBuffer.data(), frameBuffer.size(),
                   false, 0);
    }
#endif
    static auto previousTransmission = 0UL;
    if (currentTime - previousTransmission >= oneSecond) {
      previousTransmission = currentTime;
      const auto distance = String(front.getDistance());
      mqtt.publish("/smartcar/ultrasound/front", distance);
    }
  }
#ifdef __SMCE__
  // Avoid over-using the CPU if we are running in the emulator
  delay(35);
#endif
}

void handleInput()
{ // handle serial input if there is any
    if (Serial.available())
    {
        char input = Serial.read(); // read everything that has been received so far and log down
                                    // the last entry
        switch (input)
        {
        case 'a': // rotate counter-clockwise going forward
            car.setSpeed(fSpeed);
            car.setAngle(lDegrees);
            break;
        case 'd': // turn clock-wise
            car.setSpeed(fSpeed);
            car.setAngle(rDegrees);
            break;
        case 'w': // go ahead
            car.setSpeed(fSpeed);
            car.setAngle(0);
            break;
        case 's': // go back
            car.setSpeed(bSpeed);
            car.setAngle(0);
            break;
         case 'c': // checkside
           car.setSpeed(0);
           delay(400);
           checksides();
            break;
        default: // if you receive something that you don't know, just stop
            car.setSpeed(0);
            car.setAngle(0);
        }
    }
}

void getsensordistance()
{
     Serial.println(front.getDistance());
     delay(100);
}

void Objectavoid ()
{
  int distance = front.getDistance();
  if(distance > 0 && distance < 100)
  {
    Serial.println("Detected obstacle ahead. ");
    Serial.println("Stopping the car. ");
     stopCar();
     delay(400);
    Serial.println("Car is stopped, rotate the car. ");
    car.setSpeed(bSpeed);
    car.setAngle (200);
    delay(1000);
    car.setSpeed(fSpeed);
     car.setAngle (0);
    checksides();
  }
}

void stopCar ()
{
  car.setSpeed(0);
}

void turnright() //Basic turning todo (Improve truning implementation)
{
  car.setSpeed (30);
  car.setAngle (95);
  delay(2000);
  car.setAngle (0);
  car.setSpeed (50);
}

void turnleft() //Basic turning todo (Improve truning implementation)
{
  car.setSpeed (30);
  car.setAngle (-95);
  delay(2000);
  car.setAngle (0);
  car.setSpeed (50);
}

void checksides()
{
  int leftdistance = leftIR.getDistance();
  int rightdistance = rightIR.getDistance();
  int backdistance = backIR.getDistance();

  if(leftdistance > 15 && leftdistance < 60)  //checking obstacle at leftside
{
  Serial.println("Detected obstacle at left. ");
  delay(400);
  Serial.println("Turning right to avoid obstacle. ");
  delay(400);
  turnright();
}
  else if(rightdistance > 15 && rightdistance < 60) // checking obstacle at rightside
{
  Serial.println("Detected obstacle at right. ");
   delay(400);
  Serial.println("Turning left to avoid obstacle. ");
   delay(400);
  turnleft();
}
  else if (backdistance > 15 && backdistance < 60) // checking obstacle at back
{
  Serial.println("Detected obstacle at back. ");
   delay(400);
  Serial.println("Moving ahead ");
   delay(400);
  car.setSpeed(70);
  
}
}

void activeMovement()
{
  int leftside = leftIR.getDistance();
  int rightside = rightIR.getDistance();
  
  if(leftside > 15 && leftside < 30)  //checking left side so the car dont hit while moving.
{
  car.setAngle (20);
  delay(200);
  car.setAngle(0);
}
  else if(rightside > 15 && rightside < 30) // checking right side so the car dont hit while moving.
{
  car.setAngle (-20);
  delay(200);
  car.setAngle(0);
}

}

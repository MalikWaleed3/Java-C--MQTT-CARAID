# group-15


![caraid1-removebg-preview1](https://user-images.githubusercontent.com/81308510/119974140-9df48b80-bfb4-11eb-941e-13992d67f416.png)


                                                https://youtu.be/iof-ODHmS3k

# 1.0 Our Product
 We made a system where the user can control the Arduino based smart car in several different ways. Our top feature for the users is that they can use their voice to control the car entirely. But the car can also control itself by its sensors, which will help the user to avoid obstacles in the terrain. 

# 2.0 System requirements
--Android Studio
--SMCE Emulator

NOTE: In order to connect to the mqtt server you need a proper wifi connection, you can not use your mobile phone internet connection used as a hotspot. 

  ## 2.1 Installation
to be able for the user to install our program, First they need to download zip file from our Github repository: 

                       https://github.com/DIT112-V21/group-15/archive/refs/heads/master.zip


![ezgif com-gif-maker](https://user-images.githubusercontent.com/81308510/119974195-af3d9800-bfb4-11eb-9ad8-46290190b9e7.gif)
                          
                          Link to repository: https://github.com/DIT112-V21/group-15
		
After you have download our code and set it up, it’s time for you to download the SMCE emulator in order to see the car you are driving. 
                          
                          Link to the emulator: https://github.com/ItJustWorksTM/smce-gd
                                [Installation guidelines are found in the link.] 


# 3.0 How to Use CARAID?
Run the code in android studio, 
link your android phone to the computer and click the build and then run Button in android studio.

You can use our app with the Android studio emulator or with your own android device, it is entirely up to you.
(Note: USB debugging should be activated before linking the phone and choose “Transfer files” after connecting the phone)
If the link doesn't work, go to our repository and click on the green “code” button and download ZIP file:

Once you have run the code in android studio, run the smartcar.ino file in the SMCE emulator, and you are ready to go.

Back to Android studio. If you have successfully built and run the code, It's finally time to use the app. The first thing you need to do as a new user is to register.
After registering, you can go ahead and login. If you don’t like to read instructions you can always listen to Samantha explaining the app, click on the speaker in the top right corner and she will guide you through the system.

Once you have logged in, you have 3 different modes you can use in order to drive the car. 
##### → CONTROLLER: This view lets you control the car in the most basic way, 4 arrows that control the forward and reverse, right and left movements.
##### → VOICE: This mode is entirely based on your command, you can control the car by words like, FORWARD, BACKWARD, TURN LEFT, TURN RIGHT and STOP. 
##### → JOYSTICK: Here you can control the car using a classic on screen joystick.

Once you’re done using the app, or if you forgot your credentials, you can go to PROFILE. Next time you want to use the app you can insert the same information as you prompted last time since it’s saved in our database.	  

![Skärmavbild 2021-05-28 kl  13 04 44](https://user-images.githubusercontent.com/81308510/119974720-4b679f00-bfb5-11eb-83b2-ebb906277c45.png)

# 4.0 Why did we make it? What problem does it solve? 
Our idea was to develop a system where people with disabilities can drive a car. It can be used for instance with amputees, or any other type of disability where these people still can use their voice. The application recognizes voice commands so the person using the app doesnt have to use an actual steering wheel for instace. This will help tons of people with disabilities to actually drive a car which hasn’t been possible before. We hope that this kind of technology will be implemented in cars in the future so driving can be accessible to more people on this planet.

# 5.0 Software environment:
	
#### Arduino IDE:  Develop the code that runs the car
#### Android Studio: Develop the app interface to run and control the car
#### SMCE-Godot: To run arduino sketch to control the car in virtual reality world.
#### Java, Kotlin, C++ ,MQTT: Assets used to produce the system.

# 6.0 Developers:

#### Jakob Bergek - gusberjad@student.gu.se
#### Louis Mercier - gusmerlo@student.gu.se
#### Samandar Hashimi - guskhalksa@student.gu.se
#### Maryo Idress Nano Nano - gusnanoma@student.gu.se
#### Malik Waleed Mahboob - gusmahboma@student.gu.se
#### Malik Hannan Ahmed - gusmalikah@student.gu.se

# accel-point-click 
accel-point-click is an Android library for accelerometer-based pointing and clicking. The clicking methods were created with minimal screen occlusion in mind. It supports:

2 control modes for pointing:
- **Velocity-control** - Pointer acts like a ball under the effects of gravity.
- **Position-control** - Pointer has an initial position and tilt the device moves the pointer away from the initial position.

2 ways of clicking with minimal screen occlusion:
- **Volume Down Button** - Volume down button is overridden to register a click when pressed on.
- **Back Tapping** - Tapping the back of the device triggers a click.

## Using the library in your project
Before you can use the accel-point-click functionalities in your app, you must first download and import the library into your project. The minimum API level requirement for the library is 21.
### Downloading the library
Download the ```accel-point-click.aar``` library file from the releases folder.

### Importing the library into your project
To import the accel-point-click library, you must move the .AAR file into the libs folder of your app module. Create the libs folder if it does not exist.

Next, you need to update your project's ```build.gradle``` file to allow the libs folder to be tracked
```
allprojects {
    repositories {
        ...
        flatDir {
            dirs 'libs'
        }
        ...
    }
}
```

Now, update your app module's ```build.gradle``` to import the library into the app module
```
dependencies {
    ...
    implementation(name:'pointandclick', ext:'aar')
    ...
}
```

Now you can begin using the library!

## Usage
The pointer exists in a separate full screen view and is added to the activity either through the activity's code or the layout file. 
The following is an example of adding the pointer through code.

``` java
//User's activity class should extend MouseAcutivity
UserActivity extends MouseActivity{
    
}
```

#### Customising pointer control method
Setting the control mode
``` java
// For position-control
mouseView.enablePositionControl(true);

// For velocity-control
mouseView.enablePositionControl(false);

```

Configuring tilt gain settings, the settings which determines the step sizes of the pointer
``` java
// For position-control
mouseView.setPosTiltGain(tiltGain);

// For velocity-control
mouseView.setVelTiltGain(tiltGain);
```

More customisation options
```java
// Sets the bitmap of the pointer
mouseView.setMouseBitmap(bitmap);

// Sets the reference pitch added to the rest pitch of 0, when the device is laid flat
mouseView.setRefPitch(refPitch);
// same as setRefPitch but the refPitch value is the current pitch of the device
mouseView.calibratePitch();

// Sets the initial point of the pointer
mouseView.setXReference(xRef);
mouseView.setYReference(yRef);

// Enables recalibration of the pointer using the volume up button
mouseView.enableRecalibrationByVolumeUp(true);

```

#### Customising pointer clicking method
Choosing the clicking method for the pointer
```java
// Volume Down Button
mouseView.setClickingMethod(ClickingMethod.VOLUME_DOWN);

// Back Tapping
mouseView.setClickingMethod(ClickingMethod.BACK_TAP);

```
Back Tapping requires the installation of a [separate application](https://play.google.com/store/apps/details?id=com.prhlt.aemus.BoDTapService) to work. The application will need to be launch and will start a service that will listen to back taps.
You will need to click ```Start BTAP Service```, then give it the appropriate permissions by opening the settings.


## Demo Application
The Accel World demo application is used to test the functionalities provided by the accel-point-click libray. The minimum API level requirement for the application is 21.



### Installation guide
Download the ```accel-world.apk``` file from the releases folder. Run the apk on your device to install the application.

### Note
* Back Tapping to click requires the installation of a [separate application](https://play.google.com/store/apps/details?id=com.prhlt.aemus.BoDTapService) to work. The application will need to be launch and will start a service that will listen to back taps.
You will need to click ```Start BTAP Service```, then give it the appropriate permissions by opening the settings.
* Volume up button will recalibrate the pointer in the different scenarios of the application.


## New features
* Demo application now demonstrates the library with other 8 different scenarios, including 3 previous activities (Wikipedia page, Number dial pad, Keyboard) and 5 new activities (Random button, Draw, Big image, Text editor, and Calculator). 

Our application enables users to demo 8 different scenarios which includes:
Keyboard: allows users to type letters in a keyboard scenario
Numpad: allows users to dial number in a number pad scenario
Wikipedia: allows users to click on the highlighted link in a Wikipedia liked page
Draw: allows users to draw 
Calculator: allows users to do some quick maths
text editor: allows users to edit text
RandomBtn: allows users to click on a randomly generated button
BigImage: allows users to drag and browse a big image

## improvement
*Filtering method was applied to help the cursor movement perform in a more smooth way. 
*delay 
*cursor shape 

 


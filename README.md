# accel-point-click 
accel-point-click is an Android library for accelerometer-based controlling method. The clicking methods were created with minimal screen occlusion in mind. It supports:

2 control modes for pointing:
- **Velocity-control** - Pointer acts like a ball under the effects of gravity.
- **Position-control** - Pointer has an initial position and tilt the device moves the pointer away from the initial position.

2 ways of clicking with minimal screen occlusion:
- **Volume Down Button** - Volume down button is overridden to register a click when pressed on.
- **Back Tapping** - Tapping the back of the device triggers a click.

2 optional configuration for improving accuracy:
- **Smoothing Coefficient** - process the motion sensor signal data to smooth the movement of the cursor.
- **Delay Coefficient** - introduce delays on the cursor to cancel out the offset of hand movement when performing click.

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

You want the current Activity to extend the MouseActivity abstract class. This class places the cursor as a drawable on the inbuilt android overlay.
You can bind to onSensorChanged's super method to listen to sensor changes.

``` java
//User's activity class should extend MouseActivity
UserActivity extends MouseActivity{
    
}
```

```
MouseActivity {
    ...
    protected void setupMouse(...) {
        ...
        findViewById(android.R.id.content).getOverlay().add(mouse.getDrawable());
    }
}
```
### Methods provided
- `setTiltGain(int tiltGain)` - sets the current cursor's sensitivity
- `setSmooth(int smooth)` - sets the current cursor's smoothing coefficient
- `setDelay(int delay)` - sets the current cursor's delay coefficient
- `setControlMethod(ControlMethod method)` - sets the current control method (ControlMethod is a Enum class)
- `setClickingMethod(ClickingMethod method)` - sets the current clicking method (ClickingMethod is a Enum class)
- `setupMouse(Bitmap m, int width, int height, int offsetX, int offsetY)` - sets the current cursor's look
- `calibratePointer()` - sets the current position of the phone as base coordinate and center the pointer
- `simulateTouchMove()` - simulate a touch move event on the current position
- `simulateTouchDown()` - simulate a touch down event on the current position
- `simulateTouchUp()` - simulate a touch release event on the current position

#### Customising the control mode
``` java
// For position-control
setControlMethod(ControlMethod.POSITION_CONTROL);

// For velocity-control
setControlMethod(ControlMethod.VELOCITY_CONTROL);

```

#### Customising pointer control method
```java
// Volume Down Button
setClickingMethod(ClickingMethod.VOLUME_DOWN);

// Back Tapping
setClickingMethod(ClickingMethod.BACK_TAP);

```
Back Tapping requires the installation of a [separate application](https://play.google.com/store/apps/details?id=com.prhlt.aemus.BoDTapService) to work. The application will need to be launch and will start a service that will listen to back taps.
You will need to click ```Start BTAP Service```, then give it the appropriate permissions by opening the settings.

#### Configuring coefficient settings, the settings which determines the step sizes of the pointer
``` java
// The tiltGain related to screen ratio and the default value is 35
setTiltGain(35)

//The the smooth coefficient determinate the level of filtering the shake. The default setting is no filtering.
setSmooth(10)

//The the delay coefficient determinate the level of delay of the clicking. The default setting is no delay.
setDelay(10)

```

#### More customisation options
```java
// A default style of the corsor is provided and this method is used to change the style of the cursor.
setupMouse(bitmapImage, width, height, offsetX, offsetY)

```

## Demo Application
The Accel World demo application is used to test the functionalities provided by the pointandclick libray. The minimum API level requirement for the application is 21.

### Installation guide
Download the ```app-release.apk``` file from the releases folder. Run the apk on your device to install the application.

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

##
Link to the Github repository : https://github.com/bche722/softeng702-project

# TUM Workshop 2018

In this task you will create an android application which uses a geofence and the current location data in order to open/close a popup on the headunit.

## Implementing
1. Implement the function `boolean containsLocation(Location location)` within the `CircleGeofence` class. 
2. Implement a geofence solution, to notify users, when they leave the given area. You will need to implement the missing functions in the Android service class `GeofenceApp`:
	- **Register** to a *locationProvider* from the Android environment.
	- **Bind** your application to a *remote service* to show notifications. The AIDL you want to use is *HeadUnit.aidl*. You are on the client side.
	- Implement **geofencing** at `onLocationChanged(Location location)`  
	There you want to implement this logic:
		- Check the position
		- Hide/show notification correspondingly
3. **BONUS**: Implement the function `boolean containsLocation(Location location)` within the `PolygonGeofence` class. 

## Testing
Validating your software is important. You have two possibilities to test, whether your software is working as expected.
### Unit testing
Execute the unit tests of the `app`-project.

### Testing on Android
You can also verify that your application works on a real-world device or an android-emulator by installing and running it on such a device.

#### Install the apps
If you don't want to use the Gradle-GUI in AndroidStudio, you can install the apps via bash:

```bash
./gradlew headunitservicemock:installDebug
./gradlew app:installDebug
```

#### Starting the whole system
If you use an Anroid-Version with API-Levels above 22 (API-Level 22 &#x2259; Android 5.1), you need to grant geofence-service the permission to use location-services after installing and before running it:

```bash
adb shell pm grant com.esrlabs.geofence android.permission.ACCESS_FINE_LOCATION
```

Afterwards you can start the services:

```bash
adb shell am startservice -a com.esrlabs.headunitinterface.HeadUnit
adb shell am startservice -a com.esrlabs.geofence
```

If you now leave the predefined geofence, you should be able to see your custom notification.

#### Error: app is in background uid null
Starting from Android Oreo, background-services have been restricted. The provided code-base manages this change, but you need to vary the startup-routine a bit:

```bash
adb shell am start-foreground-service -a com.esrlabs.headunitinterface.HeadUnit
adb shell am start-foreground-service -a com.esrlabs.geofence
```

#### Uninstalling the apps
You can either uninstall the apps using Gradle:

```bash
./gradlew app:uninstallDebug
./gradlew headunitservicemock:uninstallDebug
```

Or you can uninstall them using adb:

```bash
adb shell pm uninstall com.esrlabs.geofence
adb shell pm uninstall com.esrlabs.headunitservice
```


#### Test locations
If you want to provide test locations:

	Notification shown:
		longitude: 11.601
		latitude:  48.1195
		
	Notification hidden:
		longitude: 11.601
		latitude:  48.119


## Help /Advice
If you have problems solving the tasks, here is a quick help.

### Receive the current location 

Use the [android location manager](http://developer.android.com/reference/android/location/LocationManager.html) to request location updates. If the location is retrieved correctly, then the following test will pass.

```java
GeofenceAppTest.shouldReceiveTheLatestLocation()
```

### Bind to notification service

Use the [AIDL](http://developer.android.com/guide/components/aidl.html) to allow interprocess communication between your app and another service, which will allow you to display notifications. You will need to bind to the service using an intent and a [ServiceConnection](http://developer.android.com/reference/android/content/ServiceConnection.html)-Object (see [here](http://developer.android.com/guide/components/aidl.html#Expose)).

```java
Intent headUnitServiceIntent = new Intent(HeadUnit.class.getName());
headUnitServiceIntent.setPackage("com.esrlabs.headunitservice");
bindService(headUnitServiceIntent, serviceConnection_object, BIND_AUTO_CREATE);
```

### Circular geofence

The first step is to implement a circle geofence. You cannot use the google API for this, because it is part of the play API, which is not supported by our android controller. A test class is included in the test suite for helping you with the circle geofence.

### Polygon geofence

You can use the [Ray-casting algorithm](https://en.wikipedia.org/wiki/Point_in_polygon). Use the slope of a straight line to tell if the ray starting from the given point and going in a fixed direction, intersects an edge of the polygon. Take care that *Math.atan()* return radians, and you will have to correct a negative angle:

```java
90 + (90 - Math.abs(angle))
```

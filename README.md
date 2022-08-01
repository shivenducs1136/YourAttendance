<p align="left">
	<h2 align="left"> YourAttendance </h2>
	<h4 align="left"> An Attendance App to maintain your 75% Attendance and track Progress. <h4>
</p>

---
[![DOCS](https://img.shields.io/badge/Documentation-see%20docs-green?style=for-the-badge&logo=appveyor)](INSERT_LINK_FOR_DOCS_HERE) 
  [![UI ](https://img.shields.io/badge/User%20Interface-Link%20to%20UI-orange?style=for-the-badge&logo=appveyor)](INSERT_UI_LINK_HERE)
  [![PLAYSTORE](https://img.shields.io/badge/Playstore-Download-blue)](https://play.google.com/store/apps/details?id=com.kenvent.yourattendance)


## Functionalities
- [ ] User can add all the subjects for first time using there ERP Portals.
- [ ] User can mark their present or absent in the class. 
- [ ] User can view that how many class the must attend to maintain their criteria.
- [ ] User can view that how many class may they leave while being in the safe side. 
- [ ] User can theoratically mark their attendance for the future class to view their status. 
- [ ] User can Save the backup of the subject added online on firebase.
- [ ] User can give feedback.




<br>


## Instructions to run

* Pre-requisites:
	-  Android Studio v4.0
	-  A working Android physical device or emulator with USB debugging enabled

* Directions to setup/install
- Clone this repository to your local storage using Git bash:
```bash
https://github.com/shivenducs1136/YourAttendance
```
- Open this project from Android Studio
- Connect to an Android physical device or emulator
- To install the app into your device, run the following using command line tools
```bash
gradlew installDebug
```

* Directions to execute
-  To launch hands free, run the following using command line tools
	```bash
	adb shell monkey -p com.kenvent.yourattendance -c android.intent.category.LAUNCHER 1
	```

<br>

## Contributors
* [Shivendu](https://github.com/shivenducs1136)

![](https://github.com/shivenducs1136/YourAttendance/blob/main/image.png)


<br>
<br>

<p align="center">
	Made during 🌙 by Shivendu Mishra
</p>


<img src = "https://play-lh.googleusercontent.com/TiRXgD_jRm-YxTTBp0LoqqtEu93IRwZeCS_cVE4CXcUOsjDmSy33NGDuF1kbCnSvuQ=w240-h480-rw" width = "100" height = "100" align = "right"> 
<p align="left">
	<h1 align="left"> Your Attendance </h2>
	<h4 align="left"> An attendance monitoring app to maintain your 75% attendance and track progress. <h4>
</p>

---
<!-- [![DOCS](https://img.shields.io/badge/Documentation-see%20docs-green?style=for-the-badge&logo=appveyor)](INSERT_LINK_FOR_DOCS_HERE)  -->
[![PLAYSTORE](https://img.shields.io/badge/Playstore-Download-blue?style=for-the-badge&logo=appveyor)](https://play.google.com/store/apps/details?id=com.kenvent.yourattendance)

## Problem Statement. 
Every college has a certain percentage of attendance requirements that must be met, such as 75% or 65%. Since many students miss classes for a variety of reasons (such as family obligations or health issues), it can be difficult for them to figure out how many classes they need to take to meet their requirements. Or other students have an idea of how many lessons they can miss while being safe because they plan their time for other creative stuffs.

## Functionalities
#### Users can do the following:
- [ ] Add all the subjects for first time using their ERP Portals.
- [ ] Mark their present or absent in the class. 
- [ ] View how many classes they must attend to maintain their criteria.
- [ ] View how many classes they may leave while being on the safe side. 
- [ ] Theoretically mark their attendance for the future class to view their status. 
- [ ] Save the backup of the subjects added online on firebase.

## Usage and screenshots

#### Add a new subject:
<img src = "https://github.com/shivenducs1136/YourAttendance/blob/main/imgs/add_subject.gif?raw=true" width = "230">
	
#### Mark present / absent:
<img src = "https://raw.githubusercontent.com/shivenducs1136/YourAttendance/main/imgs/mark_attendance.gif?raw=true" width = "230">

<br>


## Instructions to run

* __Pre-requisites:__
	-  Android Studio v4.0
	-  A working Android physical device or emulator with USB debugging enabled

* __Directions to setup/install__
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

* __Directions to execute__
	- To launch hands free, run the following using command line tools
	```bash
	adb shell monkey -p com.kenvent.yourattendance -c android.intent.category.LAUNCHER 1
	```

<br>

## Built with
- Kotlin
- XML
- RoomDB
- Firebase
## Contributors
* [Shivendu](https://github.com/shivenducs1136)

:star: To contribute, please go through our [contributing guide](https://github.com/shivenducs1136/YourAttendance/blob/main/Contributions.md).


<br>
<br>

<p align="center">
	Made during 🌙 by Shivendu Mishra
</p>

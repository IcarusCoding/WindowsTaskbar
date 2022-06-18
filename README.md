# Windows Taskbar

Windows Taskbar is a java wrapper around the Windows native [ITaskBarList3](https://docs.microsoft.com/en-us/windows/win32/api/shobjidl_core/nn-shobjidl_core-itaskbarlist3) interface with the help of [JNA](https://github.com/java-native-access/jna). 

# Features

It currently supports the following features:
* Initialization of the COM library
* Creation of [ThumbButtons](https://docs.microsoft.com/en-us/windows/win32/api/shobjidl_core/ns-shobjidl_core-thumbbutton) 
* Interception of the [WM_COMMAND](https://docs.microsoft.com/en-us/windows/win32/menurc/wm-command) message from the window message loop
* Registration of click handlers which will get notified when a [ThumbButton](https://docs.microsoft.com/en-us/windows/win32/api/shobjidl_core/ns-shobjidl_core-thumbbutton) is clicked
* Support for custom icons
* Conversion of .ico files from the classpath or filesystem to native [HICON](https://docs.microsoft.com/en-us/windows/win32/menurc/using-icons) instances
* Updating already existing [ThumbButtons](https://docs.microsoft.com/en-us/windows/win32/api/shobjidl_core/ns-shobjidl_core-thumbbutton) 
* [Java AWT](https://docs.oracle.com/javase/7/docs/api/java/awt/package-summary.html) integration
* [JavaFX](https://openjfx.io) integration

# Prerequisites
Java 17 is required to use Windows Taskbar.

# Getting Started
## Adding Taskbar buttons with JavaFX
To use the library with JavaFX one has to retrieve the window handle to the active main window.

Currently one can use any of the following methods to retrieve such a handle after the JavaFX Stage is shown:
```java
final Stage stage = ...;
var handle = WindowHandleFinder.getFromInternalJavaFX(stage);
```
```java
var handle = WindowHandleFinder.getFromActiveWindow();
```

## Adding Taskbar buttons with Java AWT
To use the library with Java AWT one has to retrieve the window handle to the active main window.

Currently one can use any of the following methods to retrieve such a handle after the JavaFX Stage is shown:
```java
final Component component = ...;
var handle = WindowHandleFinder.getFromInternalAWT(component);
```
```java
final String windowTitle = ...;
var handle = WindowHandleFinder.findByName(windowTitle);
```

## Using the library
```java
final Icon testIcon = Icon.fromClassPath("test", "test.ico");
IWindowsTaskbar taskbar = TaskbarBuilder.builder()
    .setHWnd(WindowHandleFinder.getFromInternalJavaFX(stage))
    .autoInit()
    .overrideWndProc()
    .addButtons(ToolbarButtonListBuilder.builder()
        .buttonBuilder()
            .setIcon(testIcon)
            .setTooltip("Click Me")
            .withFlag(TaskbarButtonFlag.ENABLED)
            .setOnClicked(evt -> System.out.println("Clicked on button with id " + evt.id()))
            .build()
        .buttonBuilder()
            .setIcon(testIcon)
            .setTooltip("Test Tooltip")
            .withFlag(TaskbarButtonFlag.ENABLED)
            .withFlag(TaskbarButtonFlag.DISMISS_ON_CLICK)
            .build()
        .build())
    .build();
```

# Remarks
A strong reference to an instance of the **IWindowsTaskbar** is needed, if an override of the standard window message loop of the used GUI library is desired.

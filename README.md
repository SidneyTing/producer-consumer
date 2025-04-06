# Networked Producer and Consumer

### STDISCM Problem Set #3
- Miguel Kua - S11
- Kevin Tuco - S11
- Sidney Ting - S12
- Josh Ang Ngo Ching - S12

For Visual Studio Code (Java)

## PROJECT DIRECTORY
```
/gui/
/saved/
    /... (saved videos go here)
/upload/
    /0/
        /video1.mp4
        /video2.mp4
    /1/
    /2/
    /.../

config.txt
CThread.java
Consumer.java
...
```

## INSTRUCTIONS
1. Clone the project on both the **Producer** and **Consumer** machines.
2. Add videos in the `upload` folder of the **Producer** machine. Each folder number corresponds to the Producer thread that will send the video.
    - Ex. If there's 5 Producer threads...
```
/upload/
    /0/
    /1/
    /2/
    /3/
    /4/
    /5/
```

3. Open CLI on the **Consumer** machine and run: ```java Consumer```.
    - Visit ```http://localhost:8080/``` to view the browser-based GUI.
4. To begin uploading, open CLI on the **Producer** machine and run: ```java Producer <consumer-ip-address>```.
    - All videos are saved in the `saved` folder of the **Consumer** machine. These are also viewable on the browser.

## CONFIGS (in config.txt)
```
Coming soon...
```

NOTE: Ensure that config.txt is in the same directory as the source code.
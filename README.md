# Networked Producer and Consumer

### STDISCM Problem Set #3
- Miguel Kua - S11
- Kevin Tuco - S11
- Sidney Ting - S12
- Josh Ang Ngo Ching - S12

For Visual Studio Code (Java)

## Project Directory
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

## Instructions
1. Clone the project on both the **Producer** and **Consumer** machines.
2. Compile all Java files on both machines by running ```javac *.java``` in their root directories.
3. Add videos in the `upload` folder of the **Producer** machine. Each folder number corresponds to the Producer thread that will send the video.
    - Ex. If there are 5 Producer threads, the folder structure should like this:
```
/upload/
    /0/
    /1/
    /2/
    /3/
    /4/
    /5/
```

4. On the **Consumer**, machine run: ```java Consumer```.
    - Visit ```http://localhost:8080/``` to view the browser-based GUI.
5. To begin uploading, on the **Producer** machine, run: ```java Producer <consumer-ip-address>```.
    - All videos are saved in the `saved` folder of the **Consumer** machine. These are also viewable on the browser.

## Configurations (in config.txt)
```
Coming soon...
```

NOTE: Ensure that config.txt is in the same directory as the source code.
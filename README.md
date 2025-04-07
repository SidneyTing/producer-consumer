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

## Dependencies

### FFmpeg (Required for video compression)
The consumer machine requires FFmpeg to be installed for video compression. 

#### Installation:

**macOS (using Homebrew):**
```
brew install ffmpeg
```

**Windows:**
1. Download FFmpeg from https://ffmpeg.org/download.html
2. Extract the files and move the folder to the root of the C drive (or drive of choice)
3. Add FFmpeg to your system PATH

**Verifying Installation:**
To verify if FFmpeg is correctly installed, run:
```
ffmpeg -version
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
```

4. On the **Consumer**, machine run: ```java Consumer```.
    - Visit ```http://localhost:8080/``` to view the browser-based GUI.
5. To begin uploading, on the **Producer** machine, run: ```java Producer <consumer-ip-address>```.
    - All videos are stored in the `saved` folder of the **Consumer** machine. These are also viewable on the browser.

## Configurations (in config.txt)
1. **Producer Threads (p)** - First line
   - Controls how many parallel producer threads will run
   - Valid range: 1-10
   - Default: 5
   - Higher values allow more concurrent video uploads

2. **Consumer Threads (c)** - Second line
   - Controls how many parallel consumer threads will process videos
   - Valid range: 1-10
   - Default: 1
   - Higher values allow more concurrent video processing/compression

3. **Queue Size (q)** - Third line
   - Controls the maximum number of videos that can be queued for processing
   - Valid range: 1-100
   - Default: 1
   - Higher values allow more videos to be buffered before processing

If the configuration file is missing or contains invalid values, the system will use the default values.

NOTE: Ensure that config.txt is in the root directory of both producer and consumer machines.
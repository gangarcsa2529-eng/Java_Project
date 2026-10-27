# Typing Speed & Accuracy Analyser

A Java Swing desktop application that measures your typing speed (WPM) and accuracy, and keeps a leaderboard of the top 10 scores.

Course project: OOP using Java (24CSH2114), Saintgits College of Engineering
Team Name: Stream

## Features
- Three difficulty levels: Easy, Medium, Hard
- Random passage for each test
- Live timer while you type
- WPM and accuracy calculated on submit
- Top 10 leaderboard saved to a file

## OOP Concepts Used
- Abstraction: `Participant` (abstract class)
- Interface: `Scorable`
- Inheritance: `Player extends Participant`
- Polymorphism: `@Override` of `setResult`, `getWpm`, `getAccuracy`
- Encapsulation: private fields with getters
- File handling: `PassageManager`, `Leaderboard`
- Event handling: `ActionListener`, `KeyAdapter`
- Threads/Timer: `javax.swing.Timer`

## Requirements
- JDK 8 or higher

## How to Run
**You need:** Java (JDK) installed on your computer.

1. Download this project: click **Code → Download ZIP**, then extract the ZIP.
2. Open the extracted folder.
3. Open a terminal in that folder.
4. Type these two commands, one after the other:

```
javac TypingSpeedAnalyzer.java
java TypingSpeedAnalyzer
```

The app window will open.

**Note:** Keep `easy.txt`, `medium.txt` and `hard.txt` in the same folder as `TypingSpeedAnalyzer.java`.

## How to Use
1. Click **Play Typing Test**
2. Enter your name and choose a difficulty
3. Click **Start Test** and type the passage
4. Click **Submit Result** (or press Enter)
5. Open **View Leaderboard** to see the top 10

## How Scores Are Calculated
- WPM = (characters typed ÷ 5) ÷ minutes
- Accuracy = correct characters ÷ longer of typed or original text × 100

## Project Structure
```
├── TypingSpeedAnalyzer.java
├── easy.txt
├── medium.txt
├── hard.txt
└── typing_results.txt  
```
## Team Members
- Abijith Anil Kumar – MGP25CS008
- Alcina Peter – MGP25CS024
- Aparna P Subhash – MGP25CS036
- Ganga Rajeev - MGP25CS063

Guided by: Er. HARI M

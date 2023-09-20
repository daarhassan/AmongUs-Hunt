import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 
import gab.opencv.*; 
import processing.video.*; 
import java.awt.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class hunt_imposter_AmongUs extends PApplet {

  //needed for sound effects 

  //for video controlled mode


Capture video;
OpenCV opencv;

Alien[] aliens=new Alien[300];  //more Aliens than Imposters
Imposter[] imposters=new Imposter[150];
Scope s;

SoundFile ding;  //sounds
SoundFile lose;
SoundFile shoot;
SoundFile tick;
SoundFile imposter;

PFont startup;  //startup font
PFont storyFont; //story font
int startupX = 0;  //varuables for the startup animation (must be initialized outside of draw)
int startupY = 0;
float y = 0; //for scrolling text in story

int timer = 30; //used to keep track of game time. Higher number here = longer game. 
int temp = 0;

float sx=0;  //scope x and y position and velocity
float sy=0;
float xv=0;
float yv=0;
float diff = .2f;  //amount the scope velocity changes each time you move it, gets lower and game gets harder as you get more points.

float r=0;  //shooting mechanism

char screenState = 's';  //keeps track of where we are in the program
//s = start; l = logo; i = intro; m = menu; n = secondary menu; g = game; b = leaderboard

int points=0;
String typing = "";  //temporary username storage
String username = "";  //username
boolean allowTyping = false;  //allows typing of username

Table table;  //leaderboard

PImage logo; //logo image
PImage menu;  //menu background
PImage img;  //background image
PImage kPlay;  //instruction images
PImage cPlay;

PImage[] alienImg = new PImage[4];  //alien and imposter image arrays and file name array
PImage[] imposterImg = new PImage[4];
String[] fileNames = {"1.png", "2.png", "3.png", "4.png", "1i.png", "2i.png", "3i.png", "4i.png"};  //names of alien and imposter image files. i means imposter. 


public void setup() {
  
  textAlign(CENTER);
  frameRate(30);
  table = loadTable("Leaderboard.csv");
  background(1000);
  startup = createFont("Calibri Bold", 100);  //startup font
  storyFont = createFont("Colonna MT", 100);
  img=loadImage("place.jpg");  //background image
  menu = loadImage("menu.jpg");
  kPlay = loadImage("keyboardplay.png");
  cPlay = loadImage("cameraplay.png");
  logo = loadImage("logo.png");
  logo.resize(600, 463);  //bringing image to width of screen maintaining aspect ratio
  menu.resize(600, 600);
  kPlay.resize(130, 130);
  cPlay.resize(130, 130);

  ding = new SoundFile(this, "ding.mp3");  //sound files
  lose = new SoundFile(this, "losing.mp3");
  shoot = new SoundFile(this, "shoot.mp3");
  tick = new SoundFile(this, "tick.mp3");
  imposter = new SoundFile(this, "imposter.mp3");

  video = new Capture(this, 75, 60);  //begin capturing webcam video and detecting faces (won't be displayed or used unless camera mode is chosen
  opencv = new OpenCV(this, 75, 60);
  opencv.loadCascade(OpenCV.CASCADE_FRONTALFACE);  
  video.start();
  
  for (int i = 0; i < 4; i++)  //load and resize all alien and imposter images
  {
    alienImg[i] = loadImage(fileNames[i]);
    imposterImg[i] = loadImage(fileNames[i+4]);  //first 4 file names are for the aliens, not imposters
    alienImg[i].resize(50, 0);
    imposterImg[i].resize(50, 0);
  }

  for (int i=0; i<aliens.length; i++) {  //creating the Aliens and Imposters
    aliens[i] = new Alien(this);
    if (i < 150)  //since there are more Alien objects than Imposter objects, need this to prevent out of bounds exception
      imposters[i]=new Imposter(this);
  }
  s=new Scope(this);  //create scope
}

public void draw() {
  //if statement is written in what should be chronological order. 
  if (screenState == 's')  //start animation
  {
    startupA();
  } else if (screenState == 'l')  //game logo page
  {
    background(0);
    textSize(20);

    if (keyPressed)
    {
      screenState = 'i';
    }
    image(logo, 0, 68);
  } else if (screenState == 'i')   //intro with the story
  {
    story();
  } else if (screenState == 'm') //menu page, has an option to go to more instructions or start the game with either mode
  {
    push();
    rectMode(CORNER);
    background(menu);

    fill(255);
    rect(50, 50, 100, 100);  //button 1
    rect(50, 250, 100, 100);  //button 2
    rect(50, 450, 100, 100);  //button 3


    fill(0xffFF00E6);
    textAlign(CENTER);
    textSize(15);
    text("Key Mode", 100, 100);  //button 1
    text("Camera Mode", 100, 300);  //button 2
    text("More Info", 100, 500);  //button 3
    text("In Keyboard Mode:\nUse the arrow keys to move the scope\nUse the spacebar to shoot imposters\n\n\n\n\n\n\nIn Camera Mode:\nMove your head side to side and up and down\nto move the scope.\nUse spacebar to shoot.\nIf your head is centered the scope stops moving.\n\n\n\n\n\n\nClick more info to see more info", 2*width/3, 75);
    pop();

    if (mousePressed && mouseX < 150 && mouseX > 50 && mouseY < 150 && mouseY > 50)  //button 1
    {
      username = "";  //reset username (in case the person plays multiple times)
      timer = 30;  //reset timer
      points = 0; //reset score
      background(0);
      screenState = 't'; //go to typing screen
    } 
    if (mousePressed && mouseX < 150 && mouseX > 50 && mouseY < 350 && mouseY > 250)  //button 2
    {
      username = "";  //reset username (in case the person plays multiple times)
      timer = 30;  //reset timer
      points = 0; //reset score
      background(0);
      screenState = 'u'; //go to typing screen
    }
    if (mousePressed && mouseX < 150 && mouseX > 50 && mouseY < 550 && mouseY > 450)  //button 3
    {
      screenState = 'n';  //go to the additional menu screen
    }
  } else if (screenState == 'n') //extra menu options      
  {
    background(255);
    push();
    textSize(15);
    fill(0);    
    rect(250, 50, 100, 100);  //return to menu button
    fill(0xffFF00E6);
    text("Return to\nMenu", width/2, 100);  //return to menu button
    fill(0xff008EFF);
    textSize(21);
    image(cPlay, 130, 400);
    text("Using the Camera", 200, 550);
    image(kPlay, 330, 400);
    text("Using the Keyboard", 400, 550);
    text("You have 30 seconds to shoot as many imposters as you can.\nImposters are humans dressed as aliens.\nDon't shoot any aliens, or else you lose.\nYou get an extra 5 seconds for each imposter you shoot.\nThe scope will get harder to control the more points you get.\nTry to beat our scores.\nGood luck!", width/2, 200);
    pop();

    if (mousePressed && mouseX < 350 && mouseX > 250 && mouseY < 150 && mouseY > 50)  //return to menu button
    {
      screenState = 'm';  //return to the menu screen so you can choose your game mode.
    }
  } else if (screenState == 't' || screenState == 'u')//typing screen
  {
    getInput(screenState);
  } else if (screenState == 'c') //running game in camera mode
  {
    push();
    runGame();

    yv-=0.001f;  //slow vertical drift for the scope

    sx+=xv; // movement
    sy+=yv; // movement
    s.display(r);
    fill(255);
    textSize(20);
    timer();
    text(points+" points", 20, 350);

    face();

    if (timer <= 0)
    {
      TableRow new_row=table.addRow();
      new_row.setString(0, username);
      new_row.setInt(1, points);

      screenState = 'b';
    }
    pop();
  } else if (screenState == 'g') //running game in keyboard mode
  {
    push();
    runGame();

    yv-=0.001f;  //slow vertical drift for the scope

    sx+=xv; // movement
    sy+=yv; // movement
    s.display(r);
    fill(255);
    textSize(20);
    timer();
    text(points+" points", 20, 350);
    if (timer <= 0)
    {
      TableRow new_row=table.addRow();
      new_row.setString(0, username);
      new_row.setInt(1, points);

      screenState = 'b';
    }
    pop();
  } else if (screenState == 'b') //leaderboard screen
  {
    leaderboard();
    push();
    rectMode(CENTER);
    fill(150);    
    rect(300, 500, 100, 100);  //return to menu button
    fill(255);
    text("Return to\nMenu", 300, 490);  //return to menu button
    pop();
    if (mousePressed && mouseX < 350 && mouseX > 250 && mouseY < 550 && mouseY > 450)  //return to menu button
    {
      timer = 30;
      temp = 0;
      points = 0;
      username = "";
      r=0;  //disable shooting mechanism
      xv = 0;//rest scope speed
      yv = 0;
      screenState = 'm';  //return to the menu screen so you can choose your game mode.
    }
  }
}


public void keyPressed() {
  if (screenState == 'g' || screenState == 'c')
  {
    if (keyCode==32) {  //space bar for shooting
      r=201;
      shoot.play();  //sound
    }

    if (screenState == 'g')  //can only use these buttons during the game
    {
      if (keyCode==LEFT) {
        xv+=diff;
      }// key senseing
      if (keyCode==RIGHT) {
        xv-=diff;
      }
      if (keyCode==DOWN) {
        yv-=diff;
      }
      if (keyCode==UP) {
        yv+=diff;
      }
    }
  }
}

public void captureEvent(Capture c) {
  c.read();
}
public void leaderboard()  //shows the leaderboard at the end of the game
{
  push();
  background(menu);
  textSize(40);
  String[] names = new String[table.getRowCount()];  //creates strings for the names and scores
  int[] scores = new int[table.getRowCount()];
  
  table.setColumnType(1, "int");  //sets the scores to type "int" so that they can be properly sorted.
  table.sort(1);  //sorts table into correct order. 
  //for (TableRow row : table.rows()) {  //print leaderboard
  //  println(row.getString("Names") + ": " + row.getString("Score"));
  //}
  
  for (int i=0; i<table.getRowCount(); i++) {  //sets the strings to the right values. 
    TableRow row=table.getRow(i);
    names[i]=row.getString(0);
    scores[i]=row.getInt(1);
  }

  for (int i = scores.length - 1; i > -1; i--)  //displays all scores in proper order
  {
    text(names[i], 150, 50 + 50 * (scores.length - i));
    text(scores[i], width - 100, 50 + 50 * (scores.length - i));
  }
  pop();
}
public void timer() {
  textAlign(LEFT);
  temp++;
  if (temp%30 == 0)  //increases the value of timer every x frames, where x is the frame rate
  {
    timer--;
    tick.play();
    temp = 0;  //need this to prevent memory leak
  }
  fill(255);
  textSize(20);
  text("Timer: " + timer, 20, 380);  //show timer
}
public void face()  //runs the face detection code and moves scope accordingly .
{
  push();
  scale(2);
  opencv.loadImage(video);  //load camera input

  image(video, 0, 0);  //display video
  noFill();
  stroke(0, 255, 0);
  strokeWeight(3);
  Rectangle[] faces = opencv.detect();  //decet faces using processing library
  println(faces.length);

  for (int i = 0; i < faces.length; i++) {
    println(faces[i].x + "," + faces[i].y);  //display face coordinates for debugging
    rect(faces[i].x, faces[i].y, faces[i].width, faces[i].height);  //draws the rectangle on the face. 

    if (faces[i].x > 30) //move right
    {
      xv-=diff;
    }

    if (faces[i].x < 18)  //move left
    {
      xv+=diff;
    }

    if (faces[i].y > 22)  //move down
    {
      yv-=diff;
    }

    if (faces[i].y < 13)  //move up
    {
      yv+=diff;
    }
    
    if (faces[i].x > 18 && faces[i].x < 30 && faces[i].y > 13 && faces[i].y < 22)  //stop the scope if face is centered. 
    {
      xv = 0;
      yv = 0;
    }
  }


  pop();
}
public void getInput(char game)  //gets the input, the char is to determine what game state to go to after the user presses enter. 
{
  push();
  temp++;
  if (keyPressed)
  {
    if (key == '\n') {  //enter key
      username = typing;  //set username to input
      //clear temp string after finalizing
      typing = "";
      temp = 0;
      if (game == 't')  //check to see which button was pressed
        screenState = 'g';  //go to keyboard mode

      if (game == 'u')
        screenState = 'c';  //go to camera mode
    } else if (key == BACKSPACE)  //delete current string
    {
      typing = "";
    } else
    {
      background(0);
      fill(255);
      if (temp > 10)  //prevents keys being pressed too rapidly
      {
        temp = 0;
        typing += key; //adds the pressed key to the string
      }
    }
  }
  fill(255);
  background(150);  //draw instructions and the text
  textSize(20);
  text("Type your username SLOWLY", 150, 20);
  text("Press enter to continue", 150, 40);
  text("Press backspace to start over", 150, 60);
  text(typing, width/2, height/2);
  pop();
}
public void runGame()
{
 scale(1.5f);
    background(1000);
    img.resize(700, 0);
    for (int i=round(-sx/700); i<round(-sx/700)+2; i++) {    //This moves the backgroudn around, gives the illusion of moving the scope. 
      for (int p=round(-sy/450); p<round(-sy/450)+2; p++) {
        image(img, sx+(i*700)-400, sy+(p*450)-300);
      }
    }

    r -= 10; //used to reset trigger and cause red flash after a shot

    for (int i=0; i<aliens.length; i++) {
      aliens[i].display(alienImg);
      aliens[i].move(sx, sy);
      if (i < 150)  //since there are more Alien objects than Imposter objects, need this to prevent out of bounds exception
      {
        imposters[i].display(imposterImg);
        imposters[i].move(sx, sy);
      }
    }
    for (int i=0; i<aliens.length; i++) {
      if (i < imposters.length && imposters[i].x>200-100+50 && imposters[i].x<300-150+50 && imposters[i].y>200-100+50 && imposters[i].y<300-150+50 && r>150) {  //checks to see if an imposter got shot
        imposters[i]=new Imposter(this);
        i--;
        if(i<0){i=0;}
        if (diff > 0.06f)
          diff -= .03f;  //makes the game harder
        points++;
        timer+=5;
      }
      if (aliens[i].x>200-100+50 && aliens[i].x<300-150+50 && aliens[i].y>200-100+50 && aliens[i].y<300-150+50 && r>150)  //if you shoot an alien you lose
      {
        println("shotAlien");
        TableRow new_row=table.addRow();
        new_row.setString(0, username);
        new_row.setInt(1, points);
        
        lose.play();

        screenState = 'b';
      }
    } 
}
public void startupA()
{ 
  push();
  int bColor = color(200, 100, 200);
  background(menu); 
  fill(bColor); 
  circle(width/2, height/2, 500);

  //circles
  fill(0xff00ACFF);  //circle color
  circle(startupX - 150, startupY, 100);
  circle(startupX - 50, height - startupY, 100);
  circle(width - startupX + 50, startupY, 100);
  circle(width - startupX + 150, height - startupY, 100);

  //Letters
  textAlign(CENTER);
  textFont(startup);
  textSize(100);
  fill(0xffFF0000);  //text color
  text("H", startupX - 150, startupY + 30);
  text("E", startupX - 50, height - startupY + 30);
  text("L", width - startupX + 50, startupY + 30);
  text("J", width - startupX + 150, height - startupY + 30);
  pop();


  if (startupY < height/4)  //moves circles and letters for part 1 of animation
  {
    startupX += 2;  //increase these numbers to speed up / slow down animation
    startupY += 2;  //The numbers must be the same.
  } else if (startupY < height/2)  //part 2 has a different color
  {
    startupX += 2;  //increase these numbers to speed up / slow down animation
    startupY += 2;  //The numbers must be the same.
    bColor = color(100, 255, 100);  //changes background color when the animation is done.
    //transition from the end of the animation, maybe with a timer so the end shows.
  } else
  {
    ding.play();  //sounds
    imposter.play();
    screenState = 'l';  //goes to the next screen state,
  }
}
public void story()
{
  push();
  String storyS = "You were in a fancy spaceship\nbut there were imposters\ntrying to kill you.\nThey threw everyone out of the ship.\nNow you're floating in space with a gun.\nYou need to kill the imposters\nbefore they kill you and your alien buddies.\nGOOD LUCK!!!";
  background(menu);    
  fill(0xffFF00E6);    
  textFont(storyFont);
  textSize(30);
  translate(width/2, height/2); //puts the text in the center
  textAlign(CENTER);    
  text(storyS, 0, y);    //display text
  y--;  //move the text

  if (y < -300)
  {
    screenState = 'm';
  }
  pop();
}
  public void settings() {  size(600, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "hunt_imposter_AmongUs" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

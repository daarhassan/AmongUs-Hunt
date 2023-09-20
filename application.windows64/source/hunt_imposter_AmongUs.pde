import processing.sound.*;  //needed for sound effects 

import gab.opencv.*;  //for video controlled mode
import processing.video.*;
import java.awt.*;
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
float diff = .2;  //amount the scope velocity changes each time you move it, gets lower and game gets harder as you get more points.

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


void setup() {
  size(600, 600);
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

void draw() {
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


    fill(#FF00E6);
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
    fill(#FF00E6);
    text("Return to\nMenu", width/2, 100);  //return to menu button
    fill(#008EFF);
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

    yv-=0.001;  //slow vertical drift for the scope

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

    yv-=0.001;  //slow vertical drift for the scope

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


void keyPressed() {
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

void captureEvent(Capture c) {
  c.read();
}

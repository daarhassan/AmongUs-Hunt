void timer() {
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

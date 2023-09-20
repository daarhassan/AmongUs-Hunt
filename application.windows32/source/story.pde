void story()
{
  push();
  String storyS = "You were in a fancy spaceship\nbut there were imposters\ntrying to kill you.\nThey threw everyone out of the ship.\nNow you're floating in space with a gun.\nYou need to kill the imposters\nbefore they kill you and your alien buddies.\nGOOD LUCK!!!";
  background(menu);    
  fill(#FF00E6);    
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

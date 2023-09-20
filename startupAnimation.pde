void startupA()
{ 
  push();
  color bColor = color(200, 100, 200);
  background(menu); 
  fill(bColor); 
  circle(width/2, height/2, 500);

  //circles
  fill(#00ACFF);  //circle color
  circle(startupX - 150, startupY, 100);
  circle(startupX - 50, height - startupY, 100);
  circle(width - startupX + 50, startupY, 100);
  circle(width - startupX + 150, height - startupY, 100);

  //Letters
  textAlign(CENTER);
  textFont(startup);
  textSize(100);
  fill(#FF0000);  //text color
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

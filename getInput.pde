void getInput(char game)  //gets the input, the char is to determine what game state to go to after the user presses enter. 
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

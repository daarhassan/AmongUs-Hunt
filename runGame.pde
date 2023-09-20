void runGame()
{
 scale(1.5);
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
        if (diff > 0.06)
          diff -= .03;  //makes the game harder
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

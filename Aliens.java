import processing.core.*;
    
class Alien extends Crew{
  public PApplet c;
  Alien(PApplet p){
    super(p);
    c=p;
  }
  void display(PImage[] alienImg) {  //displays 4 different types of aliens depending on random s value
    if (s==0) {
      c.image(alienImg[0], x, y);
    }
    if (s==1) {
      c.image(alienImg[1], x, y);
    }
    if (s==2) {
      c.image(alienImg[2], x, y);
    }
    if (s==3) {
      c.image(alienImg[3], x, y);
    }
  }  
}

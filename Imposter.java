import processing.core.*;

class Imposter extends Crew{
  public PApplet c;
  Imposter(PApplet p){
    super(p);
    c=p;
  }
  void display(PImage[] imposterImg) {  //displays 4 different versions of the imposter depending on the random s value from super()
    if (s==0) {
      c.image(imposterImg[0], x, y);
    }
    if (s==1) {
      c.image(imposterImg[1], x, y);
    }
    if (s==2) {
      c.image(imposterImg[2], x, y);
    }
    if (s==3) {
      c.image(imposterImg[3], x, y);
    }
  }  
}

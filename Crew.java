import processing.core.*;

class Crew {  //imposters and aliens are all crew
  public PApplet p;
  public float x;
  public float y;
  public float r;
  public float r2;
  public int s;

  Crew(PApplet p) {
    this.p=p;
    x=p.random(p.width);  //randomizes location and image
    y=p.random(p.height);
    r=p.random(-2000, 2000);
    r2=p.random(-2000, 2000);
    s=p.round(p.random(0, 3));
    
    
    
  }
  void move(float sx, float sy) {  //moves the crew member to a new spot so that they aren't all on screen at once. 
    x=sx+r;
    y=sy+r2;
  }
  
}

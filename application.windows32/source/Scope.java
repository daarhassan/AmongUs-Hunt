import processing.core.*;

class Scope {  //draws the scope.
  public PApplet p;
  Scope(PApplet p) {
    this.p=p;
  }

  void display(float r) {
    p.push();
    p.fill(255, 0, 0, r);
    p.strokeWeight(300);
    p.ellipse(200, 200, 400+300, 400+300);//scope
    p.strokeWeight(2);
    p.line(200, 0, 200, 400);
    p.line(0, 200, 400, 200);
    p.line(150, 225, 250, 225);
    p.line(175, 250, 225, 250);
    p.line(212, 275, 188, 275);
    p.pop();
  }
}

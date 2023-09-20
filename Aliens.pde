class Alien extends Crew{
  Alien(){
    super();
  }
  void display() {  //displays 4 different types of aliens depending on random s value
    if (s==0) {
      image(alienImg[0], x, y);
    }
    if (s==1) {
      image(alienImg[1], x, y);
    }
    if (s==2) {
      image(alienImg[2], x, y);
    }
    if (s==3) {
      image(alienImg[3], x, y);
    }
  }  
}

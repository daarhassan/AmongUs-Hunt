void face()  //runs the face detection code and moves scope accordingly .
{
  push();
  scale(2);
  opencv.loadImage(video);  //load camera input

  image(video, 0, 0);  //display video
  noFill();
  stroke(0, 255, 0);
  strokeWeight(3);
  Rectangle[] faces = opencv.detect();  //decet faces using processing library
  println(faces.length);

  for (int i = 0; i < faces.length; i++) {
    println(faces[i].x + "," + faces[i].y);  //display face coordinates for debugging
    rect(faces[i].x, faces[i].y, faces[i].width, faces[i].height);  //draws the rectangle on the face. 

    if (faces[i].x > 30) //move right
    {
      xv-=diff;
    }

    if (faces[i].x < 18)  //move left
    {
      xv+=diff;
    }

    if (faces[i].y > 22)  //move down
    {
      yv-=diff;
    }

    if (faces[i].y < 13)  //move up
    {
      yv+=diff;
    }
    
    if (faces[i].x > 18 && faces[i].x < 30 && faces[i].y > 13 && faces[i].y < 22)  //stop the scope if face is centered. 
    {
      xv = 0;
      yv = 0;
    }
  }


  pop();
}

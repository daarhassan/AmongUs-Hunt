void leaderboard()  //shows the leaderboard at the end of the game
{
  push();
  background(menu);
  textSize(40);
  String[] names = new String[table.getRowCount()];  //creates strings for the names and scores
  int[] scores = new int[table.getRowCount()];
  
  table.setColumnType(1, "int");  //sets the scores to type "int" so that they can be properly sorted.
  table.sort(1);  //sorts table into correct order. 
  //for (TableRow row : table.rows()) {  //print leaderboard
  //  println(row.getString("Names") + ": " + row.getString("Score"));
  //}
  
  for (int i=0; i<table.getRowCount(); i++) {  //sets the strings to the right values. 
    TableRow row=table.getRow(i);
    names[i]=row.getString(0);
    scores[i]=row.getInt(1);
  }

  for (int i = scores.length - 1; i > -1; i--)  //displays all scores in proper order
  {
    text(names[i], 150, 50 + 50 * (scores.length - i));
    text(scores[i], width - 100, 50 + 50 * (scores.length - i));
  }
  pop();
}

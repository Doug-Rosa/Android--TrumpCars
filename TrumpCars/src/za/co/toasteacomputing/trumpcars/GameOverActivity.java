package za.co.toasteacomputing.trumpcars;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameOverActivity extends Activity
{
	private ArrayList<String> scoreboardList;
	private int position = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		
		//Get player's score from extras
		Intent intent = getIntent();
		int playerScore = intent.getIntExtra("playerScore", 0);
		
		//Get high scores for checking
		scoreboardList = getHighScores();
		Toast.makeText(this, "Score: " + playerScore, Toast.LENGTH_LONG).show();	
		checkScores(playerScore);
		
		//Set stats for user to see
		TextView tvStats = (TextView)findViewById(R.id.tvGameOverStats);
		
		if(position > 0) //Cehcks if player achieved a position. position only gets set above 0 if they make top 5.
		{
			tvStats.setText("Well Done!\nYou have reached the top 5 score board\nYour Score is: " + playerScore);	
			EditText highScoreName = (EditText)findViewById(R.id.etxtHighScore);
			highScoreName.setVisibility(1);
		}
		else
			tvStats.setText("Your Score is: " + playerScore);
		
		
	}
	
	public void checkScores(int playerScore)
	{
		//Check if player score is higher and works out which position they are in if they made it
		for(int i = 1; i < scoreboardList.size(); i = i+2)	//Increment by 2 cause only every second element is and actual score value (name, score)
		{
			if(playerScore < Integer.parseInt(scoreboardList.get(i)))
			{
				position = (i-2);
				break;
			}
			
			if((i+1) == scoreboardList.size())
			{
				position = i;
			}
				
		}
		
		Log.d("TRUMP CARS", "position is: " + position);
	}
	
	public ArrayList<String> getHighScores()
	{
				
		//variables for reading scoreboard data
		String line = null;
		String [] playerRecord = new String[2];
		ArrayList<String> scoreboard = new ArrayList<String>();
		File loadFile = new File(Environment.getExternalStorageDirectory(), "/trump cars/scoreboard.txt");
		FileInputStream fins;
		InputStreamReader insr;
		BufferedReader bReader;
		
		try
		{			
			fins = new FileInputStream(loadFile);			
			insr = new InputStreamReader(fins);
			bReader = new BufferedReader(insr);			
						
			//Read each line in file, which is then split into an array list and returned
			while((line = bReader.readLine()) != null)
			{	
				playerRecord = line.split(",");	
				scoreboard.add(playerRecord[0]);
				scoreboard.add(playerRecord[1]);
			}		
			
			bReader.close();
			insr.close();
			fins.close();		
			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		return scoreboard;
	}
	
	public void onClickContinue(View view)
	{
		EditText highScoreName = (EditText)findViewById(R.id.etxtHighScore);
		
		String name = "null";
		//gets player name if they made a high score
		if(highScoreName.isShown())
		{
			Intent scoreIntent = getIntent();
			int score = scoreIntent.getIntExtra("playerScore", 0);
			
			
			if(highScoreName.getText().toString().equals(""))
				name = "null";
			else if(highScoreName.getText().toString().contains(","))
				name = "Comma Error";
			else
				name = highScoreName.getText().toString();
			
			if(!(name.equals("null") || name.equals("Comma Error")))
				writeNewScoreboard(name, score); //writes new high score
			
		}
		
		if(!name.equals("Comma Error"))
		{
			Intent intent = new Intent(GameOverActivity.this, MainMenuActivity.class);
			startActivity(intent);
			finish();
		}
		else
		{
			Toast.makeText(this, "Error: Name may not contain a comma", Toast.LENGTH_LONG).show();
			highScoreName.setText("");
		}
		
			
	}
	
	public void writeNewScoreboard(String name, int score)
	{
				
		//shift score items
		for(int i = 1; i < position; i = i+2)
		{
			Log.d("Move Score Down", "Start");
			scoreboardList.set(i, scoreboardList.get(i+2).toString());	//move item score down
			Log.d("Move Score Down", "moved " + scoreboardList.get(i+2) + " to " + scoreboardList.get(i));
			scoreboardList.set(i-1, scoreboardList.get(i+1).toString()); //move item name down
			Log.d("Move Score Down", "moved " + scoreboardList.get(i+1) + " to " + scoreboardList.get(i-1));
		}
			
		//insert new score
		scoreboardList.set(position, score + "");
		scoreboardList.set(position - 1, name);
		
				
		//writes the new scoreboard
		try
		{
			Log.d("TRUMP CARS", "commencing writing");
			File newScoreboard = new File(Environment.getExternalStorageDirectory(), "/trump cars/scoreboard.txt");
			
			FileOutputStream fout = new FileOutputStream(newScoreboard);
			OutputStreamWriter writer = new OutputStreamWriter(fout);
			
			for(int i = 1; i < scoreboardList.size(); i = i+2)
			{
				writer.write(scoreboardList.get(i-1) + "," + scoreboardList.get(i) + "\n");
			}
			
			writer.flush();
			writer.close();
			fout.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}

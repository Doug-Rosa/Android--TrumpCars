package za.co.toasteacomputing.trumpcars;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

public class MainMenuActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		this.setTitle(R.string.menu_title);
		
		
		File scoreboard = new File(Environment.getExternalStorageDirectory(), "/trump cars");
		
		//Checks if scoreboard file exist from previous
		if(!scoreboard.isDirectory())
			createScoreboardFile(scoreboard);
	}
	
	public void createScoreboardFile(File scoreboard)
	{
		//Creates scoreboard file if it does not yet exist
		scoreboard.mkdir();
		File oScoreboard = new File(scoreboard, "scoreboard.txt");
		
		try
		{
			FileOutputStream fout = new FileOutputStream(oScoreboard);
			OutputStreamWriter writer = new OutputStreamWriter(fout);
			
			for(int i = 0; i < 5; i++)
				writer.write("null,0\n");
			
			writer.flush();
			writer.close();
			fout.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	public void onClickPlay(View view)
	{
		//Starts a match
		Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
		startActivity(intent);
	}
	
	public void onClickScoreboard(View view)
	{
		//Opens the scoreboard for viewing
		Intent intent = new Intent(MainMenuActivity.this, ScoreboardActivity.class);
		startActivity(intent);
	}
	
	public void onClickHelp(View view)
	{
		//Opens help for viewing
		Intent intent = new Intent(MainMenuActivity.this, HelpActivity.class);
		startActivity(intent);
	}
	
	public void onClickExit(View view)
	{	
		//Exits game and all "callback" instances are killed
		this.finishAffinity();
	}
	
}

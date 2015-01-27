package za.co.toasteacomputing.trumpcars;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ScoreboardActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scoreboard);
		
		//Read scoreboard file and output into the 5 text views
		String [] records = readScoreboardFile();
		
		boolean noRecordsSet = true;
		for(int i = 0; i < records.length; i = i+2)
		{
			if(!records[i].equalsIgnoreCase("null"))
				noRecordsSet = false;
		}
		
		TextView tvScoreName;
		TextView tvScore;
		//TODO: Only top3 is working not top 5
		if(!noRecordsSet)
		{
			if(!records[0].equalsIgnoreCase("null"))
			{
				tvScoreName = (TextView)findViewById(R.id.tvScoreName1);
				tvScoreName.setText(records[0]);
				
				tvScore = (TextView)findViewById(R.id.tvScore1);
				tvScore.setText(records[1]);
			}
			
			if(!records[2].equalsIgnoreCase("null"))
			{
				tvScoreName = (TextView)findViewById(R.id.tvScoreName2);
				tvScoreName.setText(records[2]);
				
				tvScore = (TextView)findViewById(R.id.tvScore2);
				tvScore.setText(records[3]);
			}
			
			if(!records[4].equalsIgnoreCase("null"))
			{				
				tvScoreName = (TextView)findViewById(R.id.tvScoreName3);
				tvScoreName.setText(records[4]);
				
				tvScore = (TextView)findViewById(R.id.tvScore3);
				tvScore.setText(records[5]);
			}
				
			if(!records[6].equalsIgnoreCase("null"))
			{
				tvScoreName = (TextView)findViewById(R.id.tvScoreName4);
				tvScoreName.setText(records[6]);
				
				tvScore = (TextView)findViewById(R.id.tvScore4);
				tvScore.setText(records[7]);
			}
				
			if(!records[8].equalsIgnoreCase("null"))
			{
				tvScoreName = (TextView)findViewById(R.id.tvScoreName5);
				tvScoreName.setText(records[8]);
				
				tvScore = (TextView)findViewById(R.id.tvScore5);
				tvScore.setText(records[9]);
			}
		}
	}
	
	public void onClickReset(View view)
	{
		//resets scoreboard by writing 5 'null, 0' records to the text file
		try
		{
			Log.d("TRUMP CARS", "commencing writing");
			File newScoreboard = new File(Environment.getExternalStorageDirectory(), "/trump cars/scoreboard.txt");
					
			FileOutputStream fout = new FileOutputStream(newScoreboard);
			OutputStreamWriter writer = new OutputStreamWriter(fout);
					
			for(int i = 0; i < 5; i++)
			{
				writer.write("null,0\n");
			}
					
			writer.flush();
			writer.close();
			fout.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
				
		Intent intent = getIntent();	
		startActivity(intent);
		this.finish();
		    
	}
	
	public String[] readScoreboardFile()
	{
		File scoreboard = new File(Environment.getExternalStorageDirectory(), "/trump cars/scoreboard.txt");
		String line;
		String [] scoreRecord = new String[2];
		String [] allScoreRecords = new String[10];
		
		try
		{
			FileInputStream fins = new FileInputStream(scoreboard);
			InputStreamReader insr = new InputStreamReader(fins);
			BufferedReader buffRead = new BufferedReader(insr);
			int index = 9;
			while((line = buffRead.readLine()) != null)
			{
				scoreRecord = line.split(",");				
				allScoreRecords[index] = scoreRecord[1];
				allScoreRecords[index-1] = scoreRecord[0];
				index = index-2;
			}
			
			buffRead.close();
			insr.close();
			fins.close();
			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		return allScoreRecords;
	}
}

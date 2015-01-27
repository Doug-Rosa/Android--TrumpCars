package za.co.toasteacomputing.trumpcars;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private CarCard playerCard;
	private CarCard aiCard;
	ArrayList<CarCard> carCardsList = new ArrayList<CarCard>();
	private int playerScore = 0;
	private int playerLifes = 3;
	private int powerUpFiftyFifty = 3;
	private int powerUpHint = 3;
	private int powerUpUpgrade = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Get and set player score and lives if there is any and power ups counters
		Intent intent = getIntent();
		
		if(intent.hasExtra("playerScore"))
		{
			playerScore = intent.getIntExtra("playerScore", 0);	
			powerUpUpgrade = intent.getIntExtra("powerUpUpgrade", 3);
			powerUpHint = intent.getIntExtra("powerUpHint", 2);
			powerUpFiftyFifty = intent.getIntExtra("powerUpFiftyFifty", 2);
			
		}
		
		this.setTitle("Score: " + playerScore);
		
		//Load all car card's details
		carCardsList = loadCarCards();
		
		
		int playerCardNo = randNo(carCardsList);
		int aiCardNo = randNo(carCardsList);
		
		playerCard = carCardsList.get(playerCardNo);
		aiCard = carCardsList.get(aiCardNo);
		
		//display player card
		setCard(playerCard, 'n', 'n');
						
	}
	
	public void onClickPower(View view)
	{
		//compare vs ai card
		char result = playerCard.compare(aiCard.getCarPower(), 'p');
		
		if(result == 'w')	//player wins
		{
			setCard(aiCard, 'w', 'p');	//modifies cards to highlight chosen category and show ai card with losing/win/draw message
		}
		else if(result == 'l') //player looses
		{
			setCard(aiCard, 'l', 'p');
		}
		else	
		{
			setCard(aiCard, 'e', 'p'); //a draw
		}
		
		disableButtons();
		
	}
	
	public void onClickTorque(View view)
	{
		//compare vs ai card
				char result = playerCard.compare(aiCard.getCarTorque(), 't');
				
				if(result == 'w') //player wins
				{
					setCard(aiCard, 'w', 't'); //modifies cards to highlight chosen category and show ai card with losing/win/draw message
				}
				else if(result == 'l') 
				{
					setCard(aiCard, 'l', 't'); //player looses
				}
				else
				{
					setCard(aiCard, 'e', 't'); //a draw
				}
				
				disableButtons();
	}
	
	public void onClickWeight(View view)
	{
		//compare vs ai card
				char result = playerCard.compare(aiCard.getCarWeight(), 'w');
				
				if(result == 'w') //player wins
				{
					setCard(aiCard, 'w', 'w'); //modifies cards to highlight chosen category and show ai card with losing/win/draw message
				}
				else if(result == 'l') 
				{
					setCard(aiCard, 'l', 'w'); //player looses
				}
				else
				{
					setCard(aiCard, 'e', 'w'); //a draw
				}	
				
				disableButtons();
	}
	
	public void onClickDisplacement(View view)
	{
		
		//compare vs ai card
				char result = playerCard.compare(aiCard.getCarDisplacement());			
								
				if(result == 'w') //player wins
				{
					setCard(aiCard, 'w', 'd'); //modifies cards to highlight chosen category and show ai card with losing/win/draw message
				}
				else if(result == 'l')
				{
					setCard(aiCard, 'l', 'd'); //player looses
				}
				else
				{
					setCard(aiCard, 'e', 'd'); //a draw
				}
				
				disableButtons();
	}
	
	//Used to disable buttons so that player cant play numerious categories or one category multiple times pre round
	public void disableButtons()
	{
		Button btnPower = (Button)findViewById(R.id.btnPower);
		btnPower.setEnabled(false);
		
		Button btnTorque = (Button)findViewById(R.id.btnTorque);
		btnTorque.setEnabled(false);
		
		Button btnWeight = (Button)findViewById(R.id.btnWeight);
		btnWeight.setEnabled(false);
		
		Button btnDisplacement = (Button)findViewById(R.id.btnDisplacement);
		btnDisplacement.setEnabled(false);
	}
	
	public void onClickHint(View view)
	{
		if(powerUpHint > 0)
		{
			ImageView ivPlayerCard = (ImageView)findViewById(R.id.ivPlayerCard);
			Button btnPower = (Button)findViewById(R.id.btnPower);
			Button btnTorque = (Button)findViewById(R.id.btnTorque);
			Button btnWeight = (Button)findViewById(R.id.btnWeight);
			Button btnDisplacement = (Button)findViewById(R.id.btnDisplacement);
			
			
			//Temporary display oponents car to player
			int resID = getResources().getIdentifier(aiCard.getCardImageName(), "drawable", getPackageName());
			ivPlayerCard.setImageResource(resID);
			
			btnPower.setText("    Power:    ???Kw");
			btnTorque.setText("    Torque:    ???Nm");
			btnWeight.setText("    Weight:    ????Kg");
			btnDisplacement.setText("    Displacement: ???L   ");
			
			//Dont show opponents card's stats
			Toast.makeText(this, "Your oppenent has this car!", Toast.LENGTH_LONG).show();
			
			//uses delay before moving on
			new Handler().postDelayed(new Runnable()
			{
				//resets players card
				public void run()
				{
					setCard(playerCard, 'n', 'n');
				}
			}, 2000);
			
			powerUpHint--;
		}
		else
			Toast.makeText(this, "You have used all 3 of your hints already, sorry.", Toast.LENGTH_LONG).show();
	}
	
	public void onClickUpgrade(View view)
	{	
		if(powerUpUpgrade > 0)
		{
			String upgradeCarName = playerCard.getCardUpgradeName();
			CarCard upgradeCard = playerCard;
			
			//find upgrade card obj
			for(int i = 0; i < carCardsList.size(); i++)
			{
				if(carCardsList.get(i).getCardImageName().equalsIgnoreCase(upgradeCarName))
				{
					upgradeCard = carCardsList.get(i);
				}
			}
			
			//set new upgraded card
			playerCard = upgradeCard;
			setCard(playerCard, 'n', 'n');
			powerUpUpgrade--;
		}
		else				
			Toast.makeText(this, "You have used all 3 of your upgrades already, sorry.", Toast.LENGTH_LONG).show();
	}
	
	public void onClickFiftyFifty(View view)
	{
		//TODO: Power Up 5050 now work but incorrectly 
		if(powerUpFiftyFifty > 0)
		{	
					
			Random ranGen = new Random();
			
			int winCategory = 0;
			int removeCatA = 0;
			int removeCatB = 0;
			
			if(playerCard.getCarPower() > aiCard.getCarPower())
				winCategory = 1;
			else if(playerCard.getCarTorque() > aiCard.getCarTorque())
				winCategory = 2;
			else if (playerCard.getCarWeight() < aiCard.getCarWeight())
				winCategory = 3;
			else if(playerCard.getCarDisplacement() > aiCard.getCarDisplacement())
				winCategory = 4;
			
			if(winCategory != 0)
			{
				while(removeCatA == 0 || removeCatA == winCategory)
					removeCatA = ranGen.nextInt(4);
				
				while(removeCatB == 0 || removeCatB == winCategory || removeCatB == removeCatA)
					removeCatB = ranGen.nextInt(4);
				
				Button btn;
				
				//remove categories
				if(removeCatA == 1 || removeCatB == 1)
				{
					btn = (Button) findViewById(R.id.btnPower);
					btn.setVisibility(View.INVISIBLE);
				}
				
				if(removeCatA == 2 || removeCatB == 2)
				{
					btn = (Button) findViewById(R.id.btnTorque);
					btn.setVisibility(View.INVISIBLE);
				}
				
				if(removeCatA == 3 || removeCatB == 3)
				{
					btn = (Button) findViewById(R.id.btnWeight);
					btn.setVisibility(View.INVISIBLE);
				}
				
				if(removeCatA == 4 || removeCatB == 4)
				{
					btn = (Button) findViewById(R.id.btnDisplacement);
					btn.setVisibility(View.INVISIBLE);
				}
				
				powerUpFiftyFifty--;
			}
			else
			{
				Toast.makeText(this, "You cannot win this round, so you get to keep your power up", Toast.LENGTH_LONG).show();
			}		
			
		}
		else
			Toast.makeText(this, "You have used all 3 of your 50/50s already, sorry.", Toast.LENGTH_LONG).show();
	}
	
	//Result & category param only used if a comparison has been made
	public void setCard(CarCard Card, char result, char category)
	{  
		ImageView ivPlayerCard = (ImageView)findViewById(R.id.ivPlayerCard);
		ImageView ivChooped = (ImageView)findViewById(R.id.ivChooped);
		Button btnPower = (Button)findViewById(R.id.btnPower);
		Button btnTorque = (Button)findViewById(R.id.btnTorque);
		Button btnWeight = (Button)findViewById(R.id.btnWeight);
		Button btnDisplacement = (Button)findViewById(R.id.btnDisplacement);
		
		//Set power up upgrade visible if there is an upgrade option
		if(!Card.getCardUpgradeName().equalsIgnoreCase("null"))
		{
			Button btnUpgrade = (Button)findViewById(R.id.btnUpgrade);
			btnUpgrade.setVisibility(1);
		}
		
		//modifies cards to highlight chosen category and show ai card with losing/win message
		if(result == 'w')
		{
			if(category == 'p')
				btnPower.setTextColor(getResources().getColor(R.color.green));
			else if(category == 't')
				btnTorque.setTextColor(getResources().getColor(R.color.green));
			else if(category == 'w')
				btnWeight.setTextColor(getResources().getColor(R.color.green));
			else if(category == 'd')
				btnDisplacement.setTextColor(getResources().getColor(R.color.green));
								
			//add score			
			playerScore++;
			
		}
		
		if(result == 'l')
		{
			if(category == 'p')
				btnPower.setTextColor(getResources().getColor(R.color.red));
			else if(category == 't')
				btnTorque.setTextColor(getResources().getColor(R.color.red));
			else if(category == 'w')
				btnWeight.setTextColor(getResources().getColor(R.color.red));
			else if(category == 'd')
				btnDisplacement.setTextColor(getResources().getColor(R.color.red));
			
			ivChooped.setImageResource(R.drawable.chopped_by);
			ivChooped.setVisibility(1);
			
			playerLifes--;	
			
			//Check that player still has lives left otherwise end game
			if(playerLifes < 1)
			{
				Intent intent = new Intent(MainActivity.this, GameOverActivity.class);
				intent.putExtra("playerScore", playerScore);
				startActivity(intent);
				result = 'n';
				finish();
			}
			
		}
		else if(result == 'e')
		{
			if(category == 'p')
				btnPower.setTextColor(getResources().getColor(R.color.green));
			else if(category == 't')
				btnTorque.setTextColor(getResources().getColor(R.color.green));
			else if(category == 'w')
				btnWeight.setTextColor(getResources().getColor(R.color.green));
			else if(category == 'd')
				btnDisplacement.setTextColor(getResources().getColor(R.color.green));
			
			ivChooped.setImageResource(R.drawable.chopped_draw);
			ivChooped.setVisibility(1);
		}
		
		//Update all views
		int resID = getResources().getIdentifier(Card.getCardImageName(), "drawable", getPackageName());
		ivPlayerCard.setImageResource(resID);
		
		btnPower.setText("    Power:    " + Card.getCarPower() + "Kw");
		btnTorque.setText("    Torque:    " + Card.getCarTorque() + "Nm");
		btnWeight.setText("    Weight:    " + Card.getCarWeight() + "Kg");
		btnDisplacement.setText("    Displacement: " + Card.getCarDisplacement() + "L   ");
		
		if(result == 'w')
		{
			ivChooped.setImageResource(R.drawable.chopped);
			ivChooped.setVisibility(1);
							
		}	
		
		//n(null) is passed if the game should draw a new card yet
		if(result != 'n')
		{
			//uses delay before moving on
			new Handler().postDelayed(new Runnable()
			{
				
				public void run()
				{
					//Restart activity to draw a new card
					Intent intent = getIntent();
					intent.putExtra("playerScore", playerScore);
					intent.putExtra("playerLifes", playerLifes);
					intent.putExtra("powerUpUpgrade", powerUpUpgrade);
					intent.putExtra("powerUpHint", powerUpHint);
					intent.putExtra("powerUpFiftyFifty", powerUpFiftyFifty);
					finish();
					startActivity(intent);
				}
			}, 1500);
		}
				
	}
	
	//Generates a random number to a maximum of the number of cars in the game
	//Used to randomly draw a card for the player and computer
	public int randNo(ArrayList<CarCard> carCardsList)
	{
		int randNo = -1;
		
		Random ranGen = new Random();
		randNo = ranGen.nextInt(carCardsList.size());
		
		return randNo;
	}
	
	//Loads all cars int CarCard array list of objs
	public ArrayList<CarCard> loadCarCards()
	{
		ArrayList<CarCard> carCardsList = new ArrayList<CarCard>();
		
		AssetManager aMan = this.getResources().getAssets();
		
		//variables for reading card data
		String line = null;
		String [] cardDetails = new String[6];
		InputStream ins;
		InputStreamReader insr;
		BufferedReader bReader;
		
		try
		{
			
			ins = aMan.open("car_list.txt");			
			insr = new InputStreamReader(ins);
			bReader = new BufferedReader(insr);			
						
			//Read each line in file, which is then split into and array afterwhich an arrayList of card obj are made and returned
			while((line = bReader.readLine()) != null)
			{				
				cardDetails = line.split(",");			
								
				CarCard tempCardObj = new CarCard(cardDetails[0], cardDetails[1], cardDetails[2], cardDetails[3], cardDetails[4], cardDetails[5]);
				carCardsList.add(tempCardObj);				
			}		
			
			bReader.close();
			insr.close();
			ins.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();			
		}
		
		return carCardsList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		
		super.onCreateOptionsMenu(menu);
		
		Intent intent = getIntent();
		
		//Gets the amount of lifes the player has left
		if(intent.hasExtra("playerLifes"))
		{
			playerLifes = intent.getIntExtra("playerLifes", 3);
		}
		
		//Adds the life icons according to number of lives player has left
		for(int i = 0; i < playerLifes; i++)
		{
			//Add life icons button
			MenuItem mnuiLife = menu.add(0, 0, 0, "Life");
			{
				mnuiLife.setIcon(R.drawable.piston_heart);
				mnuiLife.setEnabled(false);
				mnuiLife.setShowAsActionFlags(1);
			}
		}				
		
		return true;
	}

}

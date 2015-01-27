package za.co.toasteacomputing.trumpcars;

public class CarCard 
{
	
	private String cardImageName;
	private int carPower;
	private int carTorque;
	private int carWeight;
	private double carDisplacement;
	private String cardUpdgradeName;
	
	public CarCard(String cardImageName, int carPower, int carTorque, int carWeight, double carDisplacement, String cardUpgradeName)
	{
		this.cardImageName = cardImageName;
		this.carPower = carPower;
		this.carTorque = carTorque;
		this.carWeight = carWeight;
		this.carDisplacement = carDisplacement;
		this.cardUpdgradeName = cardUpgradeName;
	}
	
	public CarCard(String cardImageName, String carPower, String carTorque, String carWeight, String carDisplacement, String cardUpgradeName)
	{
		this.cardImageName = cardImageName;
		this.carPower = Integer.parseInt(carPower);
		this.carTorque = Integer.parseInt(carTorque);
		this.carWeight = Integer.parseInt(carWeight);
		this.carDisplacement = Double.parseDouble(carDisplacement);
		this.cardUpdgradeName = cardUpgradeName;
	}
	
	//compares a value given to this cards same category's value to determine which car trumps the other
	public char compare(int value, char category)
	{
		char result = 'e';	//w for win, e for equal, l for lose
		
		switch(category)
		{
		case 'p':
			if(carPower > value)
				result = 'w';
			else if(carPower < value)
				result = 'l';
						
			break;
			
		case 't':
			if(carTorque > value)
				result = 'w';
			else if(carTorque < value)
				result = 'l';
						
			break;
			
		case 'w':
			if(carWeight < value)	//lighter car wins
				result = 'w';
			else if(carWeight > value)
				result = 'l';
						
			break;
		
		}
		
		return result;
	}
	
	public char compare(double value)
	{
		char result = 'e';
		
		if(carDisplacement > value)
			result = 'w';
		else if(carDisplacement < value)
			result = 'l';
				
		return result;
	}
	
	public String getCardImageName()
	{
		return cardImageName;
	}
	
	public int getCarPower()
	{
		return carPower;
	}
	
	public int getCarTorque()
	{
		return carTorque;
	}
	
	public int getCarWeight()
	{
		return carWeight;
	}
	
	public double getCarDisplacement()
	{
		return carDisplacement;
	}
	
	public String getCardUpgradeName()
	{
		return cardUpdgradeName;
	}

}

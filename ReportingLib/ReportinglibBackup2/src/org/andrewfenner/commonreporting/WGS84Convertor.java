package org.andrewfenner.commonreporting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WGS84Convertor {
	private double longitude =0;
	private double latidute = 0;
	private int easting;
	private int northing;
	private String letter;
	
	private static String[][] GT_Irish_prefixes = new String[][] {
		{"V", "Q", "L", "F", "A"},
		{"W", "R", "M", "G", "B"},
			{	"X", "S", "N", "H", "C"},
			{"Y", "T", "O", "J", "D"},
				{"Z", "U", "P", "K", "E"}};
	public WGS84Convertor(double longitude, double latidute) {
		super();
		this.longitude = longitude;
		this.latidute = latidute;
		convert();
	}
	
	private void convert() {
		
		 double e = lat_Long_to_East (latidute,longitude,6377340.189,6356034.447, 200000,1.000035,53.50000,-8.00000);
		 double n = lat_Long_to_North(latidute,longitude,6377340.189,6356034.447, 200000,250000,1.000035,53.50000,-8.00000);
		 convert(6,n,e);
		
	}
	//PHI, LAM, a, b, e0, n0, f0, PHI0, LAM0
	private double lat_Long_to_North(double PHI, double LAM,
			double a, double b, int e0, int n0, double f0, double PHI0, double LAM0) {
			double Pi = 3.14159265358979;
		    double RadPHI = PHI * (Pi / 180);
		    double RadLAM = LAM * (Pi / 180);
		    double RadPHI0 = PHI0 * (Pi / 180);
		    double RadLAM0 = LAM0 * (Pi / 180);
		    
		    double af0 = a * f0;
		    double bf0 = b * f0;
		    double e2 = (Math.pow(af0,2) - Math.pow(bf0,2)) / Math.pow(af0,2);
		    double n = (af0 - bf0) / (af0 + bf0);
		    double nu = af0 / (Math.sqrt(1 - (e2 * Math.pow(Math.sin(RadPHI),2))));
		    double rho = (nu * (1 - e2)) / (1 - (e2 * Math.pow(Math.sin(RadPHI),2)));
		    double eta2 = (nu / rho) - 1;
		    double p = RadLAM - RadLAM0;
		    double M = marc(bf0, n, RadPHI0, RadPHI);
		    
		    double I = M + n0;
		    double II = (nu / 2) * (Math.sin(RadPHI)) * (Math.cos(RadPHI));
		    double III = ((nu / 24) * (Math.sin(RadPHI)) * (Math.pow(Math.cos(RadPHI),3))) * (5 - (Math.pow(Math.tan(RadPHI),2)) + (9 * eta2));
		    double IIIA = ((nu / 720) * (Math.sin(RadPHI)) * (Math.pow(Math.cos(RadPHI),5))) * (61 - (58 * (Math.pow(Math.tan(RadPHI),2))) + (Math.pow(Math.tan(RadPHI),4)));
		    
		    return I + (Math.pow(p,2) * II) + (Math.pow(p,4) * III) + (Math.pow(p,6) * IIIA);
	}
	// bf0, n, PHI0, PHI
	private double marc(double bf0, double n, double PHI0, double PHI) {
		return bf0 * (((1 + n + ((5 / 4) * Math.pow(n,2)) + ((5 / 4) * Math.pow(n,3))) * (PHI - PHI0)) - (((3 * n) + (3 * Math.pow(n,2)) + ((21 / 8) * Math.pow(n,3))) * (Math.sin(PHI - PHI0)) * (Math.cos(PHI + PHI0))) + ((((15 / 8
		) * Math.pow(n,2)) + ((15 / 8) * Math.pow(n,3))) * (Math.sin(2 * (PHI - PHI0))) * (Math.cos(2 * (PHI + PHI0)))) - (((35 / 24) * Math.pow(n,3)) * (Math.sin(3 * (PHI - PHI0))) * (Math.cos(3 * (PHI + PHI0)))));

	}
//	PHI, LAM, a, b, e0, f0, PHI0, LAM0
	private double lat_Long_to_East(double PHI, double LAM,
			double a, double b, int e0, double f0, double PHI0, double LAM0) {
		// Convert angle measures to radians
	    double Pi = 3.14159265358979;
	    double RadPHI = PHI * (Pi / 180);
	    double RadLAM = LAM * (Pi / 180);
	    double RadPHI0 = PHI0 * (Pi / 180);
	    double RadLAM0 = LAM0 * (Pi / 180);

	    double af0 = a * f0;
	    double bf0 = b * f0;
	    double e2 = (Math.pow(af0,2) - Math.pow(bf0,2)) / Math.pow(af0,2);
	    double n = (af0 - bf0) / (af0 + bf0);
	    double nu = af0 / (Math.sqrt(1 - (e2 * Math.pow(Math.sin(RadPHI),2) )));
	    double rho = (nu * (1 - e2)) / (1 - (e2 * Math.pow(Math.sin(RadPHI),2) ));
	    double eta2 = (nu / rho) - 1;
	    double p = RadLAM - RadLAM0;
	    
	    double IV = nu * (Math.cos(RadPHI));
	    double V = (nu / 6) * ( Math.pow(Math.cos(RadPHI),3)) * ((nu / rho) - (Math.pow(Math.tan(RadPHI),2)));
	    double VI = (nu / 120) * (Math.pow(Math.cos(RadPHI),5)) * (5 - (18 * (Math.pow(Math.tan(RadPHI),2))) + (Math.pow(Math.tan(RadPHI),4)) + (14 * eta2) - (58 * (Math.pow(Math.tan(RadPHI),2)) * eta2));
	    
	    return e0 + (p * IV) + (Math.pow(p,3) * V) + (Math.pow(p,5) * VI);
		
	}

	private String fixStringLength(int input)
	{
		String result = ""+input;
		if (result.length() == 1)
		{
			result ="0000"+result;
		}
		else if (result.length() == 2)
		{
			result ="000"+result;
		}
		else if (result.length() == 3)
		{
			result ="00"+result;
		}
		else if (result.length() == 4)
		{
			result ="0"+result;
		}
		else if (result.length() > 5)
		{
			result = result.substring(0, 4);
		}
		
		return result;

	}
	public int getEasting()
	{
		
		return easting;
	}
	public int getNorthing()
	{
		return northing;
	}
	public String getLetter()
	{
		return letter;
	}
	public String getString()
	{
		String result = getLetter()+" " + fixStringLength(getEasting()) +" " + fixStringLength(getNorthing());
		return result;
	}
	
	
	public static void main(String args[])
	{
		double longitude = -7.82536 ;
		double latitude = 53.55861;
		System.out.println("input lat " + latitude + " " + longitude );
		/*
		 * 
		 * normal Grid Ref = N 115 565
10 digit Grid Ref = N 11573 56538
Latitude = 53.55861 (north)
Longitude = -7.82536 (west)
Latitude = 53°33'31" (north)
Longitude = 7°49'31" (west)
Tetrad = N15D

		 */
		WGS84Convertor xxx =new WGS84Convertor(longitude,latitude);
		
		System.out.println("getString " + xxx.getString());
		
		xxx.parseGridRef(xxx.getString(),2);
		
		double[] wgs84 = xxx.getWGS84();
		for(int i = 0; i < 2;i++)
		{
			System.out.println("lat " + wgs84[i]);
			
		}
	}
	
	private void  convert(int precision,double north,double east)
	{
		
	
	if (precision<0)
		precision=0;
	if (precision>5)
		precision=5;
		
	double e = 0;

	double n=0;
	if (precision>0)
	{
		int y=(int)Math.floor(north/100000);
		int x=(int)Math.floor(east/100000);


		 e=Math.floor(east%100000);
		 n=Math.floor(north%100000);


		double div=(5-precision);
		e=Math.floor(e / Math.pow(10, div));
		n=Math.floor(n / Math.pow(10, div));
		letter=GT_Irish_prefixes[x][y];
	}
	
	
	
	easting = (int)e;
	northing =(int)n;
	System.out.println("easting " + easting +" " + northing);
	}
	
	
	public boolean parseGridRef (String landranger,int  size)
	{
		boolean ok=false;

		
		this.northing=0;
		this.easting=0;
		
		int precision;

		for (precision=5; precision>=1; precision--)
		{
			Pattern pattern =  Pattern.compile("^([A-Z]{1})\\s*(\\d{"+precision+"})\\s*(\\d{"+precision+"})$");
			Matcher gridRef = pattern.matcher(landranger);
			if (gridRef.matches())
			{
				
				String gridSheet = gridRef.group();// [1];
			
				double gridEast=0;
				double gridNorth=0;
				String[] gridRefArray = gridSheet.split(" ");  
				gridSheet = gridRefArray[0];
				String east = gridRefArray[1].substring(0, size);
				String north = gridRefArray[2].substring(0, size);
				
				//5x1 4x10 3x100 2x1000 1x10000 
				if (precision>0)
				{
					double mult=Math.pow(10, 5-size);
					gridEast=Integer.parseInt(east) * mult;
					gridNorth=Integer.parseInt(north,10) * mult;
				}
				
				int x,y;
				search: for(x=0; x<GT_Irish_prefixes.length; x++) 
				{
					for(y=0; y<GT_Irish_prefixes[x].length; y++)
						if (GT_Irish_prefixes[x][y].equalsIgnoreCase( gridSheet)) {
							this.easting = (x * 100000)+(int)gridEast;
							this.northing = (y * 100000)+(int)gridNorth;
							ok=true;
							break search;
						}
				
				}
			
			}
		}

		System.out.println("easting " + easting +" " + northing);

		return ok;
	}

	
	
	public double[] getWGS84 ()
	{

		int height = 0;

		
		double	e = this.easting;//-49;
		double	n = this.northing;//+23.4;
		

		double lat1 = E_N_to_Lat (e,n,6377340.189,6356034.447,200000,250000,1.000035,53.50000,-8.00000);
		double lon1 = E_N_to_Long(e,n,6377340.189,6356034.447,200000,250000,1.000035,53.50000,-8.00000);

		//var wgs84=new GT_WGS84();
		double result[] = new double[]{lat1,lon1};
		return result;
	}

	
	double E_N_to_Lat (double East, double North, double a, double b, double e0, double n0, double f0, double PHI0, double LAM0)
	{
		//Un-project Transverse Mercator eastings and northings back to latitude.
		//Input: - _
		//eastings (East) and northings (North) in meters; _
		//ellipsoid axis dimensions (a & b) in meters; _
		//eastings (e0) and northings (n0) of false origin in meters; _
		//central meridian scale factor (f0) and _
		//latitude (PHI0) and longitude (LAM0) of false origin in decimal degrees.

		//'REQUIRES THE "Marc" AND "InitialLat" FUNCTIONS

		//Convert angle measures to radians
		double Pi = 3.14159265358979;
		double RadPHI0 = PHI0 * (Pi / 180);
		double RadLAM0 = LAM0 * (Pi / 180);

		//Compute af0, bf0, e squared (e2), n and Et
		double af0 = a * f0;
		double bf0 = b * f0;
		double e2 = (Math.pow(af0,2) - Math.pow(bf0,2)) / Math.pow(af0,2);
		double n = (af0 - bf0) / (af0 + bf0);
		double Et = East - e0;

		//Compute initial value for latitude (PHI) in radians
		double PHId = InitialLat(North, n0, af0, RadPHI0, n, bf0);
	    
		//Compute nu, rho and eta2 using value for PHId
		double nu = af0 / (Math.sqrt(1 - (e2 * ( Math.pow(Math.sin(PHId),2)))));
		double rho = (nu * (1 - e2)) / (1 - (e2 * Math.pow(Math.sin(PHId),2)));
		double eta2 = (nu / rho) - 1;
	    
		//Compute Latitude
		double VII = (Math.tan(PHId)) / (2 * rho * nu);
		double VIII = ((Math.tan(PHId)) / (24 * rho * Math.pow(nu,3))) * (5 + (3 * (Math.pow(Math.tan(PHId),2))) + eta2 - (9 * eta2 * (Math.pow(Math.tan(PHId),2))));
		double IX = ((Math.tan(PHId)) / (720 * rho * Math.pow(nu,5))) * (61 + (90 * ((int)Math.tan(PHId)^2)) + (45 * (Math.pow(Math.tan(PHId),4))));
	    int temp = (int)Et^6;
		double E_N_to_Lat = (180 / Pi) * (PHId - (Math.pow(Et,2) * VII) + (Math.pow(Et,4) * VIII) - (temp) * IX);
		
		return (E_N_to_Lat);
	}

	double E_N_to_Long(double East, double North,double a,double b,double e0,double n0,double f0,double PHI0,double LAM0)
	{
		//Un-project Transverse Mercator eastings and northings back to longitude.
		//Input: - _
		//eastings (East) and northings (North) in meters; _
		//ellipsoid axis dimensions (a & b) in meters; _
		//eastings (e0) and northings (n0) of false origin in meters; _
		//central meridian scale factor (f0) and _
		//latitude (PHI0) and longitude (LAM0) of false origin in decimal degrees.

		//REQUIRES THE "Marc" AND "InitialLat" FUNCTIONS

		//Convert angle measures to radians
	    double Pi = 3.14159265358979;
	    double RadPHI0 = PHI0 * (Pi / 180);
	    double RadLAM0 = LAM0 * (Pi / 180);

		//Compute af0, bf0, e squared (e2), n and Et
	    double af0 = a * f0;
	    double bf0 = b * f0;
	    double e2 = (Math.pow(af0,2) - Math.pow(bf0,2)) / Math.pow(af0,2);
	    double n = (af0 - bf0) / (af0 + bf0);
	    double Et = East - e0;

		//Compute initial value for latitude (PHI) in radians
	    double PHId = InitialLat(North, n0, af0, RadPHI0, n, bf0);
	    
		//Compute nu, rho and eta2 using value for PHId
	    double nu = af0 / (Math.sqrt(1 - (e2 * (Math.pow(Math.sin(PHId),2)))));
	    double rho = (nu * (1 - e2)) / (1 - (e2 * Math.pow(Math.sin(PHId),2)));
	    double eta2 = (nu / rho) - 1;

		//Compute Longitude
	    double X = (Math.pow(Math.cos(PHId),-1)) / nu;
	    double XI = ((Math.pow(Math.cos(PHId),-1)) / (6 * Math.pow(nu,3))) * ((nu / rho) + (2 * (Math.pow(Math.tan(PHId),2))));
	    double XII = ((Math.pow(Math.cos(PHId),-1)) / (120 * Math.pow(nu,5))) * (5 + (28 * (Math.pow(Math.tan(PHId),2))) + (24 * (Math.pow(Math.tan(PHId),4))));
	    double XIIA = ((Math.pow(Math.cos(PHId),-1)) / (5040 * Math.pow(nu,7))) * (61 + (662 * (Math.pow(Math.tan(PHId),2))) + (1320 * (Math.pow(Math.tan(PHId),4))) + (720 * (Math.pow(Math.tan(PHId),6))));

	    double E_N_to_Long = (180 / Pi) * (RadLAM0 + (Et * X) - (Math.pow(Et,3) * XI) + (Math.pow(Et,5) * XII) - (Math.pow(Et,7) * XIIA));
		
		return E_N_to_Long;
	}

	double InitialLat(double North,double n0,double afo,double PHI0,double n,double bfo)
	{
		//Compute initial value for Latitude (PHI) IN RADIANS.
		//Input: - _
		//northing of point (North) and northing of false origin (n0) in meters; _
		//semi major axis multiplied by central meridian scale factor (af0) in meters; _
		//latitude of false origin (PHI0) IN RADIANS; _
		//n (computed from a, b and f0) and _
		//ellipsoid semi major axis multiplied by central meridian scale factor (bf0) in meters.
	 
		//REQUIRES THE "Marc" FUNCTION
		//THIS FUNCTION IS CALLED BY THE "E_N_to_Lat", "E_N_to_Long" and "E_N_to_C" FUNCTIONS
		//THIS FUNCTION IS ALSO USED ON IT'S OWN IN THE  "Projection and Transformation Calculations.xls" SPREADSHEET

		//First PHI value (PHI1)
		double PHI1 = ((North - n0) / afo) + PHI0;
	    
		//Calculate M
		double M = marc(bfo, n, PHI0, PHI1);
	    
		//Calculate new PHI value (PHI2)
		double PHI2 = ((North - n0 - M) / afo) + PHI1;
	    
		//Iterate to get final value for InitialLat
		while (Math.abs(North - n0 - M) > 0.00001) 
		{
	        PHI2 = ((North - n0 - M) / afo) + PHI1;
	        M = marc(bfo, n, PHI0, PHI2);
	        PHI1 = PHI2;
		}    
	    return PHI2;
	}
}

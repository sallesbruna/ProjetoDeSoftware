package exercicio;

import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Util
{	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private static NumberFormat nf = NumberFormat.getInstance(new Locale("pt","BR"));

	static
	{	nf.setMaximumFractionDigits (2);	   // O default é 3.
		nf.setMinimumFractionDigits (2);
		nf.setGroupingUsed(false);
	}

	public static boolean dataValida(String umaData)
	{	try
		{	if(umaData.length() != 10) return false;
		
			Integer.parseInt(umaData.substring(0,2));
			if (!umaData.substring(2,3).equals("/")) return false; 
			Integer.parseInt(umaData.substring(3,5));
			if (!umaData.substring(5,6).equals("/")) return false; 
			Integer.parseInt(umaData.substring(6,10));
		
			sdf.setLenient(false);
			sdf.parse(umaData);
			return true; 
		}
		catch(Exception e)
		{	return false;
		} 
	}

	public static Date strToDate(String umaData)
	{	
		String dia = umaData.substring(0,2);
		String mes = umaData.substring(3,5);
		String ano = umaData.substring(6,10);

		return java.sql.Date.valueOf(ano + "-" + mes + "-" + dia);
	}

	public static String dateToStr(Date umaData)
	{	return sdf.format(umaData);
	}

	public static double strToDouble(String valor)
		throws NumberFormatException
	{	if (valor == null || "".equals(valor))
		{	return 0;
		}
		else   
		{	return Double.parseDouble(valor);
		}		
	}
	
	public static String doubleToStr(double valor)
	{	return nf.format(valor);
	}		
}
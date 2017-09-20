package exercicio;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class FabricaDeDAOs
{	
	private static ResourceBundle prop;
	
	static
	{	try
		{	prop = ResourceBundle.getBundle("dao");
		}
		catch(MissingResourceException e)
		{	System.out.println("Aquivo dao.properties não encontrado.");
			System.exit(1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getDAO(Class<T> tipo)
	{			
		String nomeDaClasse = null; 
		T umDAO = null;
		
		try
		{	nomeDaClasse = prop.getString(tipo.getSimpleName());
			umDAO = (T)Class.forName(nomeDaClasse).newInstance();
		} 
		catch (InstantiationException e)
		{	System.out.println("Não foi possível criar um objeto do tipo " + nomeDaClasse);
			System.exit(1);
		} 
		catch (IllegalAccessException e)
		{	System.out.println("Não foi possível criar um objeto do tipo " + nomeDaClasse);
			System.exit(1);
		} 
		catch (ClassNotFoundException e)
		{	System.out.println("Classe " + nomeDaClasse + " não encontrada");
			System.exit(1);
		}
		catch(MissingResourceException e)
		{	System.out.println("Chave " + tipo + " não encontrada em dao.properties");
			System.exit(1);
		}
		
		return umDAO;
	}
}

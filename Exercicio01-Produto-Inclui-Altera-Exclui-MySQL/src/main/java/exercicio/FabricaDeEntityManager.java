package exercicio;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class FabricaDeEntityManager {
	public static final String PERSISTENCE_UNIT = "exercicio01";

	private static FabricaDeEntityManager fabrica = null;
	private EntityManagerFactory emf = null;
			
	private FabricaDeEntityManager()
	{	
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
	}

	public static EntityManager criarSessao()
	{	if (fabrica == null)
		{	fabrica = new FabricaDeEntityManager();
		}	

		return fabrica.emf.createEntityManager();
	}

	public static void fecharFabricaDeEntityManager()
	{	if (fabrica != null)
			if (fabrica.emf != null)
				fabrica.emf.close();
	}
}
package exercicio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;

public class ProdutoDAOImpl implements ProdutoDAO
{	
	public long inclui(Produto umProduto) 
	{	EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	// transiente - objeto novo: ainda n�o persistente
			// persistente - apos ser persistido 
			// destacado - objeto persistente n�o vinculado a um entity manager
		
			em = FabricaDeEntityManager.criarSessao();
			tx = em.getTransaction();
			tx.begin();

			em.persist(umProduto);
			
			tx.commit();
			
			return umProduto.getId();
		} 
		catch(RuntimeException e)
		{	if (tx != null)
			{	try
				{
					tx.rollback();
				}
				catch(RuntimeException he)
				{ }
			}
			throw e;
		}
		finally
		{	em.close();	
		}
	}

	public void altera(Produto umProduto) throws AplicacaoException
	{	EntityManager em = null;
		EntityTransaction tx = null;
		Produto p = null;
		try
		{	
			em = FabricaDeEntityManager.criarSessao();
			tx = em.getTransaction();
			tx.begin();
			
			p = em.getReference(Produto.class, umProduto.getId());
	
			em.merge(umProduto);

			// Sem a execu��o de em.getReference(), no momento do merge() ir� 
			// ocorrer um select para que o objeto cliente possa ser colocado
			// no  contexto de  persist�ncia  para  que,  em  seguida,  possa
			// acontecer o merge. No entanto, se o cliente n�o for encontrado
			// no banco de dados, ele ser� inserido.
			
			if (umProduto == p)
				System.out.println(">>>>>>>>>>>>  SAO IGUAIS");
			else
				System.out.println(">>>>>>>>>>>>  NAO SAO IGUAIS");

			// Se n�o houver  uma inst�ncia  persistente igual no contexto de
			// persist�ncia, e  se uma  busca por  identificador  no banco de
			// dados  for  negativa, a  inst�ncia  umProduto ser� inserida no 
			// banco de dados.
			
			tx.commit();
		} 
		catch(EntityNotFoundException e)
		{	if (tx != null)
		    {   try
		        {   
		    		tx.rollback();
		        	System.out.println(">>>>>>>>>>>> Efetuou Rollback.");
		        }
		        catch(RuntimeException he)
		        {	throw he;
		        }
		    }

			throw new AplicacaoException("Produto n�o encontrado");
		}
		catch(RuntimeException e)
		{   System.out.println(">>>>>>>>>>>> Ocorreu um RuntimeException.");
			if (tx != null)
		    {   try
		        {   
		    		tx.rollback();
		        	System.out.println(">>>>>>>>>>>> Efetuou Rollback.");
		        }
		        catch(RuntimeException he)
		        { }
		    }
			else
			{	System.out.println(">>>>>>>>>>>> N�o Efetuou Rollback.");
			}
		    throw e;
		}
		finally
		{   em.close();
		}
	}

	public void exclui(long numero) throws AplicacaoException 
	{	EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	
			em = FabricaDeEntityManager.criarSessao();
			tx = em.getTransaction();
			tx.begin();

			Produto p = em.getReference(Produto.class, numero);
			
			em.remove(p);

			tx.commit();
		} 
		catch(EntityNotFoundException e)
		{	if (tx != null)
		    {   try
		        {   
		    		tx.rollback();
		        }
		        catch(RuntimeException he)
		        { 
		        	throw he;
		        }
		    }
			
			throw new AplicacaoException("Produto n�o encontrado.");
		}
		catch(RuntimeException e)
		{   if (tx != null)
		    {   try
		        {   
		    		tx.rollback();
		        }
		        catch(RuntimeException he)
		        { }
		    }
		    throw e;
		}
		finally
		{   em.close();
		}
	}

	public Produto recuperaUmProduto(long numero) throws AplicacaoException
	{	EntityManager em = null;
		
		try
		{	
			em = FabricaDeEntityManager.criarSessao();

			Produto umProduto = em.find(Produto.class, new Long(numero));
			
			// Caracter�sticas no m�todo find():
			// 1. � gen�rico: n�o requer um cast.
			// 2. Sempre efetua um acesso ao banco de dados, logo, a 
			//    inst�ncia � sempre inicializada (n�o retorna um proxy).
			// 3. Retorna null caso a linha n�o seja encontrada no banco.

			if(umProduto == null)
				throw new AplicacaoException("Produto n�o encontrado");
			
			return umProduto;
		} 
		finally
		{   em.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> recuperaProdutos()
	{	EntityManager em = null;
		
		try
		{	
			em = FabricaDeEntityManager.criarSessao();

			List<Produto> produtos = em
				.createQuery("select p from Produto p order by p.id")
				.getResultList();

			// Retorna um List vazio caso a tabela correspondente esteja vazia.
			
			return produtos;
		} 
		finally
		{   em.close();
		}
	}
}
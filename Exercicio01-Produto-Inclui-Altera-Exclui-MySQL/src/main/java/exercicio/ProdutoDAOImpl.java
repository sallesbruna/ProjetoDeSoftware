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
		{	// transiente - objeto novo: ainda não persistente
			// persistente - apos ser persistido 
			// destacado - objeto persistente não vinculado a um entity manager
		
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

			// Sem a execução de em.getReference(), no momento do merge() irá 
			// ocorrer um select para que o objeto cliente possa ser colocado
			// no  contexto de  persistência  para  que,  em  seguida,  possa
			// acontecer o merge. No entanto, se o cliente não for encontrado
			// no banco de dados, ele será inserido.
			
			if (umProduto == p)
				System.out.println(">>>>>>>>>>>>  SAO IGUAIS");
			else
				System.out.println(">>>>>>>>>>>>  NAO SAO IGUAIS");

			// Se não houver  uma instância  persistente igual no contexto de
			// persistência, e  se uma  busca por  identificador  no banco de
			// dados  for  negativa, a  instância  umProduto será inserida no 
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

			throw new AplicacaoException("Produto não encontrado");
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
			{	System.out.println(">>>>>>>>>>>> Não Efetuou Rollback.");
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
			
			throw new AplicacaoException("Produto não encontrado.");
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
			
			// Características no método find():
			// 1. É genérico: não requer um cast.
			// 2. Sempre efetua um acesso ao banco de dados, logo, a 
			//    instância é sempre inicializada (não retorna um proxy).
			// 3. Retorna null caso a linha não seja encontrada no banco.

			if(umProduto == null)
				throw new AplicacaoException("Produto não encontrado");
			
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
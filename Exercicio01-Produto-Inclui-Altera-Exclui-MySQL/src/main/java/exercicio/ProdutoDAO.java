package exercicio;

import java.util.List;


public interface ProdutoDAO
{	
	public long inclui(Produto umProduto); 

	public void altera(Produto umProduto)
		throws AplicacaoException; 
	
	public void exclui(long id) 
		throws AplicacaoException; 
	
	public Produto recuperaUmProduto(long numero) 
		throws AplicacaoException; 
	
	public List<Produto> recuperaProdutos();
}
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.algaworks.pedidovenda.controller.PesquisaProdutosBean;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.repository.Produtos;


public class Main {
	
	public static void main(String args[]) {
		
		/*EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("PedidoPU");
		EntityManager manager = factory.createEntityManager();*/
		
		List<Produto> pesquisar = new Produtos().filtrados(null);
			for(Produto p : pesquisar){
				System.out.println(p.getNome());
			}
	}

}

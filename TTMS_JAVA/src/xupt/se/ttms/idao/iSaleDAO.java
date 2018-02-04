/**
 * 
 */
package xupt.se.ttms.idao;
import xupt.se.ttms.model.Sale;
import xupt.se.ttms.model.SaleItem;
import xupt.se.ttms.model.Ticket;

import java.util.List;
import java.util.Vector;

public interface iSaleDAO {
	public boolean doSale(List<Ticket> tickets);

	public List<Sale> select(String condt);

	public int  update(Sale sale);

	public int delete(int ID);

	public boolean doSale(List<Ticket> tickets, Sale sale);

	public List<SaleItem> findSaleItemById(String condt);
	
	public Vector<Vector> selectOrder();

	public boolean doRefund(int id);

	public Vector<Vector> selectSale();
}

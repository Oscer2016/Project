package xupt.se.ttms.service;

import java.util.List;
import java.util.Vector;

import xupt.se.ttms.idao.DAOFactory;
import xupt.se.ttms.idao.iSaleDAO;
import xupt.se.ttms.model.Sale;
import xupt.se.ttms.model.SaleItem;
import xupt.se.ttms.model.Ticket;

public class SaleSrv {
	private iSaleDAO saleDAO=DAOFactory.creatSaleDAO();

	public boolean doSale(List<Ticket> tickets){
		return saleDAO.doSale(tickets);
	}
	public List<Sale> Fetch(String condt){
		return saleDAO.select(condt);
	}
	public int delete(int id) {
		return saleDAO.delete(id);
	}
	public boolean add(List<Ticket> titcks, Sale sale) {
		return saleDAO.doSale(titcks, sale);
	}
	public List<SaleItem> FechSaleItems(String id) {
		return saleDAO.findSaleItemById(id);
	}
	public boolean Fetch(int id) {
		return saleDAO.doRefund(id);
	}
	public Vector<Vector> Fetch(){
		return saleDAO.selectOrder();		
	}
	public Vector<Vector> FetchSale(){
		return saleDAO.selectSale();		
	}
}

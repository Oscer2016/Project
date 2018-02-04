package xupt.se.ttms.service;

import java.util.Date;
import java.util.List;

import xupt.se.ttms.idao.DAOFactory;
import xupt.se.ttms.idao.iTicketDAO;
import xupt.se.ttms.model.Ticket;

public class TicketSrv {
	private iTicketDAO ticketDAO=DAOFactory.creatTicketDAO();

	public int add(Ticket stu){
		return ticketDAO.insert(stu);
	}

	public int modify(Ticket stu){
		return ticketDAO.update(stu);
	}

	public int delete(int ID){
		return ticketDAO.delete(ID);
	}

	public List<Ticket> Fetch(String condt){
		return ticketDAO.select(condt);
	}
	
	public Ticket Fetchone(String condt){
		return ticketDAO.selectone(condt);
	}

	public List<Ticket> FetchAll(){
		return ticketDAO.select("");
	}
	public int lockTicket(int id, String date) {
		return ticketDAO.lockTicket(id, date);
	}

	public int unlockTicket(int ID) {
		return ticketDAO.unlockTicket(ID);
	}

	public Object CreateTicket(int studio_id) {
		return ticketDAO.createticket(studio_id);	
	}

}

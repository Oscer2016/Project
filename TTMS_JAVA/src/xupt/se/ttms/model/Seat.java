package xupt.se.ttms.model;

import java.util.Date;

public class Seat {
	/*
	  * seat_status   smallint comment   '取值含义：
                       0：待售座位
                       1: 已售座位
                       2: 锁定座位
                       -1：座位损坏
                       -2: 没有座椅',
	  */
	
	private int id;
	private int studioId;
	private int row;
	private int column;
	private int seatStatus;
	private Date current_locked_time;
		
	public Seat() {	}

	public Seat(int id, int studioId, int row, int column,int seatStatus) {
		super();
		this.id = id;
		this.studioId = studioId;
		this.row = row;
		this.column = column;
		this.seatStatus = seatStatus;
	}

	public int getSeatStatus() {
		return seatStatus;
	}

	public void setSeatStatus(int seatStatus) {
		this.seatStatus = seatStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStudioId() {
		return studioId;
	}

	public void setStudioId(int studioId) {
		this.studioId = studioId;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	
	public Date getCurrent_locked_time() {
		return current_locked_time;
	}

	public void setCurrent_locked_time(Date current_locked_time) {
		this.current_locked_time = current_locked_time;
	}
}

package it.fe.cllmhl.model;

import et.sql.BaseDaoBean;

public final class TableSequenceBean extends BaseDaoBean {
	private java.lang.String idTable;
	private java.lang.Integer keyValue; 

	public java.lang.String getIdTable() {
		return idTable;
	}
	public void setIdTable(java.lang.String idTable) {
		this.idTable = idTable;
	}
	public java.lang.Integer getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(java.lang.Integer keyValue) {
		this.keyValue = keyValue;
	}
 
 
}
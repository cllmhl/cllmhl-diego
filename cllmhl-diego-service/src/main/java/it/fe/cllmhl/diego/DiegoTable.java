package it.fe.cllmhl.diego;

import java.util.ArrayList;
import java.util.List;

import schemacrawler.schema.Table;
import schemacrawler.schema.TableType;

public class DiegoTable {
    private Table table;
    List<DiegoColumn> columns = new ArrayList<DiegoColumn>();
    
    public DiegoTable(Table table) {
        this.table = table;
        for (schemacrawler.schema.Column column : table.getColumns()) {
            columns.add(new DiegoColumn(column));
        }
    }

    public DiegoColumn getColumn(String column) {
        return new DiegoColumn(table.getColumn(column));
    }

    public List<DiegoColumn> getColumns() {
        return columns;
    }

    public String getName() {
        return table.getName();
    }

    public TableType getType() {
        return table.getTableType();
    }

    // public List<DiegoColumn> getPks() {
    // List<DiegoColumn> columns = new ArrayList<DiegoColumn>();
    // for (schemacrawler.schema.Column column : table.getColumns()) {
    // if (column.isPartOfPrimaryKey()) columns.add(new DiegoColumn(column,this));
    // }
    // return columns;
    // }
    // public List<DiegoColumn> getFks() {
    // List<DiegoColumn> columns = new ArrayList<DiegoColumn>();
    // for (schemacrawler.schema.Column column : table.getColumns()) {
    // if (column.isPartOfForeignKey()) columns.add(new DiegoColumn(column,this));
    // }
    // return columns;
    // }
    // public List<DiegoColumn> getNoPks() {
    // List<DiegoColumn> columns = new ArrayList<DiegoColumn>();
    // for (schemacrawler.schema.Column column : table.getColumns()) {
    // if (!column.isPartOfPrimaryKey()) columns.add(new DiegoColumn(column,this));
    // }
    // return columns;
    // }
    // public List<DiegoTable> getParentTables() {
    // List<DiegoTable> tables = new ArrayList<DiegoTable>();
    // for (schemacrawler.schema.Table lTable : table.getRelatedTables(TableRelationshipType.parent)) {
    // tables.add(new DiegoTable(lTable));
    // }
    // return tables;
    // }
    //
    // //per il generatore..
    // private String getJavaClassName() {
    // return StringUtil.convertToJavaClassName(getName());
    // }
    // public String getJavaName() {
    // return StringUtil.convertToJavaVariableName(getName());
    // }
    // public String getBean() {
    // return getJavaClassName() + "Bean";
    // }
    // public String getServlet() {
    // return getJavaClassName() + "Servlet";
    // }
    // public String getManager() {
    // return getJavaClassName() + "Manager";
    // }
    // public String getDao() {
    // return getJavaClassName() + "Dao";
    // }
    // public String getDecoder() {
    // return getJavaClassName() + "RowDecoder";
    // }
    // public String getI18nLabel() {
    // return "jsp." + getJavaName() + ".label";
    // }
}

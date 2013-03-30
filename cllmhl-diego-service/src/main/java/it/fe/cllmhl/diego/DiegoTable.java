package it.fe.cllmhl.diego;

import it.fe.cllmhl.util.StringUtil;

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
            columns.add(new DiegoColumn(column,this));
        }
    }

    public String getBean() {
        return getJavaClassName() + "Bean";
    }

    public List<DiegoColumn> getColumns() {
        return columns;
    }

    public String getDao() {
        return getJavaClassName() + "Dao";
    }

    public String getMapper() {
        return getJavaClassName() + "RowMapper";
    }

    public String getI18nLabel() {
        return "jsp." + getJavaName() + ".label";
    }

    // Diego helpers
    private String getJavaClassName() {
        return StringUtil.convertToJavaClassName(getName());
    }

    public String getJavaName() {
        return StringUtil.convertToJavaVariableName(getName());
    }

    public String getManager() {
        return getJavaClassName() + "Manager";
    }

    public String getName() {
        return table.getName();
    }

    public String getServlet() {
        return getJavaClassName() + "Servlet";
    }

    public TableType getType() {
        return table.getTableType();
    }
}

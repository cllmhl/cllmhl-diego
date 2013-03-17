package it.fe.cllmhl.diego;

import java.util.ArrayList;
import java.util.List;

import schemacrawler.schema.Database;
import schemacrawler.schema.Table;
import schemacrawler.schema.TableType;

public class DiegoDatabase {
    private String datasource;
    private Database database;
    List<DiegoTable> tables = new ArrayList<DiegoTable>();
    List<DiegoTable> views = new ArrayList<DiegoTable>();
    
    public DiegoDatabase(String datasource, Database database) {
        this.datasource = datasource;
        this.database = database;
        for (Table table : database.getTables()) {
            if (table.getTableType() == TableType.table) {
                tables.add(new DiegoTable(table));
            } else {
                views.add(new DiegoTable(table));
            }
        }
    }

    public String getDatasource() {
        return datasource;
    }

    public List<DiegoTable> getTables() {
        return tables;
    }

    public List<DiegoTable> getViews() {
        return views;
    }
}

dao(package,table) ::= <<
package $package$.logic;


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import et.log.Log;
import et.sql.Column;
import et.sql.DBManager;
import et.sql.IPager;
import et.sql.IRowDecoder;
import et.sql.ReflectionRowDecoder;
import et.sql.SQLParameter;
import et.sql.SQLType;
import et.sql.WhereCondition;
import $package$.model.$table.bean$;

/**
 * Classe statica deputata alla persistenza della tabella $table.name$
 */
final class $table.dao$ {
	/** IL DBManager di riferimento */
	protected static final DBManager DBMANAGER;
    /** Nome della tabella/vista gestita dal manager.*/
    protected static final String TABLE_NAME = "$table.name$";
    /** Colonne della tabella */$table.columns: {column|
    public static Column $column.javaName$ = new Column("$column.name$",SQLType.$column.type$,$if(column.partOfPrimaryKey)$true$else$false$endif$);}$ 
    
    /** Lista completa dei campi*/
    protected final static List<Column> COLUMN_LIST = new ArrayList<Column>();
    private static Log mLog = Log.getInstance($table.dao$.class);

    private $table.dao$() {}
	static {
		DBMANAGER = DBManager.getInstance();
		// Initialize lColumnList$table.columns: {column|
		COLUMN_LIST.add($column.javaName$);}$
	}
    
    protected static $table.bean$ insert($table.bean$ p$table.bean$) {
        mLog.debug("Start insert");

        List<SQLParameter> lSQLParameterList = new ArrayList<SQLParameter>();
$table.columns: {column|
        //$column.name$
        if (p$table.bean$.get$column.objectName$() != null)
        	lSQLParameterList.add(new SQLParameter($column.javaName$, p$table.bean$.get$column.objectName$()));}$
        	
        DBMANAGER.insert(TABLE_NAME, lSQLParameterList);
        mLog.debug("Finish insert");
        return p$table.bean$;
    }

    protected static void updateByPrimaryKey($table.bean$ p$table.bean$) {
        mLog.debug("Start updateByPrimaryKey");

        List<SQLParameter> lSQLParameterList = new ArrayList<SQLParameter>();
$table.columns: {column|
        //$column.name$
    	lSQLParameterList.add(new SQLParameter($column.javaName$, p$table.bean$.get$column.objectName$()));}$
        	
        //Eseguiamo!!
        DBMANAGER.updateByPrimaryKey(TABLE_NAME,lSQLParameterList);
        
        mLog.debug("finish updateByPrimaryKey");
    }

    protected static void deleteByPrimaryKey($table.bean$ p$table.bean$) {
        mLog.debug("Start DeleteByPrimaryKey");

        List<SQLParameter> lSQLParameterList = new ArrayList<SQLParameter>();
$table.pks: {column|
        //$column.name$
        lSQLParameterList.add(new SQLParameter($column.javaName$, p$table.bean$.get$column.objectName$()));}$
        
        //Eseguiamo
        DBMANAGER.deleteByPrimaryKey(TABLE_NAME, lSQLParameterList);
    }

    protected static List<$table.bean$> loadAll() {
        return loadUsingTemplate(new $table.bean$(), null);
    }

    protected static List<$table.bean$> loadUsingTemplate($table.bean$ p$table.bean$, IPager pPager) {
        mLog.debug("Start loadUsingTemplate");

        WhereCondition lWhereCondition;
        List<$table.bean$> l$table.bean$List;
        StringBuffer lStringBufferSQL;

        lStringBufferSQL = new StringBuffer();
        lStringBufferSQL.append("SELECT * FROM ");
        lStringBufferSQL.append(TABLE_NAME);
        lStringBufferSQL.append(" ");

        //Preparo ed aggiungo la condizione di where
        lWhereCondition = buildWhereConditionUsingTemplate(p$table.bean$);
        lStringBufferSQL.append(lWhereCondition.getSql());

        // order by
        if (p$table.bean$.getOrderBy() != null) {
            lStringBufferSQL.append(" ORDER BY ");
            String lStrOrderByClause = p$table.bean$.getOrderBy();
            lStringBufferSQL.append(lStrOrderByClause);
        }
        
        l$table.bean$List = executeQuery(lStringBufferSQL.toString(), lWhereCondition.getParameters(), pPager);
 
        mLog.debug("End LoadUsingTemplate");

        return l$table.bean$List;
    }

    protected static $table.bean$ loadByPrimaryKey($table.bean$ p$table.bean$) {
        mLog.debug("Start loadByPrimaryKey");

        List<SQLParameter> lSQLParameterList = new ArrayList<SQLParameter>();
$table.pks: {column|
        //$column.name$
        lSQLParameterList.add(new SQLParameter($column.javaName$, p$table.bean$.get$column.objectName$()));}$

        //Eseguiamo!!
        Object lObject = DBMANAGER.loadByPrimaryKey(TABLE_NAME,lSQLParameterList,new $table.decoder$());
        
        mLog.debug("finish loadByPrimaryKey");
        //Cast
        return ($table.bean$) lObject;
    }

    protected static WhereCondition buildWhereConditionUsingTemplate($table.bean$ p$table.bean$) {
        mLog.debug("Start buildWhereConditionUsingTemplate");

        StringBuffer lStringBufferSQL = new StringBuffer(" WHERE 1=1 ");
        List<SQLParameter> lSQLParameterList = new ArrayList<SQLParameter>();

$table.columns: {column|
        //$column.name$
        if (p$table.bean$.get$column.objectName$() != null) {
        	lStringBufferSQL.append(" AND ");
            lStringBufferSQL.append($column.javaName$.name);
            lStringBufferSQL.append("$column.qbeClause$");
        	lSQLParameterList.add(new SQLParameter($column.javaName$, $column.qbeValue$));
        \}
$if(column.needRange)$
		if (p$table.bean$.get$column.objectName$to() != null) {
        	lStringBufferSQL.append(" AND ");
            lStringBufferSQL.append($column.javaName$.name);
            lStringBufferSQL.append(" <= ?");
        	lSQLParameterList.add(new SQLParameter($column.javaName$, p$table.bean$.get$column.objectName$to()));
        \}
$endif$}$

		WhereCondition lWhereCondition = new WhereCondition();
        lWhereCondition.setSql(lStringBufferSQL.toString());
        lWhereCondition.setParameters(lSQLParameterList);

        mLog.debug("Finish buildWhereConditionUsingTemplate returning ", lWhereCondition.getSql());

        return lWhereCondition;
    }

    protected static List<$table.bean$> executeQuery(String pStrSQL, List<SQLParameter> pSQLParameterList, IPager pPager) {
        mLog.debug("Start executeQuery");

        List<Object> lBeanList = DBMANAGER.executeQuery(pStrSQL, pSQLParameterList, pPager, new $table.decoder$());
        
        List<$table.bean$> l$table.bean$List = new ArrayList<$table.bean$>(lBeanList.size());
        
        for (Object lBean : lBeanList) {
        	l$table.bean$List.add(($table.bean$) lBean);
        }
        
        mLog.debug("End executeQuery");

        return l$table.bean$List;
    }

    protected static final class $table.decoder$ implements IRowDecoder<$table.bean$> {
        public $table.bean$ decodeRow(ResultSet pResultSet) {
        	ReflectionRowDecoder lReflectionRowDecoder = new ReflectionRowDecoder(COLUMN_LIST, $table.bean$.class);
        	return ($table.bean$) lReflectionRowDecoder.decodeRow(pResultSet);
        }
    }
}
>>
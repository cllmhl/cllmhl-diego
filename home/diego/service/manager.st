manager(package,table) ::= <<
package $package$.logic;

import java.util.List;

import et.log.Log;
import et.sql.IPager;
import et.sql.TableSequenceManager;
import $package$.model.$table.bean$;

/**
 * Classe singleton deputata alla ottimizzazione/logica legata alla tabella $table.name$
 */
public final class $table.manager$ {
	
	private static $table.manager$ m$table.manager$;
    private static Log mLog = Log.getInstance($table.manager$.class);
	
    private $table.manager$() {
    }

    public synchronized static $table.manager$ getInstance() {
        if (m$table.manager$ == null) {
        	m$table.manager$ = new $table.manager$();
        	mLog.debug("$table.manager$ instantiated");
        }
        return m$table.manager$;
    }

    public int countRecordsUsingTemplate($table.bean$ p$table.bean$) {
    	return $table.dao$.DBMANAGER.countRecords($table.dao$.TABLE_NAME, $table.dao$.buildWhereConditionUsingTemplate(p$table.bean$));
    }

    public int countRecords() {
    	return $table.dao$.DBMANAGER.countRecords($table.dao$.TABLE_NAME);
    }

    public void deleteByPrimaryKey($table.bean$ p$table.bean$) {
		$table.dao$.deleteByPrimaryKey(p$table.bean$);
	}

	public void insert($table.bean$ p$table.bean$) {
		Integer lIntPrimaryKeyValue = TableSequenceManager.getPrimaryKeyValue($table.dao$.TABLE_NAME);
$table.pks: {column|
		p$table.bean$.set$column.objectName$(lIntPrimaryKeyValue);}$
		$table.dao$.insert(p$table.bean$);
	}

	public List<$table.bean$> loadAll() {
		return $table.dao$.loadAll();
	}

	public $table.bean$ loadByPrimaryKey($table.bean$ p$table.bean$) {
		return $table.dao$.loadByPrimaryKey(p$table.bean$);
	}

	public List<$table.bean$> loadAllUsingTemplate($table.bean$ p$table.bean$) {
		return $table.dao$.loadUsingTemplate(p$table.bean$, null);
	}

	public List<$table.bean$> loadPageUsingTemplate($table.bean$ p$table.bean$, IPager pPager) {
		return $table.dao$.loadUsingTemplate(p$table.bean$, pPager);
	}

	public void updateByPrimaryKey($table.bean$ p$table.bean$) {
		$table.dao$.updateByPrimaryKey(p$table.bean$);
	}
}
>>
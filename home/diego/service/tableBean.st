tableBean(package,table) ::= <<
package $package$.model;

import et.sql.BaseDaoBean;

public final class $table.bean$ extends BaseDaoBean {
$table.columns: {column|
	private $column.javaType$ $column.javaName$$if(column.needRange)$, $column.javaName$to$endif$;}$ 

$table.columns: {column|
	public $column.javaType$ get$column.objectName$() {
		return $column.javaName$;
	\}
	public void set$column.objectName$($column.javaType$ $column.javaName$) {
		this.$column.javaName$ = $column.javaName$;
	\}
}$ 
$table.columns: {column|$if(column.needRange)$
	public $column.javaType$ get$column.objectName$to() {
		return $column.javaName$to;
	\}
	public void set$column.objectName$to($column.javaType$ $column.javaName$to) {
		this.$column.javaName$to = $column.javaName$to;
	\}
$endif$}$ 
}
>>
<c:url value="svt/$table.servlet$" var="pkUrl">
    <c:param name="beanName" value="$table.bean$"/>
$table.pks: {column|		<c:param name="$column.javaName$">$column.jspValue$</c:param>}$
</c:url>

$table.columns: {column|
<td class="$column.jspClass$">$column.jspValue$</td>}$

<td valign="middle" nowrap="nowrap">&nbsp;
	<a href="\${pkUrl}&amp;action=startUpdate" class="{iframe: true, dblclick: true}"><img class="ui-icon ui-icon-pencil" src="images/spacer.gif" alt="<fmt:message key="jsp.datagrid.edit"/>"/></a>
	<a href="\${pkUrl}&amp;action=duplicate" class="{iframe: true}"><img class="ui-icon ui-icon-copy" src="images/spacer.gif" alt="<fmt:message key="jsp.datagrid.duplicate"/>"/></a>
	<a href="\${pkUrl}&amp;action=delete" class="{ajax: etDeleteWorkingRow, confirm: true}"><img class="ui-icon ui-icon-circle-close" src="images/spacer.gif" alt="<fmt:message key="jsp.datagrid.delete"/>"/></a>
</td>

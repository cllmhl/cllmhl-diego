<%@ include file="/include/directive.jsp" %>
<tags:begin/>
<head>
<title><fmt:message key="$table.i18nLabel$"/></title>
<tags:basehead/>
<script type="text/javascript">
\$(function() {
	//Memoria delle fk
$table.fks: {column|		\$("#$column.javaName$").val("$column.jspInputElValue$");}$
});
</script>
</head>
<body>
<tags:header><jsp:attribute name="title"><fmt:message key="$table.i18nLabel$"/></jsp:attribute></tags:header>
<!-- CONTENT START -->
<div id="content">
<tags:menu/>
<tags:messages/>
<!-- QUERY START -->
<div id="queryDiv" class="ui-widget" style="width:35em; margin:0 auto; margin-top: 10px;">
	<layout:portletTop><jsp:attribute name="title"><fmt:message key="jsp.form.search"/> <fmt:message key="$table.i18nLabel$"/></jsp:attribute></layout:portletTop>
		<form id="queryForm" action="svt/$table.servlet$" class="{validate:true}" method="post">
		<input type="hidden" name="beanName" value="$table.bean$" />
		<input type="hidden" id="orderBy" name="orderBy" value="\${$table.bean$.orderBy}"/>
		<input type="hidden" id="recordCount" name="recordCount" value="\${$table.bean$.recordCount}"/>
		<input type="hidden" id="pageNumber" name="pageNumber" value="\${empty $table.bean$.pageNumber ? 1 : $table.bean$.pageNumber}"/>
		<input type="hidden" id="pageSize" name="pageSize" value="\${empty $table.bean$.pageSize ? 10 : $table.bean$.pageSize}"/>
		<input type="hidden" id="action" name="action" value="loadPage"/>
		<table border="0" cellspacing="2" cellpadding="2" width="99%">
$table.columns: {column|
			<tr id="$column.javaName$_tr">
				<td class="label"><label for="$column.javaName$"><fmt:message key="$column.i18nLabel$" />:</label></td>
				<td class="input">$column.jspSearchInput$</td>
			</tr>}$
			<tr id="buttons_tr">
				<td colspan="2" align="center">
					<buttons:search/> <buttons:cancel/>
				</td>
			</tr>
		</table>
		</form>
	<layout:portletBotton/>
</div>
<!-- QUERY FINISH -->
<!-- RESULTS START -->
<div id="gridDiv" class="ui-widget" style="width:40em; margin:auto; margin-top: 10px;">
		<table id="gridTable" border="0" class="datagrid {sorter: {form: 'queryForm', pager: true, pageSize: 10}}">
			<thead>
				<tr>
$table.columns: {column|
					<th class="ui-state-default {sorter: {column: '$column.name$', type: '$column.jspType$'}}"><fmt:message key="$column.i18nLabel$" /></th>}$
					<th nowrap="nowrap">&nbsp;
						<a id="insert" href="svt/$table.servlet$?action=startInsert" class="{iframe: true}"><img src="images/spacer.gif" alt="<fmt:message key="jsp.datagrid.insert"/>" class="ui-icon ui-icon-circle-plus"/></a>
					</th>
				</tr>
			</thead>
			<tfoot>
				<tr><tags:pager thcolspan="7"/></tr>
			</tfoot>
			<tbody>
				<c:forEach items="\${$table.bean$List}" var="$table.bean$" varStatus="status">
				<tr id="row_\${status.index}">
				<%@include file="$table.javaName$Row.jspf"%>
				</tr>
				</c:forEach>
			</tbody>
		</table>
</div>
<!-- RESULTS FINISH -->
</div>
<!-- CONTENT FINISH -->
<tags:footer/>
</body>
<tags:end/>

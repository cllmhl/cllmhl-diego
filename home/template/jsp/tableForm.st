<%@ include file="/include/directive.jsp" %>
<tags:begin/>
<head>
<title><fmt:message key="$table.i18nLabel$" /></title>
<tags:basehead/>
<script type="text/javascript">
\$(function() {
	var lStatus = "\${status}";
	//Gestisce le azioni di base di tutte le form di CRUD
	etBaseFormActionHandler(lStatus);
	//I campi di pk sono gestiti con sequence -> In insert li rimuovo ed in update li nascondo
	if (lStatus == 'startInsertok' || lStatus == 'insertko' || lStatus == 'duplicateok') {
$table.pks: {column|			\$("#$column.javaName$_tr").remove();}$
	}
	if (lStatus == 'startUpdateok' || lStatus == 'updateko') {
$table.pks: {column|			\$("#$column.javaName$_tr").hide();}$
	}
	//Memoria delle fk
$table.fks: {column|		\$("#$column.javaName$").val("$column.jspInputElValue$");}$
});
</script>
</head>
<body>
<!-- CONTENT START -->
<div id="content">
<tags:messages/>
<c:if test="\${status != 'insertok' && status != 'updateok' && status != 'deleteok'}">
<!-- FORM START -->
	<div id="formDiv" class="ui-widget" style="width:35em; margin:0 auto; margin-top: 10px; background: FFF;">
	<layout:portletTop><jsp:attribute name="title"><fmt:message key="$table.i18nLabel$"/></jsp:attribute></layout:portletTop>
		<form id="detailForm" action="svt/$table.servlet$" class="{validate:true}" method="post">
		<input type="hidden" name="beanName" value="$table.bean$" />
 		<input type="hidden" id="action" name="action" value=""/>
		<table border="0" cellspacing="2" cellpadding="2">
$table.columns: {column|
			<tr id="$column.javaName$_tr">
				<td class="label"><label for="$column.javaName$"><fmt:message key="$column.i18nLabel$" />:</label></td>
				<td class="input">$column.jspInput$</td>
			</tr>}$
			<tr id="buttons_tr">
				<td colspan="2" align="center">
				<buttons:insert/>	<buttons:update/> <buttons:cancel/>
				</td>
			</tr>
		</table>
		</form>
		<layout:portletBotton/>
	</div>
</c:if>
</div>
<!-- CONTENT FINISH -->
<c:if test="\${status == 'insertok' || status == 'updateok'}">
<!-- HIDDEN ROW START -->
<table id="newTable" style="visibility: hidden">
	<tbody id="newBody">
	<tr id="newRow">
		<%@ include file="$table.javaName$Row.jspf" %>
	</tr>
	</tbody>
</table>
<!-- HIDDEN ROW FINISH -->
</c:if>
</body>
<tags:end/>




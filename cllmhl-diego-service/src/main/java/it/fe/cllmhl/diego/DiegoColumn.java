package it.fe.cllmhl.diego;

import it.fe.cllmhl.sql.orm.SqlTypeMapping;
import it.fe.cllmhl.util.StringUtil;
import schemacrawler.schema.Column;

public class DiegoColumn {
    private Column column;
    private DiegoTable diegoTable;

    public DiegoColumn(Column column, DiegoTable diegoTable) {
        this.column = column;
        this.diegoTable = diegoTable;
    }

    public DiegoTable getDiegoTable() {
        return diegoTable;
    }

    public String getI18nLabel() {
        return "jsp." + getParentJavaName() + "." + getJavaName() + ".label";
    }

    // Diego helpers
    public String getJavaName() {
        return StringUtil.convertToJavaVariableName(getName());
    }

    public String getJavaType() {
        return getSqlTypeMapping().getJavaType().getSimpleName();
    }

    public String getJspClass() {
        switch (getSqlTypeMapping()) {
        case INTEGER:
            return "integer";
        case DATE:
            return "date";
        case TIMESTAMP:
            return "date";
        case DECIMAL:
            return "decimal";
        default:
            return "";
        }
    }

    public String getJspInput() {
        StringBuffer lStringBuffer;
        if (isPartOfForeignKey()) {
            lStringBuffer = new StringBuffer("<select id=\"");
            lStringBuffer.append(getJavaName());
            lStringBuffer.append("\" name=\"");
            lStringBuffer.append(getJavaName());
            lStringBuffer.append("\" class=\"");
            lStringBuffer.append(getJspInputClass());
            lStringBuffer.append("\">");
            lStringBuffer.append("<c:forEach items=\"${" + getRefencedColumn().getDiegoTable().getJavaName() + "}\" var=\"");
            lStringBuffer.append(getRefencedColumn().getDiegoTable().getBean() + "\" varStatus=\"status\">");
            lStringBuffer.append("<option value=\"${" + getRefencedColumn().getDiegoTable().getBean() + ".");
            lStringBuffer.append(getRefencedColumn().getJavaName() + "}\"><c:out value=\"${" + getRefencedColumn().getDiegoTable().getBean());
            lStringBuffer.append("." + getRefencedColumn().getJavaName() + "}\"/></option>");
            lStringBuffer.append("</c:forEach>");
            lStringBuffer.append("</select>");
        } else {
            lStringBuffer = new StringBuffer("<input id=\"");
            lStringBuffer.append(getJavaName());
            lStringBuffer.append("\" type=\"text\" name=\"");
            lStringBuffer.append(getJavaName());
            lStringBuffer.append("\" value=\"");
            lStringBuffer.append(getJspValue());
            lStringBuffer.append("\" size=\"");
            lStringBuffer.append(getJspSize());
            lStringBuffer.append("\" maxlength=\"");
            lStringBuffer.append(getJspMaxLength());
            lStringBuffer.append("\" class=\"");
            lStringBuffer.append(getJspInputClass());
            lStringBuffer.append("\"/>");
        }
        return lStringBuffer.toString();
    }

    private String getJspInputClass() {
        switch (getSqlTypeMapping()) {
        case INTEGER:
            if (isNullable()) {
                return "integer {validate: {integer: true}}";
            } else {
                return "required integer {validate: {required:true, integer: true}}";
            }
        case DATE:
            if (isNullable()) {
                return "date {validate: {date: true}, ui_datepicker: {}}";
            } else {
                return "required date {validate: {required:true, date: true}, ui_datepicker: {}}";
            }
        case TIMESTAMP:
            if (isNullable()) {
                return "date {validate: {timestamp: true}, ui_datepicker: {}}";
            } else {
                return "required date {validate: {required:true, timestamp: true}, ui_datepicker: {}}";
            }
        case DECIMAL:
            if (isNullable()) {
                return "decimal {validate: {decimal: true}}";
            } else {
                return "required decimal {validate: {required:true, decimal: true}}";
            }
        case VARCHAR:
            if (isNullable()) {
                return "{validate: {maxlength:" + getSize() + "}}";
            } else {
                return "required {validate: {required:true, maxlength:" + getSize() + "}}";
            }
        case CHAR:
            if (isNullable()) {
                return "{validate: {maxlength:" + getSize() + "}}";
            } else {
                return "required {validate: {required:true, maxlength:" + getSize() + "}}";
            }
        default:
            return "update";
        }
    }

    public String getJspInputElValue() {
        return "${" + diegoTable.getBean() + "." + getJavaName() + "}";
    }

    private Object getJspMaxLength() {
        return getSize();
    }

    private String getJspQueryInputClass() {
        switch (getSqlTypeMapping()) {
        case INTEGER:
            return "integer {validate: {integer: true}}";
        case DATE:
            return "date {validate: {date: true}, ui_datepicker: {}}";
        case TIMESTAMP:
            return "date {validate: {timestamp: true}, ui_datepicker: {}}";
        case DECIMAL:
            return "decimal {validate: {decimal: true}}";
        default:
            return "";
        }
    }

    public String getJspSearchInput() {
        StringBuffer lStringBuffer;
        if (isPartOfForeignKey()) {
            lStringBuffer = new StringBuffer("<select id=\"");
            lStringBuffer.append(getJavaName());
            lStringBuffer.append("\" name=\"");
            lStringBuffer.append(getJavaName());
            lStringBuffer.append("\" class=\"");
            lStringBuffer.append(getJspQueryInputClass());
            lStringBuffer.append("\"><option value=\"\"></option>");
            lStringBuffer.append("<c:forEach items=\"${" + getRefencedColumn().getDiegoTable().getJavaName() + "}\" var=\"");
            lStringBuffer.append(getRefencedColumn().getDiegoTable().getBean() + "\" varStatus=\"status\">");
            lStringBuffer.append("<option value=\"${" + getRefencedColumn().getDiegoTable().getBean() + ".");
            lStringBuffer.append(getRefencedColumn().getJavaName() + "}\"><c:out value=\"${" + getRefencedColumn().getDiegoTable().getBean());
            lStringBuffer.append("." + getRefencedColumn().getJavaName() + "}\"/></option>");
            lStringBuffer.append("</c:forEach>");
            lStringBuffer.append("</select>");
        } else {
            lStringBuffer = new StringBuffer("<input id=\"");
            lStringBuffer.append(getJavaName());
            lStringBuffer.append("\" type=\"text\" name=\"");
            lStringBuffer.append(getJavaName());
            lStringBuffer.append("\" value=\"");
            lStringBuffer.append(getJspValue());
            lStringBuffer.append("\" class=\"");
            lStringBuffer.append(getJspQueryInputClass());
            lStringBuffer.append("\"/>");
            // Campo di range per le date
            if (getSqlTypeMapping() == SqlTypeMapping.DATE) {
                lStringBuffer.append("<input id=\"");
                lStringBuffer.append(getJavaName() + "to");
                lStringBuffer.append("\" type=\"text\" name=\"");
                lStringBuffer.append(getJavaName() + "to");
                lStringBuffer.append("\" value=\"");
                lStringBuffer.append("<fmt:formatDate value=\"" + "${" + diegoTable.getBean() + "." + getJavaName() + "to}\" pattern=\"${etSession.patternDate}\"/>");
                lStringBuffer.append("\" class=\"");
                lStringBuffer.append(getJspQueryInputClass());
                lStringBuffer.append("\"/>");
            }
            // Campo di range per le dati timestamp
            if (getSqlTypeMapping() == SqlTypeMapping.TIMESTAMP) {
                lStringBuffer.append("<input id=\"");
                lStringBuffer.append(getJavaName() + "to");
                lStringBuffer.append("\" type=\"text\" name=\"");
                lStringBuffer.append(getJavaName() + "to");
                lStringBuffer.append("\" value=\"");
                lStringBuffer.append("<fmt:formatDate value=\"" + "${" + diegoTable.getBean() + "." + getJavaName() + "to}\" pattern=\"${etSession.patternTimestamp}\"/>");
                lStringBuffer.append("\" class=\"");
                lStringBuffer.append(getJspQueryInputClass());
                lStringBuffer.append("\"/>");
            }
        }
        return lStringBuffer.toString();
    }

    private Object getJspSize() {
        return Math.min(getSize(), 50);
    }

    public String getJspType() {
        switch (getSqlTypeMapping()) {
        case VARCHAR:
            return "text";
        default:
            return getSqlTypeMapping().toString().toLowerCase();
        }
    }

    public String getJspValue() {
        switch (getSqlTypeMapping()) {
        case INTEGER:
            return "<fmt:formatNumber value=\"" + getJspInputElValue() + "\" pattern=\"${etSession.patternInteger}\"/>";
        case DATE:
            return "<fmt:formatDate value=\"" + getJspInputElValue() + "\" pattern=\"${etSession.patternDate}\"/>";
        case TIMESTAMP:
            return "<fmt:formatDate value=\"" + getJspInputElValue() + "\" pattern=\"${etSession.patternTimestamp}\"/>";
        case DECIMAL:
            return "<fmt:formatNumber value=\"" + getJspInputElValue() + "\" pattern=\"${etSession.patternDecimal}\"/>";
        default:
            return "<c:out value=\"" + getJspInputElValue() + "\"/>";
        }
    }

    public String getName() {
        return column.getName();
    }

    public boolean getNeedRange() {
        switch (getSqlTypeMapping()) {
        case DATE:
            return true;
        case TIMESTAMP:
            return true;
        default:
            return false;
        }
    }

    public String getObjectName() {
        return StringUtil.convertToJavaClassName(getName());
    }

    public String getParentJavaName() {
        return StringUtil.convertToJavaVariableName(column.getParent().getName());
    }

    public String getQbeClause() {
        switch (getSqlTypeMapping()) {
        case VARCHAR:
            return " LIKE ?";
        case DATE:
            return " >= ?";
        case TIMESTAMP:
            return " >= ?";
        default:
            return " = ?";
        }
    }

    public String getQbeValue() {
        switch (getSqlTypeMapping()) {
        case VARCHAR:
            return "\"%\" + p" + diegoTable.getBean() + ".get" + getObjectName() + "() + \"%\"";
        default:
            return "p" + diegoTable.getBean() + ".get" + getObjectName() + "()";
        }
    }

    public DiegoColumn getRefencedColumn() {
        if (isPartOfForeignKey()) {
            DiegoTable lReferencedTable = new DiegoTable(column.getReferencedColumn().getParent());
            if (lReferencedTable != null) {
                return new DiegoColumn(column.getReferencedColumn(),lReferencedTable);
            }
        }
        return null;
    }

    public int getSize() {
        return column.getSize();
    }

    private SqlTypeMapping getSqlTypeMapping() {
        return SqlTypeMapping.fromSqlType(column.getColumnDataType().getType());
    }

    public String getWidth() {
        return column.getWidth();
    }

    public boolean isNullable() {
        return column.isNullable();
    }

    public boolean isPartOfForeignKey() {
        return column.isPartOfForeignKey();
    }

    public boolean isPartOfPrimaryKey() {
        return column.isPartOfPrimaryKey();
    }
}

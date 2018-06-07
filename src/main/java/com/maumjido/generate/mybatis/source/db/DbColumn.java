package com.maumjido.generate.mybatis.source.db;

public class DbColumn {
  private String tableName;
  private String columnId;
  private String columnName;
  private String dataType;
  private String nullable;
  private String defaultValue;
  private String constrainst;
  private String comments;
  private String tableComments;
  private String extra;

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getColumnId() {
    return columnId;
  }

  public void setColumnId(String columnId) {
    this.columnId = columnId;
  }

  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public String getNullable() {
    return nullable;
  }

  public void setNullable(String nullable) {
    this.nullable = nullable;
  }

  public String getConstrainst() {
    return constrainst;
  }

  public void setConstrainst(String constrainst) {
    this.constrainst = constrainst;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public String getTableComments() {
    return tableComments;
  }

  public void setTableComments(String tableComments) {
    this.tableComments = tableComments;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public String getExtra() {
    return extra;
  }

  public void setExtra(String extra) {
    this.extra = extra;
  }

  @Override
  public String toString() {
    return (tableName == null ? "" : (tableName + ":")) + columnName;
  }
}

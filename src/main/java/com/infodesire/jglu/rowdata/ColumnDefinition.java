package com.infodesire.jglu.rowdata;

public class ColumnDefinition {

    private String name;

    private ColumnType type;

    public ColumnDefinition( String name, ColumnType type ) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public ColumnType getType() {
        return type;
    }
}

package com.infodesire.jglu.rowdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines fields and types of elements in rows of the same type
 *
 */
public class RowDefinition {

    private List<ColumnDefinition> columns = new ArrayList<>();
    private Map<String, Integer> indexes = new HashMap<>();

    public void add( ColumnDefinition column ) {
        columns.add( column );
        indexes.put( column.getName(), columns.size() - 1 );
    }

    public int getIndex( String name ) {
        return indexes.get( name );
    }

    public ColumnDefinition getColumn( int index ) {
        return columns.get( index );
    }

    public int size() {
        return columns.size();
    }

}

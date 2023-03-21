package com.infodesire.jglu.rowdata;

/**
 * Read and make accessible a row of data
 *
 */
public class RowReader {

    private final RowDefinition rowDefinition;
    private final Object[] values;

    public RowReader( RowDefinition rowDefinition, Object[] values ) {
        this.rowDefinition = rowDefinition;
        this.values = values;
    }

    public RowValue get( String name ) {
        return get( rowDefinition.getIndex( name ) );
    }

    public RowValue get( int index ) {
        ColumnDefinition column = rowDefinition.getColumn( index );
        return new RowValue( column.getType(), values[ index ] );
    }

}

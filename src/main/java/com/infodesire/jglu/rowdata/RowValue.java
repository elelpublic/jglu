package com.infodesire.jglu.rowdata;

import com.infodesire.jglu.busdata.ListFormat;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * An immutable value in a business object of one of the most used types in such objects.
 * <p>
 *
 * This class mainly deals with serialization and deserialization in redis.
 * Actual values and manipulation are not subject of this class.
 *
 */
public class RowValue {

    static final DateTimeFormatter dateFormat = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd
    static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // yyyy-MM-ddTHH:mm:ss
    static final DateTimeFormatter timeFormat = DateTimeFormatter.ISO_LOCAL_TIME; // HH:mm:ss

    private ColumnType type;
    private Object value;

    /**
     * Create value by parsing a string
     *
     * @param type Type of value
     * @param stringValue String representation of value (created by toString())
     *
     */
    public RowValue( ColumnType type, String stringValue ) throws ParseException {
        
        this.type = type;

        if( stringValue == null || stringValue.trim().length() == 0 ) {
            value = null;
        }
        else if( type == ColumnType.STRING ) {
            set( stringValue  );
        }
        else if( type == ColumnType.BOOLEAN ) {
            set( stringValue.equals( "true" ) || stringValue.equals( "1" ) );
        }
        else if( type == ColumnType.INTEGER ) {
            try {
                set( Long.parseLong( stringValue ) );
            }
            catch( NumberFormatException ex ) {
                throw new ParseException( stringValue, 0 );
            }
        }
        else if( type == ColumnType.FLOAT ) {
            try {
                set( Double.parseDouble( stringValue ) );
            }
            catch( NumberFormatException ex ) {
                throw new ParseException( stringValue, 0 );
            }
        }
        else if( type == ColumnType.DATE ) {
            try {
                value = LocalDate.parse( stringValue );
            }
            catch( DateTimeParseException ex ) {
                throw new ParseException( stringValue, 0 );
            }
        }
        else if( type == ColumnType.DATETIME) {
            try {
                value = LocalDateTime.parse( stringValue );
            }
            catch( DateTimeParseException ex ) {
                throw new ParseException( stringValue, 0 );
            }
        }
        else if( type == ColumnType.TIME ) {
            try {
                value = LocalTime.parse( stringValue );
            }
            catch( DateTimeParseException ex ) {
                throw new ParseException( stringValue, 0 );
            }
        }
        else if( type == ColumnType.LIST ) {
            set( ListFormat.parse( stringValue ) );
        }
        else {
            throw new RuntimeException( "Parsing business values of type " + type
                + "not implemented yet." );
        }

    }

    /**
     * Create value by parsing a string
     *
     * @param type Type of value
     * @param value Value
     *
     */
    public RowValue( ColumnType type, Object value ) {
        this.type = type;
        this.value = value;
    }

    /**
     * Create a string typed values
     *
     */
    public RowValue( String stringValue ) {
        set( stringValue );
    }

    /**
     * Create boolean typed value
     *
     */
    public RowValue( Boolean booleanValue ) {
        set( booleanValue );
    }

    /**
     * Create integer typed value
     *
     */
    public RowValue( Long intValue ) {
        set( intValue );
    }

    /**
     * Create integer typed value
     *
     */
    public RowValue( Integer intValue ) {
        set( intValue );
    }

    /**
     * Create integer typed value
     *
     */
    public RowValue( Double floatValue ) {
        set( floatValue );
    }

    /**
     * Create integer typed value
     *
     */
    public RowValue( Float floatValue ) {
        set( floatValue );
    }

    public RowValue( LocalDate dateValue ) {
        set( dateValue );
    }

    public RowValue( LocalDateTime localDateTime ) {
        set( localDateTime );
    }

    public RowValue( LocalTime timeValue ) {
        set( timeValue );
    }

    public RowValue( List<String> listValue ) {
        set( listValue );
    }

    private void set( List<String> listValue ) {
        this.type = ColumnType.LIST;
        if( listValue == null || listValue.isEmpty() ) {
            value = null;
        }
        else {
            value = listValue;
        }
    }

    private void set( LocalDate dateValue ) {
        type = ColumnType.DATE;
        value = dateValue;
    }

    private void set( LocalDateTime dateTimeValue ) {
        type = ColumnType.DATETIME;
        value = dateTimeValue;
    }

    private void set( LocalTime timeValue ) {
        type = ColumnType.TIME;
        value = timeValue;
    }

    private void set( Double floatValue ) {
        this.type = ColumnType.FLOAT;
        value = floatValue;
    }

    private void set( Float floatValue ) {
        this.type = ColumnType.FLOAT;
        value = floatValue;
    }

    private void set( Long intValue ) {
        this.type = ColumnType.INTEGER;
        value = intValue;
    }

    private void set( Integer intValue ) {
        this.type = ColumnType.INTEGER;
        value = intValue;
    }

    private void set( Boolean booleanValue ) {
        this.type = ColumnType.BOOLEAN;
        value = booleanValue;
    }

    private void set( String stringValue ) {
        this.type = ColumnType.STRING;
        if( stringValue == null ) {
            value = null;
        }
        else {
            value = stringValue.trim();
            if( ((String) value).length() == 0 ) {
                value = null;
            }
        }
    }

    /**
     * @return Parseable string representation of value
     *
     */
    public String toString() {

        if( value == null ) {
            return "";
        }

        if( type == ColumnType.STRING ) {
            return (String) value;
        }
        else if( type == ColumnType.BOOLEAN ) {
            return ((Boolean) value) ? "1" : "0";
        }
        else if( type == ColumnType.INTEGER ) {
            return "" + value;
        }
        else if( type == ColumnType.FLOAT ) {
            return "" + value;
        }
        else if( type == ColumnType.DATE ) {
            return dateFormat.format( (LocalDate) value );
        }
        else if( type == ColumnType.DATETIME ) {
            return dateTimeFormat.format( (LocalDateTime) value );
        }
        else if( type == ColumnType.TIME ) {
            return timeFormat.format( (LocalTime) value );
        }
        else if( type == ColumnType.LIST ) {
            return ListFormat.format( (List<String>) value );
        }
        else {
            throw new RuntimeException( "Serializing business values of type " + type
                    + "not implemented yet." );
        }

    }

    public ColumnType getType() {
        return type;
    }

    public boolean isNull() {
        return value == null;
    }

    public LocalDate getDate() {
        return (LocalDate) value;
    }

    public LocalDateTime getDateTime() {
        return (LocalDateTime) value;
    }

    public LocalTime getTime() {
        return (LocalTime) value;
    }

    public List<String> getList() {
        return (List<String>) value;
    }

    public Boolean getBoolean() {
        return (Boolean) value;
    }

    public Long getInteger() {
        return (Long) value;
    }

    public Double getFloat() {
        return (Double) value;
    }

    public String getString() {
        return (String) value;
    }

}

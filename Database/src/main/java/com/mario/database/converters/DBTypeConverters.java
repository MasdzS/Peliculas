package com.mario.database.converters;

import androidx.room.TypeConverter;

import java.util.Date;


public class DBTypeConverters {

    /**
     * Convierte los timestamp de la base de datos a Date()
     * @param value Timestamp proveniente de la base de datos
     * @return Objeto Date
     */
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    /**
     * Convierte los objetos Date a Timestamp para almacenarlos en la base de datos
     * @param date Objeto Date que se va a convertir
     * @return Timestamp listo para almacenarce en la base de datos
     */
    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }
}

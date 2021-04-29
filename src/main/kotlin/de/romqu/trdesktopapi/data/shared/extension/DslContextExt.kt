package de.romqu.trdesktopapi.data.shared

import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.Record
import org.jooq.Table

fun <R : Record> DSLContext.insertReturningId(
    table: Table<R>,
    columns: List<Field<*>>,
    values: List<Any>,
): Long {
    return insertInto(table)
        .columns(columns)
        .values(values)
        .returningResult(table.field("id"))
        .fetchOne()
        ?.into(Long::class.javaPrimitiveType) ?: 0
}
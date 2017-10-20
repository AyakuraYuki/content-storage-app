package cc.ayakurayuki.contentstorage.dialect

import org.hibernate.dialect.Dialect
import org.hibernate.dialect.function.SQLFunctionTemplate
import org.hibernate.dialect.function.StandardSQLFunction
import org.hibernate.dialect.function.VarArgsSQLFunction
import org.hibernate.type.StringType

import java.sql.Types

/**
 * Created by Ayakura Yuki on 2017/10/18.
 *
 * Hibernate-SQLite dialect
 */
class SQLiteDialect extends Dialect {

    SQLiteDialect() {
        registerColumnType(Types.BIT, "integer")
        registerColumnType(Types.TINYINT, "tinyint")
        registerColumnType(Types.SMALLINT, "smallint")
        registerColumnType(Types.INTEGER, "integer")
        registerColumnType(Types.BIGINT, "bigint")
        registerColumnType(Types.FLOAT, "float")
        registerColumnType(Types.REAL, "real")
        registerColumnType(Types.DOUBLE, "double")
        registerColumnType(Types.NUMERIC, "numeric")
        registerColumnType(Types.DECIMAL, "decimal")
        registerColumnType(Types.CHAR, "char")
        registerColumnType(Types.VARCHAR, "varchar")
        registerColumnType(Types.LONGVARCHAR, "longvarchar")
        registerColumnType(Types.DATE, "date")
        registerColumnType(Types.TIME, "time")
        registerColumnType(Types.TIMESTAMP, "timestamp")
        registerColumnType(Types.BINARY, "blob")
        registerColumnType(Types.VARBINARY, "blob")
        registerColumnType(Types.LONGVARBINARY, "blob")
        registerColumnType(Types.BLOB, "blob")
        registerColumnType(Types.CLOB, "clob")
        registerColumnType(Types.BOOLEAN, "integer")

        registerFunction("concat", new VarArgsSQLFunction(StringType.INSTANCE, "", "||", ""))
        registerFunction("mod", new SQLFunctionTemplate(StringType.INSTANCE, "?1 % ?2"))
        registerFunction("substr", new StandardSQLFunction("substr", StringType.INSTANCE))
        registerFunction("substring", new StandardSQLFunction("substr", StringType.INSTANCE))
    }

    boolean supportsIdentityColumns() {
        true
    }

    boolean hasDataTypeInIdentityColumn() {
        false // As specify in NHibernate dialect
    }

    String getIdentityColumnString() {
        "integer"
    }

    String getIdentitySelectString() {
        "select last_insert_rowid()"
    }

    boolean supportsLimit() {
        true
    }

    protected String getLimitString(String query, boolean hasOffset) {
        new StringBuffer(query.length() + 20).append(query).append(hasOffset ? " limit ? offset ?" : " limit ?").toString()
    }

    boolean supportsTemporaryTables() {
        true
    }

    String getCreateTemporaryTableString() {
        "create temporary table if not exists"
    }

    boolean dropTemporaryTableAfterUse() {
        false
    }

    boolean supportsCurrentTimestampSelection() {
        true
    }

    boolean isCurrentTimestampSelectStringCallable() {
        false
    }

    String getCurrentTimestampSelectString() {
        "select current_timestamp"
    }

    boolean supportsUnionAll() {
        true
    }

    boolean hasAlterTable() {
        false // As specify in NHibernate dialect
    }

    boolean dropConstraints() {
        false
    }

    String getAddColumnString() {
        "add column"
    }

    String getForUpdateString() {
        ""
    }

    boolean supportsOuterJoinForUpdate() {
        false
    }

    String getDropForeignKeyString() {
        throw new UnsupportedOperationException("No drop foreign key syntax supported by SQLiteDialect")
    }

    String getAddForeignKeyConstraintString(String constraintName,
                                            String[] foreignKey, String referencedTable, String[] primaryKey,
                                            boolean referencesPrimaryKey) {
        throw new UnsupportedOperationException("No add foreign key syntax supported by SQLiteDialect")
    }

    String getAddPrimaryKeyConstraintString(String constraintName) {
        throw new UnsupportedOperationException("No add primary key syntax supported by SQLiteDialect")
    }

    boolean supportsIfExistsBeforeTableName() {
        true
    }

    boolean supportsCascadeDelete() {
        false
    }

}

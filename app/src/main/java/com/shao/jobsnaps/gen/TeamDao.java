package com.shao.jobsnaps.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.shao.jobsnaps.pojo.Team;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TEAM".
*/
public class TeamDao extends AbstractDao<Team, Long> {

    public static final String TABLENAME = "TEAM";

    /**
     * Properties of entity Team.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property TId = new Property(0, Long.class, "tId", true, "_id");
        public final static Property TNm = new Property(1, String.class, "tNm", false, "T_NM");
        public final static Property TCt = new Property(2, java.util.Date.class, "tCt", false, "T_CT");
        public final static Property TLeaId = new Property(3, Long.class, "tLeaId", false, "T_LEA_ID");
        public final static Property TDs = new Property(4, String.class, "tDs", false, "T_DS");
    }


    public TeamDao(DaoConfig config) {
        super(config);
    }
    
    public TeamDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TEAM\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: tId
                "\"T_NM\" TEXT," + // 1: tNm
                "\"T_CT\" INTEGER," + // 2: tCt
                "\"T_LEA_ID\" INTEGER," + // 3: tLeaId
                "\"T_DS\" TEXT);"); // 4: tDs
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TEAM\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Team entity) {
        stmt.clearBindings();
 
        Long tId = entity.getTId();
        if (tId != null) {
            stmt.bindLong(1, tId);
        }
 
        String tNm = entity.getTNm();
        if (tNm != null) {
            stmt.bindString(2, tNm);
        }
 
        java.util.Date tCt = entity.getTCt();
        if (tCt != null) {
            stmt.bindLong(3, tCt.getTime());
        }
 
        Long tLeaId = entity.getTLeaId();
        if (tLeaId != null) {
            stmt.bindLong(4, tLeaId);
        }
 
        String tDs = entity.getTDs();
        if (tDs != null) {
            stmt.bindString(5, tDs);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Team entity) {
        stmt.clearBindings();
 
        Long tId = entity.getTId();
        if (tId != null) {
            stmt.bindLong(1, tId);
        }
 
        String tNm = entity.getTNm();
        if (tNm != null) {
            stmt.bindString(2, tNm);
        }
 
        java.util.Date tCt = entity.getTCt();
        if (tCt != null) {
            stmt.bindLong(3, tCt.getTime());
        }
 
        Long tLeaId = entity.getTLeaId();
        if (tLeaId != null) {
            stmt.bindLong(4, tLeaId);
        }
 
        String tDs = entity.getTDs();
        if (tDs != null) {
            stmt.bindString(5, tDs);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Team readEntity(Cursor cursor, int offset) {
        Team entity = new Team( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // tId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // tNm
            cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)), // tCt
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // tLeaId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // tDs
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Team entity, int offset) {
        entity.setTId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTNm(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTCt(cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)));
        entity.setTLeaId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setTDs(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Team entity, long rowId) {
        entity.setTId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Team entity) {
        if(entity != null) {
            return entity.getTId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Team entity) {
        return entity.getTId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

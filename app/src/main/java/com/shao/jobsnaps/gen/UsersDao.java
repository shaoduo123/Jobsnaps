package com.shao.jobsnaps.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.shao.jobsnaps.pojo.Users;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USERS".
*/
public class UsersDao extends AbstractDao<Users, Long> {

    public static final String TABLENAME = "USERS";

    /**
     * Properties of entity Users.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property UId = new Property(0, Long.class, "uId", true, "_id");
        public final static Property UNm = new Property(1, String.class, "uNm", false, "U_NM");
        public final static Property UPwd = new Property(2, String.class, "uPwd", false, "U_PWD");
        public final static Property UPho = new Property(3, String.class, "uPho", false, "U_PHO");
        public final static Property UMai = new Property(4, String.class, "uMai", false, "U_MAI");
        public final static Property UWx = new Property(5, String.class, "uWx", false, "U_WX");
        public final static Property UQq = new Property(6, String.class, "uQq", false, "U_QQ");
    }


    public UsersDao(DaoConfig config) {
        super(config);
    }
    
    public UsersDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USERS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: uId
                "\"U_NM\" TEXT NOT NULL ," + // 1: uNm
                "\"U_PWD\" TEXT NOT NULL ," + // 2: uPwd
                "\"U_PHO\" TEXT," + // 3: uPho
                "\"U_MAI\" TEXT," + // 4: uMai
                "\"U_WX\" TEXT," + // 5: uWx
                "\"U_QQ\" TEXT);"); // 6: uQq
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USERS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Users entity) {
        stmt.clearBindings();
 
        Long uId = entity.getUId();
        if (uId != null) {
            stmt.bindLong(1, uId);
        }
        stmt.bindString(2, entity.getUNm());
        stmt.bindString(3, entity.getUPwd());
 
        String uPho = entity.getUPho();
        if (uPho != null) {
            stmt.bindString(4, uPho);
        }
 
        String uMai = entity.getUMai();
        if (uMai != null) {
            stmt.bindString(5, uMai);
        }
 
        String uWx = entity.getUWx();
        if (uWx != null) {
            stmt.bindString(6, uWx);
        }
 
        String uQq = entity.getUQq();
        if (uQq != null) {
            stmt.bindString(7, uQq);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Users entity) {
        stmt.clearBindings();
 
        Long uId = entity.getUId();
        if (uId != null) {
            stmt.bindLong(1, uId);
        }
        stmt.bindString(2, entity.getUNm());
        stmt.bindString(3, entity.getUPwd());
 
        String uPho = entity.getUPho();
        if (uPho != null) {
            stmt.bindString(4, uPho);
        }
 
        String uMai = entity.getUMai();
        if (uMai != null) {
            stmt.bindString(5, uMai);
        }
 
        String uWx = entity.getUWx();
        if (uWx != null) {
            stmt.bindString(6, uWx);
        }
 
        String uQq = entity.getUQq();
        if (uQq != null) {
            stmt.bindString(7, uQq);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Users readEntity(Cursor cursor, int offset) {
        Users entity = new Users( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // uId
            cursor.getString(offset + 1), // uNm
            cursor.getString(offset + 2), // uPwd
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // uPho
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // uMai
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // uWx
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // uQq
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Users entity, int offset) {
        entity.setUId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUNm(cursor.getString(offset + 1));
        entity.setUPwd(cursor.getString(offset + 2));
        entity.setUPho(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUMai(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUWx(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setUQq(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Users entity, long rowId) {
        entity.setUId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Users entity) {
        if(entity != null) {
            return entity.getUId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Users entity) {
        return entity.getUId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

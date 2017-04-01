package com.splant.smartgarden.daoModel.Gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.splant.smartgarden.daoModel.Entity.UnitWaterModel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "UNIT_WATER_MODEL".
*/
public class UnitWaterModelDao extends AbstractDao<UnitWaterModel, Void> {

    public static final String TABLENAME = "UNIT_WATER_MODEL";

    /**
     * Properties of entity UnitWaterModel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property UnitId = new Property(0, String.class, "UnitId", false, "UNIT_ID");
        public final static Property DeviceId = new Property(1, String.class, "DeviceId", false, "DEVICE_ID");
        public final static Property DeviceName = new Property(2, String.class, "DeviceName", false, "DEVICE_NAME");
        public final static Property CreateTime = new Property(3, String.class, "CreateTime", false, "CREATE_TIME");
        public final static Property UserId = new Property(4, String.class, "UserId", false, "USER_ID");
        public final static Property ClientId = new Property(5, String.class, "ClientId", false, "CLIENT_ID");
        public final static Property MapCoord = new Property(6, String.class, "MapCoord", false, "MAP_COORD");
        public final static Property Remarks = new Property(7, String.class, "Remarks", false, "REMARKS");
        public final static Property PlantId = new Property(8, String.class, "PlantId", false, "PLANT_ID");
        public final static Property PlantName = new Property(9, String.class, "PlantName", false, "PLANT_NAME");
        public final static Property PlantCount = new Property(10, Integer.class, "PlantCount", false, "PLANT_COUNT");
        public final static Property PlantTypeId = new Property(11, String.class, "PlantTypeId", false, "PLANT_TYPE_ID");
        public final static Property PlantTypeName = new Property(12, String.class, "PlantTypeName", false, "PLANT_TYPE_NAME");
        public final static Property AreaId = new Property(13, String.class, "AreaId", false, "AREA_ID");
        public final static Property AreaName = new Property(14, String.class, "AreaName", false, "AREA_NAME");
        public final static Property PowerEmery = new Property(15, String.class, "PowerEmery", false, "POWER_EMERY");
        public final static Property DeviceType = new Property(16, String.class, "DeviceType", false, "DEVICE_TYPE");
        public final static Property GatewayId = new Property(17, String.class, "GatewayId", false, "GATEWAY_ID");
        public final static Property GatewayName = new Property(18, String.class, "GatewayName", false, "GATEWAY_NAME");
    }


    public UnitWaterModelDao(DaoConfig config) {
        super(config);
    }
    
    public UnitWaterModelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"UNIT_WATER_MODEL\" (" + //
                "\"UNIT_ID\" TEXT," + // 0: UnitId
                "\"DEVICE_ID\" TEXT," + // 1: DeviceId
                "\"DEVICE_NAME\" TEXT," + // 2: DeviceName
                "\"CREATE_TIME\" TEXT," + // 3: CreateTime
                "\"USER_ID\" TEXT," + // 4: UserId
                "\"CLIENT_ID\" TEXT," + // 5: ClientId
                "\"MAP_COORD\" TEXT," + // 6: MapCoord
                "\"REMARKS\" TEXT," + // 7: Remarks
                "\"PLANT_ID\" TEXT," + // 8: PlantId
                "\"PLANT_NAME\" TEXT," + // 9: PlantName
                "\"PLANT_COUNT\" INTEGER," + // 10: PlantCount
                "\"PLANT_TYPE_ID\" TEXT," + // 11: PlantTypeId
                "\"PLANT_TYPE_NAME\" TEXT," + // 12: PlantTypeName
                "\"AREA_ID\" TEXT," + // 13: AreaId
                "\"AREA_NAME\" TEXT," + // 14: AreaName
                "\"POWER_EMERY\" TEXT," + // 15: PowerEmery
                "\"DEVICE_TYPE\" TEXT," + // 16: DeviceType
                "\"GATEWAY_ID\" TEXT," + // 17: GatewayId
                "\"GATEWAY_NAME\" TEXT);"); // 18: GatewayName
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"UNIT_WATER_MODEL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UnitWaterModel entity) {
        stmt.clearBindings();
 
        String UnitId = entity.getUnitId();
        if (UnitId != null) {
            stmt.bindString(1, UnitId);
        }
 
        String DeviceId = entity.getDeviceId();
        if (DeviceId != null) {
            stmt.bindString(2, DeviceId);
        }
 
        String DeviceName = entity.getDeviceName();
        if (DeviceName != null) {
            stmt.bindString(3, DeviceName);
        }
 
        String CreateTime = entity.getCreateTime();
        if (CreateTime != null) {
            stmt.bindString(4, CreateTime);
        }
 
        String UserId = entity.getUserId();
        if (UserId != null) {
            stmt.bindString(5, UserId);
        }
 
        String ClientId = entity.getClientId();
        if (ClientId != null) {
            stmt.bindString(6, ClientId);
        }
 
        String MapCoord = entity.getMapCoord();
        if (MapCoord != null) {
            stmt.bindString(7, MapCoord);
        }
 
        String Remarks = entity.getRemarks();
        if (Remarks != null) {
            stmt.bindString(8, Remarks);
        }
 
        String PlantId = entity.getPlantId();
        if (PlantId != null) {
            stmt.bindString(9, PlantId);
        }
 
        String PlantName = entity.getPlantName();
        if (PlantName != null) {
            stmt.bindString(10, PlantName);
        }
 
        Integer PlantCount = entity.getPlantCount();
        if (PlantCount != null) {
            stmt.bindLong(11, PlantCount);
        }
 
        String PlantTypeId = entity.getPlantTypeId();
        if (PlantTypeId != null) {
            stmt.bindString(12, PlantTypeId);
        }
 
        String PlantTypeName = entity.getPlantTypeName();
        if (PlantTypeName != null) {
            stmt.bindString(13, PlantTypeName);
        }
 
        String AreaId = entity.getAreaId();
        if (AreaId != null) {
            stmt.bindString(14, AreaId);
        }
 
        String AreaName = entity.getAreaName();
        if (AreaName != null) {
            stmt.bindString(15, AreaName);
        }
 
        String PowerEmery = entity.getPowerEmery();
        if (PowerEmery != null) {
            stmt.bindString(16, PowerEmery);
        }
 
        String DeviceType = entity.getDeviceType();
        if (DeviceType != null) {
            stmt.bindString(17, DeviceType);
        }
 
        String GatewayId = entity.getGatewayId();
        if (GatewayId != null) {
            stmt.bindString(18, GatewayId);
        }
 
        String GatewayName = entity.getGatewayName();
        if (GatewayName != null) {
            stmt.bindString(19, GatewayName);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UnitWaterModel entity) {
        stmt.clearBindings();
 
        String UnitId = entity.getUnitId();
        if (UnitId != null) {
            stmt.bindString(1, UnitId);
        }
 
        String DeviceId = entity.getDeviceId();
        if (DeviceId != null) {
            stmt.bindString(2, DeviceId);
        }
 
        String DeviceName = entity.getDeviceName();
        if (DeviceName != null) {
            stmt.bindString(3, DeviceName);
        }
 
        String CreateTime = entity.getCreateTime();
        if (CreateTime != null) {
            stmt.bindString(4, CreateTime);
        }
 
        String UserId = entity.getUserId();
        if (UserId != null) {
            stmt.bindString(5, UserId);
        }
 
        String ClientId = entity.getClientId();
        if (ClientId != null) {
            stmt.bindString(6, ClientId);
        }
 
        String MapCoord = entity.getMapCoord();
        if (MapCoord != null) {
            stmt.bindString(7, MapCoord);
        }
 
        String Remarks = entity.getRemarks();
        if (Remarks != null) {
            stmt.bindString(8, Remarks);
        }
 
        String PlantId = entity.getPlantId();
        if (PlantId != null) {
            stmt.bindString(9, PlantId);
        }
 
        String PlantName = entity.getPlantName();
        if (PlantName != null) {
            stmt.bindString(10, PlantName);
        }
 
        Integer PlantCount = entity.getPlantCount();
        if (PlantCount != null) {
            stmt.bindLong(11, PlantCount);
        }
 
        String PlantTypeId = entity.getPlantTypeId();
        if (PlantTypeId != null) {
            stmt.bindString(12, PlantTypeId);
        }
 
        String PlantTypeName = entity.getPlantTypeName();
        if (PlantTypeName != null) {
            stmt.bindString(13, PlantTypeName);
        }
 
        String AreaId = entity.getAreaId();
        if (AreaId != null) {
            stmt.bindString(14, AreaId);
        }
 
        String AreaName = entity.getAreaName();
        if (AreaName != null) {
            stmt.bindString(15, AreaName);
        }
 
        String PowerEmery = entity.getPowerEmery();
        if (PowerEmery != null) {
            stmt.bindString(16, PowerEmery);
        }
 
        String DeviceType = entity.getDeviceType();
        if (DeviceType != null) {
            stmt.bindString(17, DeviceType);
        }
 
        String GatewayId = entity.getGatewayId();
        if (GatewayId != null) {
            stmt.bindString(18, GatewayId);
        }
 
        String GatewayName = entity.getGatewayName();
        if (GatewayName != null) {
            stmt.bindString(19, GatewayName);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public UnitWaterModel readEntity(Cursor cursor, int offset) {
        UnitWaterModel entity = new UnitWaterModel( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // UnitId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // DeviceId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // DeviceName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // CreateTime
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // UserId
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // ClientId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // MapCoord
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // Remarks
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // PlantId
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // PlantName
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10), // PlantCount
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // PlantTypeId
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // PlantTypeName
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // AreaId
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // AreaName
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // PowerEmery
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // DeviceType
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // GatewayId
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18) // GatewayName
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UnitWaterModel entity, int offset) {
        entity.setUnitId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setDeviceId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDeviceName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCreateTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUserId(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setClientId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setMapCoord(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setRemarks(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPlantId(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setPlantName(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setPlantCount(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
        entity.setPlantTypeId(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setPlantTypeName(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setAreaId(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setAreaName(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setPowerEmery(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setDeviceType(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setGatewayId(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setGatewayName(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(UnitWaterModel entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(UnitWaterModel entity) {
        return null;
    }

    @Override
    public boolean hasKey(UnitWaterModel entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

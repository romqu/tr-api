/*
 * This file is generated by jOOQ.
 */
package de.romqu.trdesktopapi.public_;


import de.romqu.trdesktopapi.public_.tables.KeypairEntity;
import de.romqu.trdesktopapi.public_.tables.SessionEntity;
import de.romqu.trdesktopapi.public_.tables.records.KeypairRecordEntity;
import de.romqu.trdesktopapi.public_.tables.records.SessionRecordEntity;
import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in 
 * public.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<KeypairRecordEntity> KEYPAIR_PKEY = Internal.createUniqueKey(KeypairEntity.KEYPAIR, DSL.name("keypair_pkey"), new TableField[]{KeypairEntity.KEYPAIR.ID}, true);
    public static final UniqueKey<KeypairRecordEntity> KEYPAIR_PRIVATE_KEY_KEY = Internal.createUniqueKey(KeypairEntity.KEYPAIR, DSL.name("keypair_private_key_key"), new TableField[]{KeypairEntity.KEYPAIR.PRIVATE_KEY}, true);
    public static final UniqueKey<KeypairRecordEntity> KEYPAIR_PUBLIC_KEY_KEY = Internal.createUniqueKey(KeypairEntity.KEYPAIR, DSL.name("keypair_public_key_key"), new TableField[]{KeypairEntity.KEYPAIR.PUBLIC_KEY}, true);
    public static final UniqueKey<SessionRecordEntity> SESSION_DEVICE_ID_KEY = Internal.createUniqueKey(SessionEntity.SESSION, DSL.name("session_device_id_key"), new TableField[]{SessionEntity.SESSION.DEVICE_ID}, true);
    public static final UniqueKey<SessionRecordEntity> SESSION_KEYPAIR_ID_KEY = Internal.createUniqueKey(SessionEntity.SESSION, DSL.name("session_keypair_id_key"), new TableField[]{SessionEntity.SESSION.KEYPAIR_ID}, true);
    public static final UniqueKey<SessionRecordEntity> SESSION_PKEY = Internal.createUniqueKey(SessionEntity.SESSION, DSL.name("session_pkey"), new TableField[]{SessionEntity.SESSION.ID}, true);
    public static final UniqueKey<SessionRecordEntity> SESSION_REFRESH_TOKEN_KEY = Internal.createUniqueKey(SessionEntity.SESSION, DSL.name("session_refresh_token_key"), new TableField[]{SessionEntity.SESSION.REFRESH_TOKEN}, true);
    public static final UniqueKey<SessionRecordEntity> SESSION_RESET_PROCESS_ID_KEY = Internal.createUniqueKey(SessionEntity.SESSION, DSL.name("session_reset_process_id_key"), new TableField[]{SessionEntity.SESSION.RESET_PROCESS_ID}, true);
    public static final UniqueKey<SessionRecordEntity> SESSION_TOKEN_KEY = Internal.createUniqueKey(SessionEntity.SESSION, DSL.name("session_token_key"), new TableField[]{SessionEntity.SESSION.TOKEN}, true);
    public static final UniqueKey<SessionRecordEntity> SESSION_TRACKING_ID_KEY = Internal.createUniqueKey(SessionEntity.SESSION, DSL.name("session_tracking_id_key"), new TableField[]{SessionEntity.SESSION.TRACKING_ID}, true);
    public static final UniqueKey<SessionRecordEntity> SESSION_UUID_ID_KEY = Internal.createUniqueKey(SessionEntity.SESSION, DSL.name("session_uuid_id_key"), new TableField[]{SessionEntity.SESSION.UUID_ID}, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<SessionRecordEntity, KeypairRecordEntity> SESSION__FK_SESSION_KEYPAIR = Internal.createForeignKey(SessionEntity.SESSION, DSL.name("fk_session_keypair"), new TableField[]{SessionEntity.SESSION.KEYPAIR_ID}, Keys.KEYPAIR_PKEY, new TableField[]{KeypairEntity.KEYPAIR.ID}, true);
}

/*
 * This file is generated by jOOQ.
 */
package de.romqu.trdesktopapi.public_.tables.records;


import de.romqu.trdesktopapi.public_.tables.SessionEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;

import java.util.UUID;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class SessionRecordEntity extends UpdatableRecordImpl<SessionRecordEntity> implements Record8<Long, UUID, UUID, String, String, String, UUID, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.session.id</code>.
     */
    public void setId(@NotNull Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.session.id</code>.
     */
    @NotNull
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.session.uuid_id</code>.
     */
    public void setUuidId(@NotNull UUID value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.session.uuid_id</code>.
     */
    @NotNull
    public UUID getUuidId() {
        return (UUID) get(1);
    }

    /**
     * Setter for <code>public.session.device_id</code>.
     */
    public void setDeviceId(@NotNull UUID value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.session.device_id</code>.
     */
    @NotNull
    public UUID getDeviceId() {
        return (UUID) get(2);
    }

    /**
     * Setter for <code>public.session.token</code>.
     */
    public void setToken(@NotNull String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.session.token</code>.
     */
    @NotNull
    public String getToken() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.session.refresh_token</code>.
     */
    public void setRefreshToken(@NotNull String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.session.refresh_token</code>.
     */
    @NotNull
    public String getRefreshToken() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.session.tracking_id</code>.
     */
    public void setTrackingId(@NotNull String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.session.tracking_id</code>.
     */
    @NotNull
    public String getTrackingId() {
        return (String) get(5);
    }

    /**
     * Create a detached, initialised SessionRecordEntity
     */
    public SessionRecordEntity(@NotNull Long id, @NotNull UUID uuidId, @NotNull UUID deviceId, @NotNull String token, @NotNull String refreshToken, @NotNull String trackingId, @Nullable UUID resetProcessId, @NotNull Long keypairId) {
        super(SessionEntity.SESSION);

        setId(id);
        setUuidId(uuidId);
        setDeviceId(deviceId);
        setToken(token);
        setRefreshToken(refreshToken);
        setTrackingId(trackingId);
        setResetProcessId(resetProcessId);
        setKeypairId(keypairId);
    }

    /**
     * Getter for <code>public.session.reset_process_id</code>.
     */
    @Nullable
    public UUID getResetProcessId() {
        return (UUID) get(6);
    }

    /**
     * Setter for <code>public.session.reset_process_id</code>.
     */
    public void setResetProcessId(@Nullable UUID value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.session.keypair_id</code>.
     */
    @NotNull
    public Long getKeypairId() {
        return (Long) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>public.session.keypair_id</code>.
     */
    public void setKeypairId(@NotNull Long value) {
        set(7, value);
    }

    @Override
    @NotNull
    public Row8<Long, UUID, UUID, String, String, String, UUID, Long> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return SessionEntity.SESSION.ID;
    }

    @Override
    @NotNull
    public Field<UUID> field2() {
        return SessionEntity.SESSION.UUID_ID;
    }

    @Override
    @NotNull
    public Field<UUID> field3() {
        return SessionEntity.SESSION.DEVICE_ID;
    }

    @Override
    @NotNull
    public Field<String> field4() {
        return SessionEntity.SESSION.TOKEN;
    }

    @Override
    @NotNull
    public Field<String> field5() {
        return SessionEntity.SESSION.REFRESH_TOKEN;
    }

    @Override
    @NotNull
    public Field<String> field6() {
        return SessionEntity.SESSION.TRACKING_ID;
    }

    @Override
    @NotNull
    public Row8<Long, UUID, UUID, String, String, String, UUID, Long> valuesRow() {
        return (Row8) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<UUID> field7() {
        return SessionEntity.SESSION.RESET_PROCESS_ID;
    }

    @Override
    @NotNull
    public Long component1() {
        return getId();
    }

    @Override
    @NotNull
    public UUID component2() {
        return getUuidId();
    }

    @Override
    @NotNull
    public UUID component3() {
        return getDeviceId();
    }

    @Override
    @NotNull
    public String component4() {
        return getToken();
    }

    @Override
    @NotNull
    public String component5() {
        return getRefreshToken();
    }

    @Override
    @NotNull
    public String component6() {
        return getTrackingId();
    }

    @Override
    @NotNull
    public Field<Long> field8() {
        return SessionEntity.SESSION.KEYPAIR_ID;
    }

    @Override
    @Nullable
    public UUID component7() {
        return getResetProcessId();
    }

    @Override
    @NotNull
    public Long value1() {
        return getId();
    }

    @Override
    @NotNull
    public UUID value2() {
        return getUuidId();
    }

    @Override
    @NotNull
    public UUID value3() {
        return getDeviceId();
    }

    @Override
    @NotNull
    public String value4() {
        return getToken();
    }

    @Override
    @NotNull
    public String value5() {
        return getRefreshToken();
    }

    @Override
    @NotNull
    public String value6() {
        return getTrackingId();
    }

    @Override
    @NotNull
    public Long component8() {
        return getKeypairId();
    }

    @Override
    @Nullable
    public UUID value7() {
        return getResetProcessId();
    }

    @Override
    @NotNull
    public SessionRecordEntity value1(@NotNull Long value) {
        setId(value);
        return this;
    }

    @Override
    @NotNull
    public SessionRecordEntity value2(@NotNull UUID value) {
        setUuidId(value);
        return this;
    }

    @Override
    @NotNull
    public SessionRecordEntity value3(@NotNull UUID value) {
        setDeviceId(value);
        return this;
    }

    @Override
    @NotNull
    public SessionRecordEntity value4(@NotNull String value) {
        setToken(value);
        return this;
    }

    @Override
    @NotNull
    public SessionRecordEntity value5(@NotNull String value) {
        setRefreshToken(value);
        return this;
    }

    @Override
    @NotNull
    public SessionRecordEntity value6(@NotNull String value) {
        setTrackingId(value);
        return this;
    }

    @Override
    @NotNull
    public Long value8() {
        return getKeypairId();
    }

    @Override
    @NotNull
    public SessionRecordEntity value7(@Nullable UUID value) {
        setResetProcessId(value);
        return this;
    }

    @Override
    @NotNull
    public SessionRecordEntity value8(@NotNull Long value) {
        setKeypairId(value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached SessionRecordEntity
     */
    public SessionRecordEntity() {
        super(SessionEntity.SESSION);
    }

    @Override
    @NotNull
    public SessionRecordEntity values(@NotNull Long value1, @NotNull UUID value2, @NotNull UUID value3, @NotNull String value4, @NotNull String value5, @NotNull String value6, @Nullable UUID value7, @NotNull Long value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }
}

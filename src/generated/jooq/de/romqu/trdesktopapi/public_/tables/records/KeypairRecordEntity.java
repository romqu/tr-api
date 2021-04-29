/*
 * This file is generated by jOOQ.
 */
package de.romqu.trdesktopapi.public_.tables.records;


import de.romqu.trdesktopapi.public_.tables.KeypairEntity;

import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class KeypairRecordEntity extends UpdatableRecordImpl<KeypairRecordEntity> implements Record3<Long, byte[], byte[]> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.keypair.id</code>.
     */
    public void setId(@NotNull Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.keypair.id</code>.
     */
    @NotNull
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.keypair.private_key</code>.
     */
    public void setPrivateKey(@NotNull byte[] value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.keypair.private_key</code>.
     */
    @NotNull
    public byte[] getPrivateKey() {
        return (byte[]) get(1);
    }

    /**
     * Setter for <code>public.keypair.public_key</code>.
     */
    public void setPublicKey(@NotNull byte[] value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.keypair.public_key</code>.
     */
    @NotNull
    public byte[] getPublicKey() {
        return (byte[]) get(2);
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
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row3<Long, byte[], byte[]> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row3<Long, byte[], byte[]> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return KeypairEntity.KEYPAIR.ID;
    }

    @Override
    @NotNull
    public Field<byte[]> field2() {
        return KeypairEntity.KEYPAIR.PRIVATE_KEY;
    }

    @Override
    @NotNull
    public Field<byte[]> field3() {
        return KeypairEntity.KEYPAIR.PUBLIC_KEY;
    }

    @Override
    @NotNull
    public Long component1() {
        return getId();
    }

    @Override
    @NotNull
    public byte[] component2() {
        return getPrivateKey();
    }

    @Override
    @NotNull
    public byte[] component3() {
        return getPublicKey();
    }

    @Override
    @NotNull
    public Long value1() {
        return getId();
    }

    @Override
    @NotNull
    public byte[] value2() {
        return getPrivateKey();
    }

    @Override
    @NotNull
    public byte[] value3() {
        return getPublicKey();
    }

    @Override
    @NotNull
    public KeypairRecordEntity value1(@NotNull Long value) {
        setId(value);
        return this;
    }

    @Override
    @NotNull
    public KeypairRecordEntity value2(@NotNull byte[] value) {
        setPrivateKey(value);
        return this;
    }

    @Override
    @NotNull
    public KeypairRecordEntity value3(@NotNull byte[] value) {
        setPublicKey(value);
        return this;
    }

    @Override
    @NotNull
    public KeypairRecordEntity values(@NotNull Long value1, @NotNull byte[] value2, @NotNull byte[] value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached KeypairRecordEntity
     */
    public KeypairRecordEntity() {
        super(KeypairEntity.KEYPAIR);
    }

    /**
     * Create a detached, initialised KeypairRecordEntity
     */
    public KeypairRecordEntity(@NotNull Long id, @NotNull byte[] privateKey, @NotNull byte[] publicKey) {
        super(KeypairEntity.KEYPAIR);

        setId(id);
        setPrivateKey(privateKey);
        setPublicKey(publicKey);
    }
}

/*
 * This file is generated by jOOQ.
 */
package de.romqu.trdesktopapi.public_.tables.daos;


import de.romqu.trdesktopapi.public_.tables.KeypairEntity;
import de.romqu.trdesktopapi.public_.tables.records.KeypairRecordEntity;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Repository
public class KeypairDaoEntity extends DAOImpl<KeypairRecordEntity, de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity, Long> {

    /**
     * Create a new KeypairDaoEntity without any configuration
     */
    public KeypairDaoEntity() {
        super(KeypairEntity.KEYPAIR, de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity.class);
    }

    /**
     * Create a new KeypairDaoEntity with an attached configuration
     */
    @Autowired
    public KeypairDaoEntity(Configuration configuration) {
        super(KeypairEntity.KEYPAIR, de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity.class, configuration);
    }

    @Override
    @NotNull
    public Long getId(de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    @NotNull
    public List<de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity> fetchRangeOfIdEntity(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(KeypairEntity.KEYPAIR.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    @NotNull
    public List<de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity> fetchByIdEntity(Long... values) {
        return fetch(KeypairEntity.KEYPAIR.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity fetchOneByIdEntity(Long value) {
        return fetchOne(KeypairEntity.KEYPAIR.ID, value);
    }

    /**
     * Fetch records that have <code>private_key BETWEEN lowerInclusive AND upperInclusive</code>
     */
    @NotNull
    public List<de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity> fetchRangeOfPrivateKeyEntity(byte[] lowerInclusive, byte[] upperInclusive) {
        return fetchRange(KeypairEntity.KEYPAIR.PRIVATE_KEY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>private_key IN (values)</code>
     */
    @NotNull
    public List<de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity> fetchByPrivateKeyEntity(byte[]... values) {
        return fetch(KeypairEntity.KEYPAIR.PRIVATE_KEY, values);
    }

    /**
     * Fetch a unique record that has <code>private_key = value</code>
     */
    public de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity fetchOneByPrivateKeyEntity(byte[] value) {
        return fetchOne(KeypairEntity.KEYPAIR.PRIVATE_KEY, value);
    }

    /**
     * Fetch records that have <code>public_key BETWEEN lowerInclusive AND upperInclusive</code>
     */
    @NotNull
    public List<de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity> fetchRangeOfPublicKeyEntity(byte[] lowerInclusive, byte[] upperInclusive) {
        return fetchRange(KeypairEntity.KEYPAIR.PUBLIC_KEY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>public_key IN (values)</code>
     */
    @NotNull
    public List<de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity> fetchByPublicKeyEntity(byte[]... values) {
        return fetch(KeypairEntity.KEYPAIR.PUBLIC_KEY, values);
    }

    /**
     * Fetch a unique record that has <code>public_key = value</code>
     */
    public de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity fetchOneByPublicKeyEntity(byte[] value) {
        return fetchOne(KeypairEntity.KEYPAIR.PUBLIC_KEY, value);
    }
}

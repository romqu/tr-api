package de.romqu.trdesktopapi.data


import de.romqu.trdesktopapi.data.shared.insertReturningId
import de.romqu.trdesktopapi.public_.tables.KeypairEntity.KEYPAIR
import de.romqu.trdesktopapi.public_.tables.daos.KeypairDaoEntity
import de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity
import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.Record
import org.jooq.Table
import org.springframework.stereotype.Repository

@Repository
class KeypairRepository(
    private val dao: KeypairDaoEntity,
    private val db: DSLContext,
) {

    fun getById(id: Long): KeypairEntity =
        dao.fetchOneByIdEntity(id)

    fun save(entity: KeypairEntity): KeypairEntity {
        val id = db.insertReturningId(
            KEYPAIR,
            listOf(KEYPAIR.PRIVATE_KEY, KEYPAIR.PUBLIC_KEY),
            listOf(entity.privateKey, entity.publicKey)
        )

        return with(entity){
            KeypairEntity(id, privateKey, publicKey)
        }
    }

    fun update(entity: KeypairEntity) {
        dao.update(entity)
    }


}
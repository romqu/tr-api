package de.romqu.trdesktopapi.data.auth.session

import de.romqu.trdesktopapi.data.shared.extension.insertReturningId
import de.romqu.trdesktopapi.public_.tables.SessionEntity.SESSION
import de.romqu.trdesktopapi.public_.tables.daos.SessionDaoEntity
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class SessionRepository(
    private val dao: SessionDaoEntity,
    private val sessionApi: SessionApi,
    private val db: DSLContext,
) {

    suspend fun getRemote(sessionId: String) = sessionApi.getSessionToken(sessionId)

/*
    fun getById(id: Long): SessionEntity? =
        dao.fetchOneByIdEntity(id)
*/

    fun getByUuid(uuid: UUID): SessionEntity? =
        dao.fetchOneByUuidIdEntity(uuid)

    fun getByUuid(uuid: String): SessionEntity? =
        dao.fetchOneByUuidIdEntity(UUID.fromString(uuid))

    fun save(entity: SessionEntity): SessionEntity {
        val id = db.insertReturningId(
            SESSION,
            listOf(
                SESSION.UUID_ID,
                SESSION.DEVICE_ID,
                SESSION.KEYPAIR_ID,
                SESSION.TRACKING_ID,
                SESSION.REFRESH_TOKEN,
                SESSION.TOKEN,
            ),
            listOf(
                entity.uuidId,
                entity.deviceId,
                entity.keypairId,
                entity.trackingId,
                entity.refreshToken,
                entity.token,
            )
        )
        return with(entity) {
            SessionEntity(
                id,
                uuidId,
                deviceId,
                token,
                refreshToken,
                trackingId,
                resetProcessId,
                keypairId,
            )
        }
    }

    fun update(entity: SessionEntity): SessionEntity {
        dao.update(entity)
        return entity
    }

    fun deleteAll() {
        db.delete(SESSION).execute()
    }

    fun doesExist(uuid: UUID): Boolean {
        return db.fetchExists(db.selectOne().from(SESSION).where(SESSION.UUID_ID.eq(uuid)))
    }
}
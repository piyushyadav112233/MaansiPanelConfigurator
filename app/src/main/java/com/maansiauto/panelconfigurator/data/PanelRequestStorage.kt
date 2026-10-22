package com.maansiauto.panelconfigurator.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Entity(tableName = "panel_requests")
data class PanelRequestEntity(
    @PrimaryKey val id: String,
    val submittedAt: Long,
    val panelTypeId: String,
    val panelTypeName: String,
    val customPanelNote: String,
    val ratingJson: String,
    val equipmentJson: String,
    val customerJson: String
)

@Dao
interface PanelRequestDao {
    @Insert
    suspend fun insert(entity: PanelRequestEntity)

    @Query("SELECT * FROM panel_requests ORDER BY submittedAt DESC")
    fun getAll(): Flow<List<PanelRequestEntity>>

    @Query("DELETE FROM panel_requests WHERE id = :id")
    suspend fun deleteById(id: String)
}

@Database(entities = [PanelRequestEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun panelRequestDao(): PanelRequestDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "maansi_panel_configurator.db"
                ).build().also { INSTANCE = it }
            }
    }
}

/**
 * Bridges the domain model (PanelRequest) to the Room entity, storing the
 * equipment/rating/customer sub-objects as JSON so we don't need extra tables.
 * All reads/writes are local to the device - no network or backend required.
 */
class PanelRequestRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).panelRequestDao()
    private val gson = Gson()

    val requests: Flow<List<PanelRequest>> = dao.getAll().map { entities ->
        entities.map { it.toDomain(gson) }
    }

    suspend fun save(request: PanelRequest) {
        dao.insert(request.toEntity(gson))
    }

    suspend fun delete(id: String) {
        dao.deleteById(id)
    }

    private fun PanelRequest.toEntity(gson: Gson) = PanelRequestEntity(
        id = id,
        submittedAt = submittedAt,
        panelTypeId = panelTypeId,
        panelTypeName = panelTypeName,
        customPanelNote = customPanelNote,
        ratingJson = gson.toJson(rating),
        equipmentJson = gson.toJson(equipment),
        customerJson = gson.toJson(customer)
    )

    private fun PanelRequestEntity.toDomain(gson: Gson): PanelRequest {
        val equipmentType = object : TypeToken<List<EquipmentSelection>>() {}.type
        return PanelRequest(
            id = id,
            submittedAt = submittedAt,
            panelTypeId = panelTypeId,
            panelTypeName = panelTypeName,
            customPanelNote = customPanelNote,
            rating = gson.fromJson(ratingJson, RatingSpec::class.java),
            equipment = gson.fromJson(equipmentJson, equipmentType),
            customer = gson.fromJson(customerJson, CustomerInfo::class.java)
        )
    }
}

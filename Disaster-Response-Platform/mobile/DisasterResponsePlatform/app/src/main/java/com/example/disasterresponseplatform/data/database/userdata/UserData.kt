package com.example.disasterresponseplatform.data.database.userdata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.disasterresponseplatform.data.database.DatabaseInfo

@Entity(tableName = DatabaseInfo.USER_DATA)
data class UserData (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = UserDataCols.id)
    val ID: Int?, // if user does not enter ID, it generates it automatically
    @ColumnInfo(name = UserDataCols.username)
    val username: String,
    @ColumnInfo(name = UserDataCols.email)
    val email: String,
    @ColumnInfo(name = UserDataCols.phone)
    val phone: String,
    @ColumnInfo(name = UserDataCols.name)
    val name: String,
    @ColumnInfo(name = UserDataCols.surname)
    val surname: String,
    @ColumnInfo(name = UserDataCols.isEmailVerified)
    val isEmailVerified: Boolean,
    @ColumnInfo(name = UserDataCols.isPhoneVerified)
    val isPhoneVerified: Boolean,
    @ColumnInfo(name = UserDataCols.notifications)
    val notifications: String?,
    @ColumnInfo(name = UserDataCols.verificationLevel)
    val verificationLevel: Int,
    @ColumnInfo(name = UserDataCols.profileInfoShared)
    val profileInfoShared: Boolean,
    @ColumnInfo(name = UserDataCols.verifiedBy)
    val verifiedBy: String?,
    @ColumnInfo(name = UserDataCols.birth)
    val birth: String?,
    @ColumnInfo(name = UserDataCols.nationality)
    val nationality: String?,
    @ColumnInfo(name = UserDataCols.idNumber)
    val idNumber: String?,
    @ColumnInfo(name = UserDataCols.address)
    val address: String?,
    @ColumnInfo(name = UserDataCols.education)
    val education: String?,
    @ColumnInfo(name = UserDataCols.healthCondition)
    val healthCondition: String?,
    @ColumnInfo(name = UserDataCols.bloodType)
    val bloodType: String?,
    @ColumnInfo(name = UserDataCols.profilePhoto)
    val profilePhoto: String?,
    @ColumnInfo(name = UserDataCols.socialMedia)
    val socialMedia: String?,
    @ColumnInfo(name = UserDataCols.skills)
    val skills: String?,
    @ColumnInfo(name = UserDataCols.languages)
    val languages: String?,
    @ColumnInfo(name = UserDataCols.professions)
    val professions: String?
)
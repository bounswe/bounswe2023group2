package com.example.disasterresponseplatform.dependencyInjection

import android.app.Application
import com.example.disasterresponseplatform.data.database.DarpDB
import com.example.disasterresponseplatform.data.repositories.NeedRepository
import com.example.disasterresponseplatform.data.repositories.UserDataRepository
import com.example.disasterresponseplatform.data.repositories.ActionRepository
import com.example.disasterresponseplatform.data.repositories.EmergencyRepository
import com.example.disasterresponseplatform.data.repositories.EventRepository
import com.example.disasterresponseplatform.data.repositories.ResourceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** This is a Module for Hilt, Hilt has some modules to define some values. If you define those values there, you
 * don't need to define them everytime to call those. The only thing to do is @Inject method before you call those values
 * If you Inject them in constructor you need to @Inject constructor, else if in class first you should
 * annotate that class with @AndroidEntryPoint then you specify its class and add @Inject annotation to head of that.
 * Because there are different lifecycles in Android, in @InstallIn parameter you specify
 * to get more details you can visit https://developer.android.com/training/dependency-injection/hilt-android
 * lifecycle of values that those class defines
 * This is SingletonComponent because those values should live as application does
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    /** It returns a DarpDB instance
     * @Provide is for providing dependency which class it returns.
     * To do that without errors, You should specify only one provide method for each class
     * otherwise you should named them and call them with their names.
     * @Singleton is for making it single instance of this. If it doesn't exist, everytime we call this it creates new instance
     * @param app is Application, as I mentioned in DarpDB class, it comes from there and returns ActivityContext
     */
    @Provides
    @Singleton
    fun provideDatabase(app: Application): DarpDB = DarpDB.getInstance(app)
    // Application comes from AppTemp thanks to annotation @HiltAndroidApp

    /**
     * It returns Repository instance
     * @param database is comes from provideDatabase method automatically
     */
    @Provides
    @Singleton
    fun provideNeedRepository(database: DarpDB): NeedRepository = NeedRepository(database.needDao)

    @Provides
    @Singleton
    fun provideUserDataRepository(database: DarpDB): UserDataRepository = UserDataRepository(database.userDataDao)

    @Provides
    @Singleton
    fun provideActionRepository(database: DarpDB): ActionRepository = ActionRepository(database.actionDao)

    @Provides
    @Singleton
    fun provideEventRepository(database: DarpDB): EventRepository = EventRepository(database.eventDao)

    @Provides
    @Singleton
    fun provideEmergencyRepository(database: DarpDB): EmergencyRepository = EmergencyRepository(database.emergencyDao)

    @Provides
    @Singleton
    fun provideResourceRepository(database: DarpDB): ResourceRepository = ResourceRepository(database.resourceDao)

}
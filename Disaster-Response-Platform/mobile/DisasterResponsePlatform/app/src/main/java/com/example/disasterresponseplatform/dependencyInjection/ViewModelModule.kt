package com.example.disasterresponseplatform.dependencyInjection

import com.example.disasterresponseplatform.data.repositories.ActionRepository
import com.example.disasterresponseplatform.data.repositories.EmergencyRepository
import com.example.disasterresponseplatform.data.repositories.EventRepository
import com.example.disasterresponseplatform.data.repositories.NeedRepository
import com.example.disasterresponseplatform.data.repositories.ResourceRepository
import com.example.disasterresponseplatform.data.repositories.UserDataRepository
import com.example.disasterresponseplatform.ui.activity.action.ActionViewModel
import com.example.disasterresponseplatform.ui.activity.emergency.EmergencyViewModel
import com.example.disasterresponseplatform.ui.activity.event.EventViewModel
import com.example.disasterresponseplatform.ui.activity.need.NeedViewModel
import com.example.disasterresponseplatform.ui.activity.resource.ResourceViewModel
import com.example.disasterresponseplatform.ui.activity.userdata.UserDataViewModel
import com.example.disasterresponseplatform.ui.authentication.AuthenticationViewModel
import com.example.disasterresponseplatform.ui.map.MapViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/** This is a module object for Dependency Injection with Hilt
 * It defines the viewModel inside that object
 * Because ViewModel's life is different than App's Life in lifecycle we have to define it another module
 * @InstallIn(ViewModelComponent::class) means it's life as much as ViewModel's life
 */
@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {


    /**
     * It defines our viewModel, app and repository are gotten from Hilt DI
     */
    @Provides
    @ViewModelScoped
    fun provideNeedViewModel(repository: NeedRepository): NeedViewModel =
        NeedViewModel(repository)
    @Provides
    @ViewModelScoped
    fun provideAuthenticationViewModel(): AuthenticationViewModel = AuthenticationViewModel()

    @Provides
    @ViewModelScoped
    fun provideUserDataViewModel(repository: UserDataRepository): UserDataViewModel =
        UserDataViewModel(repository)

    @Provides
    @ViewModelScoped
    fun provideActionViewModel(repository: ActionRepository): ActionViewModel =
        ActionViewModel(repository)

    @Provides
    @ViewModelScoped
    fun provideEventViewModel(repository: EventRepository): EventViewModel =
        EventViewModel(repository)

    @Provides
    @ViewModelScoped
    fun provideEmergencyViewModel(repository: EmergencyRepository): EmergencyViewModel =
        EmergencyViewModel(repository)

    @Provides
    @ViewModelScoped
    fun provideResourceViewModel(repository: ResourceRepository): ResourceViewModel =
        ResourceViewModel(repository)

    @Provides
    @ViewModelScoped
    fun provideMapViewModel(): MapViewModel = MapViewModel()
}
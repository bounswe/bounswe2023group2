package com.example.disasterresponseplatform.dependencyInjection

import com.example.disasterresponseplatform.data.repositories.NeedRepository
import com.example.disasterresponseplatform.ui.activity.need.NeedViewModel
import com.example.disasterresponseplatform.ui.authentication.AuthenticationViewModel
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

}
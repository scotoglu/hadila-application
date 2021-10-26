package com.scoto.hadila.di.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.scoto.hadila.helper.FirebaseDbManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun provideFireabaseFirestore(): FirebaseFirestore =
        FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthID(): String =
        provideAuth().currentUser?.uid.toString()

    @Provides
    @Singleton
    fun provideDbRef(): FirebaseDbManager =
        FirebaseDbManager(provideFireabaseFirestore(), provideAuthID())
}
package com.example.instagramclone

import com.example.instagramclone.source.remote.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltModule {
    @Provides
    @Singleton
    fun provideAuthentication():FirebaseAuth= Firebase.auth
    @Provides
    @Singleton
    fun  provideFireStore():FirebaseFirestore= Firebase.firestore
    @Provides
    @Singleton
    fun provideStorage():FirebaseStorage= Firebase.storage
    @Provides
    @Singleton
    fun provideAuthenticationRepository(
    ): AuthenticationRepository {
        return  AuthenticationRepository()
    }
}
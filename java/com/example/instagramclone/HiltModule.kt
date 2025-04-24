package com.example.instagramclone

import com.example.instagramclone.source.DefaultRepository
import com.example.instagramclone.source.DefaultRepositoryImpl
import com.example.instagramclone.source.local.LocalDefaultRepositoryImp
import com.example.instagramclone.source.remote.AuthenticationRepository
import com.example.instagramclone.source.remote.ChatRepository
import com.example.instagramclone.source.remote.RemoteDefaultRepositoryImp
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
    @Provides
    @Singleton
    fun provideChatRepository(
    ): ChatRepository {
        return  ChatRepository()
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(

    ): DefaultRepositoryImpl {
        return DefaultRepositoryImpl(
            remoteRepository = RemoteDefaultRepositoryImp(
            ),
            localRepository = LocalDefaultRepositoryImp()
        )
    }
}
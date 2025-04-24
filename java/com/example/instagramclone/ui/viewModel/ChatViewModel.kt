package com.example.instagramclone.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.instagramclone.data.model.Conversation
import com.example.instagramclone.data.model.Message
import com.example.instagramclone.source.remote.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private  val chatRepository: ChatRepository
) : ViewModel() {
    private val _listMessage = MutableStateFlow<List<Message>>(emptyList())
    val listMessage: StateFlow<List<Message>> = _listMessage
    private  val _listConversation = MutableStateFlow<List<Conversation>>(emptyList())
    val listConversation: StateFlow<List<Conversation>> = _listConversation
    private val _currentConversation = MutableStateFlow<Conversation?>(null)
    val currentConversation: StateFlow<Conversation?> = _currentConversation



    fun sendMessage(message: Message){
        viewModelScope.launch {
            chatRepository.sendMessage(message)
        }
    }
    fun setCurrentConversation(conversation: Conversation?){
        _currentConversation.value=conversation
    }
    fun fetchConversation(userId:String){
        viewModelScope.launch {
            chatRepository.fetchConversations(userId){
                _listConversation.value= it

            }
        }
    }

    fun fetchMessage(userSenderId:String,userReceiverId:String){
        viewModelScope.launch {
            chatRepository.fectchMessge(userSenderId,userReceiverId){
                messages->messages?.let { _listMessage.value = it }
            }
        }

    }
}
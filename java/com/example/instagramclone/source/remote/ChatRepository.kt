package com.example.instagramclone.source.remote

import android.media.midi.MidiSender
import android.util.Log
import com.example.instagramclone.data.model.Conversation
import com.example.instagramclone.data.model.Message
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions


class ChatRepository {
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    fun sendMessage(message:Message ){
        val chatId: String = generateChatId(message.senderId, message.receiverId)

     val chatRef=   db.collection("chats")
            .document(chatId)
        chatRef.set(
            mapOf("createdAt" to FieldValue.serverTimestamp()),
            SetOptions.merge() // merge để không ghi đè nếu đã có
        )
        chatRef.collection("messages")
            .add(message)

    }
       fun generateChatId(userId1: String, userId2: String): String {
        return if (userId1 < userId2) {
            "${userId1}_${userId2}"
        } else {
            "${userId2}_${userId1}"
        }
         }
    fun fectchMessge(
        userSenderId: String,
        userReceiverId: String,
        onMessagesFetched: (List<Message>?) -> Unit
    ) {
        val chatId = generateChatId(userSenderId, userReceiverId)

        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null || snapshots == null) {
                    onMessagesFetched(null)
                    return@addSnapshotListener
                }

                val messages = snapshots.documents.mapNotNull {
                    it.toObject(Message::class.java)
                }

                onMessagesFetched(messages)
            }
    }
    fun fetchConversations(
        currentUserId: String,
        onResult: (List<Conversation>) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val resultList = mutableListOf<Conversation>()

        db.collection("chats")
            .get()
            .addOnSuccessListener { chatDocs ->
                Log.d("ChatRepository", "fetchConversations: ${chatDocs.documents.size}")
                val relevantChats = chatDocs.filter { it.id.contains(currentUserId) }

                if (relevantChats.isEmpty()) {
                    onResult(emptyList())
                    return@addOnSuccessListener
                }


                for (chatDoc in relevantChats) {
                    val chatId = chatDoc.id

                    if (!chatId.contains(currentUserId)) continue

                    val otherUserId = chatId.split("_").first { it != currentUserId }

                    val latestMsgTask = db.collection("chats")
                        .document(chatId)
                        .collection("messages")
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .limit(1)
                        .get()

                    val userProfileTask = db.collection("users")
                        .document(otherUserId)
                        .get()

                    val combinedTask = Tasks.whenAllSuccess<Any>(latestMsgTask, userProfileTask)
                    combinedTask.addOnSuccessListener { results ->
                        val latestMsgSnapshot = results[0] as QuerySnapshot
                        val userSnapshot = results[1] as DocumentSnapshot

                        val latestMessage =
                            latestMsgSnapshot.documents.firstOrNull()?.getString("text") ?: ""
                        val timestamp =
                            latestMsgSnapshot.documents.firstOrNull()?.getLong("timestamp") ?: 0L

                        val name = userSnapshot.getString("name") ?: "Unknown"
                        val avatarUrl = userSnapshot.getString("imageUrl") ?: ""
                        val isOnline = userSnapshot.getBoolean("isOnline") ?: false

                        resultList.add(
                            Conversation(
                                userId = otherUserId,
                                name = name,
                                avatarUrl = avatarUrl,
                                lastMessage = latestMessage,
                                timestamp = timestamp,
                                isOnline = isOnline
                            )
                        )

                        if (resultList.size == relevantChats.size) {
                            onResult(resultList.sortedByDescending { it.timestamp })
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("ChatRepository", "Error loading chats", e)
            }
    }


}
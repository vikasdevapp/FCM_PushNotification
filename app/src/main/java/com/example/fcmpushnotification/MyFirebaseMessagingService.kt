package com.example.fcmpushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelid="notification_channel"
const val channelName="com.example.fcmpushnotification"


class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {

        if(message.getNotification()!=null){
            generateNotification(message.notification!!.title!!,message.notification!!.body!!)

        }
    }


    fun generateNotification(title:String,message:String){

        val intent=Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent=PendingIntent.getActivity(this,0,intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        var builder:NotificationCompat.Builder=NotificationCompat.Builder(applicationContext,
            channelid)
            .setSmallIcon(R.drawable.download)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder=builder.setContent(getRemoteView(title,message))

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            val notificationChannel=NotificationChannel(channelid, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build())



    }

    fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView=RemoteViews("com.example.fcmpushnotification",R.layout.notification_card)

        remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.message,message)
        remoteView.setImageViewResource(R.id.notification_image,R.drawable.download)
        return remoteView
    }
}
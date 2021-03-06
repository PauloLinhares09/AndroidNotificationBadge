package br.com.packapps.androidbadge

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.provider.Settings
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat


class MainActivity : AppCompatActivity() {

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //criando Group Fora de campo
        createGroupNotification("Fora de Campo", getString(R.string.off_filed_group_id))
        //criando Group Bola rolando
        createGroupNotification("Bola rolando", getString(R.string.game_on_group_id))


        //Criando Channel "Jogadores para o Group "Fora de Campo"
        createNotificationChannel(
                getString(R.string.notification_channel_players),
                "Jogadores",
                 "Canal de notícias dos jogadores fora de campo" ,
                getString(R.string.off_filed_group_id))

        //Criando Channel "Times" para o Group "Fora de Campo"
        createNotificationChannel(
                getString(R.string.notification_channel_teams),
                "Times",
                "Canal com as principais notícias fora de campo do seu Time do coração",
                getString(R.string.off_filed_group_id))

        //Criando Channel "Times dentro de Campo" para o Group "Dentro de campo"
        createNotificationChannel(
                getString(R.string.notification_channel_teams_on_game),
                "Times",
                "Canal de notícias do seu Time do coração com a bola rolando",
                getString(R.string.game_on_group_id))




        //FAB
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Notification created. See icon of the App", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            val intent = Intent(this, MainActivity::class.java)

            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val notification = NotificationCompat.Builder(this, getString(R.string.notification_channel_teams))
                    .setContentTitle("Fechou com o Flamengo")
                    .setContentText("Ronaldo fenômeno volta aos gramados e acerta passe vitalício com o Flamengo ;)")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setBadgeIconType(R.mipmap.ic_launcher_round)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)


            val notificationManagerCompat = NotificationManagerCompat.from(this)
            notificationManagerCompat.notify(0, notification.build())



        }
    }

    private fun createNotificationChannel(notificationIdChannel : String, nameChannel : String, descriptionChannel : String, groupId : String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(notificationIdChannel, nameChannel, importance).apply {
                description = descriptionChannel
                lightColor = getColor(R.color.colorAccent)
                enableVibration(true)
                enableLights(true)
                group = groupId
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

        }
    }

    private fun createGroupNotification(groupName : String, groupId : String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannelGroup(NotificationChannelGroup(groupId, groupName))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings_notification -> {
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, getString(R.string.notification_channel_teams))
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

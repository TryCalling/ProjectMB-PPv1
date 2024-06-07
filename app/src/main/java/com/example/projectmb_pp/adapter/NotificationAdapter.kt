import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmb_pp.R
import com.google.firebase.messaging.RemoteMessage

class NotificationAdapter(private val notifications: List<RemoteMessage.Notification>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notificationTitle: TextView = itemView.findViewById(R.id.notificationTitle)
        val notificationContent: TextView = itemView.findViewById(R.id.notificationContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.notificationTitle.text = notification.title
//        holder.notificationContent.text = notification.content
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}

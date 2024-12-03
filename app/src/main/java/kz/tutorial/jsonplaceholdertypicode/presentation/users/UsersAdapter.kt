package kz.tutorial.jsonplaceholdertypicode.presentation.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.tutorial.jsonplaceholdertypicode.R
import kz.tutorial.jsonplaceholdertypicode.domain.entity.User
import kz.tutorial.jsonplaceholdertypicode.presentation.utils.ClickListener
import kz.tutorial.jsonplaceholdertypicode.presentation.utils.EmailClickListener

class UsersAdapter(
    private val layoutInflater: LayoutInflater,
    private val emailClickListener: EmailClickListener,
) : RecyclerView.Adapter<UsersViewHolder>() {

    private val users: MutableList<User> = mutableListOf()
    var listener: ClickListener<User>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = layoutInflater.inflate(R.layout.item_user, parent, false)
        return UsersViewHolder(view, emailClickListener)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
        holder.itemView.setOnClickListener {
            listener?.onClick(user)
        }
    }

    fun setData(newData: List<User>) {
        notifyItemRangeRemoved(0, users.size)
        users.clear()
        users.addAll(newData)
        notifyItemRangeInserted(0, users.size)
    }

}

class UsersViewHolder(
    itemView: View,
    private val emailClickListener: EmailClickListener,
) : RecyclerView.ViewHolder(itemView) {
    private val tvUserName: TextView = itemView.findViewById(R.id.tv_username)
    private val tvFullName: TextView = itemView.findViewById(R.id.tv_full_name)
    private val tvEmail: TextView = itemView.findViewById(R.id.tv_user_email)

    fun bind(user: User) {
        tvUserName.text = user.username
        tvFullName.text = user.name
        tvEmail.text = user.email
        tvEmail.setOnClickListener { emailClickListener.onEmailClick(user.email) }
    }
}
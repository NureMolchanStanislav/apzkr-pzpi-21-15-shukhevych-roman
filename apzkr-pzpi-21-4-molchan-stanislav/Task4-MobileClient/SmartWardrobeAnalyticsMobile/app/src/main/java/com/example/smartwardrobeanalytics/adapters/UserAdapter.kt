package com.example.smartwardrobeanalytics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.User

class UserAdapter(
    private val context: Context,
    private var users: MutableList<User>,
    private val onBanClick: (User) -> Unit,
    private val onUnbanClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var filteredUsers: MutableList<User> = users.toMutableList()

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userId: TextView = view.findViewById(R.id.user_id)
        val userEmail: TextView = view.findViewById(R.id.user_email)
        val userStatus: TextView = view.findViewById(R.id.user_status)
        val actionButton: Button = view.findViewById(R.id.action_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = filteredUsers[position]
        holder.userId.text = user.id
        holder.userEmail.text = user.email
        holder.userStatus.text = if (user.isDeleted) "Banned" else "Active"
        holder.actionButton.text = if (user.isDeleted) "Unban" else "Ban"
        holder.actionButton.setOnClickListener {
            if (user.isDeleted) {
                onUnbanClick(user)
            } else {
                onBanClick(user)
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredUsers.size
    }

    fun updateUsers(newUsers: List<User>) {
        users.clear()
        users.addAll(newUsers)
        filterById("")
    }

    fun filterById(query: String) {
        filteredUsers = users.filter { it.id.contains(query, ignoreCase = true) }.toMutableList()
        notifyDataSetChanged()
    }

    fun filterByEmail(query: String) {
        filteredUsers = users.filter { it.email?.contains(query, ignoreCase = true) == true }.toMutableList()
        notifyDataSetChanged()
    }
}

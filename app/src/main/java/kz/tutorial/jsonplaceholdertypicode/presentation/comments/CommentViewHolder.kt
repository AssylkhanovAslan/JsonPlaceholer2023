package kz.tutorial.jsonplaceholdertypicode.presentation.comments

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kz.tutorial.jsonplaceholdertypicode.R
import kz.tutorial.jsonplaceholdertypicode.domain.entity.Comment
import kz.tutorial.jsonplaceholdertypicode.presentation.post_details.PostDetailsFragment
import kz.tutorial.jsonplaceholdertypicode.presentation.post_details.PostDetailsFragmentDirections
import kz.tutorial.jsonplaceholdertypicode.presentation.posts.PostsFragmentDirections
import kz.tutorial.jsonplaceholdertypicode.presentation.register.RetrofitClient
import kz.tutorial.jsonplaceholdertypicode.presentation.show_all_comments.ShowAllFragment
import kz.tutorial.jsonplaceholdertypicode.presentation.show_all_comments.ShowAllFragmentDirections
import kz.tutorial.jsonplaceholdertypicode.presentation.utils.EmailClickListener
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class CommentViewHolder(
    itemView: View,
    private val navController: NavController,
    private val isShowAll : Boolean
) : ViewHolder(itemView) {
    private val tvCommentName: TextView = itemView.findViewById(R.id.tv_comment_name)
    private val tvBody: TextView = itemView.findViewById(R.id.tv_body)
    private val tvCommentDate: TextView = itemView.findViewById(R.id.tv_comment_date)

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatPostCreationTime(creationTime: String): String {
        val postCreationDateTime = ZonedDateTime.parse(creationTime).withZoneSameInstant(ZoneId.of("UTC")).withZoneSameInstant(
            ZoneId.of("UTC+5")).toLocalDateTime()

        val currentTime = LocalDateTime.now(ZoneId.of("UTC+5"))

        val duration = Duration.between(postCreationDateTime, currentTime)

        return when {
            duration.toDays() >= 365 -> {
                val years = duration.toDays() / 365
                if (years > 1) "$years years ago" else "1 year ago"
            }
            duration.toDays() >= 30 -> {
                val months = duration.toDays() / 30
                if (months > 1) "$months months ago" else "1 month ago"
            }
            duration.toDays() >= 7 -> {
                val weeks = duration.toDays() / 7
                if (weeks > 1) "$weeks weeks ago" else "1 week ago"
            }
            duration.toDays() > 0 -> {
                val days = duration.toDays()
                if (days > 1) "$days days ago" else "1 day ago"
            }
            duration.toHours() > 0 -> {
                val hours = duration.toHours()
                if (hours > 1) "$hours hours ago" else "1 hour ago"
            }
            duration.toMinutes() > 0 -> {
                val minutes = duration.toMinutes()
                if (minutes > 1) "$minutes minutes ago" else "1 minute ago"
            }
            else -> "Just now"

        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(comment: Comment) {
//        tvCommentName.text = comment.name
        GlobalScope.launch(Dispatchers.Main) {
            val user = RetrofitClient.apiService.getUser(comment.userId)
            tvCommentName.text = user.username
        }
        tvCommentName.setOnClickListener{
            if (isShowAll)
                navController.navigate(ShowAllFragmentDirections.actionShowAllFragmentToUserProfileFragment(comment.userId))
            else
                navController.navigate(PostDetailsFragmentDirections.actionPostDetailsToUserProfileFragment(comment.userId))

        }

        tvCommentDate.text = formatPostCreationTime(comment.createdAt)
        tvBody.text = comment.body

    }
}
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kz.tutorial.jsonplaceholdertypicode.R
import kz.tutorial.jsonplaceholdertypicode.domain.entity.Post
import kz.tutorial.jsonplaceholdertypicode.presentation.post_details.PostDetailsFragmentDirections
import kz.tutorial.jsonplaceholdertypicode.presentation.posts.PostsFragmentDirections
import kz.tutorial.jsonplaceholdertypicode.presentation.register.RetrofitClient
import kz.tutorial.jsonplaceholdertypicode.presentation.register.TokenManager
import kz.tutorial.jsonplaceholdertypicode.presentation.utils.ClickListener

class PostAdapter(
    private val layoutInflater: LayoutInflater,
    private val contextProvider: Context,
    private val navController: NavController
) : RecyclerView.Adapter<PostViewHolder>() {

    private val posts: MutableList<Post> = mutableListOf()
    var listener: ClickListener<Post>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = layoutInflater.inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view, contextProvider, navController)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
        holder.itemView.setOnClickListener {
            listener?.onClick(post)
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun setData(newData: List<Post>) {
        posts.clear()
        posts.addAll(newData)
        notifyDataSetChanged()
    }
}

class PostViewHolder(itemView: View,  private val contextProvider: Context,
                     private val navController: NavController) : ViewHolder(itemView) {
    private var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
    private var tvBody: TextView = itemView.findViewById(R.id.tv_body)
    private var tvAuthor: TextView = itemView.findViewById(R.id.tv_author)
    private var tvDate: TextView = itemView.findViewById(R.id.tv_date)
    private var tvLikeCount: TextView = itemView.findViewById(R.id.like_count)
    private var tvCommentCount: TextView = itemView.findViewById(R.id.comment_count)
    private var ivFavourite: ImageView = itemView.findViewById(R.id.favorite)
    private var ivComment: ImageView = itemView.findViewById(R.id.iv_comment)
    private var tvViewCount: TextView = itemView.findViewById(R.id.views_count)
    private var isLiked : Boolean = false


    @RequiresApi(Build.VERSION_CODES.O)
    fun formatPostCreationTime(creationTime: String): String {
        val postCreationDateTime = ZonedDateTime.parse(creationTime).withZoneSameInstant(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC+5")).toLocalDateTime()

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
    fun bind(post: Post) {
        tvTitle.text = post.title
        tvBody.text = post.body
        tvDate.text = formatPostCreationTime(post.createdAt)
        tvViewCount.setText(post.ViewCount.toString())

        val token = TokenManager.getToken(contextProvider)
        val tokenRequest = "Bearer $token"


        GlobalScope.launch(Dispatchers.Main) {
            try {
                val user = RetrofitClient.apiService.getUser(post.userId)
                tvAuthor.text = user.username
                tvLikeCount.text = RetrofitClient.apiService.getPostLikes(post.id).data.toString()
                tvCommentCount.text = RetrofitClient.apiService.getPostComments(post.id).size.toString()
                isLiked = RetrofitClient.apiService.getPostLiked(post.id, tokenRequest).data
                RetrofitClient.apiService.addViewPost(post.id, tokenRequest)
            } catch (e: Exception) {
                Log.d("PostAdapter", "Error: ${e.message}")
            }


            if (isLiked)
                ivFavourite.setImageResource(R.drawable.heart_icon_liked)
            else
                ivFavourite.setImageResource(R.drawable.heart_icon_unliked)
        }

        tvAuthor.setOnClickListener {
            navController.navigate(PostsFragmentDirections.actionMenuPostsToUserProfileFragment(post.userId))
        }

        ivFavourite.setOnClickListener{
            if (!isLiked) {
                GlobalScope.launch(Dispatchers.Main) {
                    val response = try {
                        RetrofitClient.apiService.likePost(post.id, tokenRequest)
                    } catch (e: Exception) {
                        Log.d("PostAdapter", "Error: ${e.message}")
                        return@launch
                    }
                    isLiked = true
                    ivFavourite.setImageResource(R.drawable.heart_icon_liked)
                    tvLikeCount.text = RetrofitClient.apiService.getPostLikes(post.id).data.toString()
                }
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    val response = try {
                        RetrofitClient.apiService.unlikePost(post.id, tokenRequest)
                    } catch (e: Exception) {
                        Log.d("PostAdapter", "Error: ${e.message}")
                        return@launch
                    }
                    isLiked = false
                    ivFavourite.setImageResource(R.drawable.heart_icon_unliked)
                    tvLikeCount.text = RetrofitClient.apiService.getPostLikes(post.id).data.toString()
                }
            }
        }

        ivComment.setOnClickListener {
            navController.navigate(PostsFragmentDirections.toPostDetails(post.id))
            navController.navigate(PostDetailsFragmentDirections.actionPostDetailsToShowAllFragment(post.id))
        }
    }
}

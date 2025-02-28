import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appinstagram.MyInterface.PostClick
import com.example.appinstagram.R
import com.example.appinstagram.adapters.ImagePostAdapter
import com.example.appinstagram.adapters.diffutil.PostDiffCallback
import com.example.appinstagram.databinding.ItemPostBinding
import com.example.appinstagram.model.HomeData
import com.example.appinstagram.ui.fragment.MyPostFragment2
import com.example.appinstagram.utils.LikeValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DetailPostAdapter(val context: Context, val username: String?, val listener: PostClick) :
    ListAdapter<HomeData.Post, DetailPostAdapter.DetailPostViewHolder>(PostDiffCallback) {

    inner class DetailPostViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindPost(post: HomeData.Post) {
            with(binding) {
                tvUsername.text = post.author.username
                tvContent.text = post.content
                tvNumLove.text = post.totalLike.toString()
                if (!post.author.username.equals(username)) {
                    binding.ivMore.visibility = View.GONE
                }
                Glide.with(context)
                    .load(post.author.avatar)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(ivAvatar)
                val imagePostAdapter = ImagePostAdapter(post.images, context)
                vpImage.adapter = imagePostAdapter
                ciImage.setViewPager(vpImage)
                var like = post.totalLike
                var status : LikeValue = LikeValue.UNLIKE
                post.listLike.forEach{
                    Log.d("Hello", "aaaa: ${it.username}, ${username}")
                    status = if (it.username == username) {
                        LikeValue.LIKE
                    } else {
                        LikeValue.UNLIKE
                    }
                }
                if (status == LikeValue.LIKE) {
                    ivLove.setImageResource(R.drawable.ic_heart_full)
                } else {
                    ivLove.setImageResource(R.drawable.ic_heart)
                }
                ivLove.setOnClickListener {
                    if (status == LikeValue.LIKE)
                    {
                        like--
                        tvNumLove.text = (like).toString()
                        status = LikeValue.UNLIKE
                        ivLove.setImageResource(R.drawable.ic_heart)
                    }
                    else if (status == LikeValue.UNLIKE)
                    {
                        like++
                        tvNumLove.text = (like).toString()
                        status = LikeValue.LIKE
                        ivLove.setImageResource(R.drawable.ic_heart_full)

                    }
                    listener.onLikeClick(post, status)
                }
            }
            binding.ivMore.setOnClickListener {
                listener.onMorePostClick(post, it)
            }
            binding.tvTime.text = formatDate(post.createdAt.toString())

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailPostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailPostViewHolder, position: Int) {
        holder.bindPost(getItem(position)) // Thay vì `posts[position]`, ta dùng `getItem(position)`
    }
    fun formatDate(isoDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Định dạng ban đầu là UTC

        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) // Định dạng mong muốn
        val date: Date = inputFormat.parse(isoDate) ?: return "Invalid date"

        return outputFormat.format(date)
    }
}

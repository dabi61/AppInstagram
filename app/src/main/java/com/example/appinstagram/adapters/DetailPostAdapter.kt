import android.content.Context
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

class DetailPostAdapter(val context: Context, val listener: PostClick) :
    ListAdapter<HomeData.Post, DetailPostAdapter.DetailPostViewHolder>(PostDiffCallback) {

    inner class DetailPostViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindPost(post: HomeData.Post) {
            with(binding) {
                tvUsername.text = post.author.username
                tvContent.text = post.content
                tvNumLove.text = post.totalLike.toString()
                val sharePref = context.getSharedPreferences("MyPrefs", MODE_PRIVATE)
                val username = sharePref.getString("username", "")
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
            }
            binding.ivMore.setOnClickListener {
                listener.onMorePostClick(post, it)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailPostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailPostViewHolder, position: Int) {
        holder.bindPost(getItem(position)) // Thay vì `posts[position]`, ta dùng `getItem(position)`
    }
}

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.appinstagram.R
import com.example.appinstagram.adapters.ImagePostAdapter
import com.example.appinstagram.adapters.diffutil.PostDiffCallback
import com.example.appinstagram.databinding.ItemPostBinding
import com.example.appinstagram.model.HomeData

class DetailPostAdapter(val context: Context) :
    ListAdapter<HomeData.Post, DetailPostAdapter.DetailPostViewHolder>(PostDiffCallback) {

    inner class DetailPostViewHolder(val binding: ItemPostBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        fun bindPost(post: HomeData.Post) {
            with(binding) {
                tvUsername.text = post.author.username
                tvContent.text = post.content
                tvNumLove.text = post.totalLike.toString()

                Glide.with(context)
                    .load(post.author.avatar)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(ivAvatar)

                val imagePostAdapter = ImagePostAdapter(post.images, context)
                vpImage.adapter = imagePostAdapter
                ciImage.setViewPager(vpImage)
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

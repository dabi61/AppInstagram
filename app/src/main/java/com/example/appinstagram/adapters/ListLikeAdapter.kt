import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appinstagram.R
import com.example.appinstagram.adapters.diffutil.ListLikeDiffCallback
import com.example.appinstagram.adapters.diffutil.PicturePostDiffCallback
import com.example.appinstagram.databinding.ItemLikeBinding
import com.example.appinstagram.databinding.ItemPicturePostBinding
import com.example.appinstagram.model.User

class ListLikeAdapter(val context: Context) : ListAdapter<User, ListLikeAdapter.ListLikeViewHolder>(
    ListLikeDiffCallback
) {


    inner class ListLikeViewHolder(val binding: ItemLikeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                tvUsername.text = user.username
                tvName.text = user.name
                Glide.with(context)
                    .load(user.avatar)
                    .error(R.drawable.ic_profile)
                    .into(ivAvatar)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListLikeViewHolder {
        val binding = ItemLikeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d("PicturePostAdapter", "onCreateViewHolder called $currentList")
        return ListLikeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListLikeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



}
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.appgithubusers.databinding.ItemUserBinding
import com.example.appgithubusers.model.ItemsItem

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList: List<ItemsItem> = emptyList()
    private var onUserItemClickListener: OnUserItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    fun setItems(users: List<ItemsItem>) {
        userList = users
        notifyDataSetChanged()
    }

    fun setOnUserItemClickListener(listener: OnUserItemClickListener) {
        this.onUserItemClickListener = listener
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onUserItemClickListener?.onUserItemClick(userList[position])
                }
            }
        }

        fun bind(user: ItemsItem) {
            with(binding) {
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(image)
                username.text = user.login
            }
        }
    }

    interface OnUserItemClickListener {
        fun onUserItemClick(user: ItemsItem)
    }
}

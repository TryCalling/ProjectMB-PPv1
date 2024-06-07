import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmb_pp.databinding.ItemViewShowingBinding
import com.example.projectmb_pp.databinding.ItemViewShowingLinearBinding
import com.example.projectmb_pp.model.Property

class MyAdapter(
    private var data: List<Property>,
    private val onItemClick: (Property) -> Unit,
    private var isLinearView: Boolean = false // Default to grid view
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_LINEAR = 1
        private const val VIEW_TYPE_GRID = 2
    }

    inner class LinearViewHolder(private val binding: ItemViewShowingLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(property: Property) {
            binding.titleTextViewL.text = property.title
            binding.descriptionTextViewL.text = property.synopsis

            Glide.with(binding.root)
                .load(property.poster_url)
                .centerCrop()
                .into(binding.imageViewL)

            binding.root.setOnClickListener {
                onItemClick(property)
            }
        }
    }

    inner class GridViewHolder(private val binding: ItemViewShowingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(property: Property) {
            binding.tvTitle.text = property.title
            binding.tvDescription.text = property.synopsis

            Glide.with(binding.root)
                .load(property.poster_url)
                .centerCrop()
                .into(binding.imageView)

            binding.root.setOnClickListener {
                onItemClick(property)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LINEAR) {
            val binding =
                ItemViewShowingLinearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LinearViewHolder(binding)
        } else {
            val binding =
                ItemViewShowingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            GridViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val property = data[position]
        if (holder.itemViewType == VIEW_TYPE_LINEAR) {
            (holder as LinearViewHolder).bind(property)
        } else {
            (holder as GridViewHolder).bind(property)
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return if (isLinearView) VIEW_TYPE_LINEAR else VIEW_TYPE_GRID
    }

    fun updateData(newData: List<Property>) {
        data = newData
        notifyDataSetChanged()
    }

    fun setLinearView(isLinear: Boolean) {
        isLinearView = isLinear
        notifyDataSetChanged()
    }
}

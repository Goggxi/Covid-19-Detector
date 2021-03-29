package com.goggxi.covid19detector.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.goggxi.covid19detector.R
import com.goggxi.covid19detector.databinding.ItemImageRetrieveBinding
import com.goggxi.covid19detector.ui.classification.ImageUpload

class RetrieveImageAdapter (
    context: Context,
    private val listNews: ArrayList<ImageUpload>
    ) : RecyclerView.Adapter<RetrieveImageAdapter.ListViewHolder>() {

    private lateinit var context: Context
    private val mContext: Context = context



    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemImageRetrieveBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        listNews[position].let { holder.bind(it) }
        context = holder.itemView.context
    }

    override fun getItemCount(): Int = listNews.size

    inner class ListViewHolder(private val binding: ItemImageRetrieveBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(retrieve: ImageUpload) {
            with(binding){

                imageViewItemRetrieve.load(retrieve.urlImage){
                    crossfade(true)
                    crossfade(500)
                    transformations(RoundedCornersTransformation(10F))
                }

                when {
                   retrieve.resultClassification.equals("COVID-19") -> {
                       binding.textViewResultItemRetrieve.setTextColor(ContextCompat.getColor( mContext , R.color.red1))
                    }
                    retrieve.resultClassification.equals("VIRAL PNEUMONIA") -> {
                       binding.textViewResultItemRetrieve.setTextColor(ContextCompat.getColor( mContext , R.color.blue1))
                    }
                    else -> {
                       binding.textViewResultItemRetrieve.setTextColor(ContextCompat.getColor( mContext , R.color.green1))
                    }
                }

                textViewResultItemRetrieve.text = retrieve.resultClassification
                textViewResultPropabilitasItemRetrieve.text = retrieve.propabilitas
                textViewResultTimerItemRetrieve.text = retrieve.timerClassification

            }
        }
    }
}
package com.goggxi.covid19detector.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.goggxi.covid19detector.data.model.News
import com.goggxi.covid19detector.databinding.ItemRowNewsBinding

class ListNewsAdapter(private val listNews: ArrayList<News>) : RecyclerView.Adapter<ListNewsAdapter.ListViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowNewsBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listNews[position])
        context = holder.itemView.context
    }

    override fun getItemCount(): Int = listNews.size

    inner class ListViewHolder(private val binding: ItemRowNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: News) {
            with(binding){
                imgRowNews.load(news.photo){
                    crossfade(true)
                    crossfade(500)
                    transformations(RoundedCornersTransformation(10F))
                }
                textTitleNews.text = news.title
                textTypeNewsContent.text = news.type
                textTimeNews.text = news.time

                btnRead.setOnClickListener {
                    Toast.makeText(context, news.title , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

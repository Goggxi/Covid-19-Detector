package com.goggxi.covid19detector.ui.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.goggxi.covid19detector.data.model.News
import com.goggxi.covid19detector.databinding.ItemRowNewsBinding
import com.goggxi.covid19detector.ui.news.NewsDetailActivity


class NewsAdapter(private val listNews: ArrayList<News?>?) : RecyclerView.Adapter<NewsAdapter.ListViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowNewsBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        listNews?.get(position)?.let { holder.bind(it) }
        context = holder.itemView.context
    }

    override fun getItemCount(): Int = listNews?.size as Int

    inner class ListViewHolder(private val binding: ItemRowNewsBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(news: News) {
            with(binding){
                imgRowNews.load(news.poster){
                    crossfade(true)
                    crossfade(500)
                    transformations(RoundedCornersTransformation(10F))
                }
                textTitleNews.text = news.judul
                textTypeNewsContent.text = news.tipe
                textTimeNews.text = news.waktu

                btnRead.setOnClickListener {

//                    Toast.makeText(context, news.judul, Toast.LENGTH_SHORT).show()

                    val intent = Intent(itemView.context, NewsDetailActivity::class.java)
                    intent.putExtra(NewsDetailActivity.EXTRA_NEWS_DETAIL, news.link)
                    Log.e("EXTRA NEWS DETAIL ", news.link)
                    itemView.context.startActivity(intent)
                }

            }
        }
    }
}

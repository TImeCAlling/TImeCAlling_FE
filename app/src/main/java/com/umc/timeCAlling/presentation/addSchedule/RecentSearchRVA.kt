package com.umc.timeCAlling.presentation.addSchedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.umc.timeCAlling.R

class RecentSearchRVA(
    private val viewModel: AddScheduleViewModel,
    private val lifecycleOwner: LifecycleOwner // LifecycleOwner 추가
) : RecyclerView.Adapter<RecentSearchRVA.RecentSearchViewHolder>() {

    private var recentSearches: List<String> = viewModel.recentSearches.value ?: emptyList() // 초기값 설정

    init {
        viewModel.recentSearches.observe(lifecycleOwner) { searches ->
            recentSearches = searches.orEmpty() // null 처리 추가
            notifyDataSetChanged()
        }
    }

    class RecentSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchTextView: TextView = itemView.findViewById(R.id.tv_recent_search)
        val deleteImageView: ImageView = itemView.findViewById(R.id.iv_recent_search_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_search, parent, false) // item_recent_search 레이아웃 파일 이름 확인
        return RecentSearchViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        val recentSearch = recentSearches.getOrNull(position) ?: return // null 처리 추가

        holder.searchTextView.text = recentSearch
        holder.deleteImageView.setOnClickListener {
            viewModel.deleteRecentSearch(recentSearch)
        }
    }

    override fun getItemCount(): Int {
        return recentSearches.size
    }
}
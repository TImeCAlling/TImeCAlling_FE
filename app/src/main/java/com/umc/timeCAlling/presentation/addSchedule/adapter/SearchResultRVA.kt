package com.umc.timeCAlling.presentation.addSchedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.data.SearchResult
import com.umc.timeCAlling.presentation.addSchedule.AddScheduleViewModel

class SearchResultRVA(
    private val viewModel: AddScheduleViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val onSearchResultClickListener: (SearchResult) -> Unit // 클릭 리스너 추가
) : RecyclerView.Adapter<SearchResultRVA.SearchResultViewHolder>() {

    private var searchResults: List<SearchResult> = emptyList()

    init {
        viewModel.searchResults.observe(lifecycleOwner) { results ->
            searchResults = results
            notifyDataSetChanged() // 데이터 변경 알림
        }
    }

    class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_search_result_name)
        val address: TextView = itemView.findViewById(R.id.tv_search_result_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false) // item_recent_search 레이아웃 파일 이름 확인
        return SearchResultViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val searchResult = searchResults[position]
        holder.name.text = searchResult.name
        holder.address.text = searchResult.address

        // 아이템 클릭 리스너 설정
        holder.itemView.setOnClickListener {
            onSearchResultClickListener(searchResult)
        }
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }
}

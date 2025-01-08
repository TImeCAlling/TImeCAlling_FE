package com.umc.timeCAlling.presentation.addSchedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.skt.tmap.TMapData
import com.skt.tmap.TMapTapi
import com.skt.tmap.TMapView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.data.SearchResult
import com.umc.timeCAlling.presentation.addSchedule.AddScheduleViewModel
import kotlinx.coroutines.launch

class SearchResultRVA(
    private val viewModel: AddScheduleViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val onSearchResultClickListener: (SearchResult) -> Unit,
    private val navController: NavController
) : RecyclerView.Adapter<SearchResultRVA.SearchResultViewHolder>() {

    private var searchResults: List<SearchResult> = emptyList()

    init {
        viewModel.searchLocation.observe(lifecycleOwner) { results ->
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

        holder.itemView.setOnClickListener {
            onSearchResultClickListener(searchResult) // 기존 콜백 호출
            viewModel.setSelectedLocationName(searchResult.name) // ViewModel 함수 호출
        }
        val nextButton = holder.itemView.findViewById<ImageView>(R.id.iv_search_result_next)
        nextButton.setOnClickListener {
            navController.navigate(R.id.action_locationSearchFragment_to_locationResultFragment)
        }
    }
    override fun getItemCount(): Int {
        return searchResults.size
    }
}

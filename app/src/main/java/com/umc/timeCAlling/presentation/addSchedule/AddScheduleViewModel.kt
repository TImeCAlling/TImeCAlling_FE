package com.umc.timeCAlling.presentation.addSchedule

import android.content.SharedPreferences
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.umc.timeCAlling.data.SearchResult
import com.umc.timeCAlling.domain.model.request.CategoriesRequestModel
import com.umc.timeCAlling.domain.model.request.schedule.ScheduleRequestModel
import com.umc.timeCAlling.domain.model.response.tmap.CarTransportationModel
import com.umc.timeCAlling.domain.model.response.tmap.PublicTransportationModel
import com.umc.timeCAlling.domain.model.response.tmap.WalkTransportationModel
import com.umc.timeCAlling.domain.repository.ScheduleRepository
import com.umc.timeCAlling.domain.repository.TmapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.toMutableList

@HiltViewModel
class AddScheduleViewModel @Inject constructor( // @Inject : 의존성 주입을 받겠다.
    private val spf: SharedPreferences,
    private val tmapRepository: TmapRepository,
    private val scheduleRepository: ScheduleRepository
    ) : ViewModel() {
    private var mode: String = "normal"

    private val _categoryNeedsRefresh = MutableStateFlow<String>("대중교통")
    val categoryNeedsRefresh: StateFlow<String> get() = _categoryNeedsRefresh

    private val maxRecentSearches = 10 // 최대 검색어 개수

    //일정 생성 값
    private val _scheduleName = MutableLiveData<String>()
    val scheduleName : LiveData<String> = _scheduleName

    fun setScheduleName(name: String) {
        _scheduleName.value = name
    }

    private val _scheduleMemo = MutableLiveData<String>()
    val scheduleMemo : LiveData<String> = _scheduleMemo
    fun setScheduleMemo(memo: String) {
        _scheduleMemo.value = memo
    }

    private val _scheduleDate = MutableLiveData<String>()
    val scheduleDate : LiveData<String> = _scheduleDate
    fun setScheduleDate(date: String) {
        _scheduleDate.value = date
    }

    private val _scheduleTime = MutableLiveData<String>()
    val scheduleTime : LiveData<String> = _scheduleTime
    fun setScheduleTime(time: String) {
        _scheduleTime.value = time
    }

    private val _selectedLocationName = MutableLiveData<String>()
    val selectedLocationName: LiveData<String> = _selectedLocationName
    fun setSelectedLocationName(name: String) {
        _selectedLocationName.value = name
    }

    private val _locationLongitude = MutableLiveData<String>()
    val locationLongitude: LiveData<String> = _locationLongitude
    fun setLocationLongitude(longitude: String) {
        _locationLongitude.value = longitude
    }

    private val _locationLatitude = MutableLiveData<String>()
    val locationLatitude: LiveData<String> = _locationLatitude
    fun setLocationLatitude(latitude: String) {
        _locationLatitude.value = latitude
    }

    private val _moveTime = MutableLiveData<Int>()
    val moveTime: LiveData<Int> = _moveTime
    fun setMoveTime(time: Int) {
        _moveTime.value = time
    }

    private val _freeTime = MutableLiveData<String>()
    val freeTime:LiveData<String> = _freeTime
    fun setFreeTime(time: String) {
        _freeTime.value = time
    }
    private val _repeatDates = MutableLiveData<MutableList<String>>(mutableListOf()) // MutableList 사용
    val repeatDates: LiveData<MutableList<String>> = _repeatDates // LiveData 타입 변경

    fun setRepeatDates(dates: List<String>?) {
        if (dates != null) {
            _repeatDates.value?.addAll(dates)
        }
        _repeatDates.value = _repeatDates.value
    }

    private val _isRepeat = MutableLiveData<Boolean>()
    val isRepeat: LiveData<Boolean> = _isRepeat
    fun setIsRepeat(isRepeat: Boolean) {
        _isRepeat.value = isRepeat
    }

    private val _startDate = MutableLiveData<String>()
    val startDate: LiveData<String> = _startDate
    fun setStartDate(date: String) {
        _startDate.value = date
    }

    private val _endDate = MutableLiveData<String>()
    val endDate: LiveData<String> = _endDate
    fun setEndDate(date: String) {
        _endDate.value = date
    }

    private val _categoryName = MutableLiveData<String>()
    val categoryName: LiveData<String> = _categoryName
    fun setCategoryName(name: String) {
        _categoryName.value = name
    }

    private val _categoryColor = MutableLiveData<Int>()
    val categoryColor: LiveData<Int> = _categoryColor
    fun setCategoryColor(color: Int) {
        _categoryColor.value = color
    }

    private val _recentSearches = MutableLiveData<List<String>>(emptyList())
    val recentSearches: LiveData<List<String>> = _recentSearches

    private val _searchLocation = MutableLiveData<List<SearchResult>>()
    val searchLocation: LiveData<List<SearchResult>> = _searchLocation

    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location> = _currentLocation

    private val _carResult = MutableLiveData<CarTransportationModel>()
    val carResult: LiveData<CarTransportationModel> = _carResult

    private val _walkResult = MutableLiveData<WalkTransportationModel>()
    val walkResult: LiveData<WalkTransportationModel> = _walkResult

    private val _publicResult = MutableLiveData<PublicTransportationModel>()
    val publicResult: LiveData<PublicTransportationModel> = _publicResult

    private val _scheduleId = MutableLiveData<Int>()
    val scheduleId: LiveData<Int> = _scheduleId
    fun setScheduleId(id: Int) {
        _scheduleId.value = id
    }
    init {
        _recentSearches.value = loadRecentSearches() // 초기화 시 로드
    }

    fun setMode(m: String) { mode = m }
    fun getMode(): String { return mode }

    private var location: Boolean = false
    fun setLocation(l : Boolean) { location = l }
    fun getLocation(): Boolean { return location }
    fun addRecentSearch(search: String) {
        val updatedSearches = _recentSearches.value?.toMutableList()?.also {
            it.remove(search) // 중복된 검색어 제거
            it.add(0, search) // 최근 검색어를 맨 앞에 추가
            if (it.size > maxRecentSearches) {
                it.removeAt(it.size - 1) // 최대 개수 초과 시 오래된 검색어 삭제
            }
        }
        _recentSearches.value = updatedSearches ?: listOf(search)
        saveRecentSearches(_recentSearches.value!!) // 저장
    }

    fun deleteRecentSearch(search: String) {
        val updatedSearches = _recentSearches.value?.toMutableList()?.also { it.remove(search) }
        _recentSearches.value = updatedSearches ?: emptyList()
        saveRecentSearches(_recentSearches.value!!) // 저장
    }

    fun clearRecentSearches() {
        _recentSearches.value = emptyList()
        saveRecentSearches(emptyList())
    }

    fun updateSearchResults(results: List<SearchResult>) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            _searchLocation.value = results // UI 스레드에서 호출되는 경우
        } else {
            _searchLocation.postValue(results) // 다른 스레드에서 호출되는 경우
        }
    }

    private fun loadRecentSearches(): List<String> {
        val searchesJson = spf.getString("searches", null)
        return if (searchesJson != null) {
            Gson().fromJson(searchesJson, object : TypeToken<List<String>>() {}.type)
        } else {
            emptyList()
        }
    }

    private fun saveRecentSearches(searches: List<String>) {
        val searchesJson = Gson().toJson(searches)
        spf.edit().putString("searches", searchesJson).apply()
    }

    fun updateCurrentLocation(location: Location) {
        _currentLocation.value = location
    }

    fun refreshCategoryPage(category: String){
        _categoryNeedsRefresh.value = ""
        _categoryNeedsRefresh.value = category
    }

    fun getCarTransportation(startX: Double, startY: Double, endX: Double, endY: Double) {
        viewModelScope.launch {
            tmapRepository.getCarTransportation(startX, startY, endX, endY).onSuccess { response ->
                _carResult.value = response // LiveData에 API 응답 저장
                Log.d("transportation 자동차 거리", "${response.features?.get(0)?.properties?.totalDistance}") // 로그에 거리 출력
                Log.d("transportation 자동차 시간", "${response.features?.get(0)?.properties?.totalTime}") // 로그에 거리 출력
            }.onFailure { error ->
                Log.e("자동차 오류", "API 호출 실패: $error")
            }
        }
    }

    fun getWalkTransportation(startX: Double, startY: Double, endX: Double, endY: Double) {
        viewModelScope.launch {
            tmapRepository.getWalkTransportation(startX, startY, endX, endY).onSuccess { response ->
                _walkResult.value = response // LiveData에 API 응답 저장
                Log.d("transportation 걷기 거리", "${response.features?.get(0)?.properties?.totalDistance}") // 로그에 거리 출력
                Log.d("transportation 걷기 시간", "${response.features?.get(0)?.properties?.totalTime}") // 로그에 거리 출력
            }.onFailure { error ->
                Log.e("걷기 오류", "API 호출 실패: $error")

            }
        }
    }

    fun getPublicTransportation(startX: Double, startY: Double, endX: Double, endY: Double) {
        viewModelScope.launch {
            tmapRepository.getPublicTransportation(startX, startY, endX, endY).onSuccess { response ->
                _publicResult.value = response // LiveData에 API 응답 저장
                Log.d("transportation 대중교통 시간", "${response.metaData?.plan?.itineraries?.get(0)?.totalTime}")
            }.onFailure { error->
                Log.e("대중교통 오류", "API 호출 실패: $error")
            }
        }
    }

    fun createSchedule(){
        viewModelScope.launch {
            val request = ScheduleRequestModel(
                name = scheduleName.value ?: "",
                body = scheduleMemo.value ?: "",
                meetDate = scheduleDate.value ?: "",
                meetTime = scheduleTime.value ?: "",
                place = selectedLocationName.value ?: "",
                longitude = locationLongitude.value ?: "",
                latitude = locationLatitude.value ?: "",
                moveTime = moveTime.value ?: 0,
                freeTime = freeTime.value ?: "TIGHT",
                repeatDays = repeatDates.value ?: emptyList(),
                isRepeat =  isRepeat.value ?: false,
                start = startDate.value ?: "",
                end = endDate.value ?: "",
                categories =  listOf(CategoriesRequestModel(categoryName.value ?: "", categoryColor.value ?: 0))
            )
            Log.d("","${request}")
             scheduleRepository.createSchedule(request).onSuccess { response ->
                 Log.d("createSchedule", "API 호출 성공: $response")
                setScheduleId(response.scheduleId)
             }.onFailure {error->
                 Log.e("createSchedule", "API 호출 실패: $error")
             }
        }
    }

    fun deleteSchedule(scheduleId: Int){
        viewModelScope.launch {
            scheduleRepository.deleteSchedule(scheduleId).onSuccess { response ->
                Log.d("deleteSchedule", "API 호출 성공: $response")
            }.onFailure {error->
                Log.e("deleteSchedule", "API 호출 실패: $error")
            }
        }
    }

    fun editSchedule(scheduleId: Int){
        val request = ScheduleRequestModel(
            name = scheduleName.value ?: "",
            body = scheduleMemo.value ?: "",
            meetDate = scheduleDate.value ?: "",
            meetTime = scheduleTime.value ?: "",
            place = selectedLocationName.value ?: "",
            longitude = locationLongitude.value ?: "",
            latitude = locationLatitude.value ?: "",
            moveTime = moveTime.value ?: 0,
            freeTime = freeTime.value ?: "TIGHT",
            repeatDays = repeatDates.value ?: emptyList(),
            isRepeat =  isRepeat.value ?: false,
            start = startDate.value ?: "",
            end = endDate.value ?: "",
            categories =  listOf(CategoriesRequestModel(categoryName.value ?: "", categoryColor.value ?: 0))
        )
        viewModelScope.launch {
            scheduleRepository.editSchedule(scheduleId, request).onSuccess {
                Log.d("editSchedule", "API 호출 성공: $it")
            }.onFailure {
                Log.e("editSchedule", "API 호출 실패: $it")
            }
        }
    }

    private val _sharedScheduleNickname = MutableLiveData<String>()
    val sharedScheduleNickname: LiveData<String> = _sharedScheduleNickname


    fun sharedSchedule(scheduleId: Int){
        viewModelScope.launch {
            scheduleRepository.getSharedSchedule(scheduleId).onSuccess {
                setRepeatDates(it.repeatDays)
                Log.d("sharedSchedule", "API 호출 성공: $it")
                _scheduleName.value = it.name
                _sharedScheduleNickname.value = it.nickname
                _scheduleDate.value = it.meetDate
                _scheduleTime.value = it.meetTime
                _selectedLocationName.value = it.place
                _locationLongitude.value = it.longitude
                _locationLatitude.value = it.latitude
                _repeatDates.value = repeatDates.value
                _isRepeat.value = it.isRepeat
                _startDate.value = it.start?:""
                _endDate.value = it.end?:""
                Log.d("sharedSchedule isRepeat", "${it.isRepeat}")
            }.onFailure {
                Log.e("sharedSchedule", "API 호출 실패: $it")
            }
        }
    }

    fun createSharedSchedule(scheduleId: Int){
        viewModelScope.launch {
            val request = ScheduleRequestModel(
                name = scheduleName.value ?: "",
                body = scheduleMemo.value ?: "",
                meetDate = scheduleDate.value ?: "",
                meetTime = scheduleTime.value ?: "",
                place = selectedLocationName.value ?: "",
                longitude = locationLongitude.value ?: "",
                latitude = locationLatitude.value ?: "",
                moveTime = moveTime.value ?: 0,
                freeTime = freeTime.value ?: "TIGHT",
                repeatDays = repeatDates.value ?: emptyList(),
                isRepeat =  isRepeat.value ?: false,
                start = startDate.value ?: "",
                end = endDate.value ?: "",
                categories =  listOf(CategoriesRequestModel(categoryName.value ?: "", categoryColor.value ?: 0))
            )
            Log.d("","${request}")
            scheduleRepository.postSharedSchedule(scheduleId, request).onSuccess { response ->
                Log.d("createSchedule", "API 호출 성공: $response")

            }.onFailure {error->
                Log.e("createSchedule", "API 호출 실패: $error")
            }
        }
    }
    fun resetData() {
        _scheduleName.value = ""
        _scheduleMemo.value = ""
        _scheduleDate.value = ""
        _scheduleTime.value = ""
        _searchLocation.value = emptyList()
        _selectedLocationName.value = ""
        _locationLongitude.value = ""
        _locationLatitude.value = ""
        _moveTime.value = 0
        _freeTime.value = "TIGHT"
        _repeatDates.value = mutableListOf()
        _isRepeat.value = false
        _startDate.value = ""
        _endDate.value = ""
        _categoryName.value = ""
        _categoryColor.value = 0
    }
}
package com.example.storymeow.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storymeow.data.response.ListStoryItem
import com.example.storymeow.data.retrofit.ApiService

class StoryPagingSource(private val apiService: ApiService): PagingSource<Int, ListStoryItem>() {

    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getPaging(position, params.loadSize).listStory

            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        }catch (e: Exception){
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchor->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1)?: anchorPage?.nextKey
        }
    }
}
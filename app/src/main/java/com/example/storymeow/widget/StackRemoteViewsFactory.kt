package com.example.storymeow.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import com.example.storymeow.R
import com.example.storymeow.data.response.ListStoryItem


internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory, LifecycleOwner {

    private val mWidget = MediatorLiveData<List<ListStoryItem>>()

    override fun onCreate() {
        return
    }

    override fun onDataSetChanged() {
    mWidget.observe(this){
            if (!it.isNullOrEmpty()){
                it[1].photoUrl
            }
        }
    }

    override fun onDestroy() {
        return
    }

    override fun getCount(): Int = mWidget.value!!.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewResource(R.id.imageView, position)

        val extras = bundleOf(
            ImageBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    override val lifecycle: Lifecycle
        get() = TODO("Not yet implemented")
}



package com.example.storymeow

import com.example.storymeow.data.response.ListStoryItem

object DataDummy {

    fun generateDummyResponse(): List<ListStoryItem>{
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100){
            val story = ListStoryItem(
                "https://pbs.twimg.com/profile_images/1455185376876826625/s1AjSxph_400x400.jpg",
                "2022-04-21T06:41:06.470Z",
                "User $i",
                "Description of post $i",
                100.0 + i*2,
                "story-$i",
                100.0 + i*2
            )
            items.add(story)
        }
        return items
    }
}
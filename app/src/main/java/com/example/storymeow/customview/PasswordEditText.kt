package com.example.storymeow.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import com.example.storymeow.R

class PasswordEditText : AppCompatEditText{

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }
    private fun init(){
        doOnTextChanged { text, _,_,_ ->
            if (!text.isNullOrBlank()){
                error = if (text.length < 8){
                    resources.getString(R.string.password_error)
                }else{
                    null
                }
            }
        }
    }
}
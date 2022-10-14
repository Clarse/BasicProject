package com.example.oms_android.utilities

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author: Clarse
 * @date: 2022/8/29
 */
object MatchesUtils {

    //判断是否含有字母
    fun judgeContainsLetter(cardNum: String?): Boolean {
        val regex = ".*[a-zA-Z]+.*"
        val m: Matcher = Pattern.compile(regex).matcher(cardNum)
        return m.matches()
    }

}
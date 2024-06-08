package com.example.projectmb_pp.repository

import android.content.Context
import android.content.SharedPreferences

object LikeItemsRepository {
    private const val PREF_NAME = "property_prefs"
    private const val KEY_LIKED_PREFIX = "liked_property_"
    private const val KEY_LIKE_COUNT_PREFIX = "like_count_property_"

    private lateinit var preferences: SharedPreferences

    fun initialize(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setLiked(propertyId: Int, isLiked: Boolean) {
        preferences.edit().putBoolean(KEY_LIKED_PREFIX + propertyId, isLiked).apply()
    }

    fun isLiked(propertyId: Int): Boolean {
        return preferences.getBoolean(KEY_LIKED_PREFIX + propertyId, false)
    }

    fun setLikeCount(propertyId: Int, likeCount: Int) {
        preferences.edit().putInt(KEY_LIKE_COUNT_PREFIX + propertyId, likeCount).apply()
    }

    fun getLikeCount(propertyId: Int): Int {
        return preferences.getInt(KEY_LIKE_COUNT_PREFIX + propertyId, 0)
    }
}

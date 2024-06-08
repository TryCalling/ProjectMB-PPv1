package com.example.projectmb_pp.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.projectmb_pp.model.Property
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SavedItemsRepository {

    private const val PREFERENCE_NAME = "SavedItems"
    private const val KEY_LIKED_ITEMS = "SavedItems"

    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun addLikedItem(property: Property) {
        val savedItems = getSavedItems().toMutableSet()
        savedItems.remove(property)
        savedItems.add(property)
        saveLikedItems(savedItems)
    }

    fun removeSavedItem(property: Property) {
        val savedItems = getSavedItems().toMutableSet()
        savedItems.remove(property)
        saveLikedItems(savedItems)
    }

    fun getSavedItems(): Set<Property> {
        val jsonString = sharedPreferences.getString(KEY_LIKED_ITEMS, null)
        return if (jsonString != null) {
            gson.fromJson(jsonString, object : TypeToken<Set<Property>>() {}.type)
        } else {
            emptySet()
        }
    }

    private fun saveLikedItems(likedItems: Set<Property>) {
        val jsonString = gson.toJson(likedItems)
        sharedPreferences.edit().putString(KEY_LIKED_ITEMS, jsonString).apply()
    }

    fun getLikedItemById(id: Int): Property? {
        return getSavedItems().find { it.id == id }
    }
}

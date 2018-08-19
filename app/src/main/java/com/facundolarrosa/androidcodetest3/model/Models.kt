package com.facundolarrosa.androidcodetest3.model

import com.google.gson.annotations.SerializedName

data class ApiResult<T>(@SerializedName("total_count") val totalCount: Int,
                        @SerializedName("incomplete_results") val incompleteResults: Boolean,
                        @SerializedName("items") val items: List<T> )

data class Repo(@SerializedName("name") val name: String,
                @SerializedName("full_name") val fullName: String,
                val description: String)
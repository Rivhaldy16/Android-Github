package com.github.coba.data.model

data class DetailUserResponse(
    val login: String,
    val name: String,
    val id: Int,
    val avatar_url: String,
    val html_url: String,
    val followers_url: String,
    val followers: Int,
    val following_url: String,
    val following: Int,
    val repos_url: String
)

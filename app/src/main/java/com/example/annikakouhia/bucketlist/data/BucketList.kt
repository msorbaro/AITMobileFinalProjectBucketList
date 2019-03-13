package com.example.annikakouhia.bucketlist.data

data class BucketList(var uid: List<String> = listOf<String>(),
                    var authors: List<String> = listOf<String>(),
                    var title: String = "",
                    var items: List<BucketListItem> = listOf<BucketListItem>())
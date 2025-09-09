package com.example.http.server

import com.example.http.client.notion.NotionClient
import com.example.http.client.notion.NotionClientConfig
import org.koin.dsl.module

val notionModule = module {
    single<NotionClientConfig> {
        NotionClientConfig(
            token = System.getenv("NOTION_API_KEY") ?: error("Missing NOTION_API_KEY environment variable")
        )
    }

    single<NotionClient> {
        NotionClient(
            config = get()
        )
    }
}

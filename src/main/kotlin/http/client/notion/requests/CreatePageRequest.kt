import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.example.http.client.notion.responses.RichText as NotionRichText

@Serializable
data class CreatePageRequest(
    val parent: PageParent,
    val properties: Map<String, PageProperty>,
    val children: List<String>? = null
)

@Serializable
sealed interface PageParent {
    @Serializable
    @SerialName("database_id")
    data class Database(
        @SerialName("database_id")
        val databaseId: String
    ) : PageParent

    @Serializable
    @SerialName("page_id")
    data class Page(
        @SerialName("page_id")
        val pageId: String
    ) : PageParent
}

@Serializable
sealed interface PageProperty {
    @Serializable
    @SerialName("title")
    data class Title(val title: List<NotionRichText>) : PageProperty

    @Serializable
    @SerialName("rich_text")
    data class RichText(
        @SerialName("rich_text") val richText: List<NotionRichText>
    ) : PageProperty

    @Serializable
    @SerialName("url")
    data class Url(val url: String) : PageProperty

    @Serializable
    @SerialName("date")
    data class Date(val date: InnerDate) : PageProperty {
        @Serializable
        data class InnerDate(val start: LocalDate, val end: LocalDate?)
    }

    @Serializable
    @SerialName("checkbox")
    data class Checkbox(val checkbox: Boolean) : PageProperty
}

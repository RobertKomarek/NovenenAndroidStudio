import android.content.Context

class BookmarkDataStore(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("bookmarks", Context.MODE_PRIVATE)

    fun getBookmarkedNoveneTag(novenenname: String): String? {
        val key = "${novenenname}_bookmarked_novene_tag"
        return sharedPreferences.getString(key, null)
    }

    fun setBookmarkedNoveneTag(novenenname: String, tag: String?) {
        val key = "${novenenname}_bookmarked_novene_tag"
        sharedPreferences.edit().putString(key, tag).apply()
    }
}
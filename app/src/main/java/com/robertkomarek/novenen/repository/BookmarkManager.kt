import android.content.Context
import android.content.SharedPreferences

object BookmarkManager {
    private const val PREF_NAME = "Bookmarks"
    private const val KEY_BOOKMARK = "current_bookmark"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setBookmark(context: Context, noveneName: String?) {
        val prefs = getPreferences(context)
        prefs.edit().putString(KEY_BOOKMARK, noveneName).apply()
    }

    fun getBookmark(context: Context): String? {
        val prefs = getPreferences(context)
        return prefs.getString(KEY_BOOKMARK, null)
    }
}

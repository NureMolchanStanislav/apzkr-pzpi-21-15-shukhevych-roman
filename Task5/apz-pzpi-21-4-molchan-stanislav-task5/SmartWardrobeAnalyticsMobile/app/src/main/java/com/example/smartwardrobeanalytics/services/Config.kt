import android.content.Context
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

object Config {
    lateinit var ONE_SIGNAL_APP_ID: String

    fun loadConfig(context: Context) {
        val jsonString = loadJSONFromAsset(context, "config.json")
        val jsonObject = JSONObject(jsonString)
        ONE_SIGNAL_APP_ID = jsonObject.getString("ONESIGNAL_APP_ID")
    }

    private fun loadJSONFromAsset(context: Context, fileName: String): String? {
        val json: String?
        try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}

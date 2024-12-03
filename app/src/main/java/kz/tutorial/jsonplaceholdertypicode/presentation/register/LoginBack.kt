package kz.tutorial.jsonplaceholdertypicode.presentation.register

import android.content.Context
import android.os.Build
import android.util.Log
import java.util.Base64
import kz.tutorial.jsonplaceholdertypicode.data.network.MainApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://nt-odoo-development.norsec.tech:8000/"

    val apiService: MainApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(MainApi::class.java)
    }
}
object TokenManager {
    private const val TOKEN_KEY  = "token"
    fun saveToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }
    fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString(TOKEN_KEY, null)
    }
    fun clearToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(TOKEN_KEY)
        editor.apply()
    }
    fun decodeToken(jwt: String?): String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return "Requires SDK 26"
        val parts = jwt?.split(".")
        return try {
            val charset = charset("UTF-8")
            val header = String(Base64.getUrlDecoder().decode(parts?.get(0)?.toByteArray(charset) ?: null), charset)
            val payload = String(Base64.getUrlDecoder().decode(parts?.get(1)?.toByteArray(charset)
                ?: null), charset)
            "$header"
            "$payload"
        } catch (e: Exception) {
            "Error parsing JWT: $e"
        }
    }

}

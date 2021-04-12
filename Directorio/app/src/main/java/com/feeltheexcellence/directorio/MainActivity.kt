package com.feeltheexcellence.directorio

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.feeltheexcellence.directorio.database.DatabaseHandler
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    val MENU_HEALTH = "health"
    val MENU_FOOD_BED = "food-bed"
    val MENU_SUPPLIERS = "suppliers"

    val URL_HEALTH = "html/menu/health.html"
    val URL_FOOD_BED = "html/menu/food-bed.html"
    val URL_SUPPLIERS = "html/submenu/suppliers.html"
    val HOME = "file:///android_asset/index.html"
    val MIME = "text/html"
    val ENCONDING = "utf-8"
    val BASE_URL = "file:///android_asset/"

    var MENU = "menu"
    var SUBMENU = "submenu"
    var ELEMENTO = "elemento"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val indexWebView: WebView = findViewById(R.id.indexWebView)
        indexWebView.settings.javaScriptEnabled = true;
        indexWebView.loadUrl("file:///android_asset/index.html")
        val context = this;

        indexWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                var result = false
                var fileContent = ""
                var url = request?.url.toString()

                if(url.contains(SUBMENU)){
                    result = true
                    url = getUrl(url)
                    fileContent = getFileContent(url)
                    var listado = databaseHandler.getListado(url)
                    fileContent = fileContent.replace("<LISTA_ELEMENTOS/>", listado)
                }
                else if(url.contains(MENU)){
                    result = true
                    url = getUrl(url)
                    fileContent = getFileContent(url)
                }
                else if(url.contains(ELEMENTO)){
                    result = true
                    fileContent = getFileContent(getUrl(url))
                    var listado = databaseHandler.getDatosElemento(url);
                    fileContent = fileContent.replace("<DATOS_ELEMENTO/>", listado)
                }

                if(result){
                    view?.loadDataWithBaseURL(BASE_URL, fileContent, MIME, ENCONDING, HOME)
                }
                return result
            }
        }
    }
    fun getUrl(originalURL: String): String{
        var url = originalURL
        url = url.replace("open://", "")
        var secciones = url.split("/")
        if(secciones[0] == MENU){
            url = "html/menu/" + secciones[1]
        }
        else if(secciones[0] == SUBMENU){
            url = "html/submenu/" + secciones[1]
        }
        else if(secciones[0] == ELEMENTO){
            url = "html/elemento/" + secciones[1].toLowerCase() + ".html"
        }
        return url
    }

    fun getFileContent(url: String): String{
        var fileContent = ""
        try {
            val inputStream: InputStream = assets.open(url)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            fileContent = String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        finally {
            return fileContent
        }
    }
}
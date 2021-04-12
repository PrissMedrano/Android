package com.feeltheexcellence.directorio.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME,null, DB_VERSION) {
    val HEALTH = "html/menu/health.html"
    val FOOD_BED = "html/menu/food-bed.html"
    val SUPPLIERS = "html/submenu/suppliers.html"
    var TABLA_HOSPITALES = "HOSPITALES"
    var TABLA_MEDICOS = "MEDICOS"
    val TABLA_SUPPLIERS = "MEDICALSUPPLIERS"
    var context = context

    companion object {
        private var DB_VERSION = 1
        private var DB_NAME = "FeelTheExcellenceDB.sqlite3"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        println("onCreate")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        println("onUpgrade")
    }

    fun getListado(tipo: String): String {
        val db = openDatabase()
        var sql = ""
        var cursor: Cursor? = null
        var listado = ""

        if(tipo.equals(HEALTH)){

        }
        else if(tipo.equals(FOOD_BED)){

        }
        else if(tipo.equals(SUPPLIERS)){
            sql = "SELECT id, empresa from MedicalSuppliers ORDER BY empresa asc"
        }
        try{
            cursor = db.rawQuery(sql, null)
        }catch (e: SQLiteException) {
            db.execSQL(sql)
        }
        listado = "<div class=\"list-group\" id=\"list\">\n"
        if (cursor?.moveToFirst()!!) {
            do {
                listado += "<a href=\"open://elemento/" + TABLA_SUPPLIERS + "/" + cursor.getInt(0) + "\" class=\"list-group-item list-group-item-action bg-transparent text-white\">" + cursor.getString(1) + "</a>\n"
            } while (cursor.moveToNext())
        }
        listado += "</div>"
        return listado
    }

    fun getDatosElemento(url: String): String {
        val db = openDatabase()
        val sql = "SELECT * FROM " + getQueryElement(url, "tabla") + " WHERE ID = " + getQueryElement(url, "id")
        var cursor: Cursor? = null
        var datos = ""

        try{
            cursor = db.rawQuery(sql, null)
        }catch (e: SQLiteException) {
            db.execSQL(sql)
        }
        if (cursor?.moveToFirst()!!) {
            do {
                datos = " <div>\n" +
                        "   <h4>" + cursor.getString(cursor.getColumnIndex("Empresa")) + "</h4>\n" +
                        "   <p class=\"text-wrap\">"  + cursor.getString(cursor.getColumnIndex("Servicios")) + "</p>\n" +
                        "   <p class=\"text-wrap\">" + cursor.getString(cursor.getColumnIndex("Direccion")) + "</p>\n" +
                        "   <p class=\"text-wrap\"> Tel:" + cursor.getString(cursor.getColumnIndex("Telefono")) + "</p>\n" +
                        "   <p class=\"text-wrap\">" + cursor.getString(cursor.getColumnIndex("Email")) + "</p>\n" +
                        "   <p class=\"text-wrap\">" + cursor.getString(cursor.getColumnIndex("Website")) + "</p>\n" +
                        " </div>\n" +
                        " <div>\n" +
                        "   <iframe src=" + cursor.getString(cursor.getColumnIndex("Ubicacion")) + "\n" +
                        "       width=\"100%\" height=\"450\" frameborder=\"0\" style=\"border:0;\"\n" +
                        "       allowfullscreen=\"\" aria-hidden=\"false\" tabindex=\"0\"></iframe>\n" +
                        " </div>"
            } while (cursor.moveToNext())
        }
        return datos
    }

    fun getQueryElement(originalURL: String, parte: String): String{
        var url = originalURL
        url = url.replace("open://", "")
        var secciones = url.split("/")
        var elemento = ""

        if(parte == "tabla"){
            elemento = secciones[1]
        }
        else if(parte == "id"){
            elemento = secciones[2]
        }
        return elemento
    }

    fun openDatabase(): SQLiteDatabase {
        val dbFile = context.getDatabasePath(DB_NAME)
        try {
            val checkDB = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE,null)
            if (!dbFile.exists() || checkDB.version < DB_VERSION){
                checkDB.version = DB_VERSION
                checkDB?.close()
                copyDatabase(dbFile)
            } else{
                checkDB?.close()
            }
        } catch (e: IOException) {
            throw RuntimeException("Error creating source database", e)
        }
        return SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READWRITE)
    }

    @SuppressLint("WrongConstant")
    private fun copyDatabase(dbFile: File) {
        val istr = context.assets.open("databases/" + DB_NAME)
        val ostr = FileOutputStream(dbFile)

        val buffer = ByteArray(1024)
        while (istr.read(buffer) > 0) {
            ostr.write(buffer)
        }
        ostr.flush()
        ostr.close()
        istr.close()
    }
}
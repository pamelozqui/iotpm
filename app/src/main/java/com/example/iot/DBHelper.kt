package com.example.iot

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest
import java.security.SecureRandom

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "camanchaca.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE usuarios (" +
                    "usuario TEXT PRIMARY KEY, " +
                    "salt TEXT NOT NULL, " +
                    "hash TEXT NOT NULL)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    private fun generarSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun hashPassword(password: String, salt: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest((salt + password).toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun existeUsuario(usuario: String): Boolean {
        val cursor = readableDatabase.rawQuery(
            "SELECT usuario FROM usuarios WHERE usuario = ?", arrayOf(usuario)
        )
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }

    fun crearUsuario(usuario: String, password: String): Boolean {
        if (existeUsuario(usuario)) return false
        val salt = generarSalt()
        val valores = ContentValues().apply {
            put("usuario", usuario)
            put("salt", salt)
            put("hash", hashPassword(password, salt))
        }
        return writableDatabase.insert("usuarios", null, valores) != -1L
    }

    fun validarLogin(usuario: String, password: String): Boolean {
        val cursor = readableDatabase.rawQuery(
            "SELECT salt, hash FROM usuarios WHERE usuario = ?", arrayOf(usuario)
        )
        var valido = false
        if (cursor.moveToFirst()) {
            val salt = cursor.getString(0)
            val hash = cursor.getString(1)
            valido = hashPassword(password, salt) == hash
        }
        cursor.close()
        return valido
    }


    fun borrarUsuario(usuario: String, password: String): Boolean {
        if (!validarLogin(usuario, password)) return false
        return writableDatabase.delete("usuarios", "usuario = ?", arrayOf(usuario)) > 0
    }
}
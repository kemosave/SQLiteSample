package com.websarva.wings.android.sqlitesample

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var helper: TestOpenHelper;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        helper = TestOpenHelper(applicationContext);    //DBを作成する
    }

    /**
     * DBからデータをすべて取得し画面に表示する
     * @param view
     * */
    fun readData(view: View) {
        val db: SQLiteDatabase = helper.readableDatabase;
        val cursor: Cursor = db.query(
            "testdb",
            arrayOf("medicine", "value", "kind"),
            null,
            null,
            null,
            null,
            null
        )

        cursor.moveToFirst()

        val sbuilder: StringBuilder = StringBuilder();

        val medicidneNameList = arrayListOf<String>();

        for (i in 1..cursor.count) {
            sbuilder.append(cursor.getString(0))
            medicidneNameList.add(cursor.getString(0))
            sbuilder.append(":  ")
            sbuilder.append(cursor.getInt(1))
            sbuilder.append(":  ")
            sbuilder.append(cursor.getString(2))
            sbuilder.append("\n\n")
            cursor.moveToNext()
        }

        cursor.close()

        val spinner: Spinner = findViewById(R.id.spinner)
        setAdapter(spinner,medicidneNameList)

        textView.text = sbuilder.toString();
    }

    /**
    *データを保存する
    * @param view
    * */
    fun saveData(view: View) {
        val db: SQLiteDatabase = helper.writableDatabase;
        val values: ContentValues = ContentValues();

        //薬の名前と値段を取得
        val editTextMedicine: EditText = findViewById(R.id.edit_medicine);
        val editTextValue: EditText = findViewById(R.id.edit_value);
        //薬の種類を取得・ラジオボタンに関する変数
        val radioGroup: RadioGroup = findViewById(R.id.radioGroup);
        val radioId = radioGroup.checkedRadioButtonId;
        val radioButton: RadioButton = radioGroup.findViewById<RadioButton>(radioId);
        //val index = radioGroup.indexOfChild(radioButton);

        //表示させる形式に変数を変換
        val medicine: String = editTextMedicine.text.toString();
        val value: Int = editTextValue.text.toString().toInt();
        val kind: String = radioButton.text.toString();

        values.put("medicine", medicine)
        values.put("value", value)
        values.put("kind", kind)

        db.insertOrThrow("testdb", null, values)
    }

    private fun setAdapter(spinner: Spinner, arrayList: ArrayList<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter;
    }

}

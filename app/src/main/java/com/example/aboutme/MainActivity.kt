package com.example.aboutme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputBinding
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.aboutme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // Делаем отложенную загрузку класса для привязки данных

    private val myName: MyName = MyName("Ni Evgenii") //Заполняем данные

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main) //Соединяет MainActivity.kt с activity_main.xml
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main) //Это вместо строчки выше при привязки данных
        binding.myyName = myName

        //findViewById<Button>(R.id.done_button).setOnClickListener{addNickname(it)}    //Находим кнопку по id и вызываем функцию addNickname когда унопку нажали
        binding.doneButton.setOnClickListener{addNickname(it)}
    }

    private fun addNickname(view: View) {
        // Можно сделать так
        /*binding.nicknameText.text = binding.nickNameEdit.text
        binding.nickNameEdit.visibility = View.GONE
        binding.doneButton.visibility = View.GONE
        binding.nicknameText.visibility = View.VISIBLE*/

        //или так
        binding.apply {
            //nicknameText.text = nickNameEdit.text
            myName.nickname = nickNameEdit.text.toString() //Показывем такст с помощью класса MyName
            invalidateAll() //Отвязываем данные
            nickNameEdit.visibility = View.GONE
            doneButton.visibility = View.GONE
            nicknameText.visibility = View.VISIBLE
        }

        //Не нужно т.к. сделана привязка данных
        /*val editText = findViewById<EditText>(R.id.nickName_edit)
        val nicknameTextView = findViewById<TextView>(R.id.nickname_text)

        nicknameTextView.text = editText.text
        editText.visibility = View.GONE //Скрыть поле
        view.visibility = View.GONE //Скрыть кнопку
        nicknameTextView.visibility = View.VISIBLE //Показать поле
        */

        //Спрятать клавиатуру
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
/*
Привязка данных - идея
Основная идея привязки данных заключается в создании объекта, который соединяет / отображает / связывает две части удаленной информации во время компиляции, чтобы вам не приходилось искать ее во время выполнения.
Объект, который связывает вас с этими привязками, называется объектом привязки. Он создан компилятором, и хотя интересно понять, как он работает под капотом, нет необходимости знать основные способы использования привязки данных.
Привязка данных и findViewById
findViewById - это дорогостоящая операция, поскольку она пересекает иерархию представлений при каждом вызове.
При включенном связывании данных компилятор создает ссылки на все представления в a <layout>, имеющие идентификатор, и собирает их в объекте Binding.
В своем коде вы создаете экземпляр объекта привязки, а затем ссылаетесь на представления через объект привязки без дополнительных затрат.

Представления привязки данных и данные
Обновление данных, а затем обновление данных, отображаемых в представлениях, является обременительным и источником ошибок. Хранение данных в представлении также нарушает разделение данных и представление.
Привязка данных решает обе эти проблемы. Вы храните данные в классе данных. Вы добавляете <data>блок в, <layout>чтобы идентифицировать данные как переменные для использования с представлениями. Представления ссылаются на переменные.
Компилятор генерирует объект привязки, который связывает представления и данные.
В вашем коде вы ссылаетесь и обновляете данные через объект привязки, который обновляет данные и, следовательно, то, что отображается в представлении.
Привязка представлений к данным устанавливает основу для более продвинутых методов, использующих привязку данных.

В этом упражнении вы собираетесь улучшить приложение AboutMe, используя привязку данных вместо findViewById, и использовать фактические данные, связанные с представлениями name_text и nickname_text, для отображения информации.

Сделайте следующее:

Включите привязку данных в файле build.gradle в модуле приложения внутри раздела Android:

dataBinding {
enabled = true
}
Оберните все представления в activity_main.xml в <layout>тег и переместите объявления пространства имен в <layout>тег.

В MainActivity создайте объект привязки:
private lateinit var binding: ActivityMainBinding
В onCreate используйте DataBindingUtil, чтобы установить представление содержимого:
binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
Используйте объект привязки, чтобы заменить все вызовы findViewById, например:
binding.doneButton.setOnClickListener….etc
Подсказка: вы можете использовать apply () в обработчике кликов, чтобы сделать ваш код более лаконичным и читабельным.

Создайте класс данных MyName для имени и псевдонима.
data class MyName(var name: String = "", var nickname: String = "")
Добавьте <data>блок в activity_main.xml. Блок данных идет внутри тега макета, но перед тегами просмотра. Внутри блока данных добавьте переменную для класса MyName.
<data>
<!-- Declare a variable by specifying a name and a data type. -->
<!-- Use fully qualified name for the type. -->
<variable
    name="myName"
    type="com.example.android.aboutme.MyName" />
</data>
В name_text, nickname_edit и nickname_text замените ссылки на строковые текстовые ресурсы ссылками на переменные, например>
android:text="@={myName.name}"
В MainActivity создайте экземпляр MyName.
// Instance of MyName data class.
private val myName: MyName = MyName("Aleks Haecky")
И в onCreate () установите для него привязку. MyName.
binding.myName = myName
В addNickname установите значение псевдонима в myName, вызовите invalidateAll (), и данные должны отображаться в ваших представлениях.
myName?.nickname = nicknameEdit.text.toString()
// Invalidate all binding expressions and request a new rebind to refresh UI
invalidateAll()
Когда вы запускаете свой код, он не должен иметь ошибок и выглядеть и работать точно так же!



 */
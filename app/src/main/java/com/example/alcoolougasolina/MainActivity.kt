package com.example.alcoolougasolina

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var etPrecoAlcool: EditText
    private lateinit var etPrecoGasolina: EditText
    private lateinit var btCalc: Button
    private lateinit var textMsg: TextView

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    lateinit var switch: Switch
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("SetTextI18n", "StringFormatInvalid", "StringFormatMatches")
    @Suppress("NAME_SHADOWING")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPrecoAlcool = findViewById(R.id.edAlcool)
        etPrecoGasolina = findViewById(R.id.edGasolina)
        btCalc = findViewById(R.id.btCalcular)
        textMsg = findViewById(R.id.textMsg)
        switch = findViewById(R.id.swPercentual)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Recuperar os valores salvos
        val precoAlcool = sharedPreferences.getFloat("precoAlcool", 0.0f)
        val precoGasolina = sharedPreferences.getFloat("precoGasolina", 0.0f)
        val isChecked = sharedPreferences.getBoolean("isChecked", switch.isChecked)
        val textMensagem = sharedPreferences.getString("textMensagem", textMsg.text.toString())
        var swPercentual = if (isChecked) 75 else 70

        etPrecoAlcool.setText(precoAlcool.toString())
        etPrecoGasolina.setText(precoGasolina.toString())
        textMsg.text = textMensagem
        switch.isChecked = isChecked

        switch.setOnCheckedChangeListener { _, isChecked ->
            swPercentual = if (isChecked) 75 else 70
            updatePercentualText(swPercentual)
        }

        updatePercentualText(swPercentual)

        btCalc.setOnClickListener {
            val precoAlcoolText = etPrecoAlcool.text.toString()
            val precoGasolinaText = etPrecoGasolina.text.toString()

            if (precoAlcoolText.isNotEmpty() && precoGasolinaText.isNotEmpty()) {
                try {
                    val precoAlcool = precoAlcoolText.toFloat()
                    val precoGasolina = precoGasolinaText.toFloat()

                    val percentual = precoAlcool / precoGasolina * 100
                    val percentualFormatado = String.format("%.2f", percentual)

                    textMsg.text = if (percentual <= swPercentual) {
                        getString(
                            R.string.mensagem_alcool_mais_rentavel,
                            percentualFormatado,
                            swPercentual
                        )
                    } else {
                        getString(
                            R.string.mensagem_gasolina_mais_rentavel,
                            percentualFormatado,
                            swPercentual
                        )
                    }

                    with(sharedPreferences.edit()) {
                        putFloat("precoAlcool", precoAlcool)
                        putFloat("precoGasolina", precoGasolina)
                        putInt("swPercentual", swPercentual)
                        putBoolean("isChecked", switch.isChecked)
                        putString("textMensagem", textMsg.text.toString())
                        apply()
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error updating text message", e)
                    textMsg.text = getString(R.string.preecha_valores)
                }
            } else {
                textMsg.text = getString(R.string.preecha_valores)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updatePercentualText(swPercentual: Int) {
        switch.text =
            resources.getString(if (swPercentual == 75) R.string.percentual_75 else R.string.percentual_70)
    }

    override fun onResume() {
        super.onResume()
        Log.d("PDM24", "No onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.v("PDM24", "No onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.e("PDM24", "No onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.w("PDM24", "No onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.wtf("PDM24", "No Destroy")
    }
}
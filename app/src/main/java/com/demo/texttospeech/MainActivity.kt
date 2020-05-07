package com.demo.texttospeech

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private var textToSpeech: TextToSpeech? = null
    var voiceName: ArrayList<String> = ArrayList()
    var voiceList: ArrayList<Voice?> = ArrayList()

    val speechRate: Array<Float> = arrayOf(0.1f, 0.5f, 1.0f, 1.5f, 2.0f)
    val pitch: Array<Float> = arrayOf(
        0.1f,
        0.5f,
        1.0f,
        1.5f,
        2.0f,
        4f,
        8f,
        10f,
        15f,
        20f,
        25f,
        30f,
        40f,
        50f,
        70f,
        80f,
        90f,
        100f
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSpeechRateAdapter()
        setPitchAdapter()
        initialiseTextToSpeech()

        spinnerPitch?.setSelection(pitch.indexOf(1f))
        spinnerSpeechRate?.setSelection(speechRate.indexOf(1f))

        btnTextToSpeech?.setOnClickListener {
            if (edTextToSpeech?.text?.toString()?.isNotEmpty() == true) {

                Handler().postDelayed({
                    if (textToSpeech?.isSpeaking == true) {
//                        performAction()
                    } else {
                        val speechStatus =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                textToSpeech?.speak(
                                    edTextToSpeech?.text?.toString(),
                                    TextToSpeech.QUEUE_FLUSH,
                                    null,
                                    null
                                )
                            } else {
                                textToSpeech?.speak(
                                    edTextToSpeech?.text?.toString(),
                                    TextToSpeech.QUEUE_FLUSH,
                                    null
                                )
                            }
                        if (speechStatus == TextToSpeech.ERROR) {
                            Log.e("TTS", "Error in converting Text to Speech!")
                        }
                    }
                }, 10)


            }
        }

    }

    private fun initialiseTextToSpeech() {
        textToSpeech = TextToSpeech(
            applicationContext,
            TextToSpeech.OnInitListener { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val ttsLang = textToSpeech?.setLanguage(Locale.US)

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!")
                    } else {
                        Log.i("TTS", "Language Supported.")
                    }
                    Log.i("TTS", "Initialization success.")



                    textToSpeech?.let {
                        for (tmpVoice in it.voices) {
                            if (!tmpVoice.features.contains("notInstalled")) {
                                voiceName.add(tmpVoice.name)
                                voiceList.add(tmpVoice)
                                Log.e("tag ", "Tag if voice ${tmpVoice} name ${tmpVoice.name}")
                            } else {
                                Log.e("tag ", "Tag else voice ${tmpVoice} name ${tmpVoice.name}")
                            }


                        }
                        setLangaugeAdapter()
                    }


                } else {
                    Toast.makeText(
                        applicationContext,
                        "TTS Initialization failed!",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }, "com.google.android.tts"
        )


    }

    public override fun onDestroy() {
        super.onDestroy()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }

    private fun setLangaugeAdapter() {

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, voiceName)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        spinnerLanguage?.adapter = adapter
        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                postion: Int,
                p3: Long
            ) {
                Log.i("TTS", "Voice select ${voiceList[postion]}")
                textToSpeech?.voice = voiceList[postion]
            }

        }
    }

    private fun setSpeechRateAdapter() {

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, speechRate)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        spinnerSpeechRate?.adapter = adapter
        spinnerSpeechRate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                postion: Int,
                p3: Long
            ) {
                textToSpeech?.setSpeechRate(speechRate[postion])

            }

        }
    }

    private fun setPitchAdapter() {

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, pitch)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        spinnerPitch?.adapter = adapter
        spinnerPitch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                postion: Int,
                p3: Long
            ) {
                textToSpeech?.setPitch(pitch[postion])
            }

        }
    }
}

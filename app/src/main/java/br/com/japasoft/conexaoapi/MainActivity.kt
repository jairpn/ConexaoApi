package br.com.japasoft.conexaoapi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.japasoft.conexaoapi.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded as FormUrlEncoded1
import retrofit2.http.POST

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityMainBinding

private val retrofit =
    Retrofit.Builder()
        .addConverterFactory(
            GsonConverterFactory.create()
        ) // / Para converter o arquivo quem virá JSON
        .baseUrl("http://192.168.1.111")
        .build()
        .create(MainActivity.EnviaUsuario::class.java)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnEntrar.setOnClickListener {
            retrofit
                .setUsuario(binding.edtUsuario.text.toString(), binding.edtSenha.text.toString())
                .enqueue(
                    object : Callback<Usuario> {
                        override fun onFailure(call: Call<Usuario>, t: Throwable) {
                            Log.d("Erro: ", t.toString())
                        }

                        override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                            if (response.isSuccessful) {
                                response.body()?.let {
                                    if (response.body()!!.usuario == "vazio") {
                                        exibeToast(false)
                                    } else {
                                        exibeToast(true)
                                    }
                                }
                            }
                        }
                    })
        }
    }

    private fun exibeToast(respostaServidor: Boolean) {
        if (respostaServidor) {
            Toast.makeText(this, "Blz", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Senha/Usuário incorreto", Toast.LENGTH_LONG).show()
        }
    }

    interface EnviaUsuario {
        @FormUrlEncoded1
        @POST("autentica.php")
        fun setUsuario(
            @Field("usuario") usuario: String,
            @Field("senha") senha: String
        ): Call<Usuario>
    }
}

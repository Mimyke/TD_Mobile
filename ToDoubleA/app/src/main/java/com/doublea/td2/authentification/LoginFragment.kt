package com.doublea.td2.authentification

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.doublea.td2.MainActivity
import com.doublea.td2.R
import com.doublea.td2.network.Api
import com.doublea.td2.network.SHARED_PREF_TOKEN_KEY
import kotlinx.coroutines.launch
import kotlin.reflect.KParameter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val edit_email_login = view.findViewById<EditText>(R.id.email_login)
        val edit_password_login = view.findViewById<EditText>(R.id.password_login)
        val button_login = view.findViewById<Button>(R.id.button_login)

        button_login.setOnClickListener {
            val email_login = edit_email_login.text.toString()
            val password_login = edit_password_login.text.toString()
            if (email_login.isEmpty() || password_login.isEmpty()) {
                Toast.makeText(context, "Veuillez compléter les champs vides", Toast.LENGTH_LONG).show()
            } else {
                val loginForm = LoginForm(email_login, password_login)
                lifecycleScope.launch {
                    val loginResponse = Api.INSTANCE.userWebService.login(loginForm)
                    if (loginResponse.isSuccessful) {

                        val token = loginResponse.body()?.token

                        //Toast.makeText(context, token, Toast.LENGTH_LONG).show()
                        PreferenceManager.getDefaultSharedPreferences(context).edit {
                            putString(SHARED_PREF_TOKEN_KEY, token)
                        }
                        startActivity(Intent(activity, MainActivity::class.java))
                    } else {
                        Toast.makeText(context, "Erreur d'autentification", Toast.LENGTH_LONG).show()
                    }

                }
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
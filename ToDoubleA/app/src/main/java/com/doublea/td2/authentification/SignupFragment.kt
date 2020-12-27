package com.doublea.td2.authentification

import android.content.Intent
import android.os.Bundle
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val firstname_signup = view.findViewById<EditText>(R.id.firstname_signup).toString()
        val lastname_signup = view.findViewById<EditText>(R.id.lastname_signup).toString()
        val email_signup = view.findViewById<EditText>(R.id.email_signup).toString()
        val password_signup = view.findViewById<EditText>(R.id.password_signup).toString()
        val confirm_signup = view.findViewById<EditText>(R.id.confirm_signup).toString()
        val button_signup = view.findViewById<Button>(R.id.button_signup)

        button_signup.setOnClickListener {

            if (firstname_signup.isEmpty() || lastname_signup.isEmpty() || email_signup.isEmpty() || password_signup.isEmpty() || confirm_signup.isEmpty()) {
                Toast.makeText(context, "Veuillez compléter les champs vides", Toast.LENGTH_LONG).show()
            } else {
                val SignUpForm = SignUpForm(firstname_signup, lastname_signup, email_signup, password_signup, confirm_signup)
                lifecycleScope.launch {
                    val SignUpResponse = Api.INSTANCE.userWebService.signup(SignUpForm)
                    if (SignUpResponse.isSuccessful) {

                        val token = SignUpResponse.body()?.token

                        Toast.makeText(context, token, Toast.LENGTH_LONG).show()
                        PreferenceManager.getDefaultSharedPreferences(context).edit {
                            putString(SHARED_PREF_TOKEN_KEY, token)
                        }
                    } else {
                        Toast.makeText(context, "Erreur de création d'un nouveau compte", Toast.LENGTH_LONG).show()
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
         * @return A new instance of fragment SignupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
package com.doublea.td2.authentification

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.doublea.td2.R
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.doublea.td2.MainActivity
import com.doublea.td2.network.SHARED_PREF_TOKEN_KEY

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AuthentificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthentificationFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_authentification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Redirection if already logged in
        val token = PreferenceManager.getDefaultSharedPreferences(context).getString(SHARED_PREF_TOKEN_KEY, "")
        if (token != null && token != "auth_token_key")
            startActivity(Intent(activity, MainActivity::class.java))

        val go_login_button = view.findViewById<Button>(R.id.go_login_button)
        val go_signup_button = view.findViewById<Button>(R.id.go_signup_button)

        // Go to Login Fragment
        go_login_button.setOnClickListener {
            findNavController().navigate(R.id.goto_fragment_login)
        }
        // Go to SignUp Fragment
        go_signup_button.setOnClickListener {
            findNavController().navigate(R.id.goto_fragment_signup)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AuthentificationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AuthentificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
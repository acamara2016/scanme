package com.acamara.scanme.registration.ui.signup

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.acamara.scanme.Homepage
import com.acamara.scanme.R
import com.acamara.scanme.database.FirebaseHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*

class loginFragment : Fragment() {

    private lateinit var signupViewModel: signupViewModel
    private lateinit var fb : FirebaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        val login_btn = root.findViewById<Button>(R.id.login_btn)

        login_btn.setOnClickListener{
                login(email_login_input.text.toString(),password_login_input.text.toString())
        }
        root.findViewById<Button>(R.id.skip_login_view).setOnClickListener {
            skip_login()
        }
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fb = FirebaseHelper()
        if (fb.getUser()!=null){
            val intent = Intent(activity, Homepage::class.java)
            startActivity(intent)
        }
    }

    fun login(email: String?, password: String?) {
        var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        if (!email.isNullOrBlank()) {
            if (!password.isNullOrBlank()) {
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(OnCompleteListener<AuthResult>(){
                  task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        val intent = Intent(activity, Homepage::class.java)
                        startActivity(intent)
                    }else{

                    }
                })
            }else{

            }
        }
    }
    fun skip_login(){
        var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        mAuth.signInAnonymously().addOnCompleteListener(OnCompleteListener<AuthResult>(){
                task: Task<AuthResult> ->
            if (task.isSuccessful) {
                val intent = Intent(activity, Homepage::class.java)
                startActivity(intent)
            }else{

            }
        })
    }


    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): signUpFragment {
            return signUpFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}
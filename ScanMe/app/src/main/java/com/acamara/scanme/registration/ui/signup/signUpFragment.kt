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
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.skip_registration_view


class signUpFragment : Fragment() {
    private var mAuth: FirebaseAuth? = null
    private lateinit var fb : FirebaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance();
        val root = inflater.inflate(R.layout.fragment_registration, container, false)
        val sign_btn = root.findViewById<Button>(R.id.signup_btn)
        val termsLink =
            "<html>By signing in, you agree with our <a href=\"https://scanner-app.flycricket.io/privacy.html\">Terms of service</a> and <a href=\"https://scanner-app.flycricket.io/privacy.html\">Privacy policy</html>"
        val privacy_service_view = root.findViewById<CheckBox>(R.id.signup_privacy_service_text)
        privacy_service_view.setText(Html.fromHtml(termsLink))
        privacy_service_view.setMovementMethod(LinkMovementMethod.getInstance())

        sign_btn.setOnClickListener{
            if (privacy_service_view.isChecked){
                signUp(
                    email_signup_input.text.toString(), password_signup_input.text.toString(),
                    confirm_password_input?.text.toString()
                )
            }else{
                alert(privacy_service_view)
            }
        }
        root.findViewById<Button>(R.id.skip_registration_view).setOnClickListener {
            skip_registration()
        }
        return root
    }

    private fun skip_registration() {
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

    fun signUp(email: String?, password: String?, confirm_password: String?) {
        var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        if (!email.isNullOrBlank()) {
            if (!password.isNullOrBlank()) {
                if (!confirm_password.isNullOrBlank()) {
                    if (password.equals(confirm_password)) {
                        System.out.println("SignUp : " + password + " " + confirm_password)
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                            OnCompleteListener<AuthResult>() { task: Task<AuthResult> ->
                                if (task.isSuccessful) {
                                    showPreference()
                                } else {
                                    signup_feedback.visibility = View.VISIBLE
                                    signup_feedback.setError(task.result.toString())
                                }
                            })
                    }else{
                        password_signup_input.setError("Password not matching")
                        confirm_password_input.setError("Password not matching")
                    }
                }else{
                    confirm_password_input.setError("Confirm password")
                }
            }else{
                password_signup_input.setError("Input a password")
            }
        }else if(email_signup_input.text.isNullOrBlank()){
            email_signup_input.setError("Input an email")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fb = FirebaseHelper()
        if (fb.getUser()!=null){
            val intent = Intent(activity, Homepage::class.java)
            startActivity(intent)
        }
    }

    private fun showPreference(){
        //MultiSelectModel
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Pick your preferences")
        val categories = arrayOf("Automotive Parts & Accessories","Electronics","Health & Personal care","Pet supply","Sport & Outdoor","Tools","Video Games","Home & Garden","Baby products","Essentials")
        val checkedItems = booleanArrayOf(false, false, false, false, false, false, false, false, false, false)
        builder.setMultiChoiceItems(
            categories, checkedItems
        ) { dialog, which, isChecked ->
            // user checked or unchecked a box
        }
        builder.setPositiveButton("OK") { dialog, which ->
            // user clicked OK
            for (x in 0 until checkedItems.size){
                if (checkedItems[x]==true)
                    fb.sendCategoryPreference(categories[x].toString())
            }
            updateUI()
        }
        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
    }

    fun updateUI(){
        val intent = Intent(activity, Homepage::class.java)
        startActivity(intent)
    }


    private fun alert(signup_privacy_service_text: CheckBox) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Notice")
        builder.setMessage("Do you accept the terms and conditions?")
        builder.setIcon(R.drawable.ic_baseline_info_24)
        builder.setPositiveButton("Yes"){ dialogInterface, which ->
            Toast.makeText(activity, "clicked yes", Toast.LENGTH_LONG).show()
            signup_privacy_service_text.isChecked = true
        }
        builder.setNeutralButton("Cancel"){ dialogInterface, which ->
            Toast.makeText(activity, "clicked cancel\n operation cancel", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No"){ dialogInterface, which ->
            Toast.makeText(activity, "clicked No", Toast.LENGTH_LONG).show()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }



}
package com.acamara.scanme.ui.account

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.acamara.scanme.R
import com.acamara.scanme.database.FirebaseHelper
import com.acamara.scanme.registration.ui.Registration
import kotlinx.android.synthetic.main.account_fragment.*
import kotlinx.android.synthetic.main.account_fragment.view.*

class AccountFragment : Fragment() {
    val fb = FirebaseHelper()
    companion object {
        fun newInstance() = AccountFragment()
    }

    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.account_fragment, container, false)
        Handler().postDelayed({load(root)},1500)
        root.toolbar_account.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_nav_home)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email_account_view = view.findViewById<TextView>(R.id.email_account_view)
        val button4 = view.findViewById<Button>(R.id.button4)
        email_account_view.text = fb.getUser()?.email.toString()
        val intent = Intent(view.context, Registration::class.java)
        button4.setOnClickListener {
            fb.logout()
            startActivity(intent)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
    }
    fun load(view: View){
        view.progress_account.visibility = View.GONE
        main_account_layout.visibility = View.VISIBLE
    }
}
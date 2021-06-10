package com.arvind.loginapiapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.arvind.loginapiapp.R
import com.arvind.loginapiapp.databinding.ActivityMainBinding
import com.arvind.loginapiapp.utils.isNetworkConnected
import com.arvind.loginapiapp.utils.toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        doinits()
    }

    private fun doinits() = with(binding) {
        gettextwathcerlogin()
        buttonLogin.setOnClickListener {
            if (!validateUserEmail() or !validateUserPassword()) {
                return@setOnClickListener
            } else if (isNetworkConnected(this@MainActivity)){

            }
        }
    }

    private fun validateUserPassword(): Boolean {
        if (ed_password_login.text.toString()
                .isEmpty() or isValidPassword(ed_password_login.text.toString())
        ) {
            tverror_password_viewlogin.error = tverror_password_viewlogin.error
            tverror_password_viewlogin.visibility = View.VISIBLE

            return false
        } else {
            tverror_password_viewlogin.isEnabled = false
            tverror_password_viewlogin.visibility = View.GONE
            tverror_password_viewlogin.error = null
        }

        return true


    }

    private fun isValidPassword(password: String): Boolean {

        val regex = ("^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$")

        val p = Pattern.compile(regex)
        val m = p.matcher(password)
        return m.matches()

    }

    private fun validateUserEmail(): Boolean {
        if (ed_email_login.text.toString()
                .isEmpty() or !isValidEmailaddress(ed_email_login.text.toString())
        ) {
            tverror_email_viewlogin.error = tverror_email_viewlogin.error
            tverror_email_viewlogin.visibility = View.VISIBLE

            return false
        } else {
            tverror_email_viewlogin.isEnabled = false
            tverror_email_viewlogin.visibility = View.GONE
            tverror_email_viewlogin.error = null
        }

        return true
    }

    private fun isValidEmailaddress(email: String): Boolean {

        val pattern: Pattern
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()

    }

    private fun gettextwathcerlogin() {
        ed_email_login.addTextChangedListener(emailTextWatcher)

        ed_password_login.addTextChangedListener(passwordTextWatcher)
    }

    private val emailTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            validateUserEmail()
        }
    }

    private val passwordTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            validateUserPassword()
        }
    }
}
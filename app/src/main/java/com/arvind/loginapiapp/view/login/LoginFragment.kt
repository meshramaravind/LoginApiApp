package com.arvind.loginapiapp.view.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.arvind.loginapiapp.R
import com.arvind.loginapiapp.databinding.FragmentLoginBinding
import com.arvind.loginapiapp.model.DataModelLoginBody
import com.arvind.loginapiapp.utils.Resource
import com.arvind.loginapiapp.utils.hideKeyboard
import com.arvind.loginapiapp.view.base.BaseFragment
import com.arvind.loginapiapp.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.regex.Pattern

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    @ExperimentalCoroutinesApi
    override val viewModel: LoginViewModel by viewModels()
    lateinit var stringEmailorMobile: String
    lateinit var stringPassword: String
    lateinit var deviceId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("HardwareIds")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deviceId = Settings.Secure.getString(
            requireContext().contentResolver, Settings.Secure.ANDROID_ID
        )

        doinits()
    }

    private fun doinits() = with(binding) {
        gettextwathcerlogin()
        buttonLogin.setOnClickListener {
            hideKeyboard()
            stringEmailorMobile = edEmailormobileLogin.text.toString().trim()
            stringPassword = edPasswordLogin.text.toString().trim()
            if (!validateUserEmailorMobile() or !validateUserPassword()) {
                return@setOnClickListener
            } else {
                getLogin()
            }
        }
    }

    private fun getLogin() {
        val dataModelLoginBody = DataModelLoginBody(
            stringEmailorMobile,
            stringPassword, "For Login user", deviceId
        )
        viewModel.loginUser(dataModelLoginBody)
        viewModel.loginData.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { loginResponse ->
                            findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                        }
                    }

                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let { toast(it) }
                    }

                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }
        })
    }

    private fun showProgressBar() {
        progressbar_login.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressbar_login.visibility = View.GONE
    }

    private fun validateUserPassword(): Boolean {
        if (ed_password_login.text.toString()
                .isEmpty() or !isValidPassword(ed_password_login.text.toString())
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

    private fun validateUserEmailorMobile(): Boolean {
        val email: String =
            ed_emailormobile_login.text.toString().trim()

        if (ed_emailormobile_login.text.toString()
                .isEmpty() or !isValidEmailaddress(email) and !validmobilenumber(email)
        ) {
            tverror_emailormobile_login.error = tverror_emailormobile_login.error
            tverror_emailormobile_login.visibility = View.VISIBLE

            return false
        } else {
            tverror_emailormobile_login.isEnabled = false
            tverror_emailormobile_login.visibility = View.GONE
            tverror_emailormobile_login.error = null
        }

        return true

    }

    private fun validmobilenumber(password: String): Boolean {

        val p = Pattern.compile("(0/91)?[7-9][0-9]{9}")

        val m = p.matcher(password)
        return m.find() && m.group() == password

    }

    private fun isValidEmailaddress(email: String): Boolean {

        val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$"

        val pat = Pattern.compile(emailRegex)
        return pat.matcher(email).matches()

    }

    private fun gettextwathcerlogin() {
        ed_emailormobile_login.addTextChangedListener(emailTextWatcher)

        ed_password_login.addTextChangedListener(passwordTextWatcher)
    }

    private val emailTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            validateUserEmailorMobile()
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

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)
}
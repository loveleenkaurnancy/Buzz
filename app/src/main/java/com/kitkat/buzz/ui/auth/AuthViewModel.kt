package com.kitkat.buzz.ui.auth

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import com.kitkat.buzz.data.repositories.UserRepository

class AuthViewModel(
    private val repository: UserRepository
) : ViewModel() {

    //email and password for the input
    var email: String? = null
    var password: String? = null
    var confirm_password: String? = null
    var EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z0-9]+\\.+[a-z.]+"

    var authListener: AuthListener? = null

    private val disposables = CompositeDisposable()

    val user by lazy {
        repository.currentUser()
    }

    //function to perform login
    fun login() {

        //validating email and password
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Invalid email or password")
            return
        }
        else if (!email.toString().trim { it <= ' ' }.matches(EMAIL_PATTERN.toRegex())) {

            authListener?.onFailure("Enter your correct email")
            return
        }

        //authentication started
        authListener?.onStarted()

        val disposable = repository.login(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //sending a success callback
                authListener?.onSuccess()
            }, {
                //sending a failure callback
                authListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

    fun signup() {
        if (email.isNullOrEmpty()) {
            authListener?.onFailure("Please enter email")
            return
        }
        else if (!email.toString().trim { it <= ' ' }.matches(EMAIL_PATTERN.toRegex())) {

            authListener?.onFailure("Enter your correct email")
            return
        }
        else if (password.isNullOrEmpty()) {
            authListener?.onFailure("Please enter password")
            return
        }
        else if (!(password.equals(confirm_password))) {
            authListener?.onFailure("Password does not match")
            return
        }
        authListener?.onStarted()
        val disposable = repository.register(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                authListener?.onSuccess()
            }, {
                authListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

    fun goToSignup(view: View) {
        Intent(view.context, SignupActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun goToLogin(view: View) {
        Intent(view.context, LoginActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}
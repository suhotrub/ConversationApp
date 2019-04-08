package com.suhotrub.conversations.ui.activities.auth.signup

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.widget.RxTextView
import com.suhotrub.conversations.R
import com.suhotrub.conversations.ui.activities.auth.signin.SignInView
import com.suhotrub.conversations.ui.activities.main.MainActivity
import com.suhotrub.conversations.ui.util.recycler.LoadState
import com.suhotrub.conversations.ui.util.ui.config
import com.suhotrub.conversations.ui.util.ui.setVisibleOrGone
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_sign_up.*
import javax.inject.Inject


class SignUpActivity : MvpAppCompatActivity(), SignUpView {

    @Inject
    @InjectPresenter
    lateinit var presenter: SignUpPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!::presenter.isInitialized)
            AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)


        presenter.observeLogin(RxTextView.textChanges(email).map(CharSequence::toString))
        presenter.observeName(RxTextView.textChanges(name).map(CharSequence::toString))
        presenter.observeSurname(RxTextView.textChanges(surname).map(CharSequence::toString))
        presenter.observePassword(RxTextView.textChanges(password).map(CharSequence::toString))
        presenter.observePasswordAgain(RxTextView.textChanges(password_again).map(CharSequence::toString))

        button.setOnClickListener {
            presenter.signUp()
        }

        textView3.setOnClickListener {
            onBackPressed()
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    override fun renderLoadState(loadState: LoadState) {
        button.setVisibleOrGone(loadState != LoadState.MAIN_LOADING)
        progressBar.setVisibleOrGone(loadState == LoadState.MAIN_LOADING)
    }

    override fun showValidationError() {
        Snackbar
                .make(
                        root,
                        "Проверьте правильность заполнения полей",
                        Snackbar.LENGTH_SHORT
                )
                .config(this@SignUpActivity)
                .show()
    }

    override fun showError(t: Throwable) {
        Snackbar
                .make(
                        root,
                        t.message.toString(),
                        Snackbar.LENGTH_SHORT
                )
                .config(this@SignUpActivity)
                .show()
    }

    override fun navigateAfterLogin() {
        startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
        finishAffinity()
    }
}
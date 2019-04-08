package com.suhotrub.conversations.ui.activities.auth.signin

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jaeger.library.StatusBarUtil
import com.jakewharton.rxbinding2.widget.RxTextView
import com.suhotrub.conversations.R
import com.suhotrub.conversations.ui.activities.auth.signup.SignUpActivity
import com.suhotrub.conversations.ui.activities.main.MainActivity
import com.suhotrub.conversations.ui.util.recycler.LoadState
import com.suhotrub.conversations.ui.util.ui.config
import com.suhotrub.conversations.ui.util.ui.setVisibleOrGone
import dagger.android.AndroidInjection
import io.reactivex.android.plugins.RxAndroidPlugins
import kotlinx.android.synthetic.main.activity_sign_in.*
import javax.inject.Inject

class SignInActivity : MvpAppCompatActivity(), SignInView {

    @Inject
    @InjectPresenter
    lateinit var presenter: SignInPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!::presenter.isInitialized)
            AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_in)
        StatusBarUtil.setTransparent(this)

        presenter.observeLogin(RxTextView.textChanges(editText).map(CharSequence::toString))
        presenter.observePassword(RxTextView.textChanges(editText2).map(CharSequence::toString))

        textView3.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }

        button.setOnClickListener {
            presenter.signIn()
        }
    }

    override fun renderLoadState(loadState: LoadState) {
        button.setVisibleOrGone(loadState != LoadState.MAIN_LOADING)
    }

    override fun showValidationError() {
        Snackbar
                .make(
                        root,
                        "Проверьте правильность заполнения полей",
                        Snackbar.LENGTH_SHORT
                )
                .config(this@SignInActivity)
                .show()
    }

    override fun showError(t: Throwable) {
        Snackbar
                .make(
                        root,
                        t.message.toString(),
                        Snackbar.LENGTH_SHORT
                )
                .config(this@SignInActivity)
                .show()
    }

    override fun navigateAfterLogin() {
        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finishAffinity()
    }
}
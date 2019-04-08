package com.suhotrub.conversations.ui.activities.finduser

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.widget.RxTextView
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.ui.util.recycler.ItemList
import com.suhotrub.conversations.ui.util.recycler.PaginationState
import com.suhotrub.conversations.ui.util.recycler.PaginationableAdapter
import com.suhotrub.conversations.ui.util.ui.showError
import com.suhotrub.conversations.ui.util.widget.user.UserItemController
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_find_user.*
import retrofit2.HttpException
import javax.inject.Inject

class FindUserActivity : MvpAppCompatActivity(), FindUserView {

    @Inject
    @InjectPresenter
    lateinit var presenter: FindUserPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private val adapter = PaginationableAdapter()

    private val userItemController = UserItemController({
        setResult(Activity.RESULT_OK, Intent().putExtra("EXTRA_FIRST", it))
        finish()
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_find_user)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        presenter.observeUsername(RxTextView.textChanges(find_user_et).map(CharSequence::toString))

        find_user_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        find_user_rv.adapter = adapter
        adapter.setOnShowMoreListener {
            presenter.loadMore()
        }
        adapter.setState(PaginationState.READY)
    }

    override fun showErrorSnackbar(t: Throwable) =
            showError(t)

    override fun showErrorSnackbar(text: String) =
            showError(text)


    override fun renderUsers(users: List<UserDto>) {
        adapter.setItems(
                ItemList.create()
                        .addAll(users, userItemController)
        )
        adapter.setState(PaginationState.READY)
    }

    override fun setPaginationState(paginationState: PaginationState) =
            adapter.setState(paginationState)
}
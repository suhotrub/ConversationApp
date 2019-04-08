package com.suhotrub.conversations.ui.activities.creategroup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.widget.RxTextView
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.ui.activities.finduser.FindUserActivity
import com.suhotrub.conversations.ui.util.recycler.EasyAdapter
import com.suhotrub.conversations.ui.util.recycler.ItemList
import com.suhotrub.conversations.ui.util.ui.showError
import com.suhotrub.conversations.ui.util.widget.user.UserItemController
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_create_group.*
import retrofit2.HttpException
import javax.inject.Inject

class CreateGroupActivity : MvpAppCompatActivity(), CreateGroupView {

    @Inject
    @InjectPresenter
    lateinit var presenter: CreateGroupPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private val adapter = EasyAdapter()

    private val userItemController = UserItemController({
        AlertDialog.Builder(this@CreateGroupActivity)
                .setTitle("Удалить пользователя?")
                .setPositiveButton("Удалить") { dialog, _ ->
                    presenter.deleteUser(it)
                    dialog.dismiss()
                }
                .setNegativeButton("Отмена") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
    }, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_group)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        create_group_submit_btn.setOnClickListener {
            presenter.submit()
        }

        presenter.observeGroupTitle(RxTextView.textChanges(create_group_title_et).map(CharSequence::toString))
        presenter.observeGroupDescription(RxTextView.textChanges(create_group_description_et).map(CharSequence::toString))

        create_group_users_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        create_group_users_rv.adapter = adapter

        create_group_add_member_btn.setOnClickListener {
            startActivityForResult(Intent(this@CreateGroupActivity, FindUserActivity::class.java), 228)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                228 -> data?.getParcelableExtra<UserDto>("EXTRA_FIRST")?.let(presenter::addUser)
            }
        super.onActivityResult(requestCode, resultCode, data)
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
    }
}
package com.makecity.core.presentation.view

import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.makecity.core.R
import com.makecity.core.presentation.navigation.BackNavigationConsumer
import com.makecity.core.presentation.navigation.BackNavigationDelegate
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.BaseReducer
import javax.inject.Inject


abstract class BaseActivity: AppCompatActivity(), BackNavigationConsumer {

    @LayoutRes
    open val layoutId: Int? = null
    override var backButtonDelegate: BackNavigationDelegate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutId?.let {
            setContentView(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val isReturn = backButtonDelegate?.let {
            backButtonDelegate = null
            it.onBackClick()
        } ?: run {
            super.onBackPressed()
            return
        }

        if (isReturn) return
        super.onBackPressed()
    }
}


abstract class ReducibleViewActivity<Reducer: BaseReducer<AG>, AG: ActionView>: BaseActivity(), ReducibleView<Reducer, AG>, InjectableView {

    @Inject
    override lateinit var reducer: Reducer

    override fun onCreate(savedInstanceState: Bundle?) {
        onInject()
        super.onCreate(savedInstanceState)
    }

}
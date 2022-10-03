package com.example.jwst_gifts.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jwst_gifts.databinding.ActivityMainBinding
import com.example.jwst_gifts.domain.model.SpaceProgram
import com.example.jwst_gifts.presentation.util.MessageManager
import com.example.jwst_gifts.presentation.adapter.PhotosAdapter
import com.example.jwst_gifts.presentation.viewmodel.LoadMore
import com.example.jwst_gifts.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val adapter = PhotosAdapter(photos = emptyList())
    private lateinit var binding: ActivityMainBinding
    private val messageManager by lazy { MessageManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewBinding = ActivityMainBinding.inflate(layoutInflater).also { binding = it }
        setContentView(viewBinding.root)

        viewBinding.bindState(
            isLoading = viewModel.isLoading,
            spacePrograms = viewModel.spacePrograms,
            loadMore = viewModel.loadMorePhotos,
            errors = viewModel.errors
        )
    }

    private fun ActivityMainBinding.bindState(
        isLoading: LiveData<Boolean>,
        spacePrograms: LiveData<List<SpaceProgram>>,
        loadMore: (LoadMore) -> Unit,
        errors: LiveData<String>
    ) {
        bindSpacePrograms(spacePrograms = spacePrograms, loadMore = loadMore)
        bindLoader(isLoading = isLoading)
        bindErrors(errors = errors)
    }

/*    private fun ActivityMainBinding.bindCompanyInfo(
        companyInfo: LiveData<CompanyInfo?>
    ) {
        companyInfo
            .distinctUntilChanged()
            .observe(this@MainActivity) {
                companyDetails.text = it?.summary.orEmpty()
                lblCompany.isVisible = companyInfo.value != null
                companyDetails.isVisible = companyInfo.value != null
            }
    }*/

    private fun ActivityMainBinding.bindSpacePrograms(
        spacePrograms: LiveData<List<SpaceProgram>>,
        loadMore: (LoadMore) -> Unit
    ) {
        photos.adapter = adapter

        setupScrollListener(onScroll = loadMore)

        spacePrograms
            .distinctUntilChanged()
            .observe(this@MainActivity) {
                photos.isVisible = it.isNotEmpty()
                photos.adapter = PhotosAdapter(photos = it)
            }
    }

    private fun ActivityMainBinding.bindLoader(isLoading: LiveData<Boolean>) {
        isLoading
            .distinctUntilChanged()
            .observe(this@MainActivity) {
                loader.isVisible = it
            }
    }

    private fun bindErrors(errors: LiveData<String>) {
        errors
            .observe(this@MainActivity) {
                messageManager.showError(errorMessage = it)
            }
    }

    private fun ActivityMainBinding.setupScrollListener(
        onScroll: (LoadMore) -> Unit
    ) {
        val layoutManager = photos.layoutManager as LinearLayoutManager
        photos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                onScroll(
                    LoadMore(
                        visibleItemCount = visibleItemCount,
                        lastVisibleItemPosition = lastVisibleItem,
                        totalItemCount = totalItemCount
                    )
                )
            }
        })

        onScroll(
            LoadMore(
                visibleItemCount = 0,
                lastVisibleItemPosition = 0,
                totalItemCount = 0
            )
        )
    }
}
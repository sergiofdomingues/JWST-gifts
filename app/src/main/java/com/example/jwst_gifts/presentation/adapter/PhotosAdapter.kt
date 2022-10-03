package com.example.jwst_gifts.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jwst_gifts.R
import com.example.jwst_gifts.databinding.ItemSpacePhotoBinding

import com.example.jwst_gifts.domain.model.SpaceProgram
import com.example.jwst_gifts.util.loadImageUrl

class PhotosAdapter(
    private val photos: List<SpaceProgram>
) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemSpacePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount() = photos.size

    inner class ViewHolder(private val binding: ItemSpacePhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(spaceProgram: SpaceProgram) {
            binding.apply {
                photo.loadImageUrl(spaceProgram.imageUrl, R.drawable.ic_broken_image)
                mission.text = spaceProgram.details?.mission.orEmpty()
            }
        }
    }
}
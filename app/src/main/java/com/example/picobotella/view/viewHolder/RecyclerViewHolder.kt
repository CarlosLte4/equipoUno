package com.example.picobotella.view.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.picobotella.model.Challenge
import com.example.picobotella.databinding.ItemChallengeBinding

class RecyclerViewHolder(
    private val binding: ItemChallengeBinding,
    private val onEditClick: (Challenge) -> Unit,
    private val onDeleteClick: (Challenge) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun setItemChallenge(challenge: Challenge) {
        binding.tvDescription.text = challenge.description
        binding.btnEdit.setOnClickListener {
            onEditClick(challenge)
        }
        binding.btnDelete.setOnClickListener {
            onDeleteClick(challenge)
        }
    }
}

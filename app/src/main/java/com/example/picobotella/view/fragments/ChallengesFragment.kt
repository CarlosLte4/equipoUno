package com.example.picobotella.view.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.picobotella.R
import com.example.picobotella.databinding.DialogChallengeFormBinding
import com.example.picobotella.databinding.DialogDeleteChallengeBinding
import com.example.picobotella.databinding.FragmentChallengesBinding
import com.example.picobotella.model.Challenge
import com.example.picobotella.view.adapter.RecyclerAdapter
import com.example.picobotella.viewmodel.ChallengeViewModel

class ChallengesFragment : Fragment() {

    private lateinit var binding: FragmentChallengesBinding
    private lateinit var challengeAdapter: RecyclerAdapter
    private val challengeViewModel: ChallengeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChallengesBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupAddButton()
        goBackHome()
    }

    private fun setupRecyclerView() {
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        challengeAdapter = RecyclerAdapter(
            onEditClick = { challenge -> showEditChallengeDialog(challenge) },
            onDeleteClick = { challenge -> showDeleteChallengeDialog(challenge) }
        )
        binding.recyclerview.adapter = challengeAdapter
    }

    private fun setupObservers() {
        challengeViewModel.challenges.observe(viewLifecycleOwner) { challenges ->
            challengeAdapter.submitList(challenges)
        }
    }

    private fun setupAddButton() {
        binding.btnAddChallenge.setOnClickListener {
            showAddChallengeDialog()
        }
    }

    private fun goBackHome() {
        binding.btnChallengesBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showAddChallengeDialog() {
        showChallengeFormDialog(
            title = getString(R.string.add_challenge_dialog_title),
            description = "",
            saveEnabledAtStart = false
        ) { description ->
            challengeViewModel.saveChallenge(description)
        }
    }

    private fun showEditChallengeDialog(challenge: Challenge) {
        showChallengeFormDialog(
            title = getString(R.string.edit_challenge_dialog_title),
            description = challenge.description,
            saveEnabledAtStart = true
        ) { description ->
            challengeViewModel.updateChallenge(challenge, description)
        }
    }

    private fun showChallengeFormDialog(
        title: String,
        description: String,
        saveEnabledAtStart: Boolean,
        onSave: (String) -> Unit
    ) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogChallengeFormBinding.inflate(layoutInflater)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(false)

        dialogBinding.tvDialogTitle.text = title
        dialogBinding.etChallengeDescription.setText(description)
        dialogBinding.etChallengeDescription.setSelection(description.length)
        dialogBinding.btnSave.isEnabled = saveEnabledAtStart
        dialogBinding.btnSave.alpha = if (saveEnabledAtStart) 1f else 0.45f

        dialogBinding.etChallengeDescription.doAfterTextChanged { editable ->
            val hasText = !editable.isNullOrBlank()
            dialogBinding.btnSave.isEnabled = hasText
            dialogBinding.btnSave.alpha = if (hasText) 1f else 0.45f
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnSave.setOnClickListener {
            onSave(dialogBinding.etChallengeDescription.text.toString())
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun showDeleteChallengeDialog(challenge: Challenge) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogDeleteChallengeBinding.inflate(layoutInflater)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(false)

        dialogBinding.tvChallengeToDelete.text = challenge.description
        dialogBinding.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnYes.setOnClickListener {
            challengeViewModel.deleteChallenge(challenge)
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}

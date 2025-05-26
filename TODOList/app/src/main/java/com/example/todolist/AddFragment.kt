package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todolist.databinding.FragmentAddBinding
import java.util.Date

class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TodoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAddBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSaveButton()
        setupCancelButton()
    }

    private fun setupSaveButton() {

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            if (title.isNotBlank()) {
                viewModel.addItem(
                    TodoItem(
                        id = viewModel.generateId(),
                        title = title,
                        description = description,
                        createdDate = Date(),
                        isChecked = false

                    )
                )
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCancelButton() {
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
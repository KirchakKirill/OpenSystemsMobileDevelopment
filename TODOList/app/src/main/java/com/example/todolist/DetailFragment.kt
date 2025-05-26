package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.databinding.FragmentDetailBinding
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TodoViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("DEBUG: Received itemId: ${args.itemId}")

        val item = viewModel.getItemById(args.itemId).also {
            println("Debug: Requested ID: ${args.itemId}")
            println("Debug: All items: ${viewModel.todoItems.value}")
        }

        if (item == null) {
            Toast.makeText(context, "Task not found!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }


        binding.apply {
            titleTextView.text = item.title ?: "No title"
            descriptionTextView.text = item.description ?: "No description"
            createdDateTextView.text = item.createdDate.formatToString() ?: "No date"
            completedCheckBox.isChecked = item.isChecked

        }
        binding.backButton.setOnClickListener {

            findNavController().popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun Date?.formatToString(pattern: String = "dd.MM.yyyy"): String {
    return this?.let {
        SimpleDateFormat(pattern, Locale.getDefault()).format(it)
    } ?: "Unknown date"
}
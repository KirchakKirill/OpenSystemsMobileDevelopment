package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.FragmentListBinding

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TodoViewModel by activityViewModels()
    private lateinit var adapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setupObservers()
    }

    private fun initViews() {

        adapter = TodoAdapter(
            onItemClick = { item ->

                navigateToDetail(item.id)
            },
            onCheckedChange = { item, isChecked ->
                viewModel.updateItem(item.copy(isChecked = isChecked))
            }
        )


        binding.recyclerView.apply {
            adapter = this@ListFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }


        binding.addButton.setOnClickListener {
            navigateToAdd()
        }
    }

    private fun setupObservers() {
        viewModel.todoItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)

            val total = items.size
            val completed = items.count { it.isChecked }
            binding.headerTextView.text = "Completed vs total: $completed - $total"

            binding.emptyStateTextView.visibility =
                if (items.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun navigateToDetail(itemId: Int) {
        println("DEBUG: Navigating to detail with ID: $itemId")
        val direction = ListFragmentDirections.actionListFragmentToDetailFragment(itemId)
        findNavController().navigate(direction)
    }

    private fun navigateToAdd() {
        findNavController().navigate(
            ListFragmentDirections.actionListFragmentToAddFragment()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.teaapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.teaapp.databinding.FragmentProfileBinding
import com.example.teaapp.model.User
import com.example.teaapp.service.LocalPrefsService
import com.example.teaapp.viewmodel.ProfileState
import com.example.teaapp.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private lateinit var sharedPrefsService: LocalPrefsService


    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPrefsService = LocalPrefsService(requireContext())
        val userId = sharedPrefsService.getString("USER_ID") ?: ""
        setupGenderDropdown()
        setupListeners()
        observeViewModel()

        // Fetch profile data (replace with actual user ID)
        viewModel.fetchProfile(userId)

        binding.myOrdersButton.setOnClickListener {
            val intent = Intent(requireContext(), MyOrderActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupGenderDropdown() {
        val genders = arrayOf("Male", "Female", "Other")
       // val adapter = ArrayAdapter(requireContext(), R.layout.list_item, genders)
        //binding.genderAutoCompleteTextView.setAdapter(adapter)
    }

    private fun setupListeners() {
        val userId = sharedPrefsService.getString("USER_ID") ?: ""
        binding.updateProfileButton.setOnClickListener {
            val user = User(
                id = userId, // Replace with actual user ID
                username = binding.emailEditText.text.toString(),
                email = binding.emailEditText.text.toString(),
                role = "Vendor", // Assuming role doesn't change
                firstName = binding.nameEditText.text.toString().split(" ").firstOrNull() ?: "",
                lastName = binding.nameEditText.text.toString().split(" ").drop(1).joinToString(" "),
                phoneNumber = binding.phoneEditText.text.toString(),
                gender = binding.genderAutoCompleteTextView.text.toString(),
                address = binding.dobEditText.text.toString()
            )
            viewModel.updateProfile(user)
        }
        binding.logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

    }
    private fun showLogoutConfirmationDialog() {
        // Create an AlertDialog to confirm logout
        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                // Clear shared preferences
                sharedPrefsService.clearAll()

                // Navigate to SigninActivity
                val intent = Intent(requireContext(), SigninActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

                // Ensure the user cannot go back to the profile fragment
                requireActivity().finish()
            }
            .setNegativeButton("No", null)
            .create()

        dialog.show()
    }

    private fun observeViewModel() {
        viewModel.profileState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileState.Loading -> showLoading()
                is ProfileState.Success -> {
                    hideLoading()
                    updateUI(state.user)
                }
                is ProfileState.Error -> {
                    hideLoading()
                    showError(state.message)
                }
            }
        }
    }

    private fun updateUI(user: User) {
        binding.nameTextView.text = "${user.firstName} ${user.lastName}"
        binding.nameEditText.setText("${user.firstName} ${user.lastName}")
        binding.emailEditText.setText(user.email)
        binding.phoneEditText.setText(user.phoneNumber ?: "")
        binding.genderAutoCompleteTextView.setText(user.gender ?: "", false)
      binding.dobEditText.setText(user.address)
    }

    private fun showLoading() {
        binding.loadingProgressBar.visibility = View.VISIBLE
        binding.updateProfileButton.isEnabled = false
    }

    private fun hideLoading() {
        binding.loadingProgressBar.visibility = View.GONE
        binding.updateProfileButton.isEnabled = true
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
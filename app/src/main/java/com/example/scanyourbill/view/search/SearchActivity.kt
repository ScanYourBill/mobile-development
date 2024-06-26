package com.example.scanyourbill.view.search

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.scanyourbill.R
import com.example.scanyourbill.databinding.ActivitySearchBinding
import com.example.scanyourbill.view.BaseActivity
import com.example.scanyourbill.view.ViewModelFactory
import com.example.scanyourbill.view.transaction.CategoryFragment

class SearchActivity : BaseActivity() {
    private val viewModel: SearchViewModel by viewModels<SearchViewModel>{
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setupAutoCompleteInputs()
        setupInputTypeListener()
        setupInputCategoryListener()

        binding.clearBtn.setOnClickListener {
            binding.notesEditText.setText("")
            binding.autoCompleteInputType.setText("", false)
            binding.autoCompleteInputCategory.setText("", false)
        }

        binding.searchBtn.setOnClickListener {
            val notes = binding.notesEditText.text.toString()
            val category = binding.autoCompleteInputCategory.text.toString()
            var type = binding.autoCompleteInputType.text.toString()

            if(type == "Expense"){
                type = "outcome"
            } else if (type =="Income") {
                type = "income"
            } else {
                type="all"
            }

            viewModel.search(notes, category, type)

            val fragment = SearchResultFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentSearchResult, fragment)
                .addToBackStack(null)
                .commit()
        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_search
    }

    private fun setupAutoCompleteInputs() {
        val inputType = binding.autoCompleteInputType
        val inputCategory = binding.autoCompleteInputCategory

        val activityTypes = resources.getStringArray(R.array.activity_types)
        val incomeCategories = resources.getStringArray(R.array.income_categories)
        val expenseCategories = resources.getStringArray(R.array.expense_categories)
        val combinedCategories = incomeCategories + expenseCategories

        val inputTypeAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, activityTypes)
        inputType.setAdapter(inputTypeAdapter)

        val combinedCategoryAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, combinedCategories)
        inputCategory.setAdapter(combinedCategoryAdapter)
    }

    private fun setupInputTypeListener() {
        val inputType = binding.autoCompleteInputType
        val inputCategory = binding.autoCompleteInputCategory
        val activityTypes = resources.getStringArray(R.array.activity_types)
        val incomeCategories = resources.getStringArray(R.array.income_categories)
        val expenseCategories = resources.getStringArray(R.array.expense_categories)

        inputType.setOnItemClickListener { _, _, position, _ ->
            val selectedType = activityTypes[position]
            val categoryAdapter = getCategoryAdapter(selectedType, incomeCategories, expenseCategories)
            inputCategory.setAdapter(categoryAdapter)
            inputCategory.setText("", false) // clear the text when type changes
        }
    }

    private fun setupInputCategoryListener() {
        val inputCategory = binding.autoCompleteInputCategory
        val incomeCategories = resources.getStringArray(R.array.income_categories)
        val expenseCategories = resources.getStringArray(R.array.expense_categories)

        inputCategory.setOnItemClickListener { parent, _, position, _ ->
            val selectedCategory = parent.getItemAtPosition(position).toString()
            Log.d("selectedCategory", selectedCategory)
            when (selectedCategory) {
                in incomeCategories -> binding.autoCompleteInputType.setText(getString(R.string.income_capital_i), false)
                in expenseCategories -> binding.autoCompleteInputType.setText(getString(R.string.expense_capital_e), false)
            }
        }
    }

    private fun getCategoryAdapter(selectedType: String, incomeCategories: Array<String>, expenseCategories: Array<String>): ArrayAdapter<String> {
        return if (selectedType == "Expense") {
            ArrayAdapter(this, android.R.layout.simple_list_item_1, expenseCategories)
        } else {
            ArrayAdapter(this, android.R.layout.simple_list_item_1, incomeCategories)
        }
    }
}

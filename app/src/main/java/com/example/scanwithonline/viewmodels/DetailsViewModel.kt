package com.example.scanwithonline.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanwithonline.data.network.Product
import com.example.scanwithonline.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

// The Success state now includes the calculated GI value and the narrative.
sealed interface ProductUiState {
    object Loading : ProductUiState
    data class Success(val product: Product, val calculatedGi: Int, val narrative: String) : ProductUiState
    data class Error(val message: String) : ProductUiState
}

class DetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState

    // This new state will remember if the language has been selected.
    private val _languageChosen = MutableStateFlow(false)
    val languageChosen: StateFlow<Boolean> = _languageChosen

    private var currentBarcode: String? = null

    fun fetchProductDetails(barcode: String, language: String) {
        // We only fetch data if it's a new barcode.
        if (barcode == currentBarcode && _uiState.value is ProductUiState.Success) {
            return
        }
        currentBarcode = barcode
        // When this function is called, we know the language has been chosen.
        _languageChosen.value = true

        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading
            try {
                val response = RetrofitInstance.api.getProductDetails(barcode)
                if (response.status == 1) {
                    val product = response.product
                    val calculatedGi = calculateGi(product)
                    val narrative = generateNarrative(product, language)
                    _uiState.value = ProductUiState.Success(product, calculatedGi, narrative)
                } else {
                    _uiState.value = ProductUiState.Error("Product not found.")
                }
            } catch (e: Exception) {
                _uiState.value = ProductUiState.Error("An error occurred: ${e.message}")
            }
        }
    }

    private fun generateNarrative(product: Product, language: String): String {
        return when (language) {
            "hi" -> "यह ${product.product_name ?: "उत्पाद"} के बारे में एक अस्थायी कथन है। भविष्य में, यह आपके प्रोफ़ाइल के आधार पर व्यक्तिगत जानकारी प्रदान करेगा।"
            "bn" -> "এটি ${product.product_name ?: "পণ্য"} সম্পর্কে একটি অস্থায়ী বিবরণ। ভবিষ্যতে, এটি আপনার প্রোফাইলের উপর ভিত্তি করে ব্যক্তিগতকৃত অন্তর্দৃষ্টি প্রদান করবে।"
            else -> "This is a temporary narrative about ${product.product_name ?: "the product"}. In the future, this will provide personalized insights based on your profile."
        }
    }

    private fun calculateGi(product: Product): Int {
        val carbs = product.nutriments?.carbohydrates_100g ?: 0.0
        val sugar = product.nutriments?.sugars_100g ?: 0.0
        val fiber = 0.0
        if (carbs <= 0) return 0
        return (((sugar) + ((carbs - fiber - sugar) * 0.5)) / carbs * 100).toInt()
    }
}

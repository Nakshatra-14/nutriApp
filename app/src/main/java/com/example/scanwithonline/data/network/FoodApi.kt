package com.example.scanwithonline.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object RetrofitInstance {
    private const val BASE_URL = "https://world.openfoodfacts.org/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiService::class.java)
    }
}

interface ApiService {
    @GET("api/v2/product/{barcode}")
    suspend fun getProductDetails(@Path("barcode") barcode: String): ProductResponse
}

// --- Updated Data Classes ---

data class ProductResponse(
    val status: Int,
    val code: String,
    val product: Product
)

data class Product(
    val product_name: String?,
    val quantity: String?,
    val nutrition_grades: String?,
    val nutriments: Nutriments?,
    val brands: String?,
    val ecoscore_grade: String?,
    val selected_images: SelectedImages?,
    val allergens_from_ingredients: String?,
    val nova_group: Int?, // Added for processing level
    val nutrient_levels: NutrientLevels? // Added for high/low nutrient warnings
)

data class Nutriments(
    val sugars_100g: Double?,
    val carbohydrates_100g: Double?,
    val fat_100g: Double?,
    val salt_100g: Double?,
    @Json(name = "energy-kcal_100g") val energyKcal100g: Double?,
    @Json(name = "saturated-fat_100g") val saturatedFat100g: Double?,
    val proteins_100g: Double?
)

// New data class for nutrient levels
data class NutrientLevels(
    val fat: String?,
    val salt: String?,
    val sugars: String?,
    @Json(name = "saturated-fat") val saturatedFat: String?
)

data class SelectedImages(
    val front: FrontImage?
)

data class FrontImage(
    val display: ImageUrl?
)

data class ImageUrl(
    val en: String?
)

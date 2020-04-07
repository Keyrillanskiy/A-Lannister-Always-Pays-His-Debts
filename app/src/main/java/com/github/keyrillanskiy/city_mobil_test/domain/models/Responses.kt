package com.github.keyrillanskiy.city_mobil_test.domain.models

import com.google.gson.annotations.SerializedName

/**
 * Модель информации о персонаже.
 */
data class CharacterInfoResponse(
    @SerializedName("name") val name: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("culture") val culture: String,
    @SerializedName("born") val born: String,
    @SerializedName("died") val died: String,
    @SerializedName("titles") val titles: List<String>,
    @SerializedName("aliases") val aliases: List<String>,
    @SerializedName("father") val father: String,
    @SerializedName("mother") val mother: String,
    @SerializedName("spouse") val spouse: String,
    @SerializedName("tvSeries") val tvSeries: List<String>,
    @SerializedName("playedBy") val playedBy: List<String>
)
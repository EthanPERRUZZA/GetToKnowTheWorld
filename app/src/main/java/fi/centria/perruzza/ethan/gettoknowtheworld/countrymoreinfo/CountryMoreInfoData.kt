package fi.centria.perruzza.ethan.gettoknowtheworld.countrymoreinfo

data class CountryMoreInfoData(
    val id: Int,
    val name: String,
    val iso3: String,
    val iso2: String,
    val phonecode: String,
    val capital: String,
    val currency: String,
    val native: String,
    val emoji: String,
    val emojiU: String
)

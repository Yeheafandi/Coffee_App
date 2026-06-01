package com.example.coffeeapp.ui.screens.barista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random




class BaristaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BaristaUiState())
    val uiState: StateFlow<BaristaUiState> = _uiState.asStateFlow()

    fun onInputChange(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun sendMessage() {
        val userText = _uiState.value.inputText.trim()
        if (userText.isEmpty()) return

        val updatedMessages = _uiState.value.messages.toMutableList().apply {
            add(ChatMessage(userText, true))
        }

        _uiState.update {
            it.copy(messages = updatedMessages, inputText = "", isAiThinking = true)
        }

        viewModelScope.launch {
            delay(1500)
            val aiResponse = generateUltraBaristaResponse(userText)

            val finalMessages = _uiState.value.messages.toMutableList().apply {
                add(ChatMessage(aiResponse, false))
            }

            _uiState.update {
                it.copy(messages = finalMessages, isAiThinking = false)
            }
        }
    }

    private fun generateUltraBaristaResponse(userInput: String): String {
        val input = userInput.lowercase()

        val explicitHotDrink = input.contains("مشروب ساخن") || input.contains("قهوة ساخن") ||
                input.contains("قهوه ساخن") || input.contains("بدي ساخن") || input.contains("اريد ساخن") ||
                input.contains("اعطني ساخن") || (input.contains("ساخن") && !input.contains("الجو"))

        val explicitColdDrink = input.contains("مشروب بارد") || input.contains("قهوة بارد") ||
                input.contains("قهوه بارد") || input.contains("ايس") || input.contains("iced") ||
                input.contains("بدي بارد") || input.contains("اريد بارد") || (input.contains("بارد") && !input.contains("الجو"))

        val weatherIsCold = input.contains("الجو بارد") || input.contains("الطقس بارد") ||
                input.contains("بردان") || input.contains("الشتاء") || input.contains("منخفضة") ||
                input.contains("منخفض") || input.contains("تحت الصفر")

        val weatherIsHot = input.contains("الجو حار") || input.contains("الجو شوب") ||
                input.contains("الطقس حار") || input.contains("شوبان") || input.contains("الصيف") ||
                input.contains("مرتفعة") || input.contains("مرتفع") || input.contains("حرارة عالية")

        // 4. فحص النوايا المصاحبة (الحالة النفسية والجسدية)
        val hasEnergy = input.contains("طاقة") || input.contains("تعب") || input.contains("نعسان") || input.contains("دراسة") || input.contains("تركيز")
        val hasSweet = input.contains("حلو") || input.contains("شوكولاتة") || input.contains("سكر") || input.contains("كراميل") || input.contains("شوكولا")

        return when {
            explicitHotDrink || (weatherIsCold && !explicitColdDrink) -> {
                getHotDrinkResponse(hasEnergy, hasSweet)
            }

            explicitColdDrink || (weatherIsHot && !explicitHotDrink) -> {
                getColdDrinkResponse(hasEnergy, hasSweet)
            }

            hasEnergy -> {
                "التحليل يشير إلى حاجتك الفورية لجرعة طاقة وتركيز! 🧠 أنصحك بـ 'Espresso' مزدوج مركز ليقضي على الخمول، أو كوب 'Americano' أسود كلاسيكي يدوم معك طويلاً."
            }

            hasSweet -> {
                "مزاجك يطلب السكريات والدلال الفاخر! 🥰 المشروب المثالي لك الآن هو 'Hot Chocolate' الفاخر المتوفر لدينا برغوته الغنية وحليبه الساخن لإرضاء رغبتك."
            }

            input.contains("مرحبا") || input.contains("السلام") || input.contains("هلا") || input.contains("هاي") -> {
                "أهلاً بك في ركن التوصيات الذكي! 👋 أخبرني: هل درجات الحرارة مرتفعة عندك وتبحث عن انتعاش مثلج، أم منخفضة وتحتاج دفء القهوة الساخنة؟"
            }

            else -> {
                val defaultOptions = listOf(
                    "خياراتنا متميزة وتناسب كل الأجواء! لتحديد طلبك بدقة، هل تفضل كوب قهوة ساخناً يعدل المزاج، أم مشروباً مثلجاً ومنعشاً؟ ☕❄️",
                    "ما رأيك بتجربة الـ 'Cappuccino' الكلاسيكي اليوم؟ فهو مشروب متوازن تماماً ومناسب لجميع الأوقات والأجواء."
                )
                defaultOptions[Random.nextInt(defaultOptions.size)]
            }
        }
    }

    private fun getHotDrinkResponse(hasEnergy: Boolean, hasSweet: Boolean): String {
        return when {
            hasEnergy -> "جرعة كافيين ساخنة لرفع معدلات التركيز فوراً! ☕🔥 أنصحك بكوب 'Americano' ساخن ونقي، أو 'Espresso' مزدوج دافئ يرفع الأدرينالين ويدفئ مزاجك."
            hasSweet -> "تريد دفء التحلية الفاخرة؟ 🍫✨ خيارك المثالي هو 'Hot Chocolate' الساخن، المصنوع من الكاكاو الغني ورغوة الحليب الكثيفة الممتازة للتدفئة ورفع السعادة."
            else -> "كوب قهوة ساخن دافئ يعدل الراس؟ ☕ نقترح عليك تجربة 'Cappuccino' إيطالي تقليدي ساخن برغوة مخملية غنية، أو كوب دافئ ومريح من 'Apple Cinnamon Tea' بالأعشاب والقرفة."
        }
    }

    private fun getColdDrinkResponse(hasEnergy: Boolean, hasSweet: Boolean): String {
        return when {
            hasEnergy -> "جرعة طاقة منعشة ومثلجة تتحدى خمول الطقس! ⚡🧊 التوصية الذهبية لك هي 'Iced Latte' مثلج بجرعة إسبريسو مضاعفة ليعطيك نشاطاً حاداً وانتعاشاً هائلاً."
            hasSweet -> "مزاجك يطلب انتعاشاً سكرياً يبرد القلب ويرطب عليك! 🍓❄️ أنصحك بطلب 'Queen Berry Herbal Tea' المثلج، المليء بنكهات التوت البري الطبيعية المنعشة."
            else -> "تبحث عن شيء يبرد عليك؟ 🥤 خيارك الأفضل لترطيب يومك هو كوب كريمي مثلج من الـ 'Iced Latte'، أو 'Iced Americano' كلاسيكي مثلج ومنعش!"
        }
    }
}
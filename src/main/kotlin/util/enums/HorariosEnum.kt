package util.enums

enum class HorariosMatutinoEnum(private val horario: String) {
    SETE_HORAS("07:00"), SETE_E_MEIA("07:30"),
    OITO_HORAS("08:00"), OITO_E_MEIA("08:30"),
    NOVE_HORAS("09:00"), NOVE_E_MEIA("09:30"),
    DEZ_HORAS("10:00"), DEZ_E_MEIA("10:30"),
    ONZE_HORAS("11:00"), ONZE_E_MEIA("11:30");
}

enum class HorariosVespertinoEnum(private val horario: String) {
    DOZE_HORAS("12:00"), DOZE_E_MEIA("12:30"),
    TREZE_HORAS("13:00"), TREZE_E_MEIA("13:30"),
    QUATORZE_HORAS("14:00"), QUATORZE_E_MEIA("14:30"),
    QUINZE_HORAS("15:00"), QUINZE_E_MEIA("15:30"),
    DEZESSEIS_HORAS("16:00"), DEZESSEIS_E_MEIA("16:30"),
    DEZESSETE_HORAS("17:00"), DEZESSETE_E_MEIA("17:30");
}

enum class HorariosNoturnoEnum(private val horario: String) {
    DEZOITO_HORAS("18:00"), DEZOITO_E_MEIA("18:30"),
    DEZENOVE_HORAS("19:00"), DEZENOVE_E_MEIA("19:30"),
    VINTE_HORAS("20:00"), VINTE_E_MEIA("20:30"),
    VINTE_E_UMA_HORAS("21:00"), VINTE_E_UMA_E_MEIA("21:30"),
    VINTE_E_DUAS_HORAS("22:00"), VINTE_E_DUAS_E_MEIA("22:30"),
    VINTE_E_TRES_HORAS("23:00"), VINTE_E_TRES_E_MEIA("23:30");
}
package com.shivam_raj.noteapp.ui.theme

import androidx.compose.runtime.Immutable
import com.shivam_raj.noteapp.database.Priority

@Immutable
data class CustomColorWithTheme(
    val lightColorTheme: ColorFamily,
    val darkColorTheme: ColorFamily,
)

/**
 * List of custom colors for both light and dark theme which can be used for note's custom background color.
 */
val NOTE_BACKGROUND_COLOR_LIST = listOf(
    CustomColorWithTheme(
        lightColorTheme = extendedLight.customColor1,
        darkColorTheme = extendedDark.customColor1
    ),
    CustomColorWithTheme(
        lightColorTheme = extendedLight.customColor2,
        darkColorTheme = extendedDark.customColor2
    ),
    CustomColorWithTheme(
        lightColorTheme = extendedLight.customColor3,
        darkColorTheme = extendedDark.customColor3
    ),
    CustomColorWithTheme(
        lightColorTheme = extendedLight.customColor4,
        darkColorTheme = extendedDark.customColor4
    ),
    CustomColorWithTheme(
        lightColorTheme = extendedLight.customColor5,
        darkColorTheme = extendedDark.customColor5
    ),
    CustomColorWithTheme(
        lightColorTheme = extendedLight.customColor6,
        darkColorTheme = extendedDark.customColor6
    ),
    CustomColorWithTheme(
        lightColorTheme = extendedLight.customColor7,
        darkColorTheme = extendedDark.customColor7
    ),
    CustomColorWithTheme(
        lightColorTheme = extendedLight.customColor8,
        darkColorTheme = extendedDark.customColor8
    ),
    CustomColorWithTheme(
        lightColorTheme = extendedLight.customColor9,
        darkColorTheme = extendedDark.customColor9
    ),
    CustomColorWithTheme(
        lightColorTheme = extendedLight.customColor10,
        darkColorTheme = extendedDark.customColor10
    )
)

/**
 * [ColorFamily] provider for the note background. It is calculated on the basis of both [notePriority] and [noteCustomColorIndex].
 *  - If [noteCustomColorIndex] is null, the background color will be based on the [notePriority]
 *  - Else the background color will be based on the [noteCustomColorIndex]
 *          - If noteCustomColorIndex is out of the bound of NOTE_BACKGROUND_COLOR_LIST, than the color will be based on the notePriority.
 * @param isDarkTheme If true, note's background ColorFamily will be for darkTheme else it will be for lightTheme
 * @param notePriority [Priority] of the note.
 * @param noteCustomColorIndex Color Index in the list of [NOTE_BACKGROUND_COLOR_LIST]
 * @return Calculated [ColorFamily] for the note's background.
 */
fun colorProviderForNoteBackground(
    isDarkTheme: Boolean,
    notePriority: Priority,
    noteCustomColorIndex: Int?
): ColorFamily {
    return if (noteCustomColorIndex == null || noteCustomColorIndex >= NOTE_BACKGROUND_COLOR_LIST.size) {
        when (notePriority) {
            Priority.HIGH -> if (isDarkTheme) extendedDark.highPriorityNoteColor else extendedLight.highPriorityNoteColor
            Priority.MEDIUM -> if (isDarkTheme) extendedDark.mediumPriorityNoteColor else extendedLight.mediumPriorityNoteColor
            Priority.LOW -> if (isDarkTheme) extendedDark.lowPriorityNoteColor else extendedLight.lowPriorityNoteColor
            Priority.VERY_LOW -> if (isDarkTheme) extendedDark.veryLowPriorityNoteColor else extendedLight.veryLowPriorityNoteColor
            Priority.NONE -> if (!isDarkTheme){
                ColorFamily(backgroundLightMediumContrast, onBackgroundLightMediumContrast, surfaceVariantLightMediumContrast, onSurfaceVariantLightMediumContrast)
            } else{
                ColorFamily(backgroundDarkMediumContrast, onBackgroundDarkMediumContrast, surfaceVariantDarkMediumContrast, onSurfaceVariantDarkMediumContrast)
            }
        }
    } else {
        if (isDarkTheme) NOTE_BACKGROUND_COLOR_LIST[noteCustomColorIndex].darkColorTheme else NOTE_BACKGROUND_COLOR_LIST[noteCustomColorIndex].lightColorTheme
    }
}
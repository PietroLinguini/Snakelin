package components.menu

import Store
import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.useState
import store.reducers.ChangeSettingAction
import store.reducers.Settings

external interface SettingSetterProps : Props {
    var settingId: Settings
    var displayName: String
    var options: List<String>
}

abstract class SettingEditButtonProps : MenuButtonProps {
    var right: Boolean = false
    var left: Boolean = false
}

val SettingEditButton = FC<SettingEditButtonProps> { props ->
    div {
        css {
            fontFamily = FontFamily.serif
        }
        MenuButton {
            text = if (props.left) "\u25C0" else if (props.right) "\u25B6" else "" // ◀ and ▶
            onClick = props.onClick
        }
    }
}

val SettingSetter = FC<SettingSetterProps> { props ->
    val store = Store.appStore
    var state by useState(store.state)

    val settingIndex =
        state.settingsIndices[props.settingId] ?: error("No index is documented for setting '${props.settingId}'")

    store.subscribe {
        state = store.state
    }

    fun changeSetting(indexAdjustment: Int) {
        val newIndex = settingIndex + indexAdjustment
        store.dispatch(ChangeSettingAction(props.settingId, newIndex))
    }

    div {
        css { color = Color("#fff") }
        p {
            css {
                fontSize = 2.rem
                textAlign = TextAlign.center
            }
            +props.displayName
        }
        section {
            css {
                width = 85.pct
                display = Display.grid
                gridTemplateColumns = "2.4rem 1fr 2.4rem".unsafeCast<GridTemplateColumns>()
                margin = Auto.auto
            }
            SettingEditButton {
                left = true
                onClick = {
                    changeSetting(-1)
                }
            }
            p {
                css {
                    border = Border((0.1).rem, LineStyle.solid, Color("#fff"))
                    fontSize = (1.8).rem
                    textAlign = TextAlign.center
                }
                +props.options[settingIndex]
            }
            SettingEditButton {
                right = true
                onClick = {
                    changeSetting(1)
                }
            }
        }
    }
}

val SettingsMenu = FC<Props> {
    div {
        css {
            width = 30.rem
            height = 100.pct
            border = Border((0.8).rem, LineStyle.double, Color("#fff"))
            display = Display.flex
            flexDirection = FlexDirection.column

        }
        p {
            css {
                textAlign = TextAlign.center
                fontSize = (2.4).rem
                borderBottom = Border((0.27).rem, LineStyle.solid, Color("#fff"))
            }
            +"Settings"
        }
        section {
            css {
                display = Display.flex
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.spaceAround
                alignContent = AlignContent.center
                flex = Flex(number(1.0), number(1.0), Auto.auto)
            }
            SettingSetter {
                settingId = Settings.TEMPO
                displayName = "Tempo"
                options = listOf("Slow", "Moderate", "Swifty", "Fast", "SPEED")
            }
            SettingSetter {
                settingId = Settings.SIZE
                displayName = "Size"
                options = listOf("Small", "Medium", "Large", "Biggest")

            }
        }

    }
}
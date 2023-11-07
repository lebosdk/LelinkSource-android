package com.hpplay.sdk.source.test.constants;

import android.os.Build;
import android.view.KeyEvent;

import java.util.HashMap;

public interface KeyEventName {
    String KEY_NAME_UNKNOWN = "UNKNOWN";
    String kEY_NAME_SOFT_LEFT = "SOFT_LEFT";
    String kEY_NAME_SOFT_RIGHT = "SOFT_RIGHT";
    String KEY_NAME_HOME = "HOME";
    String KEY_NAME_BACK = "BACK";
    String KEY_NAME_CALL = "CALL";
    String KEY_NAME_END_CALL = "END_CALL";
    String KEY_NAME_0 = "0";
    String KEY_NAME_1 = "1";
    String KEY_NAME_2 = "2";
    String KEY_NAME_3 = "3";
    String KEY_NAME_4 = "4";
    String KEY_NAME_5 = "5";
    String KEY_NAME_6 = "6";
    String KEY_NAME_7 = "7";
    String KEY_NAME_8 = "8";
    String KEY_NAME_9 = "9";
    String KEY_NAME_STAR = "STAR";
    String KEY_NAME_POUND = "POUND";
    String KEY_NAME_DPAD_DOWN = "DPAD_DOWN";
    String KEY_NAME_DPAD_UP = "DPAD_UP";
    String KEY_NAME_DPAD_LEFT = "DPAD_LEFT";
    String KEY_NAME_DPAD_RIGHT = "DPAD_RIGHT";
    String KEY_NAME_DPAD_CENTER = "DPAD_CENTER";
    String KEY_NAME_VOLUME_UP = "VOLUME_UP";
    String KEY_NAME_VOLUME_DOWN = "VOLUME_DOWN";
    String KEY_NAME_POWER = "POWER";
    String KEY_NAME_CAMERA = "CAMERA";
    String KEY_NAME_CLEAR = "CLEAR";
    String KEY_NAME_A = "A";
    String KEY_NAME_B = "B";
    String KEY_NAME_C = "C";
    String KEY_NAME_D = "D";
    String KEY_NAME_E = "E";
    String KEY_NAME_F = "F";
    String KEY_NAME_G = "G";
    String KEY_NAME_H = "H";
    String KEY_NAME_I = "I";
    String KEY_NAME_J = "J";
    String KEY_NAME_K = "K";
    String KEY_NAME_L = "L";
    String KEY_NAME_M = "M";
    String KEY_NAME_N = "N";
    String KEY_NAME_O = "O";
    String KEY_NAME_P = "P";
    String KEY_NAME_Q = "Q";
    String KEY_NAME_R = "R";
    String KEY_NAME_S = "S";
    String KEY_NAME_T = "T";
    String KEY_NAME_U = "U";
    String KEY_NAME_V = "V";
    String KEY_NAME_W = "W";
    String KEY_NAME_X = "X";
    String KEY_NAME_Y = "Y";
    String KEY_NAME_Z = "Z";
    String KEY_NAME_COMMA = "COMMA";
    String KEY_NAME_PERIOD = "PERIOD";
    String KEY_NAME_ALT_LEFT = "ALT_LEFT";
    String KEY_NAME_ALT_RIGHT = "ALT_RIGHT";
    String KEY_NAME_SHIFT_LEFT = "SHIFT_LEFT";
    String KEY_NAME_SHIFT_RIGHT = "SHIFT_RIGHT";
    String KEY_NAME_TAB = "TAB";
    String KEY_NAME_SPACE = "SPACE";
    String KEY_NAME_SYM = "SYM";
    String KEY_NAME_EXPLORER = "EXPLORER";
    String KEY_NAME_ENVELOPE = "ENVELOPE";
    String KEY_NAME_ENTER = "ENTER";
    String KEY_NAME_DEL = "DEL";
    String KEY_NAME_GRAVE = "GRAVE";
    String KEY_NAME_MINUS = "MINUS";
    String KEY_NAME_EQUALS = "EQUALS";
    String KEY_NAME_LEFT_BRACKET = "LEFT_BRACKET";
    String KEY_NAME_RIGHT_BRACKET = "RIGHT_BRACKET";
    String KEY_NAME_BACKSLASH = "BACKSLASH";
    String KEY_NAME_SEMICOLON = "SEMICOLON";
    String KEY_NAME_APOSTROPHE = "APOSTROPHE";
    String KEY_NAME_SLASH = "SLASH";
    String KEY_NAME_AT = "AT";
    String KEY_NAME_NUM = "NUM";
    String KEY_NAME_HEADSETHOOK = "HEADSETHOOK";
    String KEY_NAME_FOCUS = "FOCUS";
    String KEY_NAME_PLUS = "PLUS";
    String KEY_NAME_MENU = "MENU";
    String KEY_NAME_NOTIFICATION = "NOTIFICATION";
    String KEY_NAME_SEARCH = "SEARCH";
    String KEY_NAME_MEDIA_PLAY_PAUSE = "MEDIA_PLAY_PAUSE";
    String KEY_NAME_MEDIA_STOP = "MEDIA_STOP";
    String KEY_NAME_MEDIA_NEXT = "MEDIA_NEXT";
    String KEY_NAME_MEDIA_PREVIOUS = "MEDIA_PREVIOUS";
    String KEY_NAME_MEDIA_REWIND = "MEDIA_REWIND";
    String KEY_NAME_MEDIA_FAST_FORWARD = "MEDIA_FAST_FORWARD";
    String KEY_NAME_MUTE = "MUTE";
    String KEY_NAME_PAGE_UP = "PAGE_UP";
    String KEY_NAME_PAGE_DOWN = "PAGE_DOWN";
    String KEY_NAME_PICTSYMBOLS = "PICTSYMBOLS";   // switch symbol-sets (Emoji,Kao-moji)
    String KEY_NAME_SWITCH_CHARSET = "SWITCH_CHARSET";   // switch char-sets (Kanji,Katakana)
    String KEY_NAME_BUTTON_A = "BUTTON_A";
    String KEY_NAME_BUTTON_B = "BUTTON_B";
    String KEY_NAME_BUTTON_C = "BUTTON_C";
    String KEY_NAME_BUTTON_X = "BUTTON_X";
    String KEY_NAME_BUTTON_Y = "BUTTON_Y";
    String KEY_NAME_BUTTON_Z = "BUTTON_Z";
    String KEY_NAME_BUTTON_L1 = "BUTTON_L1";
    String KEY_NAME_BUTTON_R1 = "BUTTON_R1";
    String KEY_NAME_BUTTON_L2 = "BUTTON_L2";
    String KEY_NAME_BUTTON_R2 = "BUTTON_R2";
    String KEY_NAME_BUTTON_THUMBL = "BUTTON_THUMBL";
    String KEY_NAME_BUTTON_THUMBR = "BUTTON_THUMBR";
    String KEY_NAME_BUTTON_START = "BUTTON_START";
    String KEY_NAME_BUTTON_SELECT = "BUTTON_SELECT";
    String KEY_NAME_BUTTON_MODE = "BUTTON_MODE";
    String KEY_NAME_ESCAPE = "ESCAPE";
    String KEY_NAME_FORWARD_DEL = "FORWARD_DEL";
    String KEY_NAME_CTRL_LEFT = "CTRL_LEFT";
    String KEY_NAME_CTRL_RIGHT = "CTRL_RIGHT";
    String KEY_NAME_CAPS_LOCK = "CAPS_LOCK";
    String KEY_NAME_SCROLL_LOCK = "SCROLL_LOCK";
    String KEY_NAME_META_LEFT = "META_LEFT";
    String KEY_NAME_META_RIGHT = "META_RIGHT";
    String KEY_NAME_FUNCTION = "FUNCTION";
    String KEY_NAME_SYSRQ = "SYSRQ";
    String KEY_NAME_BREAK = "BREAK";
    String KEY_NAME_MOVE_HOME = "MOVE_HOME";
    String KEY_NAME_MOVE_END = "_MOVE_END";
    String KEY_NAME_INSERT = "INSERT";
    String KEY_NAME_FORWARD = "FORWARD";
    String KEY_NAME_MEDIA_PLAY = "MEDIA_PLAY";
    String KEY_NAME_MEDIA_PAUSE = "MEDIA_PAUSE";
    String KEY_NAME_MEDIA_CLOSE = "MEDIA_CLOSE";
    String KEY_NAME_MEDIA_EJECT = "MEDIA_EJECT";
    String KEY_NAME_MEDIA_RECORD = "MEDIA_RECORD";
    String KEY_NAME_F1 = "F1";
    String KEY_NAME_F2 = "F2";
    String KEY_NAME_F3 = "F3";
    String KEY_NAME_F4 = "F4";
    String KEY_NAME_F5 = "F5";
    String KEY_NAME_F6 = "F6";
    String KEY_NAME_F7 = "F7";
    String KEY_NAME_F8 = "F8";
    String KEY_NAME_F9 = "F9";
    String KEY_NAME_F10 = "F10";
    String KEY_NAME_F11 = "F11";
    String KEY_NAME_F12 = "F12";
    String KEY_NAME_NUM_LOCK = "NUM_LOCK";
    String KEY_NAME_NUMPAD_0 = "NUMPAD_0";
    String KEY_NAME_NUMPAD_1 = "NUMPAD_1";
    String KEY_NAME_NUMPAD_2 = "NUMPAD_2";
    String KEY_NAME_NUMPAD_3 = "NUMPAD_3";
    String KEY_NAME_NUMPAD_4 = "NUMPAD_4";
    String KEY_NAME_NUMPAD_5 = "NUMPAD_5";
    String KEY_NAME_NUMPAD_6 = "NUMPAD_6";
    String KEY_NAME_NUMPAD_7 = "NUMPAD_7";
    /**
     * Key code constant: Numeric keypad '8' key.
     */
    String KEY_NAME_NUMPAD_8 = "NUMPAD_8";
    /**
     * Key code constant: Numeric keypad '9' key.
     */
    String KEY_NAME_NUMPAD_9 = "NUMPAD_9";
    /**
     * Key code constant: Numeric keypad '/' key (for division).
     */
    String KEY_NAME_NUMPAD_DIVIDE = "NUMPAD_DIVIDE";
    /**
     * Key code constant: Numeric keypad '*' key (for multiplication).
     */
    String KEY_NAME_NUMPAD_MULTIPLY = "NUMPAD_MULTIPLY";
    /**
     * Key code constant: Numeric keypad '-' key (for subtraction).
     */
    String KEY_NAME_NUMPAD_SUBTRACT = "NUMPAD_SUBTRACT";
    /**
     * Key code constant: Numeric keypad '+' key (for addition).
     */
    String KEY_NAME_NUMPAD_ADD = "NUMPAD_ADD";
    /**
     * Key code constant: Numeric keypad '.' key (for decimals or digit grouping).
     */
    String KEY_NAME_NUMPAD_DOT = "NUMPAD_DOT";
    /**
     * Key code constant: Numeric keypad ',' key (for decimals or digit grouping).
     */
    String KEY_NAME_NUMPAD_COMMA = "NUMPAD_COMMA";
    /**
     * Key code constant: Numeric keypad Enter key.
     */
    String KEY_NAME_NUMPAD_ENTER = "NUMPAD_ENTER";
    /**
     * Key code constant: Numeric keypad '=' key.
     */
    String KEY_NAME_NUMPAD_EQUALS = "NUMPAD_EQUALS";
    /**
     * Key code constant: Numeric keypad '(' key.
     */
    String KEY_NAME_NUMPAD_LEFT_PAREN = "NUMPAD_LEFT_PAREN";
    /**
     * Key code constant: Numeric keypad ')' key.
     */
    String KEY_NAME_NUMPAD_RIGHT_PAREN = "NUMPAD_RIGHT_PAREN";
    /**
     * Key code constant: Volume Mute key.
     * Mutes the speaker, unlike {@link # KEY_NAME_MUTE}.
     * This key should normally be implemented as a toggle such that the first press
     * mutes the speaker and the second press restores the original volume.
     */
    String KEY_NAME_VOLUME_MUTE = "VOLUME_MUTE";
    /**
     * Key code constant: Info key.
     * Common on TV remotes to show additional information related to what is
     * currently being viewed.
     */
    String KEY_NAME_INFO = "INFO";
    /**
     * Key code constant: Channel up key.
     * On TV remotes, increments the television channel.
     */
    String KEY_NAME_CHANNEL_UP = "CHANNEL_UP";
    /**
     * Key code constant: Channel down key.
     * On TV remotes, decrements the television channel.
     */
    String KEY_NAME_CHANNEL_DOWN = "CHANNEL_DOWN";
    /**
     * Key code constant: Zoom in key.
     */
    String KEY_NAME_ZOOM_IN = "ZOOM_IN";
    /**
     * Key code constant: Zoom out key.
     */
    String KEY_NAME_ZOOM_OUT = "ZOOM_OUT";
    /**
     * Key code constant: TV key.
     * On TV remotes, switches to viewing live TV.
     */
    String KEY_NAME_TV = "TV";
    /**
     * Key code constant: Window key.
     * On TV remotes, toggles picture-in-picture mode or other windowing functions.
     * On Android Wear devices, triggers a display offset.
     */
    String KEY_NAME_WINDOW = "WINDOW";
    /**
     * Key code constant: Guide key.
     * On TV remotes, shows a programming guide.
     */
    String KEY_NAME_GUIDE = "GUIDE";
    /**
     * Key code constant: DVR key.
     * On some TV remotes, switches to a DVR mode for recorded shows.
     */
    String KEY_NAME_DVR = "DVR";
    /**
     * Key code constant: Bookmark key.
     * On some TV remotes, bookmarks content or web pages.
     */
    String KEY_NAME_BOOKMARK = "BOOKMARK";
    /**
     * Key code constant: Toggle captions key.
     * Switches the mode for closed-captioning text, for example during television shows.
     */
    String KEY_NAME_CAPTIONS = "CAPTIONS";
    /**
     * Key code constant: Settings key.
     * Starts the system settings activity.
     */
    String KEY_NAME_SETTINGS = "SETTINGS";
    /**
     * Key code constant: TV power key.
     * On TV remotes, toggles the power on a television screen.
     */
    String KEY_NAME_TV_POWER = "TV_POWER";
    /**
     * Key code constant: TV input key.
     * On TV remotes, switches the input on a television screen.
     */
    String KEY_NAME_TV_INPUT = "TV_INPUT";
    /**
     * Key code constant: Set-top-box power key.
     * On TV remotes, toggles the power on an external Set-top-box.
     */
    String KEY_NAME_STB_POWER = "STB_POWER";
    /**
     * Key code constant: Set-top-box input key.
     * On TV remotes, switches the input mode on an external Set-top-box.
     */
    String KEY_NAME_STB_INPUT = "STB_INPUT";
    /**
     * Key code constant: A/V Receiver power key.
     * On TV remotes, toggles the power on an external A/V Receiver.
     */
    String KEY_NAME_AVR_POWER = "AVR_POWER";
    /**
     * Key code constant: A/V Receiver input key.
     * On TV remotes, switches the input mode on an external A/V Receiver.
     */
    String KEY_NAME_AVR_INPUT = "AVR_INPUT";
    /**
     * Key code constant: Red "programmable" key.
     * On TV remotes, acts as a contextual/programmable key.
     */
    String KEY_NAME_PROG_RED = "PROG_RED";
    /**
     * Key code constant: Green "programmable" key.
     * On TV remotes, actsas a contextual/programmable key.
     */
    String KEY_NAME_PROG_GREEN = "PROG_GREEN";
    /**
     * Key code constant: Yellow "programmable" key.
     * On TV remotes, acts as a contextual/programmable key.
     */
    String KEY_NAME_PROG_YELLOW = "PROG_YELLOW";
    /**
     * Key code constant: Blue "programmable" key.
     * On TV remotes, acts as a contextual/programmable key.
     */
    String KEY_NAME_PROG_BLUE = "PROG_BLUE";
    /**
     * Key code constant: App switch key.
     * Should bring up the application switcher dialog.
     */
    String KEY_NAME_APP_SWITCH = "APP_SWITCH";
    /**
     * Key code constant: Generic Game Pad Button #1.
     */
    String KEY_NAME_BUTTON_1 = "BUTTON_1";
    /**
     * Key code constant: Generic Game Pad Button #2.
     */
    String KEY_NAME_BUTTON_2 = "BUTTON_2";
    /**
     * Key code constant: Generic Game Pad Button #3.
     */
    String KEY_NAME_BUTTON_3 = "BUTTON_3";
    /**
     * Key code constant: Generic Game Pad Button #4.
     */
    String KEY_NAME_BUTTON_4 = "BUTTON_4";
    /**
     * Key code constant: Generic Game Pad Button #5.
     */
    String KEY_NAME_BUTTON_5 = "BUTTON_5";
    /**
     * Key code constant: Generic Game Pad Button #6.
     */
    String KEY_NAME_BUTTON_6 = "BUTTON_6";
    /**
     * Key code constant: Generic Game Pad Button #7.
     */
    String KEY_NAME_BUTTON_7 = "BUTTON_7";
    /**
     * Key code constant: Generic Game Pad Button #8.
     */
    String KEY_NAME_BUTTON_8 = "BUTTON_8";
    /**
     * Key code constant: Generic Game Pad Button #9.
     */
    String KEY_NAME_BUTTON_9 = "BUTTON_9";
    /**
     * Key code constant: Generic Game Pad Button #10.
     */
    String KEY_NAME_BUTTON_10 = "BUTTON_10";
    /**
     * Key code constant: Generic Game Pad Button #11.
     */
    String KEY_NAME_BUTTON_11 = "BUTTON_11";
    /**
     * Key code constant: Generic Game Pad Button #12.
     */
    String KEY_NAME_BUTTON_12 = "BUTTON_12";
    /**
     * Key code constant: Generic Game Pad Button #13.
     */
    String KEY_NAME_BUTTON_13 = "BUTTON_13";
    /**
     * Key code constant: Generic Game Pad Button #14.
     */
    String KEY_NAME_BUTTON_14 = "BUTTON_14";
    /**
     * Key code constant: Generic Game Pad Button #15.
     */
    String KEY_NAME_BUTTON_15 = "BUTTON_15";
    /**
     * Key code constant: Generic Game Pad Button #16.
     */
    String KEY_NAME_BUTTON_16 = "BUTTON_16";
    /**
     * Key code constant: Language Switch key.
     * Toggles the current input language such as switching between English and Japanese on
     * a QWERTY keyboard.  On some devices, the same function may be performed by
     * pressing Shift+Spacebar.
     */
    String KEY_NAME_LANGUAGE_SWITCH = "LANGUAGE_SWITCH";
    /**
     * Key code constant: Manner Mode key.
     * Toggles silent or vibrate mode on and off to make the device behave more politely
     * in certain settings such as on a crowded train.  On some devices, the key may only
     * operate when long-pressed.
     */
    String KEY_NAME_MANNER_MODE = "MANNER_MODE";
    /**
     * Key code constant: 3D Mode key.
     * Toggles the display between 2D and 3D mode.
     */
    String KEY_NAME_3D_MODE = "3D_MODE";
    /**
     * Key code constant: Contacts special function key.
     * Used to launch an address book application.
     */
    String KEY_NAME_CONTACTS = "CONTACTS";
    /**
     * Key code constant: Calendar special function key.
     * Used to launch a calendar application.
     */
    String KEY_NAME_CALENDAR = "CALENDAR";
    /**
     * Key code constant: Music special function key.
     * Used to launch a music player application.
     */
    String KEY_NAME_MUSIC = "MUSIC";
    /**
     * Key code constant: Calculator special function key.
     * Used to launch a calculator application.
     */
    String KEY_NAME_CALCULATOR = "CALCULATOR";
    /**
     * Key code constant: Japanese full-width / half-width key.
     */
    String KEY_NAME_ZENKAKU_HANKAKU = "ZENKAKU_HANKAKU";
    /**
     * Key code constant: Japanese alphanumeric key.
     */
    String KEY_NAME_EISU = "EISU";
    /**
     * Key code constant: Japanese non-conversion key.
     */
    String KEY_NAME_MUHENKAN = "MUHENKAN";
    /**
     * Key code constant: Japanese conversion key.
     */
    String KEY_NAME_HENKAN = "HENKAN";
    /**
     * Key code constant: Japanese katakana / hiragana key.
     */
    String KEY_NAME_KATAKANA_HIRAGANA = "KATAKANA_HIRAGANA";
    /**
     * Key code constant: Japanese Yen key.
     */
    String KEY_NAME_YEN = "YEN";
    /**
     * Key code constant: Japanese Ro key.
     */
    String KEY_NAME_RO = "RO";
    /**
     * Key code constant: Japanese kana key.
     */
    String KEY_NAME_KANA = "KANA";
    /**
     * Key code constant: Assist key.
     * Launches the global assist activity.  Not delivered to applications.
     */
    String KEY_NAME_ASSIST = "ASSIST";
    /**
     * Key code constant: Brightness Down key.
     * Adjusts the screen brightness down.
     */
    String KEY_NAME_BRIGHTNESS_DOWN = "BRIGHTNESS_DOWN";
    /**
     * Key code constant: Brightness Up key.
     * Adjusts the screen brightness up.
     */
    String KEY_NAME_BRIGHTNESS_UP = "BRIGHTNESS_UP";
    /**
     * Key code constant: Audio Track key.
     * Switches the audio tracks.
     */
    String KEY_NAME_MEDIA_AUDIO_TRACK = "MEDIA_AUDIO_TRACK";
    /**
     * Key code constant: Sleep key.
     * Puts the device to sleep.  Behaves somewhat like {@link # KEY_NAME_POWER} but it
     * has no effect if the device is already asleep.
     */
    String KEY_NAME_SLEEP = "SLEEP";
    /**
     * Key code constant: Wakeup key.
     * Wakes up the device.  Behaves somewhat like {@link # KEY_NAME_POWER} but it
     * has no effect if the device is already awake.
     */
    String KEY_NAME_WAKEUP = "WAKEUP";
    /**
     * Key code constant: Pairing key.
     * Initiates peripheral pairing mode. Useful for pairing remote control
     * devices or game controllers, especially if no other input mode is
     * available.
     */
    String KEY_NAME_PAIRING = "PAIRING";
    /**
     * Key code constant: Media Top Menu key.
     * Goes to the top of media menu.
     */
    String KEY_NAME_MEDIA_TOP_MENU = "MEDIA_TOP_MENU";
    /**
     * Key code constant: '11' key.
     */
    String KEY_NAME_11 = "11";
    /**
     * Key code constant: '12' key.
     */
    String KEY_NAME_12 = "12";
    /**
     * Key code constant: Last Channel key.
     * Goes to the last viewed channel.
     */
    String KEY_NAME_LAST_CHANNEL = "LAST_CHANNEL";
    /**
     * Key code constant: TV data service key.
     * Displays data services like weather, sports.
     */
    String KEY_NAME_TV_DATA_SERVICE = "TV_DATA_SERVICE";
    /**
     * Key code constant: Voice Assist key.
     * Launches the global voice assist activity. Not delivered to applications.
     */
    String KEY_NAME_VOICE_ASSIST = "VOICE_ASSIST";
    /**
     * Key code constant: Radio key.
     * Toggles TV service / Radio service.
     */
    String KEY_NAME_TV_RADIO_SERVICE = "TV_RADIO_SERVICE";
    /**
     * Key code constant: Teletext key.
     * Displays Teletext service.
     */
    String KEY_NAME_TV_TELETEXT = "TV_TELETEXT";
    /**
     * Key code constant: Number entry key.
     * Initiates to enter multi-digit channel nubmber when each digit key is assigned
     * for selecting separate channel. Corresponds to Number Entry Mode (0x1D) of CEC
     * User Control Code.
     */
    String KEY_NAME_TV_NUMBER_ENTRY = "TV_NUMBER_ENTRY";
    /**
     * Key code constant: Analog Terrestrial key.
     * Switches to analog terrestrial broadcast service.
     */
    String KEY_NAME_TV_TERRESTRIAL_ANALOG = "TV_TERRESTRIAL_ANALOG";
    /**
     * Key code constant: Digital Terrestrial key.
     * Switches to digital terrestrial broadcast service.
     */
    String KEY_NAME_TV_TERRESTRIAL_DIGITAL = "TV_TERRESTRIAL_DIGITAL";
    /**
     * Key code constant: Satellite key.
     * Switches to digital satellite broadcast service.
     */
    String KEY_NAME_TV_SATELLITE = "TV_SATELLITE";
    /**
     * Key code constant: BS key.
     * Switches to BS digital satellite broadcasting service available in Japan.
     */
    String KEY_NAME_TV_SATELLITE_BS = "TV_SATELLITE_BS";
    /**
     * Key code constant: CS key.
     * Switches to CS digital satellite broadcasting service available in Japan.
     */
    String KEY_NAME_TV_SATELLITE_CS = "TV_SATELLITE_CS";
    /**
     * Key code constant: BS/CS key.
     * Toggles between BS and CS digital satellite services.
     */
    String KEY_NAME_TV_SATELLITE_SERVICE = "TV_SATELLITE_SERVICE";
    /**
     * Key code constant: Toggle Network key.
     * Toggles selecting broacast services.
     */
    String KEY_NAME_TV_NETWORK = "TV_NETWORK";
    /**
     * Key code constant: Antenna/Cable key.
     * Toggles broadcast input source between antenna and cable.
     */
    String KEY_NAME_TV_ANTENNA_CABLE = "TV_ANTENNA_CABLE";
    /**
     * Key code constant: HDMI #1 key.
     * Switches to HDMI input #1.
     */
    String KEY_NAME_TV_INPUT_HDMI_1 = "TV_INPUT_HDMI_1";
    /**
     * Key code constant: HDMI #2 key.
     * Switches to HDMI input #2.
     */
    String KEY_NAME_TV_INPUT_HDMI_2 = "TV_INPUT_HDMI_2";
    /**
     * Key code constant: HDMI #3 key.
     * Switches to HDMI input #3.
     */
    String KEY_NAME_TV_INPUT_HDMI_3 = "TV_INPUT_HDMI_3";
    /**
     * Key code constant: HDMI #4 key.
     * Switches to HDMI input #4.
     */
    String KEY_NAME_TV_INPUT_HDMI_4 = "TV_INPUT_HDMI_4";
    /**
     * Key code constant: Composite #1 key.
     * Switches to composite video input #1.
     */
    String KEY_NAME_TV_INPUT_COMPOSITE_1 = "TV_INPUT_COMPOSITE_1";
    /**
     * Key code constant: Composite #2 key.
     * Switches to composite video input #2.
     */
    String KEY_NAME_TV_INPUT_COMPOSITE_2 = "TV_INPUT_COMPOSITE_2";
    /**
     * Key code constant: Component #1 key.
     * Switches to component video input #1.
     */
    String KEY_NAME_TV_INPUT_COMPONENT_1 = "TV_INPUT_COMPONENT_1";
    /**
     * Key code constant: Component #2 key.
     * Switches to component video input #2.
     */
    String KEY_NAME_TV_INPUT_COMPONENT_2 = "TV_INPUT_COMPONENT_2";
    /**
     * Key code constant: VGA #1 key.
     * Switches to VGA (analog RGB) input #1.
     */
    String KEY_NAME_TV_INPUT_VGA_1 = "TV_INPUT_VGA_1";
    /**
     * Key code constant: Audio description key.
     * Toggles audio description off / on.
     */
    String KEY_NAME_TV_AUDIO_DESCRIPTION = "TV_AUDIO_DESCRIPTION";
    /**
     * Key code constant: Audio description mixing volume up key.
     * Louden audio description volume as compared with normal audio volume.
     */
    String KEY_NAME_TV_AUDIO_DESCRIPTION_MIX_UP = "TV_AUDIO_DESCRIPTION_MIX_UP";
    /**
     * Key code constant: Audio description mixing volume down key.
     * Lessen audio description volume as compared with normal audio volume.
     */
    String KEY_NAME_TV_AUDIO_DESCRIPTION_MIX_DOWN = "TV_AUDIO_DESCRIPTION_MIX_DOWN";
    /**
     * Key code constant: Zoom mode key.
     * Changes Zoom mode (Normal, Full, Zoom, Wide-zoom, etc.)
     */
    String KEY_NAME_TV_ZOOM_MODE = "TV_ZOOM_MODE";
    /**
     * Key code constant: Contents menu key.
     * Goes to the title list. Corresponds to Contents Menu (0x0B) of CEC User Control
     * Code
     */
    String KEY_NAME_TV_CONTENTS_MENU = "TV_CONTENTS_MENU";
    /**
     * Key code constant: Media context menu key.
     * Goes to the context menu of media contents. Corresponds to Media Context-sensitive
     * Menu (0x11) of CEC User Control Code.
     */
    String KEY_NAME_TV_MEDIA_CONTEXT_MENU = "TV_MEDIA_CONTEXT_MENU";
    /**
     * Key code constant: Timer programming key.
     * Goes to the timer recording menu. Corresponds to Timer Programming (0x54) of
     * CEC User Control Code.
     */
    String KEY_NAME_TV_TIMER_PROGRAMMING = "TV_TIMER_PROGRAMMING";
    /**
     * Key code constant: Help key.
     */
    String KEY_NAME_HELP = "HELP";
    /**
     * Key code constant: Navigate to previous key.
     * Goes backward by one item in an ordered collection of items.
     */
    String KEY_NAME_NAVIGATE_PREVIOUS = "NAVIGATE_PREVIOUS";
    /**
     * Key code constant: Navigate to next key.
     * Advances to the next item in an ordered collection of items.
     */
    String KEY_NAME_NAVIGATE_NEXT = "NAVIGATE_NEXT";
    /**
     * Key code constant: Navigate in key.
     * Activates the item that currently has focus or expands to the next level of a navigation
     * hierarchy.
     */
    String KEY_NAME_NAVIGATE_IN = "NAVIGATE_IN";
    /**
     * Key code constant: Navigate out key.
     * Backs out one level of a navigation hierarchy or collapses the item that currently has
     * focus.
     */
    String KEY_NAME_NAVIGATE_OUT = "NAVIGATE_OUT";
    /**
     * Key code constant: Primary stem key for Wear
     * Main power/reset button on watch.
     */
    String KEY_NAME_STEM_PRIMARY = "STEM_PRIMARY";
    /**
     * Key code constant: Generic stem key 1 for Wear
     */
    String KEY_NAME_STEM_1 = "STEM_1";
    /**
     * Key code constant: Generic stem key 2 for Wear
     */
    String KEY_NAME_STEM_2 = "STEM_2";
    /**
     * Key code constant: Generic stem key 3 for Wear
     */
    String KEY_NAME_STEM_3 = "STEM_3";
    /**
     * Key code constant: Directional Pad Up-Left
     */
    String KEY_NAME_DPAD_UP_LEFT = "DPAD_UP_LEFT";
    /**
     * Key code constant: Directional Pad Down-Left
     */
    String KEY_NAME_DPAD_DOWN_LEFT = "DPAD_DOWN_LEFT";
    /**
     * Key code constant: Directional Pad Up-Right
     */
    String KEY_NAME_DPAD_UP_RIGHT = "DPAD_UP_RIGHT";
    /**
     * Key code constant: Directional Pad Down-Right
     */
    String KEY_NAME_DPAD_DOWN_RIGHT = "DPAD_DOWN_RIGHT";
    /**
     * Key code constant: Skip forward media key.
     */
    String KEY_NAME_MEDIA_SKIP_FORWARD = "MEDIA_SKIP_FORWARD";
    /**
     * Key code constant: Skip backward media key.
     */
    String KEY_NAME_MEDIA_SKIP_BACKWARD = "MEDIA_SKIP_BACKWARD";
    /**
     * Key code constant: Step forward media key.
     * Steps media forward, one frame at a time.
     */
    String KEY_NAME_MEDIA_STEP_FORWARD = "MEDIA_STEP_FORWARD";
    /**
     * Key code constant: Step backward media key.
     * Steps media backward, one frame at a time.
     */
    String KEY_NAME_MEDIA_STEP_BACKWARD = "MEDIA_STEP_BACKWARD";
    /**
     * Key code constant: put device to sleep unless a wakelock is held.
     */
    String KEY_NAME_SOFT_SLEEP = "SOFT_SLEEP";
    /**
     * Key code constant: Cut key.
     */
    String KEY_NAME_CUT = "CUT";
    /**
     * Key code constant: Copy key.
     */
    String KEY_NAME_COPY = "COPY";
    /**
     * Key code constant: Paste key.
     */
    String KEY_NAME_PASTE = "PASTE";
    /**
     * Key code constant: Consumed by the system for navigation up
     */
    String KEY_NAME_SYSTEM_NAVIGATION_UP = "SYSTEM_NAVIGATION_UP";
    /**
     * Key code constant: Consumed by the system for navigation down
     */
    String KEY_NAME_SYSTEM_NAVIGATION_DOWN = "SYSTEM_NAVIGATION_DOWN";
    /**
     * Key code constant: Consumed by the system for navigation left
     */
    String KEY_NAME_SYSTEM_NAVIGATION_LEFT = "SYSTEM_NAVIGATION_LEFT";
    /**
     * Key code constant: Consumed by the system for navigation right
     */
    String KEY_NAME_SYSTEM_NAVIGATION_RIGHT = "SYSTEM_NAVIGATION_RIGHT";
    /**
     * Key code constant: Show all apps
     */
    String KEY_NAME_ALL_APPS = "ALL_APPS";
    /**
     * Key code constant: Refresh key.
     */
    String KEY_NAME_REFRESH = "REFRESH";
    /**
     * Key code constant: Thumbs up key. Apps can use this to let user upvote content.
     */
    String KEY_NAME_THUMBS_UP = "THUMBS_UP";
    /**
     * Key code constant: Thumbs down key. Apps can use this to let user downvote content.
     */
    String KEY_NAME_THUMBS_DOWN = "THUMBS_DOWN";
    /**
     * Key code constant: Used to switch current {@link android.accounts.Account} that is
     * consuming content. May be consumed by system to set account globally.
     */
    String KEY_NAME_PROFILE_SWITCH = "PROFILE_SWITCH";

    class KeyEventMap extends HashMap<Integer, String> {
        private volatile static KeyEventMap sInstance;

        private KeyEventMap() {
            init();
        }

        public static KeyEventMap getInstance() {
            if (sInstance == null) {
                synchronized (KeyEventMap.class) {
                    if (sInstance == null) {
                        sInstance = new KeyEventMap();
                    }
                }
            }
            return sInstance;
        }

        private void init() {
            put(KeyEvent.KEYCODE_UNKNOWN, KEY_NAME_UNKNOWN);
            put(KeyEvent.KEYCODE_SOFT_LEFT, kEY_NAME_SOFT_LEFT);
            put(KeyEvent.KEYCODE_SOFT_RIGHT, kEY_NAME_SOFT_RIGHT);
            put(KeyEvent.KEYCODE_HOME, KEY_NAME_HOME);
            put(KeyEvent.KEYCODE_BACK, KEY_NAME_BACK);
            put(KeyEvent.KEYCODE_CALL, KEY_NAME_CALL);
            put(KeyEvent.KEYCODE_ENDCALL, KEY_NAME_END_CALL);
            put(KeyEvent.KEYCODE_0, KEY_NAME_0);
            put(KeyEvent.KEYCODE_1, KEY_NAME_1);
            put(KeyEvent.KEYCODE_2, KEY_NAME_2);
            put(KeyEvent.KEYCODE_3, KEY_NAME_3);
            put(KeyEvent.KEYCODE_4, KEY_NAME_4);
            put(KeyEvent.KEYCODE_5, KEY_NAME_5);
            put(KeyEvent.KEYCODE_6, KEY_NAME_6);
            put(KeyEvent.KEYCODE_7, KEY_NAME_7);
            put(KeyEvent.KEYCODE_8, KEY_NAME_8);
            put(KeyEvent.KEYCODE_9, KEY_NAME_9);
            put(KeyEvent.KEYCODE_STAR, KEY_NAME_STAR);
            put(KeyEvent.KEYCODE_POUND, KEY_NAME_POUND);
            put(KeyEvent.KEYCODE_DPAD_UP, KEY_NAME_DPAD_UP);
            put(KeyEvent.KEYCODE_DPAD_DOWN, KEY_NAME_DPAD_DOWN);
            put(KeyEvent.KEYCODE_DPAD_LEFT, KEY_NAME_DPAD_LEFT);
            put(KeyEvent.KEYCODE_DPAD_RIGHT, KEY_NAME_DPAD_RIGHT);
            put(KeyEvent.KEYCODE_DPAD_CENTER, KEY_NAME_DPAD_CENTER);
            put(KeyEvent.KEYCODE_VOLUME_UP, KEY_NAME_VOLUME_UP);
            put(KeyEvent.KEYCODE_VOLUME_DOWN, KEY_NAME_VOLUME_DOWN);
            put(KeyEvent.KEYCODE_POWER, KEY_NAME_POWER);
            put(KeyEvent.KEYCODE_CAMERA, KEY_NAME_CAMERA);
            put(KeyEvent.KEYCODE_CLEAR, KEY_NAME_CLEAR);
            put(KeyEvent.KEYCODE_A, KEY_NAME_A);
            put(KeyEvent.KEYCODE_B, KEY_NAME_B);
            put(KeyEvent.KEYCODE_C, KEY_NAME_C);
            put(KeyEvent.KEYCODE_D, KEY_NAME_D);
            put(KeyEvent.KEYCODE_E, KEY_NAME_E);
            put(KeyEvent.KEYCODE_F, KEY_NAME_F);
            put(KeyEvent.KEYCODE_G, KEY_NAME_G);
            put(KeyEvent.KEYCODE_H, KEY_NAME_H);
            put(KeyEvent.KEYCODE_I, KEY_NAME_I);
            put(KeyEvent.KEYCODE_J, KEY_NAME_J);
            put(KeyEvent.KEYCODE_K, KEY_NAME_K);
            put(KeyEvent.KEYCODE_L, KEY_NAME_L);
            put(KeyEvent.KEYCODE_M, KEY_NAME_M);
            put(KeyEvent.KEYCODE_N, KEY_NAME_N);
            put(KeyEvent.KEYCODE_O, KEY_NAME_O);
            put(KeyEvent.KEYCODE_P, KEY_NAME_P);
            put(KeyEvent.KEYCODE_Q, KEY_NAME_Q);
            put(KeyEvent.KEYCODE_R, KEY_NAME_R);
            put(KeyEvent.KEYCODE_S, KEY_NAME_S);
            put(KeyEvent.KEYCODE_T, KEY_NAME_T);
            put(KeyEvent.KEYCODE_U, KEY_NAME_U);
            put(KeyEvent.KEYCODE_V, KEY_NAME_V);
            put(KeyEvent.KEYCODE_W, KEY_NAME_W);
            put(KeyEvent.KEYCODE_X, KEY_NAME_X);
            put(KeyEvent.KEYCODE_Y, KEY_NAME_Y);
            put(KeyEvent.KEYCODE_COMMA, KEY_NAME_COMMA);
            put(KeyEvent.KEYCODE_PERIOD, KEY_NAME_PERIOD);
            put(KeyEvent.KEYCODE_ALT_LEFT, KEY_NAME_ALT_LEFT);
            put(KeyEvent.KEYCODE_ALT_RIGHT, KEY_NAME_ALT_RIGHT);
            put(KeyEvent.KEYCODE_SHIFT_LEFT, KEY_NAME_SHIFT_LEFT);
            put(KeyEvent.KEYCODE_SHIFT_RIGHT, KEY_NAME_SHIFT_RIGHT);
            put(KeyEvent.KEYCODE_TAB, KEY_NAME_TAB);
            put(KeyEvent.KEYCODE_SPACE, KEY_NAME_SPACE);
            put(KeyEvent.KEYCODE_SYM, KEY_NAME_SYM);
            put(KeyEvent.KEYCODE_EXPLORER, KEY_NAME_EXPLORER);
            put(KeyEvent.KEYCODE_ENVELOPE, KEY_NAME_ENVELOPE);
            put(KeyEvent.KEYCODE_ENTER, KEY_NAME_ENTER);
            put(KeyEvent.KEYCODE_DEL, KEY_NAME_DEL);
            put(KeyEvent.KEYCODE_GRAVE, KEY_NAME_GRAVE);
            put(KeyEvent.KEYCODE_MINUS, KEY_NAME_MINUS);
            put(KeyEvent.KEYCODE_EQUALS, KEY_NAME_EQUALS);
            put(KeyEvent.KEYCODE_LEFT_BRACKET, KEY_NAME_LEFT_BRACKET);
            put(KeyEvent.KEYCODE_RIGHT_BRACKET, KEY_NAME_RIGHT_BRACKET);
            put(KeyEvent.KEYCODE_BACKSLASH, KEY_NAME_BACKSLASH);
            put(KeyEvent.KEYCODE_SEMICOLON, KEY_NAME_SEMICOLON);
            put(KeyEvent.KEYCODE_APOSTROPHE, KEY_NAME_APOSTROPHE);
            put(KeyEvent.KEYCODE_SLASH, KEY_NAME_SLASH);
            put(KeyEvent.KEYCODE_AT, KEY_NAME_AT);
            put(KeyEvent.KEYCODE_NUM, KEY_NAME_NUM);
            put(KeyEvent.KEYCODE_HEADSETHOOK, KEY_NAME_HEADSETHOOK);
            put(KeyEvent.KEYCODE_FOCUS, KEY_NAME_FOCUS);
            put(KeyEvent.KEYCODE_PLUS, KEY_NAME_PLUS);
            put(KeyEvent.KEYCODE_MENU, KEY_NAME_MENU);
            put(KeyEvent.KEYCODE_NOTIFICATION, KEY_NAME_NOTIFICATION);
            put(KeyEvent.KEYCODE_SEARCH, KEY_NAME_SEARCH);
            put(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, KEY_NAME_MEDIA_PLAY_PAUSE);
            put(KeyEvent.KEYCODE_MEDIA_STOP, KEY_NAME_MEDIA_STOP);
            put(KeyEvent.KEYCODE_MEDIA_NEXT, KEY_NAME_MEDIA_NEXT);
            put(KeyEvent.KEYCODE_MEDIA_PREVIOUS, KEY_NAME_MEDIA_PREVIOUS);
            put(KeyEvent.KEYCODE_MEDIA_REWIND, KEY_NAME_MEDIA_REWIND);
            put(KeyEvent.KEYCODE_MEDIA_FAST_FORWARD, KEY_NAME_MEDIA_FAST_FORWARD);
            put(KeyEvent.KEYCODE_MUTE, KEY_NAME_MUTE);
            put(KeyEvent.KEYCODE_PAGE_UP, KEY_NAME_PAGE_UP);
            put(KeyEvent.KEYCODE_PAGE_DOWN, KEY_NAME_PAGE_DOWN);
            put(KeyEvent.KEYCODE_PICTSYMBOLS, KEY_NAME_PICTSYMBOLS);
            put(KeyEvent.KEYCODE_SWITCH_CHARSET, KEY_NAME_SWITCH_CHARSET);
            put(KeyEvent.KEYCODE_BUTTON_A, KEY_NAME_BUTTON_A);
            put(KeyEvent.KEYCODE_BUTTON_B, KEY_NAME_BUTTON_B);
            put(KeyEvent.KEYCODE_BUTTON_C, KEY_NAME_BUTTON_C);
            put(KeyEvent.KEYCODE_BUTTON_X, KEY_NAME_BUTTON_X);
            put(KeyEvent.KEYCODE_BUTTON_Y, KEY_NAME_BUTTON_Y);
            put(KeyEvent.KEYCODE_BUTTON_Z, KEY_NAME_BUTTON_Z);
            put(KeyEvent.KEYCODE_BUTTON_L1, KEY_NAME_BUTTON_L1);
            put(KeyEvent.KEYCODE_BUTTON_R1, KEY_NAME_BUTTON_R1);
            put(KeyEvent.KEYCODE_BUTTON_L2, KEY_NAME_BUTTON_L2);
            put(KeyEvent.KEYCODE_BUTTON_R2, KEY_NAME_BUTTON_R2);
            put(KeyEvent.KEYCODE_BUTTON_THUMBL, KEY_NAME_BUTTON_THUMBL);
            put(KeyEvent.KEYCODE_BUTTON_THUMBR, KEY_NAME_BUTTON_THUMBR);
            put(KeyEvent.KEYCODE_BUTTON_START, KEY_NAME_BUTTON_START);
            put(KeyEvent.KEYCODE_BUTTON_SELECT, KEY_NAME_BUTTON_SELECT);
            put(KeyEvent.KEYCODE_BUTTON_MODE, KEY_NAME_BUTTON_MODE);
            put(KeyEvent.KEYCODE_ESCAPE, KEY_NAME_ESCAPE);
            put(KeyEvent.KEYCODE_FORWARD_DEL, KEY_NAME_FORWARD_DEL);
            put(KeyEvent.KEYCODE_CTRL_LEFT, KEY_NAME_CTRL_LEFT);
            put(KeyEvent.KEYCODE_CTRL_RIGHT, KEY_NAME_CTRL_RIGHT);
            put(KeyEvent.KEYCODE_CAPS_LOCK, KEY_NAME_CAPS_LOCK);
            put(KeyEvent.KEYCODE_SCROLL_LOCK, KEY_NAME_SCROLL_LOCK);
            put(KeyEvent.KEYCODE_META_LEFT, KEY_NAME_META_LEFT);
            put(KeyEvent.KEYCODE_META_RIGHT, KEY_NAME_META_RIGHT);
            put(KeyEvent.KEYCODE_FUNCTION, KEY_NAME_FUNCTION);
            put(KeyEvent.KEYCODE_SYSRQ, KEY_NAME_SYSRQ);
            put(KeyEvent.KEYCODE_BREAK, KEY_NAME_BREAK);
            put(KeyEvent.KEYCODE_MOVE_HOME, KEY_NAME_MOVE_HOME);
            put(KeyEvent.KEYCODE_MOVE_END, KEY_NAME_MOVE_END);
            put(KeyEvent.KEYCODE_INSERT, KEY_NAME_INSERT);
            put(KeyEvent.KEYCODE_FORWARD, KEY_NAME_FORWARD);
            put(KeyEvent.KEYCODE_MEDIA_PLAY, KEY_NAME_MEDIA_PLAY);
            put(KeyEvent.KEYCODE_MEDIA_PAUSE, KEY_NAME_MEDIA_PAUSE);
            put(KeyEvent.KEYCODE_MEDIA_CLOSE, KEY_NAME_MEDIA_CLOSE);
            put(KeyEvent.KEYCODE_MEDIA_EJECT, KEY_NAME_MEDIA_EJECT);
            put(KeyEvent.KEYCODE_MEDIA_RECORD, KEY_NAME_MEDIA_RECORD);
            put(KeyEvent.KEYCODE_F1, KEY_NAME_F1);
            put(KeyEvent.KEYCODE_F2, KEY_NAME_F2);
            put(KeyEvent.KEYCODE_F3, KEY_NAME_F3);
            put(KeyEvent.KEYCODE_F4, KEY_NAME_F4);
            put(KeyEvent.KEYCODE_F5, KEY_NAME_F5);
            put(KeyEvent.KEYCODE_F6, KEY_NAME_F6);
            put(KeyEvent.KEYCODE_F7, KEY_NAME_F7);
            put(KeyEvent.KEYCODE_F8, KEY_NAME_F8);
            put(KeyEvent.KEYCODE_F9, KEY_NAME_F9);
            put(KeyEvent.KEYCODE_F10, KEY_NAME_F10);
            put(KeyEvent.KEYCODE_F11, KEY_NAME_F11);
            put(KeyEvent.KEYCODE_F12, KEY_NAME_F12);
            put(KeyEvent.KEYCODE_NUM_LOCK, KEY_NAME_NUM_LOCK);
            put(KeyEvent.KEYCODE_NUMPAD_0, KEY_NAME_NUMPAD_0);
            put(KeyEvent.KEYCODE_NUMPAD_1, KEY_NAME_NUMPAD_1);
            put(KeyEvent.KEYCODE_NUMPAD_2, KEY_NAME_NUMPAD_2);
            put(KeyEvent.KEYCODE_NUMPAD_3, KEY_NAME_NUMPAD_3);
            put(KeyEvent.KEYCODE_NUMPAD_4, KEY_NAME_NUMPAD_4);
            put(KeyEvent.KEYCODE_NUMPAD_5, KEY_NAME_NUMPAD_5);
            put(KeyEvent.KEYCODE_NUMPAD_6, KEY_NAME_NUMPAD_6);
            put(KeyEvent.KEYCODE_NUMPAD_7, KEY_NAME_NUMPAD_7);
            put(KeyEvent.KEYCODE_NUMPAD_8, KEY_NAME_NUMPAD_8);
            put(KeyEvent.KEYCODE_NUMPAD_9, KEY_NAME_NUMPAD_9);
            put(KeyEvent.KEYCODE_NUMPAD_DIVIDE, KEY_NAME_NUMPAD_DIVIDE);
            put(KeyEvent.KEYCODE_NUMPAD_MULTIPLY, KEY_NAME_NUMPAD_MULTIPLY);
            put(KeyEvent.KEYCODE_NUMPAD_SUBTRACT, KEY_NAME_NUMPAD_SUBTRACT);
            put(KeyEvent.KEYCODE_NUMPAD_ADD, KEY_NAME_NUMPAD_ADD);
            put(KeyEvent.KEYCODE_NUMPAD_DOT, KEY_NAME_NUMPAD_DOT);
            put(KeyEvent.KEYCODE_NUMPAD_COMMA, KEY_NAME_NUMPAD_COMMA);
            put(KeyEvent.KEYCODE_NUMPAD_ENTER, KEY_NAME_NUMPAD_ENTER);
            put(KeyEvent.KEYCODE_NUMPAD_EQUALS, KEY_NAME_NUMPAD_EQUALS);
            put(KeyEvent.KEYCODE_NUMPAD_LEFT_PAREN, KEY_NAME_NUMPAD_LEFT_PAREN);
            put(KeyEvent.KEYCODE_NUMPAD_RIGHT_PAREN, KEY_NAME_NUMPAD_RIGHT_PAREN);
            put(KeyEvent.KEYCODE_VOLUME_MUTE, KEY_NAME_VOLUME_MUTE);
            put(KeyEvent.KEYCODE_INFO, KEY_NAME_INFO);
            put(KeyEvent.KEYCODE_CHANNEL_UP, KEY_NAME_CHANNEL_UP);
            put(KeyEvent.KEYCODE_CHANNEL_DOWN, KEY_NAME_CHANNEL_DOWN);
            put(KeyEvent.KEYCODE_ZOOM_IN, KEY_NAME_ZOOM_IN);
            put(KeyEvent.KEYCODE_ZOOM_OUT, KEY_NAME_ZOOM_OUT);
            put(KeyEvent.KEYCODE_TV, KEY_NAME_TV);
            put(KeyEvent.KEYCODE_WINDOW, KEY_NAME_WINDOW);
            put(KeyEvent.KEYCODE_GUIDE, KEY_NAME_GUIDE);
            put(KeyEvent.KEYCODE_DVR, KEY_NAME_DVR);
            put(KeyEvent.KEYCODE_BOOKMARK, KEY_NAME_BOOKMARK);
            put(KeyEvent.KEYCODE_CAPTIONS, KEY_NAME_CAPTIONS);
            put(KeyEvent.KEYCODE_SETTINGS, KEY_NAME_SETTINGS);
            put(KeyEvent.KEYCODE_TV_POWER, KEY_NAME_TV_POWER);
            put(KeyEvent.KEYCODE_TV_INPUT, KEY_NAME_TV_INPUT);
            put(KeyEvent.KEYCODE_STB_POWER, KEY_NAME_STB_POWER);
            put(KeyEvent.KEYCODE_STB_INPUT, KEY_NAME_STB_INPUT);
            put(KeyEvent.KEYCODE_AVR_POWER, KEY_NAME_AVR_POWER);
            put(KeyEvent.KEYCODE_AVR_INPUT, KEY_NAME_AVR_INPUT);
            put(KeyEvent.KEYCODE_PROG_RED, KEY_NAME_PROG_RED);
            put(KeyEvent.KEYCODE_PROG_GREEN, KEY_NAME_PROG_GREEN);
            put(KeyEvent.KEYCODE_PROG_YELLOW, KEY_NAME_PROG_YELLOW);
            put(KeyEvent.KEYCODE_PROG_BLUE, KEY_NAME_PROG_BLUE);
            put(KeyEvent.KEYCODE_APP_SWITCH, KEY_NAME_APP_SWITCH);
            put(KeyEvent.KEYCODE_BUTTON_1, KEY_NAME_BUTTON_1);
            put(KeyEvent.KEYCODE_BUTTON_2, KEY_NAME_BUTTON_2);
            put(KeyEvent.KEYCODE_BUTTON_3, KEY_NAME_BUTTON_3);
            put(KeyEvent.KEYCODE_BUTTON_4, KEY_NAME_BUTTON_4);
            put(KeyEvent.KEYCODE_BUTTON_5, KEY_NAME_BUTTON_5);
            put(KeyEvent.KEYCODE_BUTTON_6, KEY_NAME_BUTTON_6);
            put(KeyEvent.KEYCODE_BUTTON_7, KEY_NAME_BUTTON_7);
            put(KeyEvent.KEYCODE_BUTTON_8, KEY_NAME_BUTTON_8);
            put(KeyEvent.KEYCODE_BUTTON_9, KEY_NAME_BUTTON_9);
            put(KeyEvent.KEYCODE_BUTTON_10, KEY_NAME_BUTTON_10);
            put(KeyEvent.KEYCODE_BUTTON_11, KEY_NAME_BUTTON_11);
            put(KeyEvent.KEYCODE_BUTTON_12, KEY_NAME_BUTTON_12);
            put(KeyEvent.KEYCODE_BUTTON_13, KEY_NAME_BUTTON_13);
            put(KeyEvent.KEYCODE_BUTTON_14, KEY_NAME_BUTTON_14);
            put(KeyEvent.KEYCODE_BUTTON_15, KEY_NAME_BUTTON_15);
            put(KeyEvent.KEYCODE_BUTTON_16, KEY_NAME_BUTTON_16);
            put(KeyEvent.KEYCODE_LANGUAGE_SWITCH, KEY_NAME_LANGUAGE_SWITCH);
            put(KeyEvent.KEYCODE_MANNER_MODE, KEY_NAME_MANNER_MODE);
            put(KeyEvent.KEYCODE_3D_MODE, KEY_NAME_3D_MODE);
            put(KeyEvent.KEYCODE_CONTACTS, KEY_NAME_CONTACTS);
            put(KeyEvent.KEYCODE_CALENDAR, KEY_NAME_CALENDAR);
            put(KeyEvent.KEYCODE_MUSIC, KEY_NAME_MUSIC);
            put(KeyEvent.KEYCODE_CALCULATOR, KEY_NAME_CALCULATOR);
            put(KeyEvent.KEYCODE_ZENKAKU_HANKAKU, KEY_NAME_ZENKAKU_HANKAKU);
            put(KeyEvent.KEYCODE_EISU, KEY_NAME_EISU);
            put(KeyEvent.KEYCODE_MUHENKAN, KEY_NAME_MUHENKAN);
            put(KeyEvent.KEYCODE_KATAKANA_HIRAGANA, KEY_NAME_KATAKANA_HIRAGANA);
            put(KeyEvent.KEYCODE_YEN, KEY_NAME_YEN);
            put(KeyEvent.KEYCODE_RO, KEY_NAME_RO);
            put(KeyEvent.KEYCODE_KANA, KEY_NAME_KANA);
            put(KeyEvent.KEYCODE_ASSIST, KEY_NAME_ASSIST);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                put(KeyEvent.KEYCODE_BRIGHTNESS_DOWN, KEY_NAME_BRIGHTNESS_DOWN);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                put(KeyEvent.KEYCODE_BRIGHTNESS_UP, KEY_NAME_BRIGHTNESS_UP);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                put(KeyEvent.KEYCODE_MEDIA_AUDIO_TRACK, KEY_NAME_MEDIA_AUDIO_TRACK);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                put(KeyEvent.KEYCODE_SLEEP, KEY_NAME_SLEEP);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                put(KeyEvent.KEYCODE_WAKEUP, KEY_NAME_WAKEUP);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                put(KeyEvent.KEYCODE_PAIRING, KEY_NAME_PAIRING);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                put(KeyEvent.KEYCODE_MEDIA_TOP_MENU, KEY_NAME_MEDIA_TOP_MENU);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                put(KeyEvent.KEYCODE_11, KEY_NAME_11);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                put(KeyEvent.KEYCODE_12, KEY_NAME_12);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                put(KeyEvent.KEYCODE_LAST_CHANNEL, KEY_NAME_LAST_CHANNEL);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                put(KeyEvent.KEYCODE_TV_DATA_SERVICE, KEY_NAME_TV_DATA_SERVICE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                put(KeyEvent.KEYCODE_VOICE_ASSIST, KEY_NAME_VOICE_ASSIST);
                put(KeyEvent.KEYCODE_TV_RADIO_SERVICE, KEY_NAME_TV_RADIO_SERVICE);
                put(KeyEvent.KEYCODE_TV_TELETEXT, KEY_NAME_TV_TELETEXT);
                put(KeyEvent.KEYCODE_TV_NUMBER_ENTRY, KEY_NAME_TV_NUMBER_ENTRY);
                put(KeyEvent.KEYCODE_TV_TERRESTRIAL_ANALOG, KEY_NAME_TV_TERRESTRIAL_ANALOG);
                put(KeyEvent.KEYCODE_TV_TERRESTRIAL_DIGITAL, KEY_NAME_TV_TERRESTRIAL_DIGITAL);
                put(KeyEvent.KEYCODE_TV_SATELLITE, KEY_NAME_TV_SATELLITE);
                put(KeyEvent.KEYCODE_TV_SATELLITE_BS, KEY_NAME_TV_SATELLITE_BS);
                put(KeyEvent.KEYCODE_TV_SATELLITE_CS, KEY_NAME_TV_SATELLITE_CS);
                put(KeyEvent.KEYCODE_TV_SATELLITE_SERVICE, KEY_NAME_TV_SATELLITE_SERVICE);
                put(KeyEvent.KEYCODE_TV_NETWORK, KEY_NAME_TV_NETWORK);
                put(KeyEvent.KEYCODE_TV_ANTENNA_CABLE, KEY_NAME_TV_ANTENNA_CABLE);
                put(KeyEvent.KEYCODE_TV_INPUT_HDMI_1, KEY_NAME_TV_INPUT_HDMI_1);
                put(KeyEvent.KEYCODE_TV_INPUT_HDMI_2, KEY_NAME_TV_INPUT_HDMI_2);
                put(KeyEvent.KEYCODE_TV_INPUT_HDMI_3, KEY_NAME_TV_INPUT_HDMI_3);
                put(KeyEvent.KEYCODE_TV_INPUT_HDMI_4, KEY_NAME_TV_INPUT_HDMI_4);
                put(KeyEvent.KEYCODE_TV_INPUT_COMPOSITE_1, KEY_NAME_TV_INPUT_COMPOSITE_1);
                put(KeyEvent.KEYCODE_TV_INPUT_COMPOSITE_2, KEY_NAME_TV_INPUT_COMPOSITE_2);
                put(KeyEvent.KEYCODE_TV_INPUT_COMPONENT_1, KEY_NAME_TV_INPUT_COMPONENT_1);
                put(KeyEvent.KEYCODE_TV_INPUT_COMPONENT_2, KEY_NAME_TV_INPUT_COMPONENT_2);
                put(KeyEvent.KEYCODE_TV_INPUT_VGA_1, KEY_NAME_TV_INPUT_VGA_1);
                put(KeyEvent.KEYCODE_TV_AUDIO_DESCRIPTION, KEY_NAME_TV_AUDIO_DESCRIPTION);
                put(KeyEvent.KEYCODE_TV_AUDIO_DESCRIPTION_MIX_UP, KEY_NAME_TV_AUDIO_DESCRIPTION_MIX_UP);
                put(KeyEvent.KEYCODE_TV_AUDIO_DESCRIPTION_MIX_DOWN, KEY_NAME_TV_AUDIO_DESCRIPTION_MIX_DOWN);
                put(KeyEvent.KEYCODE_TV_ZOOM_MODE, KEY_NAME_TV_ZOOM_MODE);
                put(KeyEvent.KEYCODE_TV_CONTENTS_MENU, KEY_NAME_TV_CONTENTS_MENU);
                put(KeyEvent.KEYCODE_TV_MEDIA_CONTEXT_MENU, KEY_NAME_TV_MEDIA_CONTEXT_MENU);
                put(KeyEvent.KEYCODE_TV_TIMER_PROGRAMMING, KEY_NAME_TV_TIMER_PROGRAMMING);
                put(KeyEvent.KEYCODE_HELP, KEY_NAME_HELP);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                put(KeyEvent.KEYCODE_NAVIGATE_PREVIOUS, KEY_NAME_NAVIGATE_PREVIOUS);
                put(KeyEvent.KEYCODE_NAVIGATE_NEXT, KEY_NAME_NAVIGATE_NEXT);
                put(KeyEvent.KEYCODE_NAVIGATE_IN, KEY_NAME_NAVIGATE_IN);
                put(KeyEvent.KEYCODE_NAVIGATE_OUT, KEY_NAME_NAVIGATE_OUT);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                put(KeyEvent.KEYCODE_STEM_PRIMARY, KEY_NAME_STEM_PRIMARY);
                put(KeyEvent.KEYCODE_STEM_1, KEY_NAME_STEM_1);
                put(KeyEvent.KEYCODE_STEM_2, KEY_NAME_STEM_2);
                put(KeyEvent.KEYCODE_STEM_3, KEY_NAME_STEM_3);
                put(KeyEvent.KEYCODE_DPAD_UP_LEFT, KEY_NAME_DPAD_UP_LEFT);
                put(KeyEvent.KEYCODE_DPAD_DOWN_LEFT, KEY_NAME_DPAD_DOWN_LEFT);
                put(KeyEvent.KEYCODE_DPAD_UP_RIGHT, KEY_NAME_DPAD_UP_RIGHT);
                put(KeyEvent.KEYCODE_DPAD_DOWN_RIGHT, KEY_NAME_DPAD_DOWN_RIGHT);
                put(KeyEvent.KEYCODE_MEDIA_SKIP_FORWARD, KEY_NAME_MEDIA_SKIP_FORWARD);
                put(KeyEvent.KEYCODE_MEDIA_SKIP_BACKWARD, KEY_NAME_MEDIA_SKIP_BACKWARD);
                put(KeyEvent.KEYCODE_MEDIA_STEP_FORWARD, KEY_NAME_MEDIA_STEP_FORWARD);
                put(KeyEvent.KEYCODE_MEDIA_STEP_BACKWARD, KEY_NAME_MEDIA_STEP_BACKWARD);
                put(KeyEvent.KEYCODE_SOFT_SLEEP, KEY_NAME_SOFT_SLEEP);
                put(KeyEvent.KEYCODE_CUT, KEY_NAME_CUT);
                put(KeyEvent.KEYCODE_COPY, KEY_NAME_COPY);
                put(KeyEvent.KEYCODE_PASTE, KEY_NAME_PASTE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                put(KeyEvent.KEYCODE_SYSTEM_NAVIGATION_UP, KEY_NAME_SYSTEM_NAVIGATION_UP);
                put(KeyEvent.KEYCODE_SYSTEM_NAVIGATION_DOWN, KEY_NAME_SYSTEM_NAVIGATION_DOWN);
                put(KeyEvent.KEYCODE_SYSTEM_NAVIGATION_LEFT, KEY_NAME_SYSTEM_NAVIGATION_LEFT);
                put(KeyEvent.KEYCODE_SYSTEM_NAVIGATION_RIGHT, KEY_NAME_SYSTEM_NAVIGATION_RIGHT);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                put(KeyEvent.KEYCODE_ALL_APPS, KEY_NAME_ALL_APPS);
                put(KeyEvent.KEYCODE_REFRESH, KEY_NAME_REFRESH);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(KeyEvent.KEYCODE_THUMBS_UP, KEY_NAME_THUMBS_UP);
                put(KeyEvent.KEYCODE_THUMBS_DOWN, KEY_NAME_THUMBS_DOWN);
                put(KeyEvent.KEYCODE_PROFILE_SWITCH, KEY_NAME_PROFILE_SWITCH);
            }
        }
    }

}

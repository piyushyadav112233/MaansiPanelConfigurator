# Maansi Panel Configurator — Android App

A native Kotlin + Jetpack Compose Android app implementing the panel
configurator: Panel Type → Rating → Equipment → Review → Submit → BOM.
Submitted requests are saved locally on the device (Room database), so the
app works fully offline and there's a "Team View" screen for your
engineering/quotation team to review submissions on the same device.

## What's in this project

```
MaansiPanelConfigurator/
├── app/
│   ├── build.gradle.kts              # dependencies
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/maansiauto/panelconfigurator/
│       │   ├── MainActivity.kt
│       │   ├── data/                 # models, catalog, Room database
│       │   ├── navigation/           # screen routing
│       │   ├── ui/screens/           # Landing, Wizard, Confirmation, Team View
│       │   ├── ui/theme/             # colors, typography
│       │   └── viewmodel/            # ConfiguratorViewModel (all app state)
│       └── res/                      # strings, theme, launcher icon
├── build.gradle.kts                  # plugin versions
└── settings.gradle.kts
```

## Step-by-step: getting it running

1. **Install Android Studio** (if you don't have it): download the latest
   stable release from developer.android.com/studio. During setup, let it
   install the Android SDK (API 34) and an emulator image, or plan to test
   on your own Android phone instead.

2. **Open the project**: Android Studio → *Open* → select the
   `MaansiPanelConfigurator` folder (the one containing `settings.gradle.kts`).

3. **Let Gradle sync**: Android Studio will detect this is a Gradle project
   and offer to create the Gradle wrapper / download dependencies
   automatically. Click through the prompts — this needs an internet
   connection the first time (it downloads Kotlin, Compose, Room, etc.).

4. **Run it**: pick a device — either a phone connected by USB with
   *Developer Options → USB debugging* turned on, or a virtual device
   (Android Studio → Device Manager → Create Device) — then click the green
   ▶ Run button.

5. **Try the flow**: Start Configuration → pick a panel type → fill in
   rating → select equipment → review & submit → check "Team View" to see
   the saved request.

## Known placeholders to swap out before this goes further

- **App icon** (`res/drawable/ic_launcher.xml`) is a simple placeholder
  shape. Before publishing anywhere, replace it using Android Studio's
  *Image Asset Studio* (right-click `res` → New → Image Asset) with your
  actual logo.
- **Fonts**: the app currently uses the system default font. To match the
  web version's look, add `.ttf` files for Space Grotesk / Inter under
  `res/font/` and wire them into `ui/theme/Type.kt`.
- **Equipment catalog** (`data/PanelCatalog.kt`): the panel types, ratings
  and spec options are a reasonable starting set based on standard
  industry practice — your engineering team should review and adjust them
  to match exactly what MAANSI stocks.

## Where this fits in the bigger plan

This is Phase 1 of the roadmap — an offline, fully working configurator.
Later phases (equipment search/filters, validation rules, cloud sync,
admin login, automatic quotation/PDF generation) build on top of this same
project structure.

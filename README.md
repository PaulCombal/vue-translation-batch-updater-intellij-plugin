# vue-translation-batch-updater-intellij-plugin

![Build](https://github.com/PaulCombal/vue-translation-batch-updater-intellij-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/28303.svg)](https://plugins.jetbrains.com/plugin/28303)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/28303.svg)](https://plugins.jetbrains.com/plugin/28303)

<!-- Plugin description -->
This is an IntelliJ plugin for [vue-translation-batch-updater](https://github.com/PaulCombal/vue-translation-batch-updater) .

You have to have it installed on your project for this plugin to work.

This is a plugin for Jetbrains IDEs that provides an easy way to translate keys in your JSON and YAML i18n files using Google Gemini.

Features:
* Context-Menu Translation: Simply right-click on a JSON or YAML key in your project to translate it directly.
* Seamless Integration: Works with JSON and YAML files, including those embedded within .vue files for i18n-loader.
* Intelligent Key Recognition: The plugin intelligently identifies and targets the key you've selected, ensuring accurate translation of the desired content.
* No Command Line Needed: Unlike the command line tool, this plugin allows you to translate keys without ever leaving your IDE, providing a more fluid development experience.
<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "vue-translation-batch-updater-intellij-plugin"</kbd> >
  <kbd>Install</kbd>
  
- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/PaulCombal/vue-translation-batch-updater-intellij-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation

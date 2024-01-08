// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.androidApplication) apply false
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.daggerHilt) apply false
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.googleServices) apply false
}

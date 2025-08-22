package com.github.paulcombal.vuetranslationbatchupdaterintellijplugin.actions

import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class TranslateJsonAction : BaseTranslateAction() {

    override fun findKeyElement(psiElement: PsiElement): PsiElement? {
        return PsiTreeUtil.getParentOfType(psiElement, JsonProperty::class.java, false)
    }

    override fun getFullKeyPath(keyElement: PsiElement): String {
        val jsonProperty = keyElement as? JsonProperty ?: return ""
        val keys = mutableListOf<String>()
        var currentProperty: JsonProperty? = jsonProperty

        while (currentProperty != null) {
            keys.add(currentProperty.name)
            currentProperty = PsiTreeUtil.getParentOfType(currentProperty, JsonProperty::class.java)
        }

        return keys.reversed().joinToString(".")
    }

    override fun isKeyElement(psiElement: PsiElement): Boolean {
        // The language check is crucial for handling injected JSON
        // val language = psiElement.language
        // val isJson = LanguageUtil.isDescendedFrom(language, Json5Language.INSTANCE) || LanguageUtil.isDescendedFrom(language, Language.findLanguageByID("JSON"))

        // if (!isJson) {
        //    return false
        // }

        // Find the parent JsonProperty and check if the element is its name
        val jsonProperty = PsiTreeUtil.getParentOfType(psiElement, JsonProperty::class.java)
        return jsonProperty != null && PsiTreeUtil.isAncestor(jsonProperty.nameElement, psiElement, false)
    }
}
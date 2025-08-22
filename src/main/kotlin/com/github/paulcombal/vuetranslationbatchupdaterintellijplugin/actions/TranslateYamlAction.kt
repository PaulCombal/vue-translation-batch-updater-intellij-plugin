package com.github.paulcombal.vuetranslationbatchupdaterintellijplugin.actions

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.psi.YAMLKeyValue

class TranslateYamlAction : BaseTranslateAction() {

    override fun findKeyElement(psiElement: PsiElement): PsiElement? {
        return PsiTreeUtil.getParentOfType(psiElement, YAMLKeyValue::class.java, false)
    }

    override fun getFullKeyPath(keyElement: PsiElement): String {
        val yamlKeyValue = keyElement as? YAMLKeyValue ?: return ""
        val keys = mutableListOf<String>()
        var currentKeyValue: YAMLKeyValue? = yamlKeyValue

        while (currentKeyValue != null) {
            val keyText = currentKeyValue.keyText
            keys.add(keyText)
            currentKeyValue = PsiTreeUtil.getParentOfType(currentKeyValue.parent, YAMLKeyValue::class.java)
        }

        return keys.reversed().joinToString(".")
    }

    override fun isKeyElement(psiElement: PsiElement): Boolean {
        //  val language = psiElement.language
        // if (!LanguageUtil.isDescendedFrom(language, YAMLLanguage.INSTANCE)) {
        //     return false
        // }

        val yamlKeyValue = PsiTreeUtil.getParentOfType(psiElement, YAMLKeyValue::class.java)
        return yamlKeyValue != null && PsiTreeUtil.isAncestor(yamlKeyValue.key, psiElement, false)
    }
}
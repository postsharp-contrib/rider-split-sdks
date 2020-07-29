package net.postsharp.splitsdks

import com.intellij.codeInspection.*
import com.intellij.lang.xml.XmlASTFactory
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.XmlElementFactory
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlText
import com.jetbrains.rd.util.remove

class SplitSdksInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return AA(holder, isOnTheFly);
    }
}
class AA(var holder: ProblemsHolder, onTheFly: Boolean) : XmlElementVisitor() {
    private val myQuickFix: AAQuickFix = AAQuickFix()
    override fun visitXmlAttribute(attribute: XmlAttribute?) {
        if (attribute == null) {
            return
        }
        if (attribute.localName == "Sdk") {
            if (attribute.parent.localName == "Project") {
                holder.registerProblem(attribute, "Sdk attribute can be split into two imports", myQuickFix)
            }
            else {
//                holder.registerProblem(attribute, "The element name is " + attribute.parent.localName + ".")
            }
        }
        else {
//            holder.registerProblem(attribute, "The attribute name is " + attribute.localName + ".")
        }
    }
}

class AAQuickFix : LocalQuickFix {
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val attribute : XmlAttribute = descriptor.psiElement as XmlAttribute
        val root = attribute.parent
        if (root == null) {
            return
        }
        val firstTag : XmlTag = XmlElementFactory.getInstance(project).createTagFromText("<Import Project=\"Sdk.props\" Sdk=\"" + attribute.value + "\" />")
        val lastTag : XmlTag = XmlElementFactory.getInstance(project).createTagFromText("<Import Project=\"Sdk.targets\" Sdk=\"" + attribute.value + "\" />")
        root.addSubTag(firstTag, true)
        for (i in 0..root.children.size) {
           if (root.children[i] == firstTag) {
               break
           }
           if (root.children.get(i) is XmlText) {
               root.children.remove(root.children.get(i))
               break
           }
        }
        root.addSubTag(lastTag, false)
        attribute.delete()
    }

    override fun getName(): String {
        return "Split into Sdk.props and Sdk.targets"
    }

    override fun getFamilyName(): String {
        return this.name
    }
}

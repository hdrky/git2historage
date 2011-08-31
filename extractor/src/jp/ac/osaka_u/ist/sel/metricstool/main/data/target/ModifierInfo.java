package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;


/**
 * �N���X�C���\�b�h�C�t�B�[���h�Ȃǂ̏C���q��\���N���X�D���݈ȉ��́C�C���q��������
 * <ul>
 * <li>public</li>
 * <li>private</li>
 * <li>virtual(abstract)
 * <li>
 * </ul>
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class ModifierInfo implements Serializable {

    public static boolean isNamespaceVisible(final Set<ModifierInfo> modifiers) {
        switch (Settings.getInstance().getLanguage()) {
        case JAVA13:
        case JAVA14:
        case JAVA15:
            final ModifierInfo privateModifier = JavaPredefinedModifierInfo.PRIVATE;
            return !modifiers.contains(privateModifier);
        default:
            throw new IllegalStateException();
        }
    }

    public static boolean isInheritanceVisible(final Set<ModifierInfo> modifiers) {
        switch (Settings.getInstance().getLanguage()) {
        case JAVA13:
        case JAVA14:
        case JAVA15:
            final ModifierInfo protectedModifier = JavaPredefinedModifierInfo.PROTECTED;
            final ModifierInfo publicModifier = JavaPredefinedModifierInfo.PUBLIC;
            return modifiers.contains(protectedModifier) || modifiers.contains(publicModifier);
        default:
            throw new IllegalStateException();
        }
    }

    public static boolean isPublicVisible(final Set<ModifierInfo> modifiers) {
        switch (Settings.getInstance().getLanguage()) {
        case JAVA13:
        case JAVA14:
        case JAVA15:
            final ModifierInfo publicModifier = JavaPredefinedModifierInfo.PUBLIC;
            return modifiers.contains(publicModifier);
        default:
            throw new IllegalStateException();
        }
    }

    public static boolean isStaticMember(final Set<ModifierInfo> modifiers) {
        switch (Settings.getInstance().getLanguage()) {
        case JAVA13:
        case JAVA14:
        case JAVA15:
            final ModifierInfo staticModifier = JavaPredefinedModifierInfo.STATIC;
            return modifiers.contains(staticModifier);
        default:
            throw new IllegalStateException();
        }
    }

    public static boolean isInstanceMember(final Set<ModifierInfo> modifiers) {
        return !isStaticMember(modifiers);
    }

    public ModifierInfo() {
    }

    /**
     * �C���q����Ԃ�
     * 
     * @return �C���q��
     */
    public String getName() {
        return this.name;
    }

    protected String name;

    @Override
    public String toString() {
        return this.name;
    }
}

package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.HashMap;
import java.util.Map;

/**
 * public��private�Ȃ�Java�̌���ŋK�肳��Ă���C���q�������N���X
 * @author a-saitoh
 *
 */

@SuppressWarnings("serial")
public final class JavaPredefinedModifierInfo extends JavaModifierInfo {


    /**
     * abstract ��\���萔
     */
    public static final String ABSTRACT_STRING = "abstract";

    /**
     * final ��\���萔
     */
    public static final String FINAL_STRING = "final";

    /**
     * private ��\���萔
     */
    public static final String PRIVATE_STRING = "private";

    /**
     * protected ��\���萔
     */
    public static final String PROTECTED_STRING = "protected";

    /**
     * public ��\���萔
     */
    public static final String PUBLIC_STRING = "public";

    /**
     * static ��\���萔
     */
    public static final String STATIC_STRING = "static";

    /**
     * virtual ��\���萔
     */
    public static final String VIRTUAL_STRING = "virtual";
    
    /**
     * synchronized ��\���萔
     */
    public static final String SYNCHRONIZED_STRING = "synchronized";

    /**
     * abstract ��\���萔
     */
    public static final JavaPredefinedModifierInfo ABSTRACT = new JavaPredefinedModifierInfo(ABSTRACT_STRING);

    /**
     * final ��\���萔
     */
    public static final JavaPredefinedModifierInfo FINAL = new JavaPredefinedModifierInfo(FINAL_STRING);

    /**
     * private ��\���萔
     */
    public static final JavaPredefinedModifierInfo PRIVATE = new JavaPredefinedModifierInfo(PRIVATE_STRING);

    /**
     * protected ��\���萔
     */
    public static final JavaPredefinedModifierInfo PROTECTED = new JavaPredefinedModifierInfo(PROTECTED_STRING);

    /**
     * public ��\���萔
     */
    public static final JavaPredefinedModifierInfo PUBLIC = new JavaPredefinedModifierInfo(PUBLIC_STRING);

    /**
     * static ��\���萔
     */
    public static final JavaPredefinedModifierInfo STATIC = new JavaPredefinedModifierInfo(STATIC_STRING);

    /**
     * virtual ��\���萔
     */
    public static final JavaPredefinedModifierInfo VIRTUAL = new JavaPredefinedModifierInfo(VIRTUAL_STRING);

    /**
     * synchronized ��\���萔
     */
    public static final JavaPredefinedModifierInfo SYNCHRONIZED = new JavaPredefinedModifierInfo(SYNCHRONIZED_STRING);

    /**
     * �C���q������C�C���q�I�u�W�F�N�g�𐶐�����t�@�N�g�����\�b�h
     * 
     * @param name �C���q��
     * @return �C���q�I�u�W�F�N�g
     */
    public static JavaPredefinedModifierInfo getModifierInfo(final String name) {

        JavaPredefinedModifierInfo requiredModifier = MODIFIERS.get(name);
        if (null == requiredModifier) {
            requiredModifier = new JavaPredefinedModifierInfo(name);
            MODIFIERS.put(name, requiredModifier);
        }

        return requiredModifier;
    }

    /**
     * �C���q����^���āC�I�u�W�F�N�g��������
     * 
     * @param name
     */
    private JavaPredefinedModifierInfo(final String name) {
        this.name = name;
    }


    /**
     * �������� ModifierInfo �I�u�W�F�N�g��ۑ����Ă������߂̒萔
     */
    private static final Map<String, JavaPredefinedModifierInfo> MODIFIERS = new HashMap<String, JavaPredefinedModifierInfo>();

    static {
        MODIFIERS.put(ABSTRACT_STRING, ABSTRACT);
        MODIFIERS.put(FINAL_STRING, FINAL);
        MODIFIERS.put(PRIVATE_STRING, PRIVATE);
        MODIFIERS.put(PROTECTED_STRING, PROTECTED);
        MODIFIERS.put(PUBLIC_STRING, PUBLIC);
        MODIFIERS.put(STATIC_STRING, STATIC);
        MODIFIERS.put(VIRTUAL_STRING, VIRTUAL);
        MODIFIERS.put(SYNCHRONIZED_STRING, SYNCHRONIZED);
    }

}

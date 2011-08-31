package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Set;


/**
 * �O���N���X����\���N���X
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class ExternalClassInfo extends ClassInfo {

    /**
     * ���O��Ԗ��ƃN���X����^���āC�I�u�W�F�N�g��������
     * 
     * @param namespace ���O��Ԗ�
     * @param className �N���X��
     */
    public ExternalClassInfo(final NamespaceInfo namespace, final String className) {

        super(new HashSet<ModifierInfo>(), namespace, className, false, 0, 0, 0, 0);
    }

    /**
     * �C���q�C���S���薼�C�A�N�Z�X����q��^���āC�N���X���I�u�W�F�N�g��������
     * 
     * @param fullQualifiedName ���S���薼
     */
    public ExternalClassInfo(final Set<ModifierInfo> modifiers, final String[] fullQualifiedName,
            final boolean isInterface) {

        super(modifiers, fullQualifiedName, isInterface, 0, 0, 0, 0);
    }

    /**
     * ���S���薼��^���āC�N���X���I�u�W�F�N�g��������
     * 
     * @param fullQualifiedName ���S���薼
     */
    public ExternalClassInfo(final String[] fullQualifiedName) {

        super(new HashSet<ModifierInfo>(), fullQualifiedName, false, 0, 0, 0, 0);
    }

    /**
     * ���O��Ԃ��s���ȊO���N���X�̃I�u�W�F�N�g��������
     * 
     * @param className �N���X��
     */
    public ExternalClassInfo(final String className) {
        super(new HashSet<ModifierInfo>(), NamespaceInfo.UNKNOWN, className, false, 0, 0, 0, 0);
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        throw new CannotUseException();
    }

    /**
     * �s���ȊO���N���X��\�����߂̒萔
     */
    public static final ExternalClassInfo UNKNOWN = new ExternalClassInfo("UNKNOWN");
}

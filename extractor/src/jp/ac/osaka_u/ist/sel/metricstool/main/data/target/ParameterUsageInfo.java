package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �����̎g�p��\���N���X
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class ParameterUsageInfo extends VariableUsageInfo<ParameterInfo> {

    @Override
    public ExecutableElementInfo copy() {
        final ParameterInfo usedParameter = this.getUsedVariable();
        final boolean reference = this.isReference();
        final boolean assignment = this.isAssignment();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ParameterUsageInfo newParameterUsage = new ParameterUsageInfo(usedParameter,
                reference, assignment, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newParameterUsage.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newParameterUsage.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newParameterUsage;
    }

    /**
     * �g�p����Ă��������^���ăI�u�W�F�N�g��������
     * 
     * @param usedParameter �g�p����Ă������
     * @param reference �Q�Ƃł��邩�ǂ���
     * @param assignment ����ł��邩�ǂ���
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    private ParameterUsageInfo(final ParameterInfo usedParameter, final boolean reference,
            final boolean assignment, final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(usedParameter, reference, assignment, ownerMethod, fromLine, fromColumn, toLine,
                toColumn);
    }

    /**
     * �g�p����Ă���p�����[�^�C�g�p�̎�ށC�g�p����Ă���ʒu����^���ăC���X�^���X���擾
     * 
     * @param usedParameter �g�p����Ă���p�����[�^
     * @param reference �Q�Ƃł��邩�ǂ���
     * @param assingment ����ł��邩�ǂ���
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     * @return �p���[���[�^�g�p�̃C���X�^���X
     */
    public static ParameterUsageInfo getInstance(final ParameterInfo usedParameter,
            final boolean reference, final boolean assingment, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        final ParameterUsageInfo instance = new ParameterUsageInfo(usedParameter, reference,
                assingment, ownerMethod, fromLine, fromColumn, toLine, toColumn);
        addParameterUsage(instance);
        return instance;
    }

    /**
     * �p�����[�^�ϐ��g�p�̃C���X�^���X���p�����[�^�ϐ�����p�����[�^�ϐ��g�p�ւ̃}�b�v�ɒǉ�
     * @param parameterUsage �p�����[�^�ϐ��g�p
     */
    private static void addParameterUsage(final ParameterUsageInfo parameterUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == parameterUsage) {
            throw new IllegalArgumentException("localVariableUsage is null");
        }

        final ParameterInfo usedParameter = parameterUsage.getUsedVariable();
        if (USAGE_MAP.containsKey(usedParameter)) {
            USAGE_MAP.get(usedParameter).add(parameterUsage);
        } else {
            final Set<ParameterUsageInfo> usages = Collections
                    .synchronizedSet(new HashSet<ParameterUsageInfo>());
            usages.add(parameterUsage);
            USAGE_MAP.put(usedParameter, usages);
        }
    }

    /**
     * �^����ꂽ�p�����[�^�̎g�p���̃Z�b�g���擾
     * @param parameter �g�p�����擾���������[�J���ϐ�
     * @return �p�����[�^�g�p�̃Z�b�g�D�����ŗ^����ꂽ���[�J���ϐ����g�p����Ă��Ȃ��ꍇ��null
     */
    public final static Set<ParameterUsageInfo> getUsages(final ParameterInfo parameter) {
        if (USAGE_MAP.containsKey(parameter)) {
            return USAGE_MAP.get(parameter);
        } else {
            return Collections.<ParameterUsageInfo> emptySet();
        }
    }

    /**
     * �^����ꂽ�ϐ����p��Collection�Ɋ܂܂��������p��Set��Ԃ�
     * 
     * @param variableUsages �ϐ����p��Collection
     * @return �^����ꂽ�ϐ����p��Collection�Ɋ܂܂��������p��Set
     */
    public final static Set<ParameterUsageInfo> getParameterUsages(
            Collection<VariableUsageInfo<?>> variableUsages) {
        final Set<ParameterUsageInfo> parameterUsages = new HashSet<ParameterUsageInfo>();
        for (final VariableUsageInfo<?> variableUsage : variableUsages) {
            if (variableUsage instanceof ParameterUsageInfo) {
                parameterUsages.add((ParameterUsageInfo) variableUsage);
            }
        }
        return Collections.unmodifiableSet(parameterUsages);
    }

    private static final ConcurrentMap<ParameterInfo, Set<ParameterUsageInfo>> USAGE_MAP = new ConcurrentHashMap<ParameterInfo, Set<ParameterUsageInfo>>();
}

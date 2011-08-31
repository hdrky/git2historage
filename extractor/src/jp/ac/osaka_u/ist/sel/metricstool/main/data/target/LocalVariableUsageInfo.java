package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���[�J���ϐ��̎g�p��\���N���X
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class LocalVariableUsageInfo extends VariableUsageInfo<LocalVariableInfo> {

    @Override
    public ExecutableElementInfo copy() {
        final LocalVariableInfo usedVariable = this.getUsedVariable();
        final boolean reference = this.isReference();
        final boolean assignment = this.isAssignment();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final LocalVariableUsageInfo newVariableUsage = getInstance(usedVariable, reference,
                assignment, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newVariableUsage.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newVariableUsage.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newVariableUsage;
    }

    /**
     * �g�p����Ă��郍�[�J���ϐ���^���ăI�u�W�F�N�g��������
     * 
     * @param usedLocalVariable �g�p����Ă��郍�[�J���ϐ�
     * @param reference �Q�Ƃł��邩�ǂ���
     * @param assignment ����ł��邩�ǂ���
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    private LocalVariableUsageInfo(final LocalVariableInfo usedLocalVariable,
            final boolean reference, final boolean assignment, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(usedLocalVariable, reference, assignment, ownerMethod, fromLine, fromColumn, toLine,
                toColumn);
    }

    /**
     * �g�p����Ă��郍�[�J���ϐ��C�g�p�̎�ށC�g�p����Ă���ʒu����^���ăC���X�^���X���擾
     * 
     * @param usedLocalVariable �g�p����Ă��郍�[�J���ϐ�
     * @param reference �Q�Ƃł��邩�ǂ���
     * @param assignment ����ł��邩�ǂ���
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     * @return ���[�J���ϐ��g�p�̃C���X�^���X
     */
    public static LocalVariableUsageInfo getInstance(final LocalVariableInfo usedLocalVariable,
            final boolean reference, final boolean assignment, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        final LocalVariableUsageInfo instance = new LocalVariableUsageInfo(usedLocalVariable,
                reference, assignment, ownerMethod, fromLine, fromColumn, toLine, toColumn);
        addLocalVariableUsage(instance);
        return instance;
    }

    /**
     * ���[�J���ϐ��g�p�̃C���X�^���X�����[�J���ϐ����烍�[�J���ϐ��g�p�ւ̃}�b�v�ɒǉ�
     * @param localVariableUsage ���[�J���ϐ��g�p
     */
    private static void addLocalVariableUsage(final LocalVariableUsageInfo localVariableUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == localVariableUsage) {
            throw new IllegalArgumentException("localVariableUsage is null");
        }

        final LocalVariableInfo usedLocalVariable = localVariableUsage.getUsedVariable();
        if (USAGE_MAP.containsKey(usedLocalVariable)) {
            USAGE_MAP.get(usedLocalVariable).add(localVariableUsage);
        } else {
            final Set<LocalVariableUsageInfo> usages = Collections
                    .synchronizedSet(new HashSet<LocalVariableUsageInfo>());
            usages.add(localVariableUsage);
            USAGE_MAP.put(usedLocalVariable, usages);
        }
    }

    /**
     * �^����ꂽ���[�J���ϐ��̎g�p���̃Z�b�g���擾
     * @param localVarible �g�p�����擾���������[�J���ϐ�
     * @return ���[�J���ϐ��g�p�̃Z�b�g�D�����ŗ^����ꂽ���[�J���ϐ����g�p����Ă��Ȃ��ꍇ��null
     */
    public final static Set<LocalVariableUsageInfo> getUsages(final LocalVariableInfo localVarible) {
        if (USAGE_MAP.containsKey(localVarible)) {
            return USAGE_MAP.get(localVarible);
        } else {
            return Collections.<LocalVariableUsageInfo> emptySet();
        }
    }

    /**
     * �^����ꂽ�ϐ����p��Collection�Ɋ܂܂�郍�[�J���ϐ����p��Set��Ԃ�
     * 
     * @param variableUsages �ϐ����p��Collection
     * @return �^����ꂽ�ϐ����p��Collection�Ɋ܂܂�郍�[�J���ϐ����p��Set
     */
    public final static Set<LocalVariableUsageInfo> getLocalVariableUsages(
            Collection<VariableUsageInfo<?>> variableUsages) {
        final Set<LocalVariableUsageInfo> localVariableUsages = new HashSet<LocalVariableUsageInfo>();
        for (final VariableUsageInfo<?> variableUsage : variableUsages) {
            if (variableUsage instanceof LocalVariableUsageInfo) {
                localVariableUsages.add((LocalVariableUsageInfo) variableUsage);
            }
        }
        return Collections.unmodifiableSet(localVariableUsages);
    }

    private static final ConcurrentMap<LocalVariableInfo, Set<LocalVariableUsageInfo>> USAGE_MAP = new ConcurrentHashMap<LocalVariableInfo, Set<LocalVariableUsageInfo>>();
}
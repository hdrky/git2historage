package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �t�B�[���h�̎g�p��\���N���X
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class FieldUsageInfo extends VariableUsageInfo<FieldInfo> {

    /**
     * �g�p����Ă���t�B�[���h��^���ăI�u�W�F�N�g��������
     * 
     * @param qualifierExpression �t�B�[���h�g�p�����s�����e�̎�
     * @param usedField �g�p����Ă���t�B�[���h
     * @param reference �Q�Ƃł��邩�ǂ���
     * @param assignment ����ł��邩�ǂ���
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    protected FieldUsageInfo(final ExpressionInfo qualifierExpression,
            final TypeInfo qualifierType, final FieldInfo usedField, final boolean reference,
            final boolean assignment, final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(usedField, reference, assignment, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        this.qualifierExpression = qualifierExpression;
        this.qualifierType = qualifierType;

        // �t�B�[���h�̎g�p�����i�[
        if (reference) {
            usedField.addReferencer(ownerMethod);
        }

        if (assignment) {
            usedField.addAssignmenter(ownerMethod);
        }
    }

    @Override
    public void setOwnerExecutableElement(ExecutableElementInfo ownerExecutableElement) {
        super.setOwnerExecutableElement(ownerExecutableElement);
        this.qualifierExpression.setOwnerExecutableElement(ownerExecutableElement);
    }

    /**
     * ���̃t�B�[���h�g�p�̐e�C�܂肱�̃t�B�[���h�g�p���������Ă��鎮��Ԃ�
     * 
     * @return ���̃t�B�[���h�g�p�̐e
     */
    public final TypeInfo getQualifierType() {
        return this.qualifierType;
    }

    /**
     * �t�B�[���h�g�p�����s�����e�̎���Ԃ�
     * @return �t�B�[���h�g�p�����s�����e�̎�
     */
    public final ExpressionInfo getQualifierExpression() {
        return this.qualifierExpression;
    }

    /**
     * ���̕ϐ��g�p�̃e�L�X�g�\���i�^�j��Ԃ�
     * 
     * @return ���̕ϐ��g�p�̃e�L�X�g�\���i�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ExpressionInfo quantifierExpression = this.getQualifierExpression();
        if (null != quantifierExpression) {
            sb.append(quantifierExpression.getText());
            sb.append(".");
        }

        final FieldInfo field = this.getUsedVariable();
        sb.append(field.getName());

        return sb.toString();
    }

    /**
     * ���̎��i�t�B�[���h�g�p�j�ɂ�����ϐ����p�̈ꗗ��Ԃ�
     * 
     * @return �ϐ����p��Set
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {

        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(super.getVariableUsages());

        final ExpressionInfo qualifierExpression = this.getQualifierExpression();
        variableUsages.addAll(qualifierExpression.getVariableUsages());

        return Collections.unmodifiableSortedSet(variableUsages);
    }

    @Override
    public ExecutableElementInfo copy() {
        final ExpressionInfo qualifierExpression = this.getQualifierExpression();
        final TypeInfo qualifierType = this.getQualifierType();
        final FieldInfo usedField = this.getUsedVariable();
        final boolean reference = this.isReference();
        final boolean assignment = this.isAssignment();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final FieldUsageInfo newFieldUsage = new FieldUsageInfo(qualifierExpression, qualifierType,
                usedField, reference, assignment, ownerMethod, fromLine, fromColumn, toLine,
                toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newFieldUsage.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newFieldUsage.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newFieldUsage;
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return this.getQualifierExpression().getCalls();
    }

    private final TypeInfo qualifierType;

    /**
     * �t�B�[���h�Q�Ƃ����s�����e�̎�("."�̑O�̂��)��ۑ�����ϐ�
     */
    private final ExpressionInfo qualifierExpression;

    /**
     * �K�v�ȏ���^���āC�C���X�^���X���擾
     *
     * @param qualifierExpression �e�̎�
     * @param qualifierType �e�G���e�B�e�B�̌^
     * @param usedField �g�p����Ă���t�B�[���h
     * @param reference �Q�Ƃł��邩�ǂ���
     * @param assignment ����ł��邩�ǂ���
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     * @return �t�B�[���h�g�p�̃C���X�^���X
     */
    public static FieldUsageInfo getInstance(final ExpressionInfo qualifierExpression,
            final TypeInfo qualifierType, final FieldInfo usedField, final boolean reference,
            final boolean assignment, final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        final FieldUsageInfo instance = new FieldUsageInfo(qualifierExpression, qualifierType,
                usedField, reference, assignment, ownerMethod, fromLine, fromColumn, toLine,
                toColumn);
        addFieldUsage(instance);
        return instance;
    }

    /**
     * �t�B�[���h�g�p�̃C���X�^���X���t�B�[���h����t�B�[���h�g�p�ւ̃}�b�v�ɒǉ�
     * @param fieldUsage �t�B�[���h�g�p
     */
    private static void addFieldUsage(final FieldUsageInfo fieldUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == fieldUsage) {
            throw new IllegalArgumentException("localVariableUsage is null");
        }

        final FieldInfo usedField = fieldUsage.getUsedVariable();
        if (USAGE_MAP.containsKey(usedField)) {
            USAGE_MAP.get(usedField).add(fieldUsage);
        } else {
            final Set<FieldUsageInfo> usages = Collections
                    .synchronizedSet(new HashSet<FieldUsageInfo>());
            usages.add(fieldUsage);
            USAGE_MAP.put(usedField, usages);
        }
    }

    /**
     * �^����ꂽ�t�B�[���h�̎g�p���̃Z�b�g���擾
     * @param field �g�p�����擾�������t�B�[���h
     * @return �t�B�[���h�g�p�̃Z�b�g�D�����ŗ^����ꂽ�t�B�[���h���g�p����Ă��Ȃ��ꍇ��null
     */
    public final static Set<FieldUsageInfo> getUsages(final FieldInfo field) {
        if (USAGE_MAP.containsKey(field)) {
            return USAGE_MAP.get(field);
        } else {
            return Collections.<FieldUsageInfo> emptySet();
        }
    }

    /**
     * �^����ꂽ�ϐ����p��Collection�Ɋ܂܂��t�B�[���h���p��Set��Ԃ�
     * 
     * @param variableUsages �ϐ����p��Collection
     * @return �^����ꂽ�ϐ����p��Collection�Ɋ܂܂��t�B�[���h���p��Set
     */
    public final static Set<FieldUsageInfo> getFieldUsages(
            Collection<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsages) {
        final Set<FieldUsageInfo> fieldUsages = new HashSet<FieldUsageInfo>();
        for (final VariableUsageInfo<?> variableUsage : variableUsages) {
            if (variableUsage instanceof FieldUsageInfo) {
                fieldUsages.add((FieldUsageInfo) variableUsage);
            }
        }
        return Collections.unmodifiableSet(fieldUsages);
    }

    private static final ConcurrentMap<FieldInfo, Set<FieldUsageInfo>> USAGE_MAP = new ConcurrentHashMap<FieldInfo, Set<FieldUsageInfo>>();
}

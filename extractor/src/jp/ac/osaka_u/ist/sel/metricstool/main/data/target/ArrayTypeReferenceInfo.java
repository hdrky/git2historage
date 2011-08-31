package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �z��^�Q�Ƃ�\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class ArrayTypeReferenceInfo extends ExpressionInfo {

    /**
     * �I�u�W�F�N�g��������
     * 
     * @param arrayType �Q�Ƃ���Ă���z��̌^
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ArrayTypeReferenceInfo(final ArrayTypeInfo arrayType,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == arrayType) {
            throw new IllegalArgumentException();
        }

        this.arrayType = arrayType;
    }

    /**
     * �^��Ԃ�
     */
    @Override
    public TypeInfo getType() {
        return this.arrayType;
    }

    /**
     * �z��̌^�Q�Ƃɂ����ĕϐ����g�p����邱�Ƃ͂Ȃ��̂ŋ�̃Z�b�g��Ԃ�
     * 
     * @return ��̃Z�b�g
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo���̃Z�b�g
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return CallInfo.EmptySet;
    }

    /**
     * ���̔z��^�Q�Ƃ̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ���̔z��^�̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {
        final TypeInfo type = this.getType();
        return type.getTypeName();
    }

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(new HashSet<ReferenceTypeInfo>());
    }

    @Override
    public ExecutableElementInfo copy() {
        final ArrayTypeInfo arrayType = (ArrayTypeInfo) this.getType();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ArrayTypeReferenceInfo newArrayTypeReference = new ArrayTypeReferenceInfo(arrayType,
                ownerMethod, fromLine, fromColumn, toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newArrayTypeReference.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newArrayTypeReference.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newArrayTypeReference;

    }

    private final ArrayTypeInfo arrayType;
}

package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �z��^�� length �t�B�[���h�g�p��\���N���X
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class ArrayLengthUsageInfo extends FieldUsageInfo {

    /**
     * �e�ƂȂ�G���e�B�e�B�g�p��^���ăI�u�W�F�N�g��������
     *
     * @param qualifierExpression �e�G���e�B�e�B
     * @param qualifierType �e�G���e�B�e�B�̌^�i�K�v�Ȃ������D�D�D�j
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ArrayLengthUsageInfo(final ExpressionInfo qualifierExpression,
            final ArrayTypeInfo qualifierType, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(qualifierExpression, qualifierType,
                ArrayLengthInfo.getArrayLengthInfo(qualifierType), true, false, ownerMethod,
                fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * length �t�B�[���h�g�p�̌^��Ԃ��D
     * 
     * @return length �t�B�[���h�g�p�̌^
     */
    @Override
    public TypeInfo getType() {
        return PrimitiveTypeInfo.INT;
    }

    @Override
    public ExecutableElementInfo copy() {
        final ExpressionInfo qualifierExpression = this.getQualifierExpression();
        final ArrayTypeInfo qualifierType = (ArrayTypeInfo) this.getQualifierType();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ArrayLengthUsageInfo newArrayLengthUsage = new ArrayLengthUsageInfo(
                qualifierExpression, qualifierType, ownerMethod, fromLine, fromColumn, toLine,
                toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newArrayLengthUsage.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newArrayLengthUsage.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newArrayLengthUsage;
    }
}

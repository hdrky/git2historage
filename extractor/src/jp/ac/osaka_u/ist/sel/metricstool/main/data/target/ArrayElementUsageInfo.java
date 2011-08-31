package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * �z��v�f�̎g�p��\���N���X
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class ArrayElementUsageInfo extends ExpressionInfo {

    /**
     * �v�f�̐e�C�܂�z��^�̎��ƃC���f�b�N�X��^���āC�I�u�W�F�N�g��������
     * 
     * @param indexExpression �C���f�b�N�X
     * @param qualifierExpression �z��^�̎�
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ArrayElementUsageInfo(final ExpressionInfo indexExpression,
            final ExpressionInfo qualifierExpression, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == qualifierExpression) {
            throw new NullPointerException();
        }

        this.qualifierExpression = qualifierExpression;
        this.indexExpression = indexExpression;

        this.indexExpression.setOwnerExecutableElement(this);
    }

    /**
     * ���̔z��v�f�̎g�p�̌^��Ԃ�
     * 
     * @return ���̔z��v�f�̎g�p�̌^
     */
    @Override
    public TypeInfo getType() {

        final TypeInfo ownerType = this.getQualifierExpression().getType();

        // �e���z��^�ł���C�Ɖ����ł��Ă���ꍇ
        if (ownerType instanceof ArrayTypeInfo) {
            // �z��̎����ɉ����Č^�𐶐�
            final int ownerArrayDimension = ((ArrayTypeInfo) ownerType).getDimension();
            final TypeInfo ownerArrayElement = ((ArrayTypeInfo) ownerType).getElementType();

            // �z�񂪓񎟌��ȏ�̏ꍇ�́C����������Ƃ����z���Ԃ��C�ꎟ���̏ꍇ�́C�v�f�̌^��Ԃ��D
            return 1 < ownerArrayDimension ? ArrayTypeInfo.getType(ownerArrayElement,
                    ownerArrayDimension - 1) : ownerArrayElement;
        }

        // �z��^�łȂ��C���s���^�łȂ��ꍇ�͂�������
        assert ownerType instanceof UnknownTypeInfo : "ArrayElementUsage attaches unappropriate type!";

        return ownerType;
    }

    /**
     * ���̗v�f�̐e�C�܂�z��^�̎���Ԃ�
     * 
     * @return ���̗v�f�̐e��Ԃ�
     */
    public ExpressionInfo getQualifierExpression() {
        return this.qualifierExpression;
    }

    /**
     * ���̗v�f�̃C���f�b�N�X��Ԃ�
     * 
     * @return�@���̗v�f�̃C���f�b�N�X
     */
    public ExpressionInfo getIndexExpression() {
        return this.indexExpression;
    }

    /**
     * ���̎��i�z��v�f�̎g�p�j�ɂ�����ϐ����p�̈ꗗ��Ԃ�
     * 
     * @return �ϐ����p��Set
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final Set<VariableUsageInfo<?>> variableUsages = new HashSet<VariableUsageInfo<?>>(
                this.indexExpression.getVariableUsages());
        variableUsages.addAll(this.getQualifierExpression().getVariableUsages());
        return Collections.unmodifiableSet(variableUsages);
        //return this.getOwnerEntityUsage().getVariableUsages();
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        final Set<CallInfo<?>> calls = new HashSet<CallInfo<?>>();
        final ExpressionInfo quantifierExpression = this.getQualifierExpression();
        calls.addAll(quantifierExpression.getCalls());
        final ExpressionInfo indexExpression = this.getIndexExpression();
        calls.addAll(indexExpression.getCalls());
        return Collections.unmodifiableSet(calls);
    }

    @Override
    public void setOwnerExecutableElement(ExecutableElementInfo ownerExecutableElement) {
        super.setOwnerExecutableElement(ownerExecutableElement);

        this.qualifierExpression.setOwnerExecutableElement(ownerExecutableElement);
    }

    /**
     * ���̔z��v�f�g�p�̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ���̔z��v�f�g�p�̃e�L�X�g�\��
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ExpressionInfo expression = this.getQualifierExpression();
        sb.append(expression.getText());

        sb.append("[");

        final ExpressionInfo indexExpression = this.getIndexExpression();
        sb.append(indexExpression.getText());

        sb.append("]");

        return sb.toString();
    }

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(this.getIndexExpression().getThrownExceptions());
    }

    @Override
    public ExecutableElementInfo copy() {
        final ExpressionInfo indexExpression = (ExpressionInfo) this.getIndexExpression().copy();
        final ExpressionInfo qualifiedExpression = this.getQualifierExpression();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ArrayElementUsageInfo newArrayElementUsage = new ArrayElementUsageInfo(
                indexExpression, qualifiedExpression, ownerMethod, fromLine, fromColumn, toLine,
                toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newArrayElementUsage.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newArrayElementUsage.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newArrayElementUsage;
    }

    private final ExpressionInfo qualifierExpression;

    private final ExpressionInfo indexExpression;
}
